package io.interact.app.movie.library.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(indexes = {@Index(columnList = "title"), @Index(columnList = "releaseYear"), @Index(columnList = "duration")})
@NamedQueries(
        {
                @NamedQuery(
                        name = "io.interact.app.movie.library.core.model.Movie.findAll",
                        query = "SELECT m FROM Movie m"
                ),
                @NamedQuery(
                        name = "io.interact.app.movie.library.core.model.Movie.findByTitleAndReleaseYearAndDuration",
                        query = "SELECT m FROM Movie m WHERE m.title = :title AND m.releaseYear = :releaseYear AND m.duration = :duration"
                ),
        })
public class Movie extends BasicAbstractEntity {

    @NotNull
    private String title;
    @NotNull
    @Min(1900)
    @Max(2100)
    private Long releaseYear;
    @NotNull
    private Duration duration;
    private String description;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Cascade({SAVE_UPDATE})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "movie_actor",
            joinColumns = {@JoinColumn(name = "movie_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "actor_id", referencedColumnName = "id")})
    private List<Person> actors = new ArrayList<>();

}
