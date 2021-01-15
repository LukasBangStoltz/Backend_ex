package rest;

import DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;
import errorhandling.PersonNotFoundException;
import facades.FacadeExample;
import facades.UserFacade;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.SetupTestUsers;

/**
 * @author lam@cphbusiness.dk
 */
@Path("users")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ExecutorService ES = Executors.newCachedThreadPool();
    private static final UserFacade FACADE = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @Path("phonenumber/{phone}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUserByPhone(@PathParam("phone") String phone) throws PersonNotFoundException {
        UserDTO user = FACADE.getUserByPhone(phone);
        return GSON.toJson(user);

    }

    @Path("hobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUsersByHobby(@PathParam("hobby") String hobby) throws PersonNotFoundException {
        List<UserDTO> userList = FACADE.getAllUsersByHobby(hobby);
        return GSON.toJson(userList);

    }
    @Path("hobbycount/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUserCOuntByHobby(@PathParam("hobby") String hobby) {
        long myCount = FACADE.getUserCountByHobby(hobby);
        return "{\"count\":" + myCount + "}";

    }

    @Path("city/{city}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUsersByCity(@PathParam("city") String city) {
        List<UserDTO> userList = FACADE.getAllUsersByCity(city);
        return GSON.toJson(userList);

    }


    @Path("allzips")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllZips() {
        List<Long> zipList = FACADE.getAllZipCodes();

        return GSON.toJson(zipList) ;

    }
    
    @Path("createUser")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(String user) {
        UserDTO u = GSON.fromJson(user, UserDTO.class);
        u = FACADE.createUser(u);
        return Response.ok(u).build();
    }
    
    
    
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @Path("setupusers")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public void setUpUsers() {
        SetupTestUsers.setUpUsers();
    }
    
    @Path("catfacts")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCountries() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        String result = fetcher.CatFactFetcher.catResponse(ES, GSON);
        return result;
    }

}
