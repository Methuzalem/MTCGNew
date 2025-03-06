package Application.MTCG.entity;

import Application.MTCG.exceptions.InvalidCardName;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    private String cardID;
    private CardName cardName;
    private float damage;
    private String ownerID;
    private String elementType;
    private String deckId;

    public Card(@JsonProperty("Id") String id,
                @JsonProperty("Name") String name,
                @JsonProperty("elementType") String elementType,
                @JsonProperty("Damage") float damage,
                @JsonProperty("ownerUuid") String ownerUuid,
                @JsonProperty("deckId") String deckId) {
        this.cardID = id;
        this.elementType = elementType;
        try {
            this.cardName = CardName.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new InvalidCardName("Invalid card name: " + name);
        }
        switch (cardName) {
            case FireGoblin, FireDragon, FireWizzard, FireOrk, FireKnight, FireKraken, FireElf, FireSpell ->
                    this.elementType = "Fire";
            case WaterGoblin, WaterDragon, WaterWizzard, WaterOrk, WaterKnight, WaterKraken, WaterElf, WaterSpell ->
                    this.elementType = "Water";
            case RegularGoblin, RegularDragon, RegularWizzard, RegularOrk, RegularKnight, RegularKraken, RegularElf,
                 RegularSpell -> this.elementType = "Regular";
            default -> this.elementType = "Unknown";
        }
        this.damage = damage;
        this.ownerID = ownerUuid;
        this.deckId = deckId;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getCardName() {
        return cardName.getName();
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

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }
}
