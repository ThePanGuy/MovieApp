package com.myprojects.demo.services;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.exceptions.InvalidInputException;
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
    private final MovieService movieService;

    public ReactionService(ReactionRepository reactionRepository, MovieRepository movieRepository, MovieService movieService) {
        this.reactionRepository = reactionRepository;
        this.movieRepository = movieRepository;
        this.movieService = movieService;
    }

    @Transactional
    public Reaction tryToAddReaction(User user, Movie movie, Reaction.ReactionType reactionType) {
        switch (reactionType) {
            case LIKE:
                return tryToAddOrRemoveLikeReaction(user, movie);
            case HATE:
                return tryToAddOrRemoveHateReaction(user, movie);
        }
        return null;
    }

    private Reaction tryToAddOrRemoveLikeReaction(User user, Movie movie) {
        Optional<Reaction> reaction = reactionRepository.findByUserAndMovie(user, movie);
        if (reaction.isPresent()) {
            return updateOrRemoveReaction(reaction.get(), Reaction.ReactionType.LIKE);
        }
        return addNewReaction(user, movie, Reaction.ReactionType.LIKE);
    }

    private Reaction tryToAddOrRemoveHateReaction(User user, Movie movie) {
        Optional<Reaction> reaction = reactionRepository.findByUserAndMovie(user, movie);
        if (reaction.isPresent()) {
            return updateOrRemoveReaction(reaction.get(), Reaction.ReactionType.HATE);
        }
        return addNewReaction(user, movie, Reaction.ReactionType.HATE);
    }

    private Reaction addNewReaction(User user, Movie movie, Reaction.ReactionType reactionType) {
        Reaction newReaction = new Reaction(user, movie);
        addMovieReaction(movie, reactionType);
        newReaction.addReaction(reactionType);
        return reactionRepository.save(newReaction);
    }

    private Reaction updateOrRemoveReaction(Reaction reaction, Reaction.ReactionType reactionType) {
        updateOrRemoveMovieReaction(reaction, reactionType);
        return reactionRepository.save(reaction);
    }



    public Movie updateOrRemoveMovieReaction(Reaction oldReaction, Reaction.ReactionType reactionType) {
        Movie movie = oldReaction.getMovie();
        if (oldReaction.getReactionType() == null) {
            addMovieReaction(movie, reactionType);
            oldReaction.addReaction(reactionType);
            log.info("User with id: {} {} movie with id: {}.",
                    oldReaction.getUser().getId(), reactionType.name(), movie.getId());
        } else if (oldReaction.getReactionType() == reactionType) {
            removeMovieReaction(movie, reactionType);
            oldReaction.undo();
            log.info("User with id: {} removed {} from movie with id: {}",
                    oldReaction.getUser().getId(), reactionType.name(), movie.getId());
        } else {
            changeMovieReaction(movie, reactionType);
            oldReaction.addReaction(reactionType);
            log.info("User with id: {} changed from {} to {} for movie with id: {}",
                    oldReaction.getUser().getId(), oldReaction.getReactionType().name(), reactionType.name(), movie.getId());
        }
        return movieRepository.save(movie);
    }

    public void addMovieReaction(Movie movie, Reaction.ReactionType reactionType) {
        switch (reactionType) {
            case LIKE:
                movie.addLike();
                break;
            case HATE:
                movie.addHate();
                break;
        }
    }

    private void removeMovieReaction(Movie movie, Reaction.ReactionType reactionType) {
        switch (reactionType) {
            case LIKE -> movie.removeLike();
            case HATE -> movie.removeHate();
        }
    }

    private void changeMovieReaction(Movie movie, Reaction.ReactionType reactionType) {
        switch (reactionType) {
            case LIKE:
                movie.removeHate();
                movie.addLike();
                break;
            case HATE:
                movie.removeLike();
                movie.addHate();
                break;
        }
    }

    public Reaction undo(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found."));
        Reaction reaction = reactionRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new EntityNotFoundException("There is no reaction for movie: " + movie.getTitle() + " by user: " + user.getUsername()));
        if (reaction.getReactionType() == null) throw new InvalidInputException("Reaction has already been retracted.");
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
