package io.interact.app.movie.library;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MovielibraryApplication extends Application<MovielibraryConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MovielibraryApplication().run(args);
    }

    @Override
    public String getName() {
        return "Movie library";
    }

    @Override
    public void initialize(final Bootstrap<MovielibraryConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final MovielibraryConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
