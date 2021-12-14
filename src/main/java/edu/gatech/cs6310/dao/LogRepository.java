package edu.gatech.cs6310.dao;

import edu.gatech.cs6310.entity.LogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LogRepository extends JpaRepository<LogDO, String> {
    /**
     * Find LogDO Object from the type.
     * @param type Log's type.
     * @return Return a list of LogDO.
     */
    List<LogDO> findByType(String type);

    /**
     * Find LogDO Object from the type and identifier.
     * @param type Log's type.
     * @param identifier Log's identifier.
     * @return Return a list of LogDO.
     */
    List<LogDO> findByTypeAndIdentifier(String type, String identifier);

}
