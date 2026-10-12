package com.ralph.disney_watchlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String title);

    // new for the filter feature
    List<Movie> findByWatched(boolean watched);

    List<Movie> findByTitleContainingIgnoreCaseAndWatched(String title, boolean watched);
}