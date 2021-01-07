package dto;

public class PlanetDTO {

    private String name;
    private String climate;
    private String terrain;
    private String population;

    public PlanetDTO(String name, String climate, String terrain, String population) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

}
