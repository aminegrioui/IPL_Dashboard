package com.javaSpingBoot.com.IPL.Project.repository;

import com.javaSpingBoot.com.IPL.Project.model.Match;
import com.javaSpingBoot.com.IPL.Project.model.Team;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends CrudRepository<Match,Long> {

    List<Match> findByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable pageable);

    default  List<Match> findLastetMatches(String nameTeam, int count){
      return   findByTeam1OrTeam2OrderByDateDesc(nameTeam,nameTeam, PageRequest.of(0,count));
    }

    /*
        Method to get Match for a teamName between two date
        using api of jpa findBy ....
     */
    List<Match> findByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(String team1,
                                                                              LocalDate start,
                                                                              LocalDate end,
                                                                              String team2,
                                                                              LocalDate start1,
                                                                              LocalDate end1);

    /*
       use query
     */

    @Query("select m from Match m where (m.team1 = :teamName or " +
            "m.team2=: teamName ) and m.date between :start and :end order by date Desc")
    List<Match> getMatchesBetweenDate( @Param("teamName") String teamName,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);

}
