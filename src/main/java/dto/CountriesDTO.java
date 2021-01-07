package dto;


//Håndtering af array UDEN NAVN. De attributer der skal trækkes SKAL navngives 
public class CountriesDTO {
    
    public String Country;
    public String Slug;

    public CountriesDTO(String Country, String Slug) {
        this.Country = Country;
        this.Slug = Slug;
    }

}