package com.portal.foodordering.repositories;

import com.portal.foodordering.models.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT restaurant FROM Restaurant restaurant WHERE LOWER(restaurant.name) = LOWER(:name)")
    Optional<Restaurant> findByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT restaurant FROM Restaurant restaurant WHERE LOWER(restaurant.cuisine) = LOWER(:cuisine)")
    List<Restaurant> findByCuisineIgnoreCase(@Param("cuisine") String cuisine);
}
