package Application.MTCG.service;

import Application.MTCG.dto.ShowStatsDTO;
import Application.MTCG.repositorys.UserRepo;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

public class ScoreboardService {
    private final UserRepo userRepo;

    public ScoreboardService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<ShowStatsDTO> buildScoreBoard() {
        List<ShowStatsDTO> scoreboard = userRepo.getAllUserStats();
        /* Elomanipulation for Test
        int newElo = 101;
        for (ShowStatsDTO stats : scoreboard) {
            stats.setElo(String.valueOf(newElo));
            newElo++;
        }
        */
        Collections.sort(scoreboard, new Comparator<ShowStatsDTO>() {
            @Override
            public int compare(ShowStatsDTO s1, ShowStatsDTO s2) {
                return Integer.compare(Integer.parseInt(s2.getElo()), Integer.parseInt(s1.getElo())); // decending
            }
        });

        scoreboard.removeIf(stats -> stats.getName().equals("Enter your name here"));

        return scoreboard;
    }

}
