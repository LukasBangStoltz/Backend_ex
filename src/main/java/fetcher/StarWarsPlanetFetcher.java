package fetcher;

import com.google.gson.Gson;
import dto.CombinedDTO;
import dto.PeopleDTO;
import dto.PlanetDTO;
import dto.PlanetsDTO;
import dto.SpeciesDTO;
import dto.StarshipDTO;
import dto.VehicleDTO;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;


public class StarWarsPlanetFetcher {
    private static String STARWARS_URL = "https://swapi.dev/api/planets/";

    public static String responseFromExternalServersSequential(ExecutorService threadPool, Gson gson) throws InterruptedException, ExecutionException, TimeoutException, IOException {

        Callable<PlanetsDTO> planetsTask = new Callable<PlanetsDTO>() {
            @Override
            public PlanetsDTO call() throws Exception {
                String planets = HttpUtils.fetchData(STARWARS_URL);
                PlanetsDTO planetsDTO = gson.fromJson(planets, PlanetsDTO.class);

                return planetsDTO;
            }
        };
        
        Future<PlanetsDTO> futurePlanets = threadPool.submit(planetsTask);

        PlanetsDTO planets = futurePlanets.get(2, TimeUnit.SECONDS);

        String planetsJson = gson.toJson(planets);
        
        return planetsJson;
    }
}