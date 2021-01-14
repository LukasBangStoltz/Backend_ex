/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import DTO.UserDTO;
import errorhandling.PersonNotFoundException;
import facades.UserFacade;
import java.awt.BorderLayout;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utils.EMF_Creator;
import utils.UserFacadeInterface;

/**
 *
 * @author lukas
 */
public class Tester {

    public static void main(String[] args) throws PersonNotFoundException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        UserFacade userFacade = UserFacade.getUserFacade(emf);

        User user1 = new User("jens", "pas", "jens", "efternavn", "4444");
        User user2 = new User("ole", "hemmeligt", "ole", "veddel", "2222");

        Hobby hobby1 = new Hobby("fodbold");
        Hobby hobby2 = new Hobby("håndbold");
        Hobby hobby3 = new Hobby("tennis");
        Hobby hobby4 = new Hobby("badminton");

        Address address2 = new Address("grimvej");
        Address address1 = new Address("flotvej");
        CityInfo cityInfo1 = new CityInfo(2200, "osteby");
        Role role = new Role("user");

        user1.addHobbies(hobby1);
        user1.addHobbies(hobby2);
        user1.addHobbies(hobby3);
        user2.addHobbies(hobby1);

        address1.setCityInfo(cityInfo1);
        address2.setCityInfo(cityInfo1);

        user2.setAddress(address1);
        user1.setAddress(address2);

        user1.addRole(role);
        user2.addRole(role);

        em.getTransaction().begin();

        em.persist(role);
        em.persist(user1);
        em.persist(user2);

        em.getTransaction().commit();
        UserDTO tester123 = userFacade.getUserByPhone("2222");
        System.out.println(tester123.fName);

        List<UserDTO> dtolist = userFacade.getAllUsersByHobby("fodbold");

        System.out.println(dtolist);
        long counts = userFacade.getUserCountByHobby("fodbold");

        System.out.println("her står der forhåbentligt 2:" + counts);

        List<Long> zips = userFacade.getAllZipCodes();

        System.out.println("Her kommer zips:" + zips);
        
        
        
        

    }
}
