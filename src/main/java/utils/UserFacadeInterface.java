/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import DTO.UserDTO;
import errorhandling.PersonNotFoundException;
import java.util.List;

/**
 *
 * @author lukas
 */
public interface UserFacadeInterface {

    public abstract UserDTO getUserByPhone(String phone) throws PersonNotFoundException;

    public abstract List<UserDTO> getAllUsersByHobby(String hobby);

    public abstract List<UserDTO> getAllUsersByCity(String city);

    public abstract int getUserCountByHobby(String hobby);

    public abstract List<Long> getAllZipCodes();

    public abstract UserDTO createUser(UserDTO userDTO);

    public abstract UserDTO editUser(UserDTO userDTO);

    public abstract UserDTO deleteUser(String userName);

}
