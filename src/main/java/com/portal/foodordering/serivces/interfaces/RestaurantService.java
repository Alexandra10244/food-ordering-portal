package com.portal.foodordering.serivces.interfaces;

import com.portal.foodordering.models.dtos.RestaurantDTO;

import java.util.List;

public interface RestaurantService {

    RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO);
    List<RestaurantDTO> getAllRestaurants();
    RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO);
    String deleteRestaurant(Long id);
    RestaurantDTO findRestaurantById(Long id);
    RestaurantDTO findRestaurantByName(String name);
    RestaurantDTO findRestaurantByCuisine(String cuisine);

}
