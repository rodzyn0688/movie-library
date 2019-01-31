package io.interact.app.movie.library.core.model;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class MovieUnitTest extends AbstractModelTest<Movie> {

    private static final Person examplePerson1 = new Person();
    private static final Person examplePerson2 = new Person();
    private static final Person examplePerson3 = new Person();
    private static final String exampleMovieTitle1 = "example title 1";
    private static final Long exampleMovieReleaseYear1 = 2017L;
    private static final Duration exampleMovieDuration1 = Duration.ofMinutes(75L);
    private static final String exampleMovieDescription1 = "example description 1";
    private static final TestValue exampleMovieTitle2 = new TestValue<>(
            "example title 2",
            (value, destenity) -> destenity.setTitle(value),
            Movie::getTitle);
    private static final TestValue exampleMovieReleaseYear2 = new TestValue<>(
            2018L,
            (value, destenity) -> destenity.setReleaseYear(value),
            Movie::getReleaseYear);
    private static final TestValue exampleMovieDuration2 = new TestValue<>(
            Duration.ofMinutes(120L),
            (value, destenity) -> destenity.setDuration(value),
            Movie::getDuration);
    private static final TestValue exampleMovieDescription2 = new TestValue<>(
            "example description 2",
            (value, destenity) -> destenity.setDescription(value),
            Movie::getDescription);
    private static final TestValue exampleMovieActors = new TestValue<>(
            Arrays.asList(examplePerson1, examplePerson3),
            (value, destenity) -> destenity.setActors(value),
            Movie::getActors);
    private Movie exampleMovie;
    private Movie exampleMovieClone;

    @Before
    public void setUp() throws Exception {
        examplePerson1.setFirstName("person1");
        examplePerson2.setFirstName("person2");
        examplePerson3.setFirstName("person3");
        exampleMovie = new Movie();
        exampleMovie.setTitle(exampleMovieTitle1);
        exampleMovie.setReleaseYear(exampleMovieReleaseYear1);
        exampleMovie.setDuration(exampleMovieDuration1);
        exampleMovie.setDescription(exampleMovieDescription1);
        exampleMovie.setActors(Arrays.asList(examplePerson1, examplePerson2));
        exampleMovieClone = this.clone(exampleMovie);
        assertThat(exampleMovie != exampleMovieClone).isTrue();
    }

    @Test
    @Parameters(method = "parametersForTestEqual")
    @TestCaseName("{0}")
    public void testEqual(String name, TestEqualFlow testFlow, Boolean expectedValue) {
        // GIVEN
        val data = testFlow.given.apply(exampleMovie, exampleMovieClone);

        // WHEN
        val result = testFlow.when.apply(data);

        // THEN
        testFlow.then.accept(result, expectedValue);
    }

    private Object[] parametersForTestEqual() {
        return new Object[]{
                new Object[]{
                        "shouldBeEqualByReference",
                        new TestEqualFlow<>(createChangeClone(null)),
                        Boolean.TRUE
                },
                new Object[]{
                        "shouldBeEqualWithDifferentReference",
                        new TestEqualFlow(TestParameter::new),
                        Boolean.TRUE
                },
                new Object[]{
                        "shouldNotBeEqualByTitle",
                        new TestEqualFlow(this.createChangeClone(exampleMovieTitle2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByTitle",
                        new TestEqualFlow(this.createChangeClone(exampleMovieTitle2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByReleaseYear",
                        new TestEqualFlow(this.createChangeClone(exampleMovieReleaseYear2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByDuration",
                        new TestEqualFlow(this.createChangeClone(exampleMovieDuration2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByDescription",
                        new TestEqualFlow(this.createChangeClone(exampleMovieDescription2)),
                        Boolean.FALSE},
                new Object[]{
                        "shouldNotBeEqualByActors",
                        new TestEqualFlow(this.createChangeClone(exampleMovieActors)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldBeEqualWhenOnlyCloneIsPersist",
                        new TestEqualFlow(this.prepareMovieToBePersistWithId(null, 5L)),
                        Boolean.TRUE},
                new Object[]{
                        "shouldNotBeEqualWhenBothPersistAndDiferrentOnlyId",
                        new TestEqualFlow(this.prepareMovieToBePersistWithId(1L, 2L)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldBeEqualWhenBothPersistAndDiferrentOnlyId",
                        new TestEqualFlow(this.prepareMovieToBePersistWithId(3L, 3L)),
                        Boolean.TRUE
                }
        };
    }

    Movie clone(Movie movie) {
        val clonedMovie = new Movie();
        clonedMovie.setTitle(movie.getTitle());
        clonedMovie.setReleaseYear(movie.getReleaseYear());
        clonedMovie.setDuration(movie.getDuration());
        clonedMovie.setDescription(movie.getDescription());
        clonedMovie.setActors(new ArrayList<>(movie.getActors()));
        return clonedMovie;
    }
}
