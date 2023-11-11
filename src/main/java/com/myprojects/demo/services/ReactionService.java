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
    public Reaction likeOrUnlikeMovie(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        if (movie.getUploadedBy() == user) {
            throw new InvalidInputException("You can not rate your own movies.");
        }
        return addOrUpdateReaction(user, movie, true);
    }

    @Transactional
    public Reaction hateOrUnhateMovie(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        if (movie.getUploadedBy() == user) {
            throw new InvalidInputException("You can not rate your own movies.");
        }
        return addOrUpdateReaction(user, movie, false);
    }

    @Transactional
    public Reaction addOrUpdateReaction(User user, Movie movie, boolean isLike) {
        Optional<Reaction> reaction = reactionRepository.findByUserAndMovie(user, movie);
        return reaction.map(value -> updateOrRemoveMovieReaction(value, isLike))
                .orElseGet(() -> addNewReaction(user, movie, isLike));
    }

    private Reaction addNewReaction(User user, Movie movie, boolean like) {
        Reaction newReaction = new Reaction(user, movie);
        addMovieReaction(movie, like);
        newReaction.addReactionLike(like);
        return reactionRepository.save(newReaction);
    }


    public Reaction updateOrRemoveMovieReaction(Reaction reaction, boolean isLike) {
        Movie movie = reaction.getMovie();
        if (reaction.getIsLike() == null) {
            addMovieReaction(movie, isLike);
            reaction.addReactionLike(isLike);
            log.info("User with id: {} {} movie with id: {}.",
                    reaction.getUser().getId(), isLike ? "Likes" : "Hates", movie.getId());
        } else if (reaction.getIsLike() == isLike) {
            removeMovieReaction(movie, isLike);
            reaction.removeReaction();
            log.info("User with id: {} removed {} from movie with id: {}",
                    reaction.getUser().getId(), isLike ? "Like" : "Hate", movie.getId());
        } else {
            changeMovieReaction(movie, isLike);
            reaction.addReactionLike(isLike);
            log.info("User with id: {} changed from {} to {} for movie with id: {}",
                    reaction.getUser().getId(), isLike ? "Hate" : "Like", isLike ? "Like" : "Hate", movie.getId());
        }
        movieRepository.save(movie);
        return reactionRepository.save(reaction);
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
