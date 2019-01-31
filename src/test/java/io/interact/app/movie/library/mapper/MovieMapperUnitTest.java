package io.interact.app.movie.library.mapper;

import io.interact.app.movie.library.api.MovieService.MovieDTO;
import io.interact.app.movie.library.api.MovieService.MovieDTO.ActorDTO;
import io.interact.app.movie.library.core.model.BasicAbstractEntity;
import io.interact.app.movie.library.core.model.Movie;
import io.interact.app.movie.library.core.model.Person;
import lombok.SneakyThrows;
import lombok.val;
import org.joda.time.DateTime;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieMapperUnitTest {

    @Test
    public void shouldMapMovieEntityToDto() {
        // GIVEN
        val movie = new Movie();
        movie.setTitle("title 1");
        movie.setReleaseYear(2007L);
        movie.setDuration(Duration.ofMinutes(129L));
        movie.setDescription("description to title 1");
        this.setId(movie, 11L);

        val actor1 = new Person();
        actor1.setFirstName("actor first name");
        actor1.setLastName("actor last name");
        actor1.setBorn(new DateTime(1981, 4, 12, 0, 0, 0, 0).toDate());
        actor1.setCurrentNationality(Locale.US);
        this.setId(actor1, 5L);

        val actor2 = new Person();
        actor2.setFirstName("actor first name");
        actor2.setLastName("actor last name");
        actor2.setBorn(new DateTime(1981, 4, 12, 0, 0, 0, 0).toDate());
        actor2.setCurrentNationality(Locale.US);
        actor2.setDescription("the best actor ever");
        this.setId(actor2, 6L);

        movie.setActors(Arrays.asList(actor1, actor2));

        // WHEN
        val result = MovieMapper.INSTANCE.entityToDto(movie);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(movie.getId());
        assertThat(result.getTitle()).isEqualTo(movie.getTitle());
        assertThat(result.getReleaseYear()).isEqualTo(movie.getReleaseYear());
        assertThat(result.getDuration()).isEqualTo(movie.getDuration());
        assertThat(result.getDescription()).isEqualTo(movie.getDescription());

        assertThat(result.getActors()).isNotNull().hasSize(2);

        assertThat(result.getActors().get(0)).isNotNull();
        assertThat(result.getActors().get(0).getId()).isEqualTo(actor1.getId());
        assertThat(result.getActors().get(0).getFirstName()).isEqualTo(actor1.getFirstName());
        assertThat(result.getActors().get(0).getLastName()).isEqualTo(actor1.getLastName());
        assertThat(result.getActors().get(0).getBorn()).isEqualTo(actor1.getBorn());
        assertThat(result.getActors().get(0).getCurrentNationality()).isEqualTo(actor1.getCurrentNationality());
        assertThat(result.getActors().get(0).getDescription()).isEqualTo(actor1.getDescription());

        assertThat(result.getActors().get(1)).isNotNull();
        assertThat(result.getActors().get(1).getId()).isEqualTo(actor2.getId());
        assertThat(result.getActors().get(1).getFirstName()).isEqualTo(actor2.getFirstName());
        assertThat(result.getActors().get(1).getLastName()).isEqualTo(actor2.getLastName());
        assertThat(result.getActors().get(1).getBorn()).isEqualTo(actor2.getBorn());
        assertThat(result.getActors().get(1).getCurrentNationality()).isEqualTo(actor2.getCurrentNationality());
        assertThat(result.getActors().get(1).getDescription()).isEqualTo(actor2.getDescription());
    }

    @Test
    public void shouldMapMovieDtoToEntity() {
        // GIVEN
        val movieDTO = new MovieDTO();
        movieDTO.setTitle("title 1");
        movieDTO.setReleaseYear(2007L);
        movieDTO.setDuration(Duration.ofMinutes(129L));
        movieDTO.setDescription("description to title 1");
        movieDTO.setId(11L);

        val actor1DTO = new ActorDTO();
        actor1DTO.setFirstName("actor first name");
        actor1DTO.setLastName("actor last name");
        actor1DTO.setBorn(new DateTime(1981, 4, 12, 0, 0, 0, 0).toDate());
        actor1DTO.setCurrentNationality(Locale.US);
        actor1DTO.setId(5L);

        val actor2DTO = new ActorDTO();
        actor2DTO.setFirstName("actor first name");
        actor2DTO.setLastName("actor last name");
        actor2DTO.setBorn(new DateTime(1981, 4, 12, 0, 0, 0, 0).toDate());
        actor2DTO.setCurrentNationality(Locale.US);
        actor2DTO.setDescription("the best actor ever");
        actor2DTO.setId(6L);

        movieDTO.setActors(Arrays.asList(actor1DTO, actor2DTO));

        // WHEN
        val result = MovieMapper.INSTANCE.dtoToEntity(movieDTO);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(movieDTO.getId());
        assertThat(result.getTitle()).isEqualTo(movieDTO.getTitle());
        assertThat(result.getReleaseYear()).isEqualTo(movieDTO.getReleaseYear());
        assertThat(result.getDuration()).isEqualTo(movieDTO.getDuration());
        assertThat(result.getDescription()).isEqualTo(movieDTO.getDescription());

        assertThat(result.getActors()).isNotNull().hasSize(2);

        assertThat(result.getActors().get(0)).isNotNull();
        assertThat(result.getActors().get(0).getId()).isEqualTo(actor1DTO.getId());
        assertThat(result.getActors().get(0).getFirstName()).isEqualTo(actor1DTO.getFirstName());
        assertThat(result.getActors().get(0).getLastName()).isEqualTo(actor1DTO.getLastName());
        assertThat(result.getActors().get(0).getBorn()).isEqualTo(actor1DTO.getBorn());
        assertThat(result.getActors().get(0).getCurrentNationality()).isEqualTo(actor1DTO.getCurrentNationality());
        assertThat(result.getActors().get(0).getDescription()).isEqualTo(actor1DTO.getDescription());

        assertThat(result.getActors().get(1)).isNotNull();
        assertThat(result.getActors().get(1).getId()).isEqualTo(actor2DTO.getId());
        assertThat(result.getActors().get(1).getFirstName()).isEqualTo(actor2DTO.getFirstName());
        assertThat(result.getActors().get(1).getLastName()).isEqualTo(actor2DTO.getLastName());
        assertThat(result.getActors().get(1).getBorn()).isEqualTo(actor2DTO.getBorn());
        assertThat(result.getActors().get(1).getCurrentNationality()).isEqualTo(actor2DTO.getCurrentNationality());
        assertThat(result.getActors().get(1).getDescription()).isEqualTo(actor2DTO.getDescription());
    }

    @SneakyThrows
    <Z extends BasicAbstractEntity> void setId(Z entity, Long newId) {
        assertThat(entity.getId()).isNull();
        Field idField = entity.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, newId);
        assertThat(entity.getId()).isEqualTo(newId);
    }

}
