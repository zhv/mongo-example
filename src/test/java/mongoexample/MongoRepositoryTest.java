package mongoexample;

import com.mongodb.MongoClient;
import mongoexample.model.Address;
import mongoexample.model.Person;
import mongoexample.model.QPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author zhv
 * @since 24.01.2018
 */
@ContextConfiguration(classes = MongoRepositoryTest.Config.class)
public class MongoRepositoryTest extends AbstractTestNGSpringContextTests {

    static final Logger LOG = LoggerFactory.getLogger(MongoRepositoryTest.class);

    @EnableMongoRepositories(basePackageClasses = PersonRepository.class)
    @Configuration
    public static class Config {

        @Bean
        MongoClient mongoClient() throws Exception {
            return new MongoClient("localhost");
        }

        @Bean
        MongoDbFactory mongoDbFactory(MongoClient mongo) {
            return new SimpleMongoDbFactory(mongo, "test");
        }

        @Bean
        MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
            return new MongoTemplate(mongoDbFactory);
        }
    }

    final int MULTIPLIER = 10;

    @Autowired
    PersonRepository personRepository;

    @BeforeClass
    public void setUp() throws Exception {
        personRepository.deleteAll();

        List<Person> people = IntStream.range(0, 100 * MULTIPLIER)
                .mapToObj(i -> Person.builder()
                        .firstname("Mob" + i)
                        .address(Address.builder()
                                .city("New York")
                                .street("Wall Street")
                                .apartment(i)
                                .build())
                        .build())
                .collect(Collectors.toList());
        personRepository.save(people);

        people = IntStream.range(100 * MULTIPLIER, 140 * MULTIPLIER)
                .mapToObj(i -> Person.builder()
                        .firstname("Mob" + i)
                        .address(Address.builder()
                                .city("New York")
                                .street("Broadway")
                                .apartment(i)
                                .build())
                        .build())
                .collect(Collectors.toList());
        personRepository.save(people);
    }

    @Test
    public void testCount() throws Exception {
        Assert.assertEquals(personRepository.count(), 140 * MULTIPLIER);
    }

    @Test
    public void testCountByQueryMethod() throws Exception {
//        List<Person> people = personRepository.findAll();
//        LOG.debug("PEOPLE: {}", people);

        Assert.assertEquals(personRepository.countByAddressStreet("Wall Street"), 100 * MULTIPLIER);
        Assert.assertEquals(personRepository.countByAddressStreet("Broadway"), 40 * MULTIPLIER);
    }

    @Test
    public void testCountByJSONQuery() throws Exception {
        Assert.assertEquals(personRepository.countByQuery(100 * MULTIPLIER), 40 * MULTIPLIER);
    }

    @Test
    public void testCountByQueryDSL() throws Exception {
        QPerson person = new QPerson("person");
        Assert.assertEquals(personRepository.count(person.address.apartment.goe(100 * MULTIPLIER)), 40 * MULTIPLIER);
    }
}
