package io.interact.app.movie.library.test.integration;

import io.interact.app.movie.library.core.model.Movie;
import io.interact.app.movie.library.core.model.Person;
import lombok.val;
import lombok.var;
import org.joda.time.DateTime;

import java.time.Duration;
import java.time.YearMonth;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class GenerateCorectMovieOrPerson {

    public static Movie createCorrectMovie(Person... people) {
        val title = createCorrectTitle();
        val releaseYear = createCorectReleaseYear();
        val duration = Duration.ofMinutes(createDuration());
        val description = createCorrectDescription();
        var movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setDuration(duration);
        movie.setDescription(description);
        for (Person person : people) {
            movie.getActors().add(person);
        }
        return movie;
    }

    public static Movie createCloneWithoutPersist(Movie persistMovie) {
        val title = persistMovie.getTitle();
        val releaseYear = persistMovie.getReleaseYear();
        val duration = persistMovie.getDuration();
        val description = persistMovie.getDescription();
        var movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setDuration(duration);
        movie.setDescription(description);
        for (Person person : persistMovie.getActors()) {
            movie.getActors().add(person);
        }
        return movie;
    }

    public static Person createCorrectPerson(Person... person) {
        var firstName = createCorrectFirstName();
        var lastName = createCorrectLastName();
        var born = createCorectBorn();
        var nationality = createCorrectNationality();
        var description = createCorrectDescription();
        if (person != null && person.length > 0) {
            firstName = person[0].getFirstName();
            lastName = person[0].getLastName();
            born = person[0].getBorn();
            nationality = person[0].getCurrentNationality();
            description = person[0].getDescription();
        }
        val newPerson = new Person();
        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setBorn(born);
        newPerson.setCurrentNationality(nationality);
        newPerson.setDescription(description);
        return newPerson;
    }

    public static String createCorrectTitle() {
        return UUID.randomUUID().toString();
    }

    private static long createCorectReleaseYear() {
        return new Random().longs(1900, 2100).findFirst().getAsLong();
    }

    private static long createDuration() {
        return new Random().longs(1, 1000).findFirst().getAsLong();
    }

    private static String createCorrectDescription() {
        return UUID.randomUUID().toString();
    }

    private static String createCorrectFirstName() {
        return UUID.randomUUID().toString();
    }

    private static String createCorrectLastName() {
        return UUID.randomUUID().toString();
    }

    private static Date createCorectBorn() {
        val year = new Random().ints(1900, DateTime.now().getYear()).findFirst().getAsInt();
        val monthOfYear = new Random().ints(1, 12).findFirst().getAsInt();
        val dayOfMonth = new Random().ints(1, YearMonth.of(year, monthOfYear).lengthOfMonth()).findFirst().getAsInt();
        return new DateTime(year, monthOfYear, dayOfMonth, 0, 0, 0, 0).toDate();
    }

    private static Locale createCorrectNationality() {
        val allNationality = Locale.getAvailableLocales();
        val randomId = new Random().ints(0, allNationality.length).findFirst().getAsInt();
        return allNationality[randomId];
    }

}
