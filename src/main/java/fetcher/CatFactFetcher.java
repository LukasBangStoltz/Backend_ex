/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetcher;

import DTO.CatFactDTO;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;

/**
 *
 * @author lukas
 */
public class CatFactFetcher {

    private static String CATFACT_URL = "https://cat-fact.herokuapp.com/facts";
    
    public static String catResponse(ExecutorService threadPool, Gson gson) throws InterruptedException, ExecutionException, TimeoutException, IOException{
        
        Callable<CatFactDTO[]> catTask = new Callable<CatFactDTO[]>() {
        @Override
        public CatFactDTO[] call() throws Exception{
            String catFacts = HttpUtils.fetchData(CATFACT_URL);
            CatFactDTO[] catFactsDTO = gson.fromJson(catFacts, CatFactDTO[].class);
            
            
            return catFactsDTO;
        }
    };
        
        Future<CatFactDTO[]> futureFacts = threadPool.submit(catTask);
        CatFactDTO[] catFacts = futureFacts.get(2, TimeUnit.SECONDS);
        String catFactsJson = gson.toJson(catFacts);
        
        return(catFactsJson);
        
    }

}
