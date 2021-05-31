package com.javaSpingBoot.com.IPL.Project.controller;

import com.javaSpingBoot.com.IPL.Project.model.Match;
import com.javaSpingBoot.com.IPL.Project.model.Team;
import com.javaSpingBoot.com.IPL.Project.repository.MatchRepository;
import com.javaSpingBoot.com.IPL.Project.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
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

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTame(@PathVariable String teamName, @RequestParam int year){
        LocalDate start=LocalDate.of(year,1,1);
        LocalDate end=LocalDate.of(year + 1,1,1);

        // using API og JPA
     /*   return  this.matchRepository.findByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(teamName,
                start,end,teamName,start,end);*/

        return  this.matchRepository.getMatchesBetweenDate(teamName,start,end);

    }


}
