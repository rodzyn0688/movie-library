package io.interact.app.movie.library.core.service;

import io.interact.app.movie.library.api.MovieService;
import io.interact.app.movie.library.db.MovieDAO;
import io.interact.app.movie.library.mapper.MovieMapper;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Optional;

@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private static final MovieMapper MOVIE_MAPPER = MovieMapper.INSTANCE;
    private final MovieDAO movieDAO;

    @Override
    public Long create(MovieDTO movieDTO) {
        val entity = MOVIE_MAPPER.dtoToEntity(movieDTO);
        val result = this.movieDAO.createOrUpdate(entity);
        return result.getId();
    }

    @Override
    public Long update(Long id, MovieDTO movieDTO) throws MovieUpdateException {
        if (id == null) {
            throw new  MovieUpdateException("You cannot update something without id");
        }
        val idDTO = movieDTO.getId();
        if ((idDTO != null && idDTO.equals(id)) || (idDTO == null)) {
            val movieOptional = this.movieDAO.findById(id);
            if (movieOptional.isPresent()) {
                val movieForUpdate = movieOptional.get();
                MOVIE_MAPPER.fromDto(movieDTO, movieForUpdate);
                val result = this.movieDAO.createOrUpdate(movieForUpdate);
                return result.getId();
            }
        } else {
            throw new  MovieUpdateException("You want to update movie with id: "+ id + ", but in content exist id: "+idDTO);
        }
        throw new  MovieUpdateException("Not exist movie with id: "+ id);
    }

    @Override
    public Optional<MovieDTO> find(Long id) {
        val result = this.movieDAO.findById(id);
        if (result.isPresent()) {
            val entity = result.get();
            return Optional.of(MOVIE_MAPPER.entityToDto(entity));
        }
        return Optional.empty();
    }

    @Override
    public Boolean delete(Long id) {
        val result = this.movieDAO.findById(id);
        if (result.isPresent()) {
            this.movieDAO.delete(result.get());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
