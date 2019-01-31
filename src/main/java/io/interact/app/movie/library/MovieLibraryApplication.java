package io.interact.app.movie.library;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.interact.app.movie.library.api.MovieService;
import io.interact.app.movie.library.core.model.Movie;
import io.interact.app.movie.library.core.model.Person;
import io.interact.app.movie.library.core.service.MovieServiceImpl;
import io.interact.app.movie.library.db.MovieDAO;
import io.interact.app.movie.library.resources.MovieResource;
import lombok.val;

public class MovieLibraryApplication extends Application<MovieLibraryConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MovieLibraryApplication().run(args);
    }

    private final HibernateBundle<MovieLibraryConfiguration> hibernateBundle =
            new HibernateBundle<MovieLibraryConfiguration>(Movie.class, Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(MovieLibraryConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "Movie library";
    }

    @Override
    public void initialize(final Bootstrap<MovieLibraryConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new MigrationsBundle<MovieLibraryConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(MovieLibraryConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final MovieLibraryConfiguration configuration,
                    final Environment environment) {
        val movieDao = new MovieDAO(hibernateBundle.getSessionFactory());
        val movieService = new MovieServiceImpl(movieDao);
        environment.jersey().register(new MovieResource(movieService));
    }

}
