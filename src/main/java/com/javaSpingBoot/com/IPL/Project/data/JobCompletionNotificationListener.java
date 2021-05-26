package com.javaSpingBoot.com.IPL.Project.data;


import com.javaSpingBoot.com.IPL.Project.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport{

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager entityManager;

    @Autowired
    public JobCompletionNotificationListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String,Team> stringTeamMap=new HashMap<>();

          /*  jdbcTemplate.query("SELECT team1, team2, date FROM match",
                    (rs, row) -> "team1 "+ rs.getString(1)+ " team2 " +rs.getString(2)+" date "+rs.getString(3)
            ).forEach(str -> System.out.println(str) );*/

            entityManager.createQuery("select new  com.javaSpingBoot.com.IPL.Project.model.Team ( m.team1, count(*)) from Match m group by m.team1", Team.class).
                    getResultList().
                    forEach(team -> stringTeamMap.put(team.getTeamName(), team));

           stringTeamMap.values().stream().forEach(System.out::println);
            System.out.println(" after last result \n");
            entityManager.createQuery("select  new  com.javaSpingBoot.com.IPL.Project.model.Team ( m.team2, count(*)) from Match m group by m.team2", Team.class).
                    getResultList().
                    forEach(team->{
                        long l=stringTeamMap.get(team.getTeamName()).getTotalMatches();
                        team.setTotalMatches(team.getTotalMatches() + l);
                        stringTeamMap.put(team.getTeamName(),team);
                     //  System.out.println(team);
                    });
            entityManager.createQuery("select  m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class).
                    getResultList().
                    stream().
                    forEach(e ->{
                        Team team=stringTeamMap.get((String) e[0]);

                       if (team != null) {
                           team.setTotalWins((long) e[1]);

                       }

                    });

            stringTeamMap.values().forEach(team -> entityManager.persist(team));
            System.out.println(" the last result :  \n");
            stringTeamMap.values().forEach(team -> System.out.println(team));


        }
    }
}
