package com.microservice.moviecatalogservice.resources;

import com.microservice.moviecatalogservice.models.CatalogItem;
import com.microservice.moviecatalogservice.models.Movie;
import com.microservice.moviecatalogservice.models.Rating;
import com.microservice.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate; // RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {


//        restTemplate.getForObject("http://localhost:8081/movies/foo", Movie.class);
        // get all rated movie IDs
        UserRating ratings = restTemplate.getForObject("http://localhost:8081/ratingsdata/users/foo" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            // For each movie ID, call movie info service and get details
            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
            // put them all togetherreturn new CatalogItem(movie.getName(),  "Test",  rating.getRating());
            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        })
                .collect(Collectors.toList());
    }
}

/*
            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();
 */
