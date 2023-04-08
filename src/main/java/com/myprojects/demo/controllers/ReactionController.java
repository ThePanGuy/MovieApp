package com.myprojects.demo.controllers;

import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.entities.User;
import com.myprojects.demo.repositories.UserRepository;
import com.myprojects.demo.services.ReactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reaction")
public class ReactionController {
    private final ReactionService reactionService;
    private final UserRepository userRepository;

    public ReactionController(ReactionService reactionService, UserRepository userRepository) {
        this.reactionService = reactionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/undo/{userId}/{movieId}")
    public Reaction undoReaction(@PathVariable Long userId,
                                 @PathVariable Long movieId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        return reactionService.undo(user, movieId);
    }
}
