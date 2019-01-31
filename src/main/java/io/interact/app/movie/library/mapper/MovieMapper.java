package io.interact.app.movie.library.mapper;

import io.interact.app.movie.library.api.MovieService.MovieDTO;
import io.interact.app.movie.library.api.MovieService.MovieDTO.ActorDTO;
import io.interact.app.movie.library.core.model.Movie;
import io.interact.app.movie.library.core.model.Person;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDTO entityToDto(Movie movie);
    Movie dtoToEntity(MovieDTO movieDTO);

    @InheritInverseConfiguration(name = "entityToDto")
    @Mapping(target = "id", ignore = true)
    void fromDto(MovieDTO movieDTO, @MappingTarget Movie movie);


    void fromDto(List<ActorDTO> actorsDTO, @MappingTarget List<Person> actors);
}
