package com.ralph.disney_watchlist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final MovieRepository movieRepository;

    public DataLoader(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() == 0) {
            log.debug("Database is empty, loading movies from movies.txt");
            ClassPathResource resource = new ClassPathResource("movies.txt");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    movieRepository.save(new Movie(line.trim(), false));
                    count++;
                }
            }
            reader.close();

            log.info("Loaded {} Disney movies into the database.", count);
        } else {
            log.info("Database already has {} movies, skipping load", movieRepository.count());
        }
    }
}