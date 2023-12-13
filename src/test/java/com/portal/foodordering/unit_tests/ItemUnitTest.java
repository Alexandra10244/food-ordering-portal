package com.portal.foodordering.unit_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.exceptions.ItemNotFoundException;
import com.portal.foodordering.models.dtos.ItemDTO;
import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.repositories.ItemRepository;
import com.portal.foodordering.serivces.implementations.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class ItemUnitTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void createItemShouldPass() {
        Item item = createItemEntity("Pizza Margherita", 23, "Pizza with something", 10);
        ItemDTO itemDTO = createItemDTO("Pizza Margherita", 23, "Pizza with something", 10);

        Item savedItem = new Item();

        when(objectMapper.convertValue(itemDTO, Item.class)).thenReturn(item);
        when(objectMapper.convertValue(savedItem, ItemDTO.class)).thenReturn(itemDTO);
        when(itemRepository.save(item)).thenReturn(savedItem);

        ItemDTO result = itemService.createItem(itemDTO);

        assertEquals(itemDTO, result);

    }

    @Test
    void testGetAllItemsShouldPass() {
        List<Item> itemList = Arrays.asList(
                createItemEntity("Pizza Margherita", 23, "Pizza with something", 10),
                createItemEntity("Pizza Marinara", 20, "Pizza with something", 11)
        );

        when(itemRepository.findAll()).thenReturn(itemList);

        List<ItemDTO> itemDTOsList = itemList.stream()
                .map(item -> objectMapper
                        .convertValue(item, ItemDTO.class))
                .collect(Collectors.toList());

        List<ItemDTO> result = itemService.getAllItems();

        assertEquals(itemDTOsList.get(0), result.get(0));

    }

    @Test
    void testGetAllItemsShouldNotPass() {
        List<Item> itemList = Arrays.asList(
                createItemEntity("Pizza Margherita", 23, "Pizza with something", 10),
                createItemEntity("Pizza Marinara", 20, "Pizza with something", 11)
        );

        when(itemRepository.findAll()).thenReturn(itemList);

        List<ItemDTO> result = itemService.getAllItems();

        assertNotEquals(itemList, result);
    }


    @Test
    void testDeleteItemSuccess() {
        Long itemId = 1L;

        when(itemRepository.existsById(itemId)).thenReturn(true);
        String result = itemService.deleteItem(itemId);
        assertEquals("Product " + itemId + " successfully deleted!", result);

        Mockito.verify(itemRepository).deleteById(itemId);
    }

    @Test
    void testDeleteItemNotFound() {
        Long itemId = 1L;

        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(ItemNotFoundException.class, () -> {
            itemService.deleteItem(itemId);
        });

        Mockito.verify(itemRepository, Mockito.never()).deleteById(itemId);
    }

    @Test
    void testFindItemByIdSuccess() {
        Long itemId = 1L;
        Item item = createItemEntity("Pizza Margherita", 23, "Pizza with something", 10);
        ItemDTO itemDTO = createItemDTO("Pizza Margherita", 23, "Pizza with something", 10);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(objectMapper.convertValue(item, ItemDTO.class)).thenReturn(itemDTO);

        ItemDTO result = itemService.findItemById(itemId);

        assertEquals(itemDTO, result);
    }

    @Test
    void findItemByIdNotFound() {
        Long itemId = 1L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> {
            itemService.findItemById(itemId);
        });
    }

    @Test
    void findItemByNameSuccess() {
        String name = "Pizza Margherita";
        Item item = createItemEntity("Pizza Margherita", 23, "Pizza with something", 10);
        ItemDTO itemDTO = createItemDTO("Pizza Margherita", 23, "Pizza with something", 10);

        when(itemRepository.findItemByNameIgnoreCase(name)).thenReturn(Optional.of(item));
        when(objectMapper.convertValue(item, ItemDTO.class)).thenReturn(itemDTO);

        ItemDTO result = itemService.findItemByName(name);
        assertEquals(itemDTO, result);
    }

    @Test
    void findItemByNameNotFound() {
        String name = "Pizza Margherita";

        when(itemRepository.findItemByNameIgnoreCase(name)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> {
            itemService.findItemByName(name);
        });
    }

    ItemDTO createItemDTO(String name, double price, String description, int noOfAvailableItems) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName(name);
        itemDTO.setPrice(price);
        itemDTO.setDescription(description);
        itemDTO.setNoOfAvailableItems(noOfAvailableItems);

        return itemDTO;
    }

    Item createItemEntity(String name, double price, String description, int noOfAvailableItems) {
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        item.setNoOfAvailableItems(noOfAvailableItems);

        return item;
    }
}
