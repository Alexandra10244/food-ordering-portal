package com.portal.foodordering.repositories;

import com.portal.foodordering.models.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    @Query("SELECT item FROM Item item WHERE LOWER(item.name) = LOWER(:name)")
    Optional<Item> findItemByNameIgnoreCase(@Param("name") String name);
}
