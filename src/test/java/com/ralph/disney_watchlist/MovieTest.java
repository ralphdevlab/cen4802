package com.ralph.disney_watchlist;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void newMovieStartsUnwatched() {
        Movie movie = new Movie("Toy Story", false);
        assertFalse(movie.isWatched());
    }

    @Test
    void togglingWatchedFlipsState() {
        Movie movie = new Movie("Toy Story", false);
        movie.setWatched(true);
        assertTrue(movie.isWatched());

        movie.setWatched(false);
        assertFalse(movie.isWatched());
    }
}