package com.ralph.disney_watchlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movies")
    public String listMovies(@RequestParam(required = false) String search,
                             @RequestParam(required = false, defaultValue = "all") String filter,
                             Model model) {
        boolean hasSearch = search != null && !search.isBlank();
        List<Movie> movies;

        if (filter.equals("watched")) {
            movies = hasSearch
                    ? movieRepository.findByTitleContainingIgnoreCaseAndWatched(search, true)
                    // after bug
                    : movieRepository.findByWatched(false);
        } else if (filter.equals("unwatched")) {
            movies = hasSearch
                    ? movieRepository.findByTitleContainingIgnoreCaseAndWatched(search, false)
                    : movieRepository.findByWatched(false);
        } else {
            filter = "all";
            movies = hasSearch
                    ? movieRepository.findByTitleContainingIgnoreCase(search)
                    : movieRepository.findAll();
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
        Movie movie = movieRepository.findById(id).orElseThrow();
        movie.setWatched(!movie.isWatched());
        movieRepository.save(movie);
        return "redirect:/movies?filter=" + filter;
    }

    @PostMapping("/movies/add")
    public String addMovie(@RequestParam String title) {
        movieRepository.save(new Movie(title, false));
        return "redirect:/movies";
    }
}