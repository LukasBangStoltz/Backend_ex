package facades;

import DTO.UserDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import utils.EMF_Creator;

import entities.Role;
import entities.User;
import errorhandling.PersonNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
@Disabled
public class FacadeExampleTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static User user;
    private static User admin;
    private static User both;

    private Address a1 = new Address("ostegade");
    private Address a2 = new Address("pikgade");

    private CityInfo cityInfo1 = new CityInfo(2250, "gentofte");
    private CityInfo cityInfo2 = new CityInfo(4422, "osteby");

    private Hobby h1 = new Hobby("fodbold");
    private Hobby h2 = new Hobby("håndbold");

    public FacadeExampleTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user = new User("user", "test", "navn", "enavn", "2424");
            user.addRole(userRole);
            admin = new User("admin", "test", "navnelort", "efterlort", "123123");
            admin.addRole(adminRole);
            both = new User("user_admin", "test", "navneost", "efernavnostjaja", "12421455");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            //System.out.println("Saved test data to database");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testVerifyUser() throws AuthenticationException {
        User user = facade.getVeryfiedUser("admin", "test");
        assertEquals("admin", admin.getUserName());
    }

    @Test
    public void testGetUserByPhone() throws PersonNotFoundException {
        User test123 = new User("uname", "upass", "fname", "lname", "22222222");

        UserDTO userDTO = facade.getUserByPhone("22222222");
        String expName = "fname";

        assertEquals(expName, userDTO.fName);
    }
}
