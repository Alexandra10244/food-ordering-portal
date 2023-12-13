package com.portal.foodordering.serivces.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.exceptions.ItemNotFoundException;
import com.portal.foodordering.exceptions.RestaurantNotFoundException;
import com.portal.foodordering.models.dtos.RestaurantDTO;
import com.portal.foodordering.models.entities.Item;
import com.portal.foodordering.models.entities.Restaurant;
import com.portal.foodordering.repositories.ItemRepository;
import com.portal.foodordering.repositories.RestaurantRepository;
import com.portal.foodordering.serivces.interfaces.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ItemRepository itemRepository;
    private final ObjectMapper objectMapper;

    @Override
    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository.save(objectMapper.convertValue(restaurantDTO, Restaurant.class));

        return objectMapper.convertValue(restaurant, RestaurantDTO.class);
    }

    @Override
    public String addIemToRestaurant(Long itemId, Long restaurantId){
        Restaurant restaurant= restaurantRepository.findById(restaurantId).orElseThrow(()-> new RestaurantNotFoundException("Restaurant does not exist"));
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemNotFoundException("Item does not exist"));
        restaurant.getItemSetRestaurant().add(item);
        return item.getName()+ " added to " + restaurant.getName();
    }

    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(restaurant-> objectMapper.convertValue(restaurant, RestaurantDTO.class))
                .toList();

    }

    @Override
    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found!"));

        if (restaurantDTO.getName() != null) {
            restaurant.setName(restaurantDTO.getName());
        }
        if (restaurantDTO.getCuisine() != null) {
            restaurant.setCuisine(restaurantDTO.getCuisine());
        }
        Restaurant restaurantUpdated = restaurantRepository.save(restaurant);

        return objectMapper.convertValue(restaurantUpdated, RestaurantDTO.class);
    }

    @Override
    public String deleteRestaurant(Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);

            return "Restaurant successfully deleted!";
        } else {
            throw new RestaurantNotFoundException("Restaurant not found!");
        }
    }

    @Override
    public RestaurantDTO findRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository
                .findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not fount!"));

        return objectMapper.convertValue(restaurant, RestaurantDTO.class);
    }

    @Override
    public RestaurantDTO findRestaurantByName(String name) {
        Restaurant restaurant = restaurantRepository
                .findByNameIgnoreCase(name)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with name " + name + " not fount!"));

        return objectMapper.convertValue(restaurant, RestaurantDTO.class);
    }

    @Override
    public List<RestaurantDTO> findRestaurantByCuisine(String cuisine) {
        List<Restaurant> restaurants = restaurantRepository
                .findByCuisineIgnoreCase(cuisine);
                //.orElseThrow(() -> new RestaurantNotFoundException("Restaurant with cuisine " + cuisine + " not fount!"));

        return restaurants.stream()
                .map(restaurant-> objectMapper.convertValue(restaurant, RestaurantDTO.class))
                .toList();
    }
}