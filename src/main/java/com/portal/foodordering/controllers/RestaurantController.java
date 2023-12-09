package com.portal.foodordering.controllers;

import com.portal.foodordering.models.dtos.RestaurantDTO;
import com.portal.foodordering.serivces.interfaces.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@Validated @RequestBody RestaurantDTO restaurantDTO) {
        return ResponseEntity.ok(restaurantService.createRestaurant(restaurantDTO));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurant() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO restaurantDTO) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.deleteRestaurant(id));
    }

    @GetMapping("/restaurant_by_id")
    public ResponseEntity<RestaurantDTO> findRestaurantById(@RequestParam Long id) {
        return ResponseEntity.ok(restaurantService.findRestaurantById(id));
    }

    @GetMapping("/restaurant_by_name")
    public ResponseEntity<RestaurantDTO> findRestaurantByName(@RequestParam String name) {
        return ResponseEntity.ok(restaurantService.findRestaurantByName(name));
    }

    @GetMapping("/restaurant_by_cuisine")
    public ResponseEntity<RestaurantDTO> findRestaurantByCuisine(@RequestParam String cuisine) {
        return ResponseEntity.ok(restaurantService.findRestaurantByCuisine(cuisine));
    }
}
