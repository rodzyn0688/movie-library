package io.interact.app.movie.library;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MovieLibraryApplication extends Application<MovieLibraryConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MovieLibraryApplication().run(args);
    }

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
    }

    @Override
    public void run(final MovieLibraryConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
