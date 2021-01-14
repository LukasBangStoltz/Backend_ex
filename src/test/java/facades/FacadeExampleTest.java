package facades;

import DTO.UserDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import utils.EMF_Creator;
import entities.Role;
import entities.User;
import errorhandling.PersonNotFoundException;
import java.util.List;
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
public class FacadeExampleTest {
    
    private static EntityManagerFactory emf;
    private static UserFacade facade;
    
    private static User user;
    private static User admin;
    private static User both;
    
    private static Address a1;
    private static Address a2;
    private static Address a3;
    
    private static CityInfo c1;
    private static CityInfo c2;
    
    private static Hobby h1;
    private static Hobby h2;
    
    public FacadeExampleTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
        EntityManager em = emf.createEntityManager();
        
        a1 = new Address("Ostegade 2");
        a2 = new Address("Kælkegade 4");
        a3 = new Address("Kosvinget 54");
        
        c1 = new CityInfo(3400, "Hillerød");
        c2 = new CityInfo(3480, "Fredensborg");
        
        h1 = new Hobby("Fodbold");
        h2 = new Hobby("Tennis");
        
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
//            em.createQuery("delete from User").executeUpdate();
//            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            
            a1.setCityInfo(c1);
            a2.setCityInfo(c1);
            a3.setCityInfo(c2);
            
            user = new User("user", "userpas", "userFname", "userLname", "45142241");
            user.addRole(userRole);
            user.addHobbies(h1);
            user.setAddress(a1);
            
            admin = new User("admin", "adminpas", "adminFname", "adminLname", "45874412");
            admin.addRole(adminRole);
            admin.addHobbies(h2);
            admin.setAddress(a2);
            
            both = new User("both", "bothpas", "bothFname", "bothLname", "65887410");
            both.addRole(userRole);
            both.addRole(adminRole);
            both.addHobbies(h1);
            both.addHobbies(h2);
            both.setAddress(a3);
            
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        
    }
    
    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void tesVverifyUser() throws AuthenticationException {
        User user = facade.getVeryfiedUser("admin", "adminpas");
        assertEquals("admin", admin.getUserName());
    }
    
    @Test
    public void testUserByPhone() throws PersonNotFoundException {
        UserDTO u = facade.getUserByPhone("45142241");
        String expectedfName = "userFname";
        assertEquals(expectedfName, u.fName);
    }
    
    @Test
    public void testGetAllUsersByHobby() {
        List<UserDTO> allUsers = facade.getAllUsersByHobby("Fodbold");
        int expectedSize = 2;
        assertEquals(expectedSize, allUsers.size());
    }
    
    @Test
    public void testCreateUser() {
        User user = new User("uName", "uPass", "fName", "lName", "phonenr");
        user.addHobbies(h2);
        user.setAddress(a1);
        
        UserDTO u = facade.createUser(new UserDTO(user));
        
        assertEquals("fName", u.fName);
        
    }
    
    @Test
    public void testEditUser() throws PersonNotFoundException {
        both.setfName("newFName");
        
        UserDTO eDTO = facade.editUser(new UserDTO(both));
        
        assertEquals(eDTO.fName, "newFName");
        
    }
    
    @Test
    public void testAddHobby() throws PersonNotFoundException {
        
        admin.addHobbies(h1);
        
        UserDTO uDTO = facade.addHobby(new UserDTO(admin));
        
        assertEquals(2, uDTO.hobby.size());
        
    }
    
    @Test
    public void testRemoveHobby() throws PersonNotFoundException {
       
        
        
        UserDTO userDTO = facade.deleteHobby("both", "Tennis");
        
        
        assertEquals(userDTO.hobby.size(), 1);
        
       
        
    }
    
    @Test
    public void testDeleteUser(){
        
        UserDTO userDTO = facade.deleteUser(new UserDTO(user));
        
        
        assertEquals(userDTO.fName, "userFname");
        
    }
    
 
}
