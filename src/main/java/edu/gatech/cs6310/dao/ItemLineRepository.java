package edu.gatech.cs6310.dao;

import edu.gatech.cs6310.entity.ItemLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemLineRepository extends JpaRepository<ItemLine, String> {

}
