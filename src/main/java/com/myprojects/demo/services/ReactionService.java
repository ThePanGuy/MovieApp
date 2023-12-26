package com.myprojects.demo.services;

import com.myprojects.demo.dto.MovieReactions;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.ReactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
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
    public Reaction likeOrUnlikeMovie(MovieUser movieUser, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        if (movie.getUploadedBy() == movieUser) {
            throw new InvalidInputException("You can not rate your own movies.");
        }
        return addOrUpdateReaction(movieUser, movie, true);
    }

    @Transactional
    public Reaction hateOrUnhateMovie(MovieUser movieUser, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        if (movie.getUploadedBy() == movieUser) {
            throw new InvalidInputException("You can not rate your own movies.");
        }
        return addOrUpdateReaction(movieUser, movie, false);
    }

    @Transactional
    public Reaction addOrUpdateReaction(MovieUser movieUser, Movie movie, boolean isLike) {
        Optional<Reaction> reaction = reactionRepository.findByMovieUserAndMovie(movieUser, movie);
        return reaction.map(value -> updateOrRemoveMovieReaction(value, isLike))
                .orElseGet(() -> addNewReaction(movieUser, movie, isLike));
    }

    private Reaction addNewReaction(MovieUser movieUser, Movie movie, boolean like) {
        Reaction newReaction = new Reaction(movieUser, movie, like);
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

    public MovieReactions getReactions(Movie movie) {
        List<Reaction> reactions = movie.getReactions();
        Long numberOfLikes = reactions.stream().filter(Reaction::getIfLike).count();
        Long numberOfHates = reactions.stream().filter(Reaction::getIfHate).count();
        return new MovieReactions(numberOfLikes, numberOfHates);
    }
}
