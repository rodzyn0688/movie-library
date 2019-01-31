package io.interact.app.movie.library.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(indexes = {@Index(columnList = "firstName"), @Index(columnList = "lastName"), @Index(columnList = "born")})
public class Person extends BasicAbstractEntity {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date born;
    @NotNull
    private Locale currentNationality;
    private String description;

    @NotNull
    @Cascade({SAVE_UPDATE})
    @ManyToMany(mappedBy = "actors")
    private List<Movie> moviesAsActor = new ArrayList<>();
}
