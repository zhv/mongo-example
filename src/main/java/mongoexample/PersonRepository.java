package mongoexample;

import mongoexample.model.Person;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author zhv
 * @since 24.01.2018
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, UUID>, QueryDslPredicateExecutor<Person> {

    @Override
    List<Person> findAll();

    long countByAddressStreet(String city);

    @Query(value = "{ 'address.apartment' : { $gte: ?0 } }", count = true)
    long countByQuery(int apartment);
}
