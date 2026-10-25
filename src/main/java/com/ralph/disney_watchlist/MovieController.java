package com.ralph.disney_watchlist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class MovieController {

    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies")
    public String listMovies(@RequestParam(required = false) String search,
                             @RequestParam(required = false, defaultValue = "all") String filter,
                             Model model) {
        boolean hasSearch = search != null && !search.isBlank();
        log.debug("Listing movies: filter={}, search='{}'", filter, hasSearch ? search : "");
        List<Movie> movies;

        if (filter.equals("watched")) {
            movies = hasSearch
                    ? movieRepository.findByTitleContainingIgnoreCaseAndWatched(search, true)
                    : movieRepository.findByWatched(true);
        } else if (filter.equals("unwatched")) {
            movies = hasSearch
                    ? movieRepository.findByTitleContainingIgnoreCaseAndWatched(search, false)
                    : movieRepository.findByWatched(false);
        } else {
            if (!filter.equals("all")) {
                log.warn("Unknown filter '{}' received, falling back to 'all'", filter);
            }
            filter = "all";
            movies = hasSearch
                    ? movieRepository.findByTitleContainingIgnoreCase(search)
                    : movieRepository.findAll();
        }

        log.info("GET /movies filter={} search='{}' returned {} movies",
                filter, hasSearch ? search : "", movies.size());
        if (hasSearch && movies.isEmpty()) {
            log.warn("Search for '{}' (filter={}) returned no results", search, filter);
        }

        model.addAttribute("movies", movies);
        model.addAttribute("search", search);
        model.addAttribute("filter", filter);
        model.addAttribute("movieCount", movies.size());
        return "movies";
    }

    @PostMapping("/movies/toggle/{id}")
    public String toggleWatched(@org.springframework.web.bind.annotation.PathVariable Long id,
                                @RequestParam(required = false, defaultValue = "all") String filter) {
        log.debug("Toggle requested for movie id={} (filter={})", id, filter);
        Movie movie;
        try {
            movie = movieRepository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            log.error("Toggle failed: no movie exists with id={} (filter={})", id, filter, e);
            return "redirect:/movies?filter=" + filter;
        }
        movie.setWatched(!movie.isWatched());
        movieRepository.save(movie);
        log.info("Toggled movie id={} title='{}' watched={}", id, movie.getTitle(), movie.isWatched());
        return "redirect:/movies?filter=" + filter;
    }

    @PostMapping("/movies/add")
    public String addMovie(@RequestParam String title) {
        if (title.isBlank()) {
            log.warn("Rejected add request: title was blank");
            return "redirect:/movies";
        }
        Movie saved = movieRepository.save(new Movie(title.trim(), false));
        log.info("Added movie id={} title='{}'", saved.getId(), saved.getTitle());
        return "redirect:/movies";
    }
}