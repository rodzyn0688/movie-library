package io.interact.app.movie.library.api;

import lombok.Data;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public interface MovieService {

    Long create(MovieDTO movieDTO);
    Long update(MovieDTO movieDTO);
    MovieDTO find(Long id);
    void delete(Long id);

    @Data
    class MovieDTO {

        private Long id;
        private String title;
        private Long releaseYear;
        private Duration duration;
        private String description;
        private List<ActorDTO> actors;

        @Data
        public static class ActorDTO {

            private Long id;
            private String firstName;
            private String lastName;
            private Date born;
            private Locale currentNationality;
            private String description;

        }

    }

}
