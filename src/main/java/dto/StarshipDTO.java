package dto;

public class StarshipDTO {

    private String name;
    private String model;
    private int cost_in_credits;

    public StarshipDTO(String name, String model, int cost_in_credits) {
        this.name = name;
        this.model = model;
        this.cost_in_credits = cost_in_credits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCost_in_credits() {
        return cost_in_credits;
    }

    public void setCost_in_credits(int cost_in_credits) {
        this.cost_in_credits = cost_in_credits;
    }

}
