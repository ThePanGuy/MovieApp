package com.myprojects.demo.services;

import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.exceptions.InvalidInputException;
import com.myprojects.demo.exceptions.UsernameException;
import com.myprojects.demo.repositories.MovieRepository;
import com.myprojects.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReactionService reactionService;

    public UserService(UserRepository userRepository, MovieRepository movieRepository, ReactionService reactionService) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.reactionService = reactionService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Reaction likeMovie(User user, Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        if (movie.getUploadedBy() == user) {
            throw new InvalidInputException("You can not rate your own movies.");
        }
        movie.addLike();
        movieRepository.save(movie);
        return reactionService.addReaction(user, movie, Reaction.ReactionType.LIKE);
    }

    @Transactional
    public User addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        Optional<User> existingUser = userRepository.findUserByUsername(username);
        if (existingUser.isPresent()) {
            throw new UsernameException("Username: " + username + " already exists.");
        }
        user = userRepository.save(user);
        log.info("New user: {} added.", username);
        return user;
    }
}
