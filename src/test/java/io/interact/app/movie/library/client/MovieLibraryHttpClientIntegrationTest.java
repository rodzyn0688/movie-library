package io.interact.app.movie.library.client;

import io.interact.app.movie.library.mapper.MovieMapper;
import io.interact.app.movie.library.test.integration.GenerateCorectMovieOrPerson;
import lombok.val;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieLibraryHttpClientIntegrationTest extends GenerateCorectMovieOrPerson {

    private final static String SERVICE_URL = "http://localhost:8080";
    private static final MovieMapper MOVIE_MAPPER = MovieMapper.INSTANCE;
    private MovieLibraryHttpClient client;

    @Before
    public void setUp() throws Exception {
        this.client = new MovieLibraryHttpClient(SERVICE_URL);
    }

    @Test
    @Ignore
    public void shouldAddNewMovie() {
        // GIVEN
        val exampleActor1 = createCorrectPerson();
        val exampleActor2 = createCorrectPerson();
        val exampleMovie = createCorrectMovie(exampleActor1, exampleActor2);
        val exampleMovieDTO = MOVIE_MAPPER.entityToDto(exampleMovie);

        // WHEN
        val result = client.create(exampleMovieDTO);

        // THEN
        val exampleMovieDTOPersist = client.find(result);

        assertThat(exampleMovieDTOPersist).isPresent();
        assertThat(exampleMovieDTOPersist.get().getId()).isNotNull();
        assertThat(exampleMovieDTOPersist.get().getTitle()).isEqualTo(exampleMovieDTO.getTitle());
        assertThat(exampleMovieDTOPersist.get().getReleaseYear()).isEqualTo(exampleMovieDTO.getReleaseYear());
        assertThat(exampleMovieDTOPersist.get().getDuration()).isEqualTo(exampleMovieDTO.getDuration());
        assertThat(exampleMovieDTOPersist.get().getDescription()).isEqualTo(exampleMovieDTO.getDescription());

        assertThat(exampleMovieDTOPersist.get().getActors()).isNotNull().hasSize(2);

        assertThat(exampleMovieDTOPersist.get().getActors().get(0)).isNotNull();
        assertThat(exampleMovieDTOPersist.get().getActors().get(0).getId()).isNotNull();
        assertThat(exampleMovieDTOPersist.get().getActors().get(0).getFirstName()).isEqualTo(exampleActor1.getFirstName());
        assertThat(exampleMovieDTOPersist.get().getActors().get(0).getLastName()).isEqualTo(exampleActor1.getLastName());
        assertThat(exampleMovieDTOPersist.get().getActors().get(0).getBorn()).isEqualTo(exampleActor1.getBorn());
        assertThat(exampleMovieDTOPersist.get().getActors().get(0).getCurrentNationality()).isEqualTo(exampleActor1.getCurrentNationality());
        assertThat(exampleMovieDTOPersist.get().getActors().get(0).getDescription()).isEqualTo(exampleActor1.getDescription());

        assertThat(exampleMovieDTOPersist.get().getActors().get(1)).isNotNull();
        assertThat(exampleMovieDTOPersist.get().getActors().get(1).getId()).isNotNull();
        assertThat(exampleMovieDTOPersist.get().getActors().get(1).getFirstName()).isEqualTo(exampleActor2.getFirstName());
        assertThat(exampleMovieDTOPersist.get().getActors().get(1).getLastName()).isEqualTo(exampleActor2.getLastName());
        assertThat(exampleMovieDTOPersist.get().getActors().get(1).getBorn()).isEqualTo(exampleActor2.getBorn());
        assertThat(exampleMovieDTOPersist.get().getActors().get(1).getCurrentNationality()).isEqualTo(exampleActor2.getCurrentNationality());
        assertThat(exampleMovieDTOPersist.get().getActors().get(1).getDescription()).isEqualTo(exampleActor2.getDescription());
    }

    @Test
    @Ignore
    public void shouldUpdateMovie() {
        // GIVEN
        val exampleActor1 = createCorrectPerson();
        val exampleActor2 = createCorrectPerson();
        val exampleMovie = createCorrectMovie(exampleActor1, exampleActor2);
        val exampleMovieDTO = MOVIE_MAPPER.entityToDto(exampleMovie);
        val resultId = client.create(exampleMovieDTO);
        val exampleMovieDTOPersist = client.find(resultId).get();
        val newTitle = createCorrectTitle();
        val oldTitle = exampleMovieDTOPersist.getTitle();
        exampleMovieDTOPersist.setTitle(newTitle);
        // WHEN
        val resultAfterUpdatedId = client.update(resultId, exampleMovieDTOPersist);

        // THEN
        val exampleMovieDTOUpdated = client.find(resultAfterUpdatedId);
        assertThat(resultAfterUpdatedId).isEqualTo(resultId);
        assertThat(exampleMovieDTOUpdated).isPresent();
        assertThat(exampleMovieDTOUpdated.get().getTitle()).isEqualTo(newTitle);
    }

    @Test
    @Ignore
    public void shouldDeleteMovie() {
        // GIVEN
        val exampleActor1 = createCorrectPerson();
        val exampleActor2 = createCorrectPerson();
        val exampleMovie = createCorrectMovie(exampleActor1, exampleActor2);
        val exampleMovieDTO = MOVIE_MAPPER.entityToDto(exampleMovie);
        val resultId = client.create(exampleMovieDTO);
        // WHEN
        val status = client.delete(resultId);

        // THEN
        assertThat(status).isTrue();
    }

}
