package io.interact.app.movie.library.mapper;

import io.interact.app.movie.library.api.MovieService.MovieDTO;
import io.interact.app.movie.library.core.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDTO entityToDto(Movie movie);
    Movie dtoToEntity(MovieDTO movieDTO);

}
