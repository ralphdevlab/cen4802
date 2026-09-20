package com.ralph.disney_watchlist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findByTitleContainingIgnoreCase_findsPartialMatch() {
        movieRepository.save(new Movie("Toy Story", false));
        movieRepository.save(new Movie("Toy Story 2", false));
        movieRepository.save(new Movie("The Lion King", false));

        List<Movie> results = movieRepository.findByTitleContainingIgnoreCase("Toy");

        assertEquals(2, results.size());
    }

    @Test
    void findByTitleContainingIgnoreCase_isCaseInsensitive() {
        movieRepository.save(new Movie("Toy Story", false));

        List<Movie> results = movieRepository.findByTitleContainingIgnoreCase("TOY STORY");

        assertEquals(1, results.size());
    }

    @Test
    void findByTitleContainingIgnoreCase_returnsEmptyWhenNoMatch() {
        movieRepository.save(new Movie("Toy Story", false));

        List<Movie> results = movieRepository.findByTitleContainingIgnoreCase("Nonexistent Movie");

        assertTrue(results.isEmpty());
    }
}