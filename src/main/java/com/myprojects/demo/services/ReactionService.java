package com.myprojects.demo.services;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.ReactionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final MovieRepository movieRepository;

    public ReactionService(ReactionRepository reactionRepository, MovieRepository movieRepository) {
        this.reactionRepository = reactionRepository;
        this.movieRepository = movieRepository;
    }

    public Reaction addReaction(User user, Movie movie, Reaction.ReactionType reactionType) {
        Reaction reaction = new Reaction(user, movie);
        switch (reactionType) {
            case LIKE:
                reaction.setIsLike(true);
                reaction.setIsHate(false);
            case HATE:
                reaction.setIsHate(true);
                reaction.setIsLike(false);
        }
        return reactionRepository.save(reaction);
    }

    public Reaction undo(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found."));
        Reaction reaction = reactionRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new EntityNotFoundException("There is no reaction for movie: " + movie.getTitle() + " by user: " + user.getUsername()));
        reaction.undo();
        return reactionRepository.save(reaction);
    }
}
