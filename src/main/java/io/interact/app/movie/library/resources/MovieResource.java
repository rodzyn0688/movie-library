package io.interact.app.movie.library.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.interact.app.movie.library.api.MovieService;
import io.interact.app.movie.library.api.MovieService.MovieDTO;
import io.interact.app.movie.library.core.model.Movie;
import io.interact.app.movie.library.db.MovieDAO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static javax.ws.rs.core.Response.Status.*;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Slf4j
public class MovieResource {

    private final MovieService movieService;

    @POST
    @UnitOfWork
    public Response createMovie(MovieDTO newMovie, @Context UriInfo uriInfo) {
        log.debug("TRY ADD NEW MOVIE : ", newMovie.toString());
        val result = movieService.create(newMovie);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Long.toString(result));
        return Response.created(builder.build()).entity(Long.toString(result)).build();
    }

    @PUT
    @Path("/{movieId}")
    @UnitOfWork
    public Response updateMovie(@PathParam("movieId") LongParam movieId, MovieDTO newMovie, @Context UriInfo uriInfo) {
        try {
            movieService.update(movieId.get(), newMovie);
            return Response.status(NO_CONTENT).build();
        } catch (MovieService.MovieUpdateException e) {
           return Response.serverError().entity(e.getMessage()).build();
        }
    }


    @GET
    @Path("/{movieId}")
    @UnitOfWork
    public Response findMovie(@PathParam("movieId") LongParam movieId, @Context UriInfo uriInfo) {
        val result = movieService.find(movieId.get());
        if (result.isPresent()) {
            val movie = result.get();
            return  Response.ok(movie).build();
        }
        return Response.status(NOT_FOUND).build();
    }

    @DELETE
    @Path("/{movieId}")
    @UnitOfWork
    public Response deleteMovie(@PathParam("movieId") LongParam movieId, @Context UriInfo uriInfo) {
        val result = movieService.delete(movieId.get());
        if (result) {
            return Response.ok().build();
        }
        return Response.status(NO_CONTENT).build();
    }

}
