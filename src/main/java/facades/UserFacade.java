package facades;

import DTO.UserDTO;
import com.sun.org.apache.xalan.internal.res.XSLTErrorResources_ko;
import entities.User;
import errorhandling.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import security.errorhandling.AuthenticationException;
import utils.UserFacadeInterface;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade implements UserFacadeInterface{

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
        
        try{
            Query q1 = em.createQuery("SELECT u FROM User u WHERE u.phone = :phone");
            q1.setParameter("phone", phone);
            User user = (User) q1.getSingleResult();
            if(user.getfName()==null){
                throw new PersonNotFoundException("No person, with given phonenumber in database");
            }
            return new UserDTO(user);
        }finally{
            em.close();
        }
        
        
        
    }

    @Override
    public List<UserDTO> getAllUsersByHobby(String hobby) {
                EntityManager em = emf.createEntityManager();
                
        Query query = em.createQuery("SELECT u FROM User u JOIN u.hobbyList h WHERE h.name = :hobby", User.class);
        query.setParameter("hobby", hobby);
        List <User> userList= query.getResultList();
        
        List<UserDTO> temp = new ArrayList();
        
        return temp;
    }

    @Override
    public List<UserDTO> getAllUsersByCity(String city) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUserCountByHobby(String hobby) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Long> getAllZipCodes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserDTO editUser(UserDTO userDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserDTO deleteUser(String userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
    

