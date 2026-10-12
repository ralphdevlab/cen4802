package com.ralph.disney_watchlist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private Model model;

    private MovieController buildController() {
        MovieController controller = new MovieController();
        ReflectionTestUtils.setField(controller, "movieRepository", movieRepository);
        return controller;
    }

    @Test
    void listMovies_withNoSearchTerm_callsFindAll() {
        MovieController controller = buildController();
        when(movieRepository.findAll()).thenReturn(Arrays.asList(new Movie("Toy Story", false)));

        controller.listMovies(null, "all", model);

        verify(movieRepository, times(1)).findAll();
        verify(movieRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    @Test
    void listMovies_withSearchTerm_callsSearchMethod() {
        MovieController controller = buildController();
        List<Movie> matches = Arrays.asList(new Movie("Toy Story", false));
        when(movieRepository.findByTitleContainingIgnoreCase("Toy")).thenReturn(matches);

        controller.listMovies("Toy", "all", model);

        verify(movieRepository, times(1)).findByTitleContainingIgnoreCase("Toy");
        verify(movieRepository, never()).findAll();
    }

    @Test
    void listMovies_withWatchedFilter_callsFindByWatchedTrue() {
        MovieController controller = buildController();
        when(movieRepository.findByWatched(true)).thenReturn(Arrays.asList(new Movie("Frozen", true)));

        controller.listMovies(null, "watched", model);

        verify(movieRepository, times(1)).findByWatched(true);
        verify(movieRepository, never()).findAll();
    }

    @Test
    void listMovies_withUnwatchedFilter_callsFindByWatchedFalse() {
        MovieController controller = buildController();
        when(movieRepository.findByWatched(false)).thenReturn(Arrays.asList(new Movie("Up", false)));

        controller.listMovies(null, "unwatched", model);

        verify(movieRepository, times(1)).findByWatched(false);
        verify(movieRepository, never()).findByWatched(true);
    }

    @Test
    void listMovies_withWatchedFilterAndSearch_callsCombinedMethod() {
        MovieController controller = buildController();
        when(movieRepository.findByTitleContainingIgnoreCaseAndWatched("Toy", true))
                .thenReturn(Arrays.asList(new Movie("Toy Story", true)));

        controller.listMovies("Toy", "watched", model);

        verify(movieRepository, times(1)).findByTitleContainingIgnoreCaseAndWatched("Toy", true);
        verify(movieRepository, never()).findByWatched(anyBoolean());
    }

    @Test
    void listMovies_withUnknownFilter_fallsBackToAllMovies() {
        MovieController controller = buildController();
        when(movieRepository.findAll()).thenReturn(Arrays.asList(new Movie("Toy Story", false)));

        controller.listMovies(null, "banana", model);

        verify(movieRepository, times(1)).findAll();
        verify(movieRepository, never()).findByWatched(anyBoolean());
    }
}