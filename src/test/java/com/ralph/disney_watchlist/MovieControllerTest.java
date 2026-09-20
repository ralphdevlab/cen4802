package com.ralph.disney_watchlist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private Model model;

    @Test
    void listMovies_withNoSearchTerm_callsFindAll() {
        MovieController controller = new MovieController();
        ReflectionTestUtils.setField(controller, "movieRepository", movieRepository);

        when(movieRepository.findAll()).thenReturn(Arrays.asList(new Movie("Toy Story", false)));

        controller.listMovies(null, model);

        verify(movieRepository, times(1)).findAll();
        verify(movieRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    @Test
    void listMovies_withSearchTerm_callsSearchMethod() {
        MovieController controller = new MovieController();
        ReflectionTestUtils.setField(controller, "movieRepository", movieRepository);

        List<Movie> matches = Arrays.asList(new Movie("Toy Story", false));
        when(movieRepository.findByTitleContainingIgnoreCase("Toy")).thenReturn(matches);

        controller.listMovies("Toy", model);

        verify(movieRepository, times(1)).findByTitleContainingIgnoreCase("Toy");
        verify(movieRepository, never()).findAll();
    }
}