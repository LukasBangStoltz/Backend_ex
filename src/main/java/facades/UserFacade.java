package facades;

import DTO.HobbyDTO;
import DTO.UserDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Role;

import entities.User;
import errorhandling.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import security.errorhandling.AuthenticationException;
import utils.UserFacadeInterface;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade implements UserFacadeInterface {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public UserDTO getUserByPhone(String phone) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();

        try {
            Query q1 = em.createQuery("SELECT u FROM User u WHERE u.phone = :phone");
            q1.setParameter("phone", phone);
            User user = (User) q1.getSingleResult();
            if (user.getfName() == null) {
                throw new PersonNotFoundException("No person, with given phonenumber in database");
            }
            return new UserDTO(user);
        } finally {
            em.close();
        }

    }

    @Override
    public List<UserDTO> getAllUsersByHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT u FROM User u JOIN u.hobbyList h WHERE h.name = :hobby", User.class);
            query.setParameter("hobby", hobby);
            List<User> userList = query.getResultList();
            List<UserDTO> userDTOs = new ArrayList();

            for (User user : userList) {
                userDTOs.add(new UserDTO(user));
            }

            return userDTOs;
        } finally {
            em.close();
        }
    }

    @Override
    public List<UserDTO> getAllUsersByCity(String city) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT u FROM User u JOIN u.address.cityInfo c WHERE c.city = :city", User.class);
            query.setParameter("city", city);
            List<User> userList = query.getResultList();
            List<UserDTO> userDTOs = new ArrayList();

            for (User user : userList) {
                userDTOs.add(new UserDTO(user));
            }

            return userDTOs;

        } finally {
            em.close();
        }
    }

    @Override
    public long getUserCountByHobby(String hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(u) FROM User u JOIN u.hobbyList hobbies WHERE hobbies.name = :hobby", User.class);
            query.setParameter("hobby", hobby);
            long count = (long) query.getSingleResult();

            return count;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Long> getAllZipCodes() {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT c FROM CityInfo c", CityInfo.class);
            List<CityInfo> cityInfoList = query.getResultList();
            List<Long> listOfZips = new ArrayList();

            for (CityInfo info : cityInfoList) {
                listOfZips.add(info.getZip());
            }

            return listOfZips;
        } finally {
            em.close();
        }

    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        EntityManager em = emf.createEntityManager();

        User user = new User(userDTO.userName, userDTO.userPass, userDTO.fName, userDTO.lName, userDTO.phone);
        Address address;
        Hobby h;
        try {
            try {

                Query q1 = em.createQuery("SELECT a FROM Address a WHERE a.street = :street", Address.class);
                q1.setParameter("street", userDTO.street);
                address = (Address) q1.getSingleResult();
            } catch (NoResultException e) {
                address = null;
            }

            if (address == null) {
                address = new Address(userDTO.street);
            }
            user.setAddress(address);

            CityInfo cityInfo = em.find(CityInfo.class, userDTO.zip);
            if (cityInfo == null) {
                cityInfo = new CityInfo(userDTO.zip, userDTO.city);
            }
            address.setCityInfo(cityInfo);

            for (HobbyDTO hobby : userDTO.hobby) {
                try {

                    Query q2 = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :hobby", Hobby.class);
                    q2.setParameter("hobby", hobby.name);
                    h = (Hobby) q2.getSingleResult();

                } catch (NoResultException e) {
                    h = null;
                }
                if (h == null) {
                    h = new Hobby(hobby.name);
                }
                user.addHobbies(h);
            }
            Role role = em.find(Role.class, "user");
            user.addRole(role);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return new UserDTO(user);

        } finally {
            em.close();
        }
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Address address;
        try {
            User user = em.find(User.class, userDTO.userName);
            if (user == null) {
                throw new PersonNotFoundException("Person not found in DB");
            }

            user.setPhone(userDTO.phone);
            user.setfName(userDTO.fName);
            user.setlName(userDTO.lName);

            try {

                Query q1 = em.createQuery("SELECT a FROM Address a WHERE a.street = :street", Address.class);
                q1.setParameter("street", userDTO.street);
                address = (Address) q1.getSingleResult();
            } catch (NoResultException e) {
                address = null;
            }
            if (address == null) {
                address = new Address(userDTO.street);
            }
            user.setAddress(address);

            CityInfo cityInfo = em.find(CityInfo.class, userDTO.zip);
            if (cityInfo == null) {
                cityInfo = new CityInfo(userDTO.zip, userDTO.city);
            }
            address.setCityInfo(cityInfo);

            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();

            return new UserDTO(user);
        } finally {
            em.close();
        }

    }

    @Override
    public UserDTO addHobby(UserDTO userDTO) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Hobby h;
        try {
            User user = em.find(User.class, userDTO.userName);
            if (user == null) {
                throw new PersonNotFoundException("Person not found in DB");
            }

            HobbyDTO hobby = userDTO.hobby.get(userDTO.hobby.size() - 1);

            try {

                Query q2 = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :hobby", Hobby.class);
                q2.setParameter("hobby", hobby.name);
                h = (Hobby) q2.getSingleResult();
            } catch (NoResultException e) {
                h = null;
            }
            if (h == null) {
                h = new Hobby(hobby.name);

            }
            user.addHobbies(h);

            em.getTransaction().begin();
            em.merge(user);

            em.getTransaction().commit();

            return new UserDTO(user);
        } finally {
            em.close();
        }
    }

    @Override
    public UserDTO deleteHobby(String userName, String hobby) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Hobby dbHobby;
        try {
            User user = em.find(User.class, userName);
            if (user == null) {
                throw new PersonNotFoundException("Person not found in DB ");
            }

            try {
                Query query = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :hobby");
                query.setParameter("hobby", hobby);
                dbHobby = (Hobby) query.getSingleResult();

            } catch (NoResultException e) {
                throw new PersonNotFoundException("Hobby not found");
            }

            user.removeHobbie(dbHobby);

            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();

            return new UserDTO(user);
        } finally {
            em.close();
        }

    }

    @Override
    public UserDTO deleteUser(UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();

        try {
            User user = em.find(User.class, userDTO.userName);

            for (Hobby h : user.getHobbyList()) {
                if (h.getUsers().size() <= 1) {
                    em.remove(h);
                } else {
                    h.getUsers().remove(h);
                }
            }

            if (user.getAddress().getUsers().size() <= 1) {
                em.remove(user.getAddress());

            } else {
                user.getAddress().getUsers().remove(user);
            }
            if (user.getAddress().getCityInfo().getAddresses().size() <= 1) {
                em.remove(user.getAddress().getCityInfo());
            } else {
                user.getAddress().getCityInfo().getAddresses().remove(user.getAddress());
            }

            em.getTransaction().begin();
            em.remove(user);

            em.getTransaction().commit();

            return new UserDTO(user);
        } finally {
            em.close();
        }

    }

}
