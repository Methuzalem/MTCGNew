package Application.MTCG.entity;

public class Deck {
    private String deckId;
    private String userId;

    public Deck() {}

    public Deck(String deckId, String userId) {
        this.deckId = deckId;
        this.userId = userId;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
