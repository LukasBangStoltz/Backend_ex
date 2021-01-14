package rest;

import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Role;
import entities.User;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test

public class UserResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

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

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        a1 = new Address("Ostegade 2");
        a2 = new Address("Kælkegade 4");
        a3 = new Address("Kosvinget 54");

        c1 = new CityInfo(3400, "Hillerød");
        c2 = new CityInfo(3480, "Fredensborg");

        h1 = new Hobby("Fodbold");
        h2 = new Hobby("Tennis");
        em.getTransaction().begin();

        em.createNativeQuery("DELETE FROM user_roles").executeUpdate();
        em.createNativeQuery("DELETE FROM roles").executeUpdate();
        em.createNativeQuery("DELETE FROM HOBBY_users").executeUpdate();
        em.createNativeQuery("DELETE FROM HOBBY").executeUpdate();
        em.createNativeQuery("DELETE FROM users").executeUpdate();
        em.createNativeQuery("DELETE FROM ADDRESS").executeUpdate();
        em.createNativeQuery("DELETE FROM CITYINFO").executeUpdate();

        em.getTransaction().commit();
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

        try {
            em.getTransaction().begin();
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
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/users").then().statusCode(200);
    }

    @Test
    public void testGetUserByPhone() {
        given()
                .contentType("application/json")
                .get("/users/phonenumber/45142241").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("fName", equalTo("userFname"));
    }

    @Test
    public void testGetAllUsersByHobby() {
        given()
                .contentType("application/json")
                .get("/users/hobby/tennis").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2));
    }

    @Test
    public void testGetAllUsersByCity() {
        given()
                .contentType("application/json")
                .get("/users/city/Hillerød").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2));
    }

    @Test
    public void testGetUserCountByHobby() {
        given()
                .contentType("application/json")
                .get("/users/hobbycount/tennis").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }

    @Test
    public void testGetAllZipCodes() {
        given()
                .contentType("application/json")
                .get("/users/allzips").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(2));
    }

}
