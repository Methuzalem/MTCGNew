package Application.MTCG.dto;

public class ShowStatsDTO {
    private String name;
    private String elo;
    private String wins;
    private String losses;

    public ShowStatsDTO(String name, String elo, String wins, String losses) {
        this.name = name;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    public ShowStatsDTO() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getElo() {
        return elo;
    }

    public void setElo(String elo){
        this.elo = elo;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins){
        this.wins = wins;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses){
        this.losses = losses;
    }
}
