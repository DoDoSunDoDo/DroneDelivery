package edu.gatech.cs6310.dao;

import edu.gatech.cs6310.entity.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PilotRepository extends JpaRepository<Pilot, String> {

    /**
     * Find Pilot Object from the account name.
     * @param account The account Name of Pilot.
     * @return Return a list of customer for this account.
     */
    List<Pilot> findByAccount(String account);

    /**
     * Check if Pilot Object account exists.
     * @param account The account Name of Pilot.
     * @return Return True if it exists otherwise, false.
     */
    boolean existsByAccount(String account);
}
