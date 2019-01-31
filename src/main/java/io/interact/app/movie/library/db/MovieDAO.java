package io.interact.app.movie.library.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.interact.app.movie.library.core.model.Movie;
import io.interact.app.movie.library.core.model.Person;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MovieDAO extends AbstractDAO<Movie> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public MovieDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Movie createOrUpdate(final Movie movie) {
        log.debug("TEST LOGERA !!!!!!");
        if (movie.getId() == null) {
            val query = namedQuery("io.interact.app.movie.library.core.model.Movie.findByTitleAndReleaseYearAndDuration")
                    .setParameter("title", movie.getTitle())
                    .setParameter("releaseYear", movie.getReleaseYear())
                    .setParameter("duration", movie.getDuration());
            final List<Movie> movieList = list((Query<Movie>) query);
            val persistMovie = movieList.stream().filter(f -> f.equals(movie)).findFirst();
            if(persistMovie.isPresent()) {
                return persistMovie.get();
            }
            if (movie.getActors() != null) {
                val actorsCanPersisted = movie.getActors().stream().map(this::tryFindPersistPerson).distinct().collect(Collectors.toList());
                movie.setActors(actorsCanPersisted);
            }
        }
        return persist(movie);
    }

    private Person tryFindPersistPerson(Person presonNotPersist) {
        val query = currentSession().createNamedQuery("io.interact.app.movie.library.core.model.Person.findByFirstNameAndLastNameAndBornAndCurrentNationality", Person.class)
                .setParameter("firstName", presonNotPersist.getFirstName())
                .setParameter("lastName", presonNotPersist.getLastName())
                .setParameter("born", presonNotPersist.getBorn())
                .setParameter("currentNationality", presonNotPersist.getCurrentNationality());
        val peopleList = ((Query<Person>)query).getResultList();
        val personPersist = peopleList.stream().filter(f -> f.equals(presonNotPersist)).findFirst();
        if (personPersist.isPresent()) {
            return personPersist.get();
        }
        return presonNotPersist;
    }

    public Optional<Movie> findById(long id) {
        return Optional.ofNullable(get(id));
    }

    public List<Movie> findAll() {
        return list((Query<Movie>) namedQuery("io.interact.app.movie.library.core.model.Movie.findAll"));
    }

    public void delete(Movie movie) {
        currentSession().delete(movie);
    }
}
