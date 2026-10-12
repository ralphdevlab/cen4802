package com.ralph.disney_watchlist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class MovieRepositoryFilterTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findByWatched_returnsOnlyWatchedMovies() {
        movieRepository.save(new Movie("Frozen", true));
        movieRepository.save(new Movie("Up", false));

        List<Movie> result = movieRepository.findByWatched(true);

        assertEquals(1, result.size());
        assertEquals("Frozen", result.get(0).getTitle());
    }

    @Test
    void findByWatched_returnsOnlyUnwatchedMovies() {
        movieRepository.save(new Movie("Frozen", true));
        movieRepository.save(new Movie("Up", false));

        List<Movie> result = movieRepository.findByWatched(false);

        assertEquals(1, result.size());
        assertEquals("Up", result.get(0).getTitle());
    }

    @Test
    void findByTitleAndWatched_combinesBothConditions() {
        movieRepository.save(new Movie("Toy Story", true));
        movieRepository.save(new Movie("Toy Story 2", false));
        movieRepository.save(new Movie("Up", true));

        List<Movie> result = movieRepository.findByTitleContainingIgnoreCaseAndWatched("toy", true);

        assertEquals(1, result.size());
        assertEquals("Toy Story", result.get(0).getTitle());
    }
}