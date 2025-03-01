package Application.MTCG.entity;

public class User {
    private String uuid = "";
    private String username;
    private String password;
    private String token;
    private int coins;
    private int packageCount = 0;
    private String bio = "";
    private int elo;
    private String image = "";
    private int wins = 0;
    private int losses = 0;


    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String uuid, String username, String password, String token, int coins, int packageCount, String bio, String image, int elo, int wins, int losses) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.token = token;
        this.coins = coins;
        this.packageCount = packageCount;
        this.bio = bio;
        this.image = image;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBio() {
        return bio;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getImage() {
        return image;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPackageCount() {return packageCount;}

    public void setPackageCount(int packageCount) {this.packageCount = packageCount;}
}
