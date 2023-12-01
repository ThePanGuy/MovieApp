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
        Reaction newReaction = new Reaction(user, movie, like);
        return reactionRepository.save(newReaction);
    }


    public Reaction updateOrRemoveMovieReaction(Reaction reaction, boolean isLike) {
        if (reaction.getIsLike() == null) {
            reaction.setIsLike(isLike);
            log.info("User with id: {} {} movie with id: {}.",
                    reaction.getUser().getId(), isLike ? "Likes" : "Hates", reaction.getMovie().getId());
        } else if (reaction.getIsLike() == isLike) {
            reaction.setIsLike(null);
            log.info("User with id: {} removed {} from movie with id: {}",
                    reaction.getUser().getId(), isLike ? "Like" : "Hate", reaction.getMovie().getId());
        } else {
            reaction.setIsLike(isLike);
            log.info("User with id: {} changed from {} to {} for movie with id: {}",
                    reaction.getUser().getId(), isLike ? "Hate" : "Like", isLike ? "Like" : "Hate", reaction.getMovie().getId());
        }
        return reactionRepository.save(reaction);
    }
}
