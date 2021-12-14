package edu.gatech.cs6310.dao;

import edu.gatech.cs6310.entity.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * Find Customer Object from the last name.
     * Reference:
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords
     * @param lastName Customer's last name.
     * @return Return a list of customer for this last name.
     */
    List<Customer> findByLastName(String lastName);


}
