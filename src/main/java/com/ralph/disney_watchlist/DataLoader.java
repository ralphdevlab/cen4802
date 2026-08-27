package com.ralph.disney_watchlist;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class DataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;

    public DataLoader(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() == 0) {
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

            System.out.println("Loaded " + count + " Disney movies into the database.");
        }
    }
}