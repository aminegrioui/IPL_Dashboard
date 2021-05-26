package com.javaSpingBoot.com.IPL.Project.controller;

import com.javaSpingBoot.com.IPL.Project.model.Match;
import com.javaSpingBoot.com.IPL.Project.model.Team;
import com.javaSpingBoot.com.IPL.Project.repository.MatchRepository;
import com.javaSpingBoot.com.IPL.Project.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@CrossOrigin
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @GetMapping("/team/{teamName}")
    public Team getTeamByTeamName(@PathVariable String teamName){
        Team team=teamRepository.findByTeamName(teamName);

        List<Match> matches=matchRepository.findLastetMatches(teamName,4);
        team.setMatches(matches);
        return  team;
    }
}
