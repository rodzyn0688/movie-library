package io.interact.app.movie.library.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface MovieService {

    Long create(MovieDTO movieDTO);
    Long update(Long id, MovieDTO movieDTO) throws MovieUpdateException;
    Optional<MovieDTO> find(Long id);
    Boolean delete(Long id);

    @Data
    class MovieDTO {
        @JsonProperty
        private Long id;
        @JsonProperty
        private String title;
        @JsonProperty
        private Long releaseYear;
        @JsonProperty
        private Duration duration;
        @JsonProperty
        private String description;
        @JsonProperty
        private List<ActorDTO> actors;

        @Data
        public static class ActorDTO {
            @JsonProperty
            private Long id;
            @JsonProperty
            private String firstName;
            @JsonProperty
            private String lastName;
            @JsonProperty
            private Date born;
            @JsonProperty
            private Locale currentNationality;
            @JsonProperty
            private String description;
        }
    }

    class MovieUpdateException extends Exception {
        public MovieUpdateException(String msg) {
            super(msg);
        }
    }
}
