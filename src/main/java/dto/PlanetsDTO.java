package dto;


//Håndtering af array MED NAVN. Navnet på listen skal have samme navn. 
public class PlanetsDTO {
    
    private Object[] results;

    public PlanetsDTO(Object[] results) {
        this.results = results;
    }

    public Object[] getResults() {
        return results;
    }

    public void setResults(Object[] results) {
        this.results = results;
    }

}