/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author lukas
 */
public class CombinedDTO {
    private String peopleName;
    private String planetName;
    private String speciesName;
    private String starshipName;
    private String vehicleName;

    public CombinedDTO(PeopleDTO people, PlanetDTO planet, SpeciesDTO species, StarshipDTO starship, VehicleDTO vehicle) {
        this.peopleName = people.getName();
        this.planetName = planet.getName();
        this.speciesName = species.getName();
        this.starshipName = starship.getName();
        this.vehicleName = vehicle.getName();
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getStartshipName() {
        return starshipName;
    }

    public void setStartshipName(String startshipName) {
        this.starshipName = startshipName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
    
    
    
}
