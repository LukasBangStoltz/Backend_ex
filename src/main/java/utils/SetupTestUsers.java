package utils;


import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Role;
import entities.User;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

  public static void setUpUsers() {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords
    
    
    User user = new User("user", "testuser", "lars", "efternavn", "424");
    User admin = new User("admin", "testadmin", "jens", "efternavn", "2424");
    User both = new User("user_admin", "testuseradmin", "ostefar", "efternavn", "24424");
      CityInfo cityInfo = new CityInfo(2920, "Charlottenlund");
      Address address = new Address("Hovmarksvej");
      cityInfo.addAddresses(address);
      Hobby hobby = new Hobby("Fodbold");
      Hobby hobby2 = new Hobby("Tennis");
      user.addHobbies(hobby);
      user.setAddress(address);
      admin.addHobbies(hobby);
      admin.addHobbies(hobby2);
      admin.setAddress(address);
      both.addHobbies(hobby);
      both.setAddress(address);
      
      
      
    if(admin.getUserPass().equals("test")||user.getUserPass().equals("test")||both.getUserPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    user.addRole(userRole);
    admin.addRole(adminRole);
    both.addRole(userRole);
    both.addRole(adminRole);
    em.persist(userRole);
    em.persist(adminRole);
    em.persist(user);
    em.persist(admin);
    em.persist(both);
    em.getTransaction().commit();
    System.out.println("PW: " + user.getUserPass());
    System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
    System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
    System.out.println("Created TEST Users");
   
  }

}
