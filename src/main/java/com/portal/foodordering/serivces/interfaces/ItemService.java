package com.portal.foodordering.serivces.interfaces;

import com.portal.foodordering.models.dtos.EditItem;
import com.portal.foodordering.models.dtos.ItemDTO;

import java.util.List;

public interface ItemService {

    ItemDTO createItem(ItemDTO itemDTO);
    List<ItemDTO> getAllItems();
    ItemDTO updateItem(Long id, EditItem editItem);
    String deleteItem(Long id);
    ItemDTO findItemById(Long id);
    ItemDTO findItemByName(String name);

}
