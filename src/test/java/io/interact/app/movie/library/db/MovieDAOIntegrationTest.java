package io.interact.app.movie.library.db;

import io.dropwizard.testing.junit.DAOTestRule;
import io.interact.app.movie.library.core.model.Movie;
import io.interact.app.movie.library.core.model.Person;
import io.interact.app.movie.library.test.integration.GenerateCorectMovieOrPerson;
import lombok.val;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertTrue;

public class MovieDAOIntegrationTest extends GenerateCorectMovieOrPerson {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(Movie.class)
            .addEntityClass(Person.class)
            .build();

    private MovieDAO movieDAO;
    private Movie exampleMovie;

    @Before
    public void setUp() throws Exception {
        val sessionFactory = this.daoTestRule.getSessionFactory();
        this.movieDAO = new MovieDAO(sessionFactory);

        exampleMovie = createCorrectMovie();

        assertTrue("Example movie is not persist", exampleMovie.getId() == null);
        assertTrue("The database should be clean int table with movies", movieDAO.findAll().isEmpty());
    }

    @Test
    public void shouldCreateNewMovieWithDateTimeCreatedAndLastModify() {
        // GIVEN
        val expectedId = 1L;
        assertTrue("No row", !movieDAO.findById(expectedId).isPresent());

        // WHEN
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        // THEN
        val persistMovie = movieDAO.findById(1);
        assertThat(persistMovie).isPresent();
        assertThat(persistMovie.get()).isEqualTo(exampleMovie);
        assertThat(persistMovie.get().getCreatedDateTime()).isNotNull();
        assertThat(persistMovie.get().getLastModifyDateTime()).isNotNull();
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    public void shouldCreateNewMovieWithoutActorsButWithAllParameters() {
        // GIVEN
        val expectedId = 1L;

        assertTrue("No row", !movieDAO.findById(expectedId).isPresent());
        // WHEN
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        // THEN
        val persistMovie = movieDAO.findById(1);
        assertThat(persistMovie).isPresent();
        assertThat(persistMovie.get()).isEqualTo(exampleMovie);
        assertThat(persistMovie.get().getId()).isEqualTo(expectedId);
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    public void shouldNotCreateNewMovieWhenExist() {
        // GIVEN
        val expectedId = 1L;

        assertTrue("No row", !movieDAO.findById(expectedId).isPresent());
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));
        val notPersistCloneMovie = createCloneWithoutPersist(exampleMovie);

        // WHEN
        val resultDoublePerist = this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(notPersistCloneMovie));

        // THEN
        val persistMovie = movieDAO.findById(1);
        assertThat(persistMovie).isPresent();
        assertThat(persistMovie.get()).isEqualTo(exampleMovie);
        assertThat(persistMovie.get()).isEqualTo(resultDoublePerist);
        assertThat(persistMovie.get().getId()).isEqualTo(expectedId);
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    public void shouldCreateNewMovieWithoutActorsButWithoutOptionalField() {
        // GIVEN
        exampleMovie.setDescription(null);
        val expectedId = 1L;

        assertTrue("No row", !movieDAO.findById(expectedId).isPresent());
        // WHEN
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        // THEN
        val persistMovie = movieDAO.findById(1);
        assertThat(persistMovie).isPresent();
        assertThat(persistMovie.get().getDescription()).isNull();
        assertThat(persistMovie.get()).isEqualTo(exampleMovie);
        assertThat(persistMovie.get().getId()).isEqualTo(expectedId);
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    public void shouldNotCreateNewMovieWithoutActorsButWithRequiredFieldTitleAsNull() {
        // GIVEN
        exampleMovie.setTitle(null);

        // WHEN & THEN
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->
                daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie)));
    }

    @Test
    public void shouldNotCreateNewMovieWithoutActorsButWithRequiredFieldYearReleaseAsNull() {
        // GIVEN
        exampleMovie.setReleaseYear(null);

        // WHEN & THEN
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->
                daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie)));
    }

    @Test
    public void shouldNotCreateNewMovieWithoutActorsButWithRequiredFieldDurationAsNull() {
        // GIVEN
        exampleMovie.setDuration(null);

        // WHEN & THEN
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->
                daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie)));
    }

    @Test
    public void shouldNotCreateNewMovieWithoutActorsButWithRequiredFieldActorsAsNull() {
        // GIVEN
        exampleMovie.setActors(null);

        // WHEN & THEN
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->
                daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie)));
    }

    @Test
    public void shouldNotCreateNewMovieWithoutActorsButWithToHighYearValue() {
        // GIVEN
        exampleMovie.setReleaseYear(2101L);

        // WHEN & THEN
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->
                daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie)));
    }

    @Test
    public void shouldNotCreateNewMovieWithoutActorsButWithToLowYearValue() {
        // GIVEN
        exampleMovie.setReleaseYear(1899L);


        // WHEN & THEN
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->
                daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie)));
    }

    @Test
    public void shouldCreateNewMovieWithActors() {
        // GIVEN
        val exampleActor1 = createCorrectPerson();
        val exampleActor2 = createCorrectPerson();
        val exampleMovie = createCorrectMovie(exampleActor1, exampleActor2);
        val expectedId = 1L;
        val expectedNumberofActors = 2;

        assertTrue("example movie is not persist", exampleMovie.getId() == null);
        assertTrue("example actor1 is not persist", exampleActor1.getId() == null);
        assertTrue("example actor2 is not persist", exampleActor2.getId() == null);
        assertTrue("No row", !movieDAO.findById(expectedId).isPresent());

        // WHEN
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        // THEN
        val persistMovie = movieDAO.findById(1);
        assertThat(persistMovie).isPresent();
        assertThat(persistMovie.get()).isEqualTo(exampleMovie);
        assertThat(persistMovie.get().getId()).isEqualTo(expectedId);
        assertThat(persistMovie.get().getActors()).hasSize(expectedNumberofActors);
        assertThat(persistMovie.get().getActors().get(0).getId()).isEqualTo(1L);
        assertThat(persistMovie.get().getActors().get(1).getId()).isEqualTo(2L);
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    public void shouldCreateNextMovieWithoutActors() {
        // GIVEN
        this.daoTestRule.inTransaction(() -> {
            movieDAO.createOrUpdate(createCorrectMovie());
            movieDAO.createOrUpdate(createCorrectMovie());
            movieDAO.createOrUpdate(createCorrectMovie());
        });
        val lastExistId = 3;
        val expectedId = 4L;

        assertTrue("The database should be clean int table with movies", movieDAO.findAll().size() == lastExistId);
        assertTrue("Last exist row has id 3", movieDAO.findById(lastExistId).isPresent());
        assertTrue("No row", !movieDAO.findById(expectedId).isPresent());

        // WHEN
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        // THEN
        val persistMovie = movieDAO.findById(expectedId);
        assertThat(persistMovie).isPresent();
        assertThat(persistMovie.get()).isEqualTo(exampleMovie);
        assertThat(persistMovie.get().getId()).isEqualTo(expectedId);
        assertThat(movieDAO.findAll()).hasSize(4);
    }

    @Test
    public void shouldCreateNextMovieWithActors() {
        // GIVEN
        this.daoTestRule.inTransaction(() -> {
            movieDAO.createOrUpdate(createCorrectMovie(createCorrectPerson()));
            movieDAO.createOrUpdate(createCorrectMovie(createCorrectPerson(), createCorrectPerson()));
            movieDAO.createOrUpdate(createCorrectMovie(createCorrectPerson()));
        });
        val exampleActor1 = createCorrectPerson(createCorrectPerson());
        val exampleActor2 = createCorrectPerson(createCorrectPerson());
        val exampleActor3 = createCorrectPerson(createCorrectPerson());
        val exampleMovie = createCorrectMovie(exampleActor1, exampleActor2, exampleActor3);
        val lastExistId = 3;
        val expectedId = 4L;

        assertTrue("Example movie is not persist", exampleMovie.getId() == null);
        assertTrue("example actor1 is not persist", exampleActor1.getId() == null);
        assertTrue("example actor2 is not persist", exampleActor2.getId() == null);
        assertTrue("example actor4 is not persist", exampleActor3.getId() == null);
        assertTrue("The database should be clean int table with movies", movieDAO.findAll().size() == lastExistId);
        assertTrue("Last exist row has id 3", movieDAO.findById(lastExistId).isPresent());
        assertTrue("No row", !movieDAO.findById(expectedId).isPresent());

        // WHEN
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        // THEN
        val persistMovie = movieDAO.findById(expectedId);
        assertThat(persistMovie).isPresent();
        assertThat(persistMovie.get()).isEqualTo(exampleMovie);
        assertThat(persistMovie.get().getId()).isEqualTo(expectedId);
        assertThat(persistMovie.get().getActors().get(0).getId()).isEqualTo(5L);
        assertThat(persistMovie.get().getActors().get(1).getId()).isEqualTo(6L);
        assertThat(persistMovie.get().getActors().get(2).getId()).isEqualTo(7L);
        assertThat(movieDAO.findAll()).hasSize(4);
    }

    @Test
    public void shouldUpdateMovieWithoutActors() {
        // GIVEN
        val expectedId = 1L;
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        assertThat(movieDAO.findAll()).hasSize(1);

        val persistMovie = movieDAO.findById(1).get();
        val oldTitle = persistMovie.getTitle();
        val newTitle = createCorrectTitle();

        assertThat(oldTitle).isNotEqualTo(newTitle);

        // WHEN
        persistMovie.setTitle(newTitle);
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(persistMovie));

        // THEN
        val persistUpdatedMovie = movieDAO.findById(1).get();
        assertThat(persistUpdatedMovie.getTitle()).isEqualTo(newTitle);
        assertThat(persistUpdatedMovie.getId()).isEqualTo(expectedId);
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    public void shouldUpdateMovieWithActors() {
        // GIVEN
        val exampleActor = createCorrectPerson();
        val exampleMovie = createCorrectMovie(exampleActor);
        val expectedId = 1L;

        assertTrue("The database should be clean int table with movies", movieDAO.findAll().isEmpty());

        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        assertThat(movieDAO.findAll()).hasSize(1);

        val persistMovie = movieDAO.findById(1).get();

        assertThat(persistMovie.getActors()).hasSize(1);

        val examplNeweActor = createCorrectPerson();

        // WHEN
        persistMovie.getActors().add(examplNeweActor);
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(persistMovie));

        // THEN
        val persistUpdatedMovie = movieDAO.findById(1).get();
        assertThat(persistMovie.getActors()).hasSize(2);
        assertThat(persistUpdatedMovie.getId()).isEqualTo(expectedId);
        assertThat(movieDAO.findAll()).hasSize(1);
    }

    @Test
    public void shouldDeleteMovieWithoutActors() {
        // GIVEN
        val exampleActor1 = createCorrectPerson();
        val exampleMovie1= createCorrectMovie(exampleActor1);
        val exampleActor2 = createCorrectPerson();
        val exampleMovie2= createCorrectMovie(exampleActor2, exampleActor1);
        val exampleActor3 = createCorrectPerson();
        val exampleMovie3= createCorrectMovie(exampleActor3, exampleActor2, exampleActor1);
        val expectedSizePre = 3L;
        val idToDelete = 2;

        assertTrue("The database should be clean int table with movies", movieDAO.findAll().isEmpty());

        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie1));
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie2));
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie3));

        assertTrue("The table movies should have size: "+expectedSizePre, movieDAO.findAll().size() == expectedSizePre);

        // WHEN
        val persistMovie = movieDAO.findById(idToDelete).get();
        this.daoTestRule.inTransaction(() -> movieDAO.delete(persistMovie));

        // THEN
        val expectedSizePost = expectedSizePre -1;
        val allMovieInDb = movieDAO.findAll();
        assertTrue("The table movies should have after size: "+expectedSizePost, allMovieInDb.size() == expectedSizePost);
        assertThat(allMovieInDb).contains(exampleMovie1, exampleMovie3);
        assertThat(allMovieInDb).doesNotContain(exampleMovie2);
        assertThat(exampleMovie3.getActors()).hasSize(3);
    }

    @Test
    public void shouldNotDuplicateActorsInDatabase() {
        // GIVEN
        val exampleActor1 = createCorrectPerson();
        val exampleMovie1= createCorrectMovie(createCorrectPerson(exampleActor1));
        val exampleActor2 = createCorrectPerson();
        val exampleMovie2= createCorrectMovie(createCorrectPerson(exampleActor2), createCorrectPerson(exampleActor1));
        val exampleActor3 = createCorrectPerson();
        val exampleMovie3= createCorrectMovie(createCorrectPerson(exampleActor3), createCorrectPerson(exampleActor1));
        val expectedSizePre = 3L;

        assertTrue("The database should be clean int table with movies", movieDAO.findAll().isEmpty());

        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie1));
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie2));
        this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie3));

        assertTrue("The table movies should have size: "+expectedSizePre, movieDAO.findAll().size() == expectedSizePre);
        val exampleMovie= createCorrectMovie(exampleActor1, exampleActor2, exampleActor3);

        // WHEN
        val result = this.daoTestRule.inTransaction(() -> movieDAO.createOrUpdate(exampleMovie));

        // THEN
        val expectedSizePost = expectedSizePre + 1;
        val allMovieInDb = movieDAO.findAll();
        assertTrue("The table movies should have after size: "+expectedSizePost, allMovieInDb.size() == expectedSizePost);
        assertThat(allMovieInDb).contains(exampleMovie1, exampleMovie2, exampleMovie3, result);
        assertThat(allMovieInDb.stream().flatMap(m -> m.getActors().stream()).distinct().count()).isEqualTo(3L);
    }
}
