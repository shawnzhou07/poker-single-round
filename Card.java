public class Card {
    
    private String rankString;
    private int rankInteger;
    private String suit;

    public Card(String rankString, String suit) {
        this.rankString = rankString;
        this.suit = suit;

        switch (rankString) {
            case "A": rankInteger = 14; break;
            case "K": rankInteger = 13; break;
            case "Q": rankInteger = 12; break;
            case "J": rankInteger = 11; break;
            case "10": rankInteger = 10; break;
            case "9": rankInteger = 9; break;
            case "8": rankInteger = 8; break;
            case "7": rankInteger = 7; break;
            case "6": rankInteger = 6; break;
            case "5": rankInteger = 5; break;
            case "4": rankInteger = 4; break;
            case "3": rankInteger = 3; break;
            case "2": rankInteger = 2; break;
            default: rankInteger = 0; 
        }
        //assigns a number value to each single character of a cards rank
    }

    //GETTERS

    public String getCard() {
        return rankString + suit;
    }
    
    public String getCardRankString() {
        return rankString;
    }

    public Integer getCardRankInteger() {
        return rankInteger;
    }

    public String getCardSuit() {
        return suit;
    }

    //CHECK VALID

    public boolean validCard(String input) {

        boolean valid = true;

    if (input.length() != 2) {
        valid = false;
        return valid;
    }
    //if input is longer than two characters then it is not a valid card
    
    if(input.charAt(1) != 's' && input.charAt(1) != 'c' && input.charAt(1) != 'h' && input.charAt(1) != 'd'){
        valid = false;
    }
    //if the 2nd part of the input is not a valid single character of a cards suit then it is not a valid card

    if(input.charAt(0) != '2' && input.charAt(0) != '3' && input.charAt(0) != '3' && input.charAt(0) != '4' && input.charAt(0) != '5' 
    && input.charAt(0) != '6' && input.charAt(0) != '7' && input.charAt(0) != '8' && input.charAt(0) != '9' && input.charAt(0) != 'T' 
    && input.charAt(0) != 'J' && input.charAt(0) != 'Q' && input.charAt(0) != 'K' && input.charAt(0) != 'A'){
        valid = false;
    }
    //if the 1st part of the input is a valid single character of a cards ranka then it is a valid card

    return valid;

    }
}