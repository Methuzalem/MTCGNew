package Application.MTCG.entity;

import Application.MTCG.exceptions.InvalidCardName;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    private String cardID;
    private CardName cardName;
    private String elementType;
    private float damage;
    private String ownerID;
    private String deckUserId;

    public Card(@JsonProperty("Id") String id,
                @JsonProperty("Name") String name,
                @JsonProperty("Damage") float damage,
                @JsonProperty("ownerUuid") String ownerUuid,
                @JsonProperty("DeckUserId") String deckUserId) {
        this.cardID = id;
        try {
            this.cardName = CardName.valueOf(name);
            switch (cardName) {
                case FireGoblin, FireDragon, FireWizzard, FireOrk, FireKnight, FireKraken, FireElf, FireSpell -> this.elementType = "Fire";
                case WaterGoblin, WaterDragon, WaterWizzard, WaterOrk, WaterKnight, WaterKraken, WaterElf, WaterSpell -> this.elementType = "Water";
                case RegularGoblin, RegularDragon, RegularWizzard, RegularOrk, RegularKnight, RegularKraken, RegularElf, RegularSpell -> this.elementType = "Regular";
                default -> elementType = "Unknown";
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidCardName("Invalid card name: " + name);
        }
        this.damage = damage;
        this.ownerID = ownerUuid;
        this.deckUserId = deckUserId;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public CardName getCardName() {
        return cardName;
    }

    public void setCardName(CardName cardName) {
        this.cardName = cardName;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getDeckUserId() {
        return deckUserId;
    }

    public void setDeckUserId(String deckUserId) {
        this.deckUserId = deckUserId;
    }
}
