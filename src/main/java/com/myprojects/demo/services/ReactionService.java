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

    public ReactionService(ReactionRepository reactionRepository, MovieRepository movieRepository) {
        this.reactionRepository = reactionRepository;
        this.movieRepository = movieRepository;
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
            return updateOrRemoveReaction(reaction.get(), true);
        }
        return addNewReaction(user, movie, true);
    }

    private Reaction tryToAddOrRemoveHateReaction(User user, Movie movie) {
        Optional<Reaction> reaction = reactionRepository.findByUserAndMovie(user, movie);
        if (reaction.isPresent()) {
            return updateOrRemoveReaction(reaction.get(), false);
        }
        return addNewReaction(user, movie, false);
    }

    private Reaction addNewReaction(User user, Movie movie, boolean like) {
        Reaction newReaction = new Reaction(user, movie);
        addMovieReaction(movie, like);
        newReaction.addReactionLike(like);
        return reactionRepository.save(newReaction);
    }

    private Reaction updateOrRemoveReaction(Reaction reaction, boolean like) {
        updateOrRemoveMovieReaction(reaction, like);
        return reactionRepository.save(reaction);
    }



    public Movie updateOrRemoveMovieReaction(Reaction oldReaction, boolean like) {
        Movie movie = oldReaction.getMovie();
        if (oldReaction.getIsLike() == null) {
            addMovieReaction(movie, like);
            oldReaction.addReactionLike(like);
            log.info("User with id: {} {} movie with id: {}.",
                    oldReaction.getUser().getId(), like ? "Likes" : "Hates", movie.getId());
        } else if (oldReaction.getIsLike() == like) {
            removeMovieReaction(movie, like);
            oldReaction.removeReaction();
            log.info("User with id: {} removed {} from movie with id: {}",
                    oldReaction.getUser().getId(), like ? "Like" : "Hate", movie.getId());
        } else {
            changeMovieReaction(movie, like);
            oldReaction.addReactionLike(like);
            log.info("User with id: {} changed from {} to {} for movie with id: {}",
                    oldReaction.getUser().getId(), like ? "Hate" : "Like", like ? "Like" : "Hate", movie.getId());
        }
        return movieRepository.save(movie);
    }

    public void addMovieReaction(Movie movie, boolean like) {
        if (like) {
            movie.addLike();
        } else {
            movie.addHate();
        }
        movieRepository.save(movie);
    }

    private void removeMovieReaction(Movie movie, boolean like) {
        if (like) {
            movie.removeLike();
        } else {
            movie.removeHate();
        }
    }

    private void changeMovieReaction(Movie movie, boolean like) {
        if (like) {
            movie.removeHate();
            movie.addLike();
        } else {
            movie.removeLike();
            movie.addHate();
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
