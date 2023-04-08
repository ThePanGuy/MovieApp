package com.myprojects.demo.services;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.ReactionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final MovieRepository movieRepository;
    private final MovieService movieService;

    public ReactionService(ReactionRepository reactionRepository, MovieRepository movieRepository, MovieService movieService) {
        this.reactionRepository = reactionRepository;
        this.movieRepository = movieRepository;
        this.movieService = movieService;
    }

    public Reaction tryToAddReaction(User user, Movie movie, Reaction.ReactionType reactionType) {
        switch (reactionType) {
            case LIKE:
                return tryToAddLikeReaction(user, movie);
            case HATE:
                return tryToAddHateReaction(user, movie);
        }
        return null;
    }

    public Reaction tryToAddLikeReaction(User user, Movie movie) {
        Optional<Reaction> reaction = reactionRepository.findByUserAndMovie(user, movie);
        if (reaction.isPresent()) {
            return updateReaction(reaction.get(), Reaction.ReactionType.LIKE);
        }
        return addNewReaction(user, movie, Reaction.ReactionType.LIKE);
    }

    public Reaction tryToAddHateReaction(User user, Movie movie) {
        Optional<Reaction> reaction = reactionRepository.findByUserAndMovie(user, movie);
        if (reaction.isPresent()) {
            return updateReaction(reaction.get(), Reaction.ReactionType.HATE);
        }
        return addNewReaction(user, movie, Reaction.ReactionType.HATE);
    }

    public Reaction addNewReaction(User user, Movie movie, Reaction.ReactionType reactionType) {
        Reaction newReaction = new Reaction(user, movie);
        movieService.addMovieReaction(movie, reactionType);
        newReaction.addReaction(reactionType);
        return reactionRepository.save(newReaction);
    }

    private Reaction updateReaction(Reaction reaction, Reaction.ReactionType reactionType) {
        movieService.updateMovieReaction(reaction, reactionType);
        reaction.addReaction(reactionType);
        return reactionRepository.save(reaction);
    }

    public Reaction undo(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found."));
        Reaction reaction = reactionRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new EntityNotFoundException("There is no reaction for movie: " + movie.getTitle() + " by user: " + user.getUsername()));
        if (reaction.getReactionType() == null) throw new RuntimeException("No reaction for movie: " + movie.getTitle() + " from user: " + user.getUsername());
        switch (reaction.getReactionType()) {
            case LIKE:
                movie.removeLike();
            case HATE:
                movie.removeHate();
        }
        reaction.undo();
        return reactionRepository.save(reaction);
    }
}
