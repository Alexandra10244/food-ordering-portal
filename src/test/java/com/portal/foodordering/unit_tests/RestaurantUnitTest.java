package com.portal.foodordering.unit_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.exceptions.RestaurantNotFoundException;
import com.portal.foodordering.models.dtos.RestaurantDTO;
import com.portal.foodordering.models.entities.Restaurant;
import com.portal.foodordering.repositories.RestaurantRepository;
import com.portal.foodordering.serivces.implementations.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUnitTest {

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    RestaurantServiceImpl restaurantService;

    @Test
    void testCreateRestaurantShouldPass() {
        RestaurantDTO restaurantDTO = createRestaurantDTO("Thalia", "Italian cuisine");
        Restaurant restaurant = createRestaurantEntity("Thalia", "Italian cuisine");

        Restaurant savedRestaurant = new Restaurant();

        when(objectMapper.convertValue(restaurantDTO, Restaurant.class)).thenReturn(restaurant);
        when(objectMapper.convertValue(savedRestaurant, RestaurantDTO.class)).thenReturn(restaurantDTO);
        when(restaurantRepository.save(restaurant)).thenReturn(savedRestaurant);

        RestaurantDTO savedRestaurantDTO = restaurantService.createRestaurant(restaurantDTO);

        Assertions.assertEquals(restaurantDTO, savedRestaurantDTO);

    }

    @Test
    void testGetAllRestaurantsShouldPass() {
        List<Restaurant> restaurantList = Arrays.asList(
                createRestaurantEntity("Thalia", "Italian cuisine"),
                createRestaurantEntity("Le Gourmet", "French cuisine")
        );

        when(restaurantRepository.findAll()).thenReturn(restaurantList);

        List<RestaurantDTO> restaurantDTOsList = restaurantList.stream()
                .map(restaurant -> objectMapper
                        .convertValue(restaurant, RestaurantDTO.class))
                .collect(Collectors.toList());

        List<RestaurantDTO> result = restaurantService.getAllRestaurants();

        assertEquals(restaurantDTOsList.get(0), result.get(0));

    }

    @Test
    void testGetAllRestaurantsShouldNotPass() {
        List<Restaurant> restaurantList = Arrays.asList(
                createRestaurantEntity("Thalia", "Italian cuisine"),
                createRestaurantEntity("Le Gourmet", "French cuisine")
        );

        when(restaurantRepository.findAll()).thenReturn(restaurantList);

        List<RestaurantDTO> result = restaurantService.getAllRestaurants();

        assertNotEquals(restaurantList, result);
    }

    @Test
    void testDeleteRestaurantSuccess() {
        Long idRestaurant = 1L;

        when(restaurantRepository.existsById(idRestaurant)).thenReturn(true);

        String result = restaurantService.deleteRestaurant(idRestaurant);
        assertEquals("Restaurant successfully deleted!", result);

        Mockito.verify(restaurantRepository).deleteById(idRestaurant);
    }

    @Test
    void testDeleteRestaurantNotFound() {
        Long restaurantId = 1L;

        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.deleteRestaurant(restaurantId);
        });

        Mockito.verify(restaurantRepository, Mockito.never()).deleteById(restaurantId);
    }

    @Test
    void testFindRestaurantByIdSuccess() {
        Long restaurantId = 1L;
        Restaurant restaurantEntity = createRestaurantEntity("Thalia", "Italian cuisine");
        RestaurantDTO restaurantDTO = createRestaurantDTO("Thalia", "Italian cuisine");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(objectMapper.convertValue(restaurantEntity, RestaurantDTO.class)).thenReturn(restaurantDTO);

        RestaurantDTO result = restaurantService.findRestaurantById(restaurantId);

        assertEquals(restaurantDTO, result);
    }

    @Test
    void findRestaurantByIdNotFound() {
        Long restaurantId = 1L;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.findRestaurantById(restaurantId);
        });
    }

    @Test
    void findRestaurantByNameSuccess() {
        String name = "Thalia";
        Restaurant restaurant = createRestaurantEntity("Thalia", "Italian cuisine");
        RestaurantDTO restaurantDTO = createRestaurantDTO("Thalia", "Italian cuisine");

        when(restaurantRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(restaurant));
        when(objectMapper.convertValue(restaurant, RestaurantDTO.class)).thenReturn(restaurantDTO);

        RestaurantDTO result = restaurantService.findRestaurantByName(name);
        assertEquals(restaurantDTO, result);
    }

    @Test
    void findRestaurantByNameNotFound() {
        String name = "Thalia";

        when(restaurantRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.findRestaurantByName(name);
        });
    }

    @Test
    void findRestaurantByCuisineSuccess() {
        String cuisine = "Italian cuisine";
        Restaurant restaurant = createRestaurantEntity("Thalia", "Italian cuisine");
        RestaurantDTO restaurantDTO = createRestaurantDTO("Thalia", "Italian cuisine");

        when(restaurantRepository.findByCuisineIgnoreCase(cuisine)).thenReturn(List.of(restaurant));
        when(objectMapper.convertValue(restaurant, RestaurantDTO.class)).thenReturn(restaurantDTO);

        List<RestaurantDTO> result = restaurantService.findRestaurantByCuisine(cuisine);
        assertEquals(restaurantDTO, restaurantDTO);
    }

    @Test
    void findRestaurantByCuisineNotFound() {
        String cuisine = "Italian cuisine";

        when(restaurantRepository.findByCuisineIgnoreCase(cuisine)).thenReturn(new ArrayList<>());
        List<RestaurantDTO> result = restaurantService.findRestaurantByCuisine(cuisine);
        assertTrue(result.isEmpty());
    }

    RestaurantDTO createRestaurantDTO(String name, String cuisine) {
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setName(name);
        restaurantDTO.setCuisine(cuisine);

        return restaurantDTO;
    }

    Restaurant createRestaurantEntity(String name, String cuisine) {
        Restaurant restaurant = new Restaurant();
        restaurant.getName();
        restaurant.getCuisine();

        return restaurant;
    }

}
