package com.myprojects.demo.services;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.ReactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ReactionService {
    private static final Logger log = LoggerFactory.getLogger(ReactionService.class);
    private final ReactionRepository reactionRepository;
    private final MovieRepository movieRepository;

    public ReactionService(ReactionRepository reactionRepository, MovieRepository movieRepository) {
        this.reactionRepository = reactionRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Reaction addReaction(MovieUser user, Long movieId, Reaction.ReactionType type) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        Reaction reaction = reactionRepository.findByMovieUserAndMovie(user, movie)
                .orElseGet(() -> new Reaction(user, movie, type));
        reaction.setType(type);

        log.info("User with id: {} {} movie with id: {}.",
                reaction.getUser().getId(), reaction.getIfLike() ? "Likes" : "Hates", reaction.getMovie().getId());
        return reactionRepository.save(reaction);
    }

    @Transactional
    public Movie removeReaction(MovieUser user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        reactionRepository.findByMovieUserAndMovie(user, movie)
                .ifPresent(reaction -> {
                    log.info("User with id: {} removed reaction from movie with id: {}", reaction.getUser().getId(), reaction.getMovie().getId());
                    reactionRepository.delete(reaction);
                });
        return movie;
    }

    public Boolean getMovieReactionForUser(MovieUser user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        Optional<Reaction> reaction = reactionRepository.findByMovieUserAndMovie(user, movie);
        return reaction.map(Reaction::getIfLike).orElse(null);
    }
}
