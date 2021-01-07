package fetcher;

import com.google.gson.Gson;
import dto.CombinedDTO;
import dto.PeopleDTO;
import dto.PlanetDTO;
import dto.SpeciesDTO;
import dto.StarshipDTO;
import dto.VehicleDTO;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;

public class StarWarsFetcher {
    private static final String PEOPLE_URL = "https://swapi.dev/api/people/1/";
    private static final String PLANET_URL = "https://swapi.dev/api/planets/3/";
    private static final String SPECIES_URL = "https://swapi.dev/api/species/9/";
    private static final String STARSHIP_URL = "https://swapi.dev/api/starships/3/";
    private static final String VEHICLE_URL = "https://swapi.dev/api/vehicles/4/";

    public static String responseFromExternalServersParrallel(ExecutorService threadPool, Gson gson) throws InterruptedException, ExecutionException, TimeoutException {

        Callable<PeopleDTO> peopleTask = new Callable<PeopleDTO>() {
            @Override
            public PeopleDTO call() throws Exception {
                String people = HttpUtils.fetchData(PEOPLE_URL);
                PeopleDTO peopleDTO = gson.fromJson(people, PeopleDTO.class);

                return peopleDTO;
            }
        };

        Callable<PlanetDTO> planetTask = new Callable<PlanetDTO>() {
            @Override
            public PlanetDTO call() throws Exception {
                String planet = HttpUtils.fetchData(PLANET_URL);
                PlanetDTO planetDTO = gson.fromJson(planet, PlanetDTO.class);

                return planetDTO;
            }
        };

        Callable<SpeciesDTO> speciesTask = new Callable<SpeciesDTO>() {
            @Override
            public SpeciesDTO call() throws Exception {
                String species = HttpUtils.fetchData(SPECIES_URL);
                SpeciesDTO speciesDTO = gson.fromJson(species, SpeciesDTO.class);

                return speciesDTO;
            }
        };

        Callable<StarshipDTO> starshipTask = new Callable<StarshipDTO>() {
            @Override
            public StarshipDTO call() throws Exception {
                String starship = HttpUtils.fetchData(STARSHIP_URL);
                StarshipDTO starshipDTO = gson.fromJson(starship, StarshipDTO.class);

                return starshipDTO;
            }
        };

        Callable<VehicleDTO> vehicleTask = new Callable<VehicleDTO>() {
            @Override
            public VehicleDTO call() throws Exception {
                String vehicle = HttpUtils.fetchData(VEHICLE_URL);
                VehicleDTO vehicleDTO = gson.fromJson(vehicle, VehicleDTO.class);

                return vehicleDTO;
            }
        };
        Future<PeopleDTO> futurePeople = threadPool.submit(peopleTask);
        Future<PlanetDTO> futurePlanet = threadPool.submit(planetTask);
        Future<SpeciesDTO> futureSpecies = threadPool.submit(speciesTask);
        Future<StarshipDTO> futureStarship = threadPool.submit(starshipTask);
        Future<VehicleDTO> futureVehicle = threadPool.submit(vehicleTask);

        PeopleDTO people = futurePeople.get(2, TimeUnit.SECONDS);
        PlanetDTO planet = futurePlanet.get(2, TimeUnit.SECONDS);
        SpeciesDTO species = futureSpecies.get(2, TimeUnit.SECONDS);
        StarshipDTO starship = futureStarship.get(2, TimeUnit.SECONDS);
        VehicleDTO vehicle = futureVehicle.get(2, TimeUnit.SECONDS);

        CombinedDTO combinedDTO = new CombinedDTO(people, planet, species, starship, vehicle);
        String combinedJSON = gson.toJson(combinedDTO);
        
        return combinedJSON;
    }

}
