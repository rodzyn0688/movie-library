package io.interact.app.movie.library.core.model;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import lombok.val;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class PersonUnitTest extends AbstractModelTest<Person> {

    private static final Movie exampleMovie1 = new Movie();
    private static final Movie exampleMovie2 = new Movie();
    private static final Movie exampleMovie3 = new Movie();
    private static final String examplePersonFirstName1 = "example first name 1";
    private static final String examplePersonLastName1 = "example last name 1";
    private static final Date examplePersonBorn1 = new DateTime(1975, 11, 25, 0, 0, 0).toDate();
    private static final Locale examplePersonCurrentNationality1 = Locale.UK;
    private static final String examplePersonDescription1 = "example description 1";
    private static final TestValue examplePersonFirstName2 = new TestValue<>(
            "example first name 2",
            (value, destenity) -> destenity.setFirstName(value),
            Person::getFirstName);
    private static final TestValue examplePersonLastName2 = new TestValue<>(
            "example last name 2",
            (value, destenity) -> destenity.setLastName(value),
            Person::getLastName);
    private static final TestValue examplePersonBorn2 = new TestValue<>(
            new DateTime(1983, 5, 3, 0, 0, 0).toDate(),
            (value, destenity) -> destenity.setBorn(value),
            Person::getBorn);
    private static final TestValue examplePersonCurrentNationality2 = new TestValue<>(
            Locale.US,
            (value, destenity) -> destenity.setCurrentNationality(value),
            Person::getCurrentNationality);
    private static final TestValue examplePersonDescription2 = new TestValue<>(
            "example description 2",
            (value, destenity) -> destenity.setDescription(value),
            Person::getDescription);
    private static final TestValue examplePersonMovies = new TestValue<>(
            Arrays.asList(exampleMovie1, exampleMovie3),
            (value, destenity) -> destenity.setMoviesAsActor(value),
            Person::getMoviesAsActor);
    private Person examplePerson;
    private Person examplePersonClone;

    @Before
    public void setUp() throws Exception {
        exampleMovie1.setTitle("movie1");
        exampleMovie2.setTitle("movie2");
        exampleMovie3.setTitle("movie3");
        examplePerson = new Person();
        examplePerson.setFirstName(examplePersonFirstName1);
        examplePerson.setLastName(examplePersonLastName1);
        examplePerson.setBorn(examplePersonBorn1);
        examplePerson.setCurrentNationality(examplePersonCurrentNationality1);
        examplePerson.setDescription(examplePersonDescription1);
        examplePerson.setMoviesAsActor(Arrays.asList(exampleMovie1, exampleMovie2));
        examplePersonClone = this.clone(examplePerson);
        assertThat(examplePerson != examplePersonClone).isTrue();
    }

    @Test
    @Parameters(method = "parametersForTestEqual")
    @TestCaseName("{0}")
    public void testEqual(String name, TestEqualFlow testFlow, Boolean expectedValue) {
        // GIVEN
        val data = testFlow.given.apply(examplePerson, examplePersonClone);

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
                        "shouldNotBeEqualByFirstName",
                        new TestEqualFlow(this.createChangeClone(examplePersonFirstName2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByLastName",
                        new TestEqualFlow(this.createChangeClone(examplePersonLastName2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByBorn",
                        new TestEqualFlow(this.createChangeClone(examplePersonBorn2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByCurrentNationality",
                        new TestEqualFlow(this.createChangeClone(examplePersonCurrentNationality2)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldNotBeEqualByDescription",
                        new TestEqualFlow(this.createChangeClone(examplePersonDescription2)),
                        Boolean.FALSE},
                new Object[]{
                        "shouldNotBeEqualByMovies",
                        new TestEqualFlow(this.createChangeClone(examplePersonMovies)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldBeEqualWhenOnlyCloneIsPersist",
                        new TestEqualFlow(this.prepareMovieToBePersistWithId(null, 6L)),
                        Boolean.TRUE},
                new Object[]{
                        "shouldNotBeEqualWhenBothPersistAndDiferrentOnlyId",
                        new TestEqualFlow(this.prepareMovieToBePersistWithId(9L, 8L)),
                        Boolean.FALSE
                },
                new Object[]{
                        "shouldBeEqualWhenBothPersistAndDiferrentOnlyId",
                        new TestEqualFlow(this.prepareMovieToBePersistWithId(7L, 7L)),
                        Boolean.TRUE
                }
        };
    }

    @Override
    Person clone(Person person) {
        val clonedPerson = new Person();
        clonedPerson.setFirstName(person.getFirstName());
        clonedPerson.setLastName(person.getLastName());
        clonedPerson.setBorn(person.getBorn());
        clonedPerson.setCurrentNationality(person.getCurrentNationality());
        clonedPerson.setDescription(person.getDescription());
        clonedPerson.setMoviesAsActor(person.getMoviesAsActor());
        return clonedPerson;
    }
}
