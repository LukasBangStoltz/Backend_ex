/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import entities.Hobby;

/**
 *
 * @author lukas
 */
public class HobbyDTO {

    private String name;

    public HobbyDTO() {
    }

    public HobbyDTO(Hobby hobby) {
        this.name = hobby.getName();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
