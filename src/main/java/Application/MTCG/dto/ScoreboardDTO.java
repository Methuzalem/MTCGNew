package Application.MTCG.dto;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardDTO {
    private List<ShowStatsDTO> scoreboard;

    public ScoreboardDTO() {
        this.scoreboard = new ArrayList<>();
    }

    public ScoreboardDTO(List<ShowStatsDTO> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public List<ShowStatsDTO> getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(List<ShowStatsDTO> scoreboard) {
        this.scoreboard = scoreboard;
    }
}
