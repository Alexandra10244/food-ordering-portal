package com.portal.foodordering.serivces.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.exceptions.ItemNotFoundException;
import com.portal.foodordering.models.dtos.EditItem;
import com.portal.foodordering.models.dtos.ItemDTO;
import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.repositories.ItemRepository;
import com.portal.foodordering.serivces.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ItemDTO createItem(ItemDTO itemDTO) {
        Item item = itemRepository.save(objectMapper.convertValue(itemDTO, Item.class));
        return objectMapper.convertValue(item, ItemDTO.class);
    }

    @Override
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(item -> objectMapper.convertValue(item, ItemDTO.class))
                .toList();
    }

    @Override
    public ItemDTO updateItem(Long id, EditItem editItem) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found!"));

        if (editItem.getName() != null && !editItem.getName().isEmpty()) {
            item.setName(editItem.getName());
        }
        if (editItem.getPrice() > 0) {
            item.setPrice(editItem.getPrice());
        }
        if (editItem.getDescription() != null) {
            item.setDescription(editItem.getDescription());
        }
        if (editItem.getNoOfAvailableItems() != null) {
            item.setNoOfAvailableItems(editItem.getNoOfAvailableItems());
        }

        Item updatedItem = itemRepository.save(item);

        return objectMapper.convertValue(updatedItem, ItemDTO.class);
    }

    @Override
    public String deleteItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);

            return "Product " + id + " successfully deleted!";
        } else {
            throw new ItemNotFoundException("Item not found!");
        }

    }

    @Override
    public ItemDTO findItemById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found!"));

        return objectMapper.convertValue(item, ItemDTO.class);
    }

    @Override
    public ItemDTO findItemByName(String name) {
        Item item = itemRepository.findItemByNameIgnoreCase(name).orElseThrow(() -> new ItemNotFoundException("Item not found!"));

        return objectMapper.convertValue(item, ItemDTO.class);
    }
}

