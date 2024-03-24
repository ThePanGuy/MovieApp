package com.myprojects.demo.controllers.secured;

import com.myprojects.demo.dto.movie.MovieReactions;
import com.myprojects.demo.entities.MovieUser;
import com.myprojects.demo.entities.Reaction;
import com.myprojects.demo.services.ReactionService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("secured/reaction")
public class ReactionController {
    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping("/like/{movieId}")
    public MovieReactions likeMovie(@AuthenticationPrincipal MovieUser user,
                                    @PathVariable Long movieId) {
        Reaction reaction = reactionService.likeOrUnlikeMovie(user, movieId);
        return reactionService.getReactions(reaction.getMovie());
    }

    @GetMapping("/hate/{movieId}")
    public MovieReactions hateMovie(@AuthenticationPrincipal MovieUser user, @PathVariable Long movieId) {
        Reaction reaction = reactionService.hateOrUnhateMovie(user, movieId);
        return reactionService.getReactions(reaction.getMovie());
    }
}
