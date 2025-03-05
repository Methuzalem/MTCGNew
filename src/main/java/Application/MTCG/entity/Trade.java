package Application.MTCG.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade {
    private String id;
    private String cardToTrade;
    private String type;
    private float minimumDamage;
    private String tradeStatus;
    private String ownerId;

    public Trade(@JsonProperty("Id") String id,
                       @JsonProperty("CardToTrade") String cardToTrade,
                       @JsonProperty("Type") String type,
                       @JsonProperty("MinimumDamage") float minimumDamage) {
        this.id = id;
        this.type = type;
        this.cardToTrade = cardToTrade;
        this.minimumDamage = minimumDamage;
        this.tradeStatus = "open";
    }

    public Trade(String id, String cardToTrade, String type, float minimumDamage, String tradeStatus, String ownerId) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minimumDamage = minimumDamage;
        this.tradeStatus = tradeStatus;
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardToTrade() {
        return cardToTrade;
    }

    public void setCardToTrade(String cardToTrade) {
        this.cardToTrade = cardToTrade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMinimumDamage() {
        return minimumDamage;
    }

    public void setMinimumDamage(float minimumDamage) {
        this.minimumDamage = minimumDamage;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
