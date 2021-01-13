/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import entities.Hobby;
import entities.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lukas
 */
public class UserDTO {

    public String userName;
    public String userPass;
    public String fName;
    public String lName;
    public String street;
    public List<HobbyDTO> hobby;
    public String phone;
    public long zip;
    public String city;

    public UserDTO(User user) {
        this.userName = user.getUserName();
        this.userPass = user.getUserPass();
        this.fName = user.getfName();
        this.lName = user.getlName();
        this.street = user.getAddress().getStreet();
        this.city = user.getAddress().getCityInfo().getCity();
        this.zip = user.getAddress().getCityInfo().getZip();
        this.phone = user.getPhone();

        this.hobby = new ArrayList();
        for (Hobby hobby : user.getHobbyList()) {
            this.hobby.add(new HobbyDTO(hobby));
        }
    }

}
