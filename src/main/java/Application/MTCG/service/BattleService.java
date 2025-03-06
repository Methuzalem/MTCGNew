package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.repositorys.DeckRepo;
import Application.MTCG.repositorys.CardRepo;
import Application.MTCG.entity.User;
import Application.MTCG.repositorys.UserRepo;

import java.util.List;
import java.util.Random;

public class BattleService {
    private final UserService userService;
    private final UserRepo userRepo;
    private final CardRepo cardRepo;
    private final StringBuilder battleLog = new StringBuilder();
    private final DeckService deckService;
    private final DeckRepo deckRepo;


    public BattleService(UserService userService, UserRepo userRepo, CardRepo cardRepo, DeckService deckService, DeckRepo deckRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.cardRepo = cardRepo;
        this.deckService = deckService;
        this.deckRepo = deckRepo;
    }

    public String startBattle(String loginTokenPlayer1, String loginTokenPlayer2) {
        User player1 = userService.getUserByToken(loginTokenPlayer1);
        User player2 = userService.getUserByToken(loginTokenPlayer2);
        String deckIdPlayer1 = deckRepo.getDeckIdFromUser(player1);
        String deckIdPlayer2 = deckRepo.getDeckIdFromUser(player2);
        List<Card> tempDeckPlayer1 = deckService.getDeckCardsOfUser(loginTokenPlayer1);
        List<Card> tempDeckPlayer2 = deckService.getDeckCardsOfUser(loginTokenPlayer2);
        double damagePlayer1;
        double damagePlayer2;
        Random rand = new Random();
        int roundCounter = 1;
        String winMSG;

        battleLog.append("Welcome to the epic battle between ").append(player1.getName()).append(" and ").append(player2.getName()).append(System.lineSeparator());
        battleLog.append("Good luck to both of you").append('\n');

        while (true) {
            if (tempDeckPlayer1.isEmpty() && tempDeckPlayer2.isEmpty()) {
                break;
            } else if (tempDeckPlayer1.isEmpty() || tempDeckPlayer2.isEmpty() || roundCounter == 101) {
                break;
            }

            battleLog.append("\n").append("Round ").append(roundCounter).append(":").append('\n');
            Card cardPlayer1 = tempDeckPlayer1.get(rand.nextInt(tempDeckPlayer1.size()));
            Card cardPlayer2 = tempDeckPlayer2.get(rand.nextInt(tempDeckPlayer2.size()));

            battleLog.append("Player1 plays ").append(cardPlayer1.getCardName()).append(" with ").append(cardPlayer1.getDamage()).append(" Damage!").append('\n');
            battleLog.append("Player2 plays ").append(cardPlayer2.getCardName()).append(" with ").append(cardPlayer2.getDamage()).append(" Damage!").append('\n');

            damagePlayer1 = calculateDamage(cardPlayer1, cardPlayer2, battleLog);
            damagePlayer2 = calculateDamage(cardPlayer2, cardPlayer1, battleLog);

            battleLog.append(cardPlayer1.getCardName()).append(" deals ").append(damagePlayer1).append(" damage!").append('\n');
            battleLog.append(cardPlayer2.getCardName()).append(" deals ").append(damagePlayer2).append(" damage!").append('\n');

            if (damagePlayer1 > damagePlayer2) {
                battleLog.append("Player 1 wins the ").append(roundCounter).append(" Round").append('\n').append("\n");
                tempDeckPlayer1.add(cardPlayer2);
                tempDeckPlayer2.remove(cardPlayer2);
                cardRepo.updateDeckIdAndOwner(deckIdPlayer1, player1, cardPlayer2);
                roundCounter++;
            } else if (damagePlayer2 > damagePlayer1) {
                battleLog.append("Player 2 wins the ").append(roundCounter).append(" Round").append('\n').append("\n");
                tempDeckPlayer2.add(cardPlayer1);
                tempDeckPlayer1.remove(cardPlayer1);
                cardRepo.updateDeckIdAndOwner(deckIdPlayer2, player2, cardPlayer1);
                roundCounter++;
            } else {
                battleLog.append("Its a draw. The cards got the same power!").append('\n');
                roundCounter++;
            }

            if (roundCounter == 50) {
                int random = new Random().nextInt(2);

                if (random == 1) {
                    tempDeckPlayer1 = enhanceMonsters(tempDeckPlayer1);
                    tempDeckPlayer2 = enhanceSpells(tempDeckPlayer2);
                } else {
                    tempDeckPlayer1 = enhanceSpells(tempDeckPlayer1);
                    tempDeckPlayer2 = enhanceMonsters(tempDeckPlayer2);
                }
            }
        }

        if (tempDeckPlayer1.isEmpty() && tempDeckPlayer2.isEmpty()) {
            winMSG = "Its a draw";
            battleLog.append(winMSG).append('\n');
        } else if (tempDeckPlayer1.isEmpty()) {
            winMSG = " " + player2.getName() + " wins the epic battle! \n";
            battleLog.append(winMSG).append('\n');
            calculateElo(player2, player1, battleLog);
        } else if (tempDeckPlayer2.isEmpty()) {
            winMSG = player1.getName() + " wins the epic battle!";
            battleLog.append(winMSG).append('\n');
            calculateElo(player1, player2, battleLog);
        } else if (roundCounter == 101) {
            winMSG = "We reached the limit of Rounds. The Battle is now over. The cards you won, stay with you.";
            battleLog.append(winMSG).append('\n');
        }
        return battleLog.toString();
    }

    double calculateDamage(Card card1, Card card2, StringBuilder battleLog) {
        double damage = 0;
        double damageEffectiveness = calculateEffectiveness(card1, card2);

        if (card1.getCardName().contains("Goblin") && card2.getCardName().contains("Dragon")) {
            battleLog.append("Goblins are too afraid of Dragons to attack.").append('\n');
            return damage;
        } else if (card1.getCardName().contains("Ork") && card2.getCardName().contains("Wizzard")) {
            battleLog.append("Wizzard can control Orks so they are not able to damage them.").append('\n');
            return damage;
        } else if (card1.getCardName().contains("Knight") && card2.getCardName().equals("WaterSpell")) {
            battleLog.append("The armor of Knights is so heavy that WaterSpells make them drown them instantly before dealing damage.").append('\n');
            return damage;
        } else if (card1.getCardName().contains("Spell") && card2.getCardName().contains("Kraken")) {
            battleLog.append("The Kraken is immune against spells.").append('\n');
            return damage;
        } else if (card1.getCardName().contains("Dragon") && card2.getCardName().equals("FireElf")) {
            battleLog.append("The FireElves know Dragons since they were little and can evade their attacks.").append('\n');
            return damage;
        }

        damage = card1.getDamage();

        return damage * damageEffectiveness;
    }

    double calculateEffectiveness(Card card1, Card card2) {
        double effectiveness = 1.00;

        if (card1.getCardName().contains("Spell")) {
            if (card1.getElementType().equals("Fire") && card2.getElementType().equals("Regular")) {
                effectiveness = 2.00;
            } else if (card1.getElementType().equals("Water") && card2.getElementType().equals("Fire")) {
                effectiveness = 2.00;
            } else if (card1.getElementType().equals("Regular") && card2.getElementType().equals("Water")) {
                effectiveness = 2.00;
            } else if (card1.getElementType().equals("Fire") && card2.getElementType().equals("Water")) {
                effectiveness = 0.50;
            }
        }
        return effectiveness;
    }

    void calculateElo(User winner, User loser, StringBuilder battleLog) {
        winner.setElo(winner.getElo() + 3);
        winner.setWins(winner.getWins() + 1);
        loser.setElo(loser.getElo() - 5);
        loser.setLosses(loser.getLosses() + 1);

        userRepo.updateUserByUuid(winner);
        userRepo.updateUserByUuid(loser);

        battleLog.append(winner.getName()).append(" Elo increases for +3 points!").append('\n');
        battleLog.append(winner.getName()).append("s Elo is now ").append(winner.getElo()).append('\n');
        battleLog.append(loser.getName()).append(" Elo decreases for -5 points!").append('\n');
        battleLog.append(loser.getName()).append("s Elo is now ").append(loser.getElo()).append('\n');
    }

    List<Card> enhanceMonsters(List<Card> tempDeck) {
        for (int i = 0; i < tempDeck.size(); i++) {
            if (tempDeck.get(i).getCardName().contains("Goblin") ||
                    tempDeck.get(i).getCardName().contains("Dragon") ||
                    tempDeck.get(i).getCardName().contains("Ork") ||
                    tempDeck.get(i).getCardName().contains("Kraken") ||
                    tempDeck.get(i).getCardName().contains("Elf") ||
                    tempDeck.get(i).getCardName().contains("Wizzard") ||
                    tempDeck.get(i).getCardName().contains("Knight")) {

                tempDeck.get(i).setDamage(tempDeck.get(i).getDamage() + 25);
            }
        }
        return tempDeck;
    }

    List<Card> enhanceSpells(List<Card> tempDeck) {
        for (int i = 0; i < tempDeck.size(); i++) {
            if (tempDeck.get(i).getCardName().contains("Spell")) {
                tempDeck.get(i).setDamage(tempDeck.get(i).getDamage() + 10);
            }
        }
        return tempDeck;
    }
}
