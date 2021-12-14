package edu.gatech.cs6310.dao;

import edu.gatech.cs6310.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    /**
     * Find the store by the name.
     * @param name The name of the store.
     * @return The store for the name.
     */
    List<Store> findByName(String name);
}
