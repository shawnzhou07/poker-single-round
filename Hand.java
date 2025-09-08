import java.util.*;

public class Hand {
    
    ArrayList<Card> hand = new ArrayList<Card>();
    ArrayList<Integer> handRankAndKickers = new ArrayList<Integer>();

    public Hand() {

    }

    //DISPLAY

    public String handDisplay() {

        String handString = "";
        for(int i = 0; i < hand.size(); i++){
            handString = handString + hand.get(i).getCard();
            if(i == 0){
                handString = handString + " ";
            }
        }
        return handString;

    }

    //DEAL

    public void handDeal(Deck deck) {

        hand.add(deck.deckDeal());
        hand.add(deck.deckDeal());

    }

    public void handReset(){

        while(!hand.isEmpty()){
            hand.remove(0);
        }

    }

    public void handRankAndKickersReset(){

        while(!handRankAndKickers.isEmpty()){
            handRankAndKickers.remove(0);
        }

    }

    //HAND RANKING

    public int handGetHandRankIndex(int i){
        return handRankAndKickers.get(i);
    }

    public ArrayList<Integer> handGetHandRank(Board board){

        handRank(board);
        return handRankAndKickers;

    }

    public void handRank(Board board){

    ArrayList<String> allCards = new ArrayList<>();

    for(int i = 0; i < hand.size(); i++){
        allCards.add(hand.get(i).getCard());
    }

    for(int i = 0; i < board.size(); i++){
        allCards.add(board.get(i).getCard());
    }

    ArrayList<String> suitUnsorted = new ArrayList<>();
    ArrayList<String> rankUnsorted = new ArrayList<>();

    for(int i = 0; i < allCards.size(); i++){
        rankUnsorted.add(String.valueOf(allCards.get(i).charAt(0)));
        suitUnsorted.add(String.valueOf(allCards.get(i).charAt(1)));
    }

    ArrayList<Integer> valueUnsorted = handRankRankToValue(rankUnsorted);

    ArrayList<Integer> value = new ArrayList<>();
    ArrayList<String> suit = new ArrayList<>();

    for(int i = 0; i < allCards.size(); i++){
        int minValue = 15;
        int minIndex = 0;
        for(int j = 0; j < allCards.size(); j++){
            if(minValue > valueUnsorted.get(j)){
                minValue = valueUnsorted.get(j);
                minIndex = j;
            }
        }
        value.add(valueUnsorted.get(minIndex));
        suit.add(suitUnsorted.get(minIndex));
        valueUnsorted.set(minIndex, 99);
    }

    HashMap<Integer, Integer> countedValues = handRankCountValues(value);
    HashMap<String, Integer> countedSuits = handRankCountSuits(suit);

    boolean flush = false;
    String flushSuit = "";
    if(countedSuits.containsValue(5) || countedSuits.containsValue(6) || countedSuits.containsValue(7)){
        flush = true;
        for(String s : countedSuits.keySet()){
            if(countedSuits.get(s) == 5 || countedSuits.get(s) == 6 || countedSuits.get(s) == 7){
                flushSuit = s;
            }
        }
    }

    ArrayList<Integer> flushValues = new ArrayList<>();
    for(int i = 0; i < allCards.size(); i++){
        if(flushSuit.equals(suit.get(i))){
            flushValues.add(value.get(i));
        }
    }
    
    boolean straightFlush = false;
    int consecutiveFlushValues = 0;
    int straightFlushHeight = 0;
    for(int i = 0; i < flushValues.size() - 1; i++){
        if(flushValues.get(i+1) - flushValues.get(i) == 1){
            consecutiveFlushValues++;
        } else {
            consecutiveFlushValues = 0;
        }
        if(consecutiveFlushValues >= 4){
            straightFlush = true;
            straightFlushHeight = flushValues.get(i+1);
        }
    }
    if(flushValues.containsAll(Arrays.asList(14, 2, 3, 4, 5))){
        straightFlush = true;
        straightFlushHeight = 5;
    }

    boolean royalFlush = false;
    if(straightFlush && straightFlushHeight == 14){
        royalFlush = true;
    }

    ArrayList<Integer> straightValues = new ArrayList<>();
    for(int i = 0; i < allCards.size(); i++){
        if(!straightValues.contains(value.get(i))){
            straightValues.add(value.get(i));
        }
    }
    boolean straight = false;
    int consecutiveStraightValues = 0;
    int straightHeight = 0;
    for(int i = 0; i < straightValues.size() - 1; i++){
        if(straightValues.get(i+1) - straightValues.get(i) == 1){
            consecutiveStraightValues++;
        } else {
            consecutiveStraightValues = 0;
        }
        if(consecutiveStraightValues >= 4){
            straight = true;
            straightHeight = straightValues.get(i+1);
        }
    }
    if(straightValues.containsAll(Arrays.asList(14, 2, 3, 4, 5))){
        straight = true;
        straightHeight = 5;
    }
    
    boolean quads = false;
    int quadsValue = 0;
    if(countedValues.containsValue(4)){
        quads = true;
    }
    for(int num : countedValues.keySet()){
        if(countedValues.get(num) == 4){
            quadsValue = num;
        }
    }

    boolean tripsOne = false; 
    boolean tripsTwo = false;
    int tripsOneValue = 0;
    int tripsTwoValue = 0;

    for(int num : countedValues.keySet()){
        if(countedValues.get(num) == 3){
            if(tripsOne){
                tripsTwo = true;
                tripsTwoValue = num;
            } else {
                tripsOne = true;
                tripsOneValue = num;
            }
        }
    }

    boolean pairOne = false; 
    boolean pairTwo = false;
    boolean pairThree = false;
    int pairOneValue = 0;
    int pairTwoValue = 0;
    int pairThreeValue = 0;

    for(int num : countedValues.keySet()){
        if(countedValues.get(num) == 2){
            if(pairOne && pairTwo){
                pairThree = true;
                pairThreeValue = num;
            } else if(pairOne) {
                pairTwo = true;
                pairTwoValue = num;
            } else {
                pairOne = true;
                pairOneValue = num;
            }
        }
    }

    if(flush){
        if(straightFlush){
            if(royalFlush){

                handRankAndKickers.add(10);
            } else{

                handRankAndKickers.add(9);
                handRankAndKickers.add(straightFlushHeight);
            }
        } else{

            handRankAndKickers.add(6);
            for(int i = flushValues.size() - 1; i > flushValues.size() - 6; i--){
                handRankAndKickers.add(flushValues.get(i));
            }
        }
    } else if(straight) {

        handRankAndKickers.add(5);
        handRankAndKickers.add(straightHeight);  
    } else{
        if(quads) {

            handRankAndKickers.add(8);
            handRankAndKickers.add(quadsValue);
            for(int i = 0; i < allCards.size(); i++){
                if(value.get(i) == quadsValue){
                    value.set(i, 0);
                }
            }
            for(int i = allCards.size()-1; i >= 0; i--){
                if(value.get(i) != 0){
                    handRankAndKickers.add(value.get(i));
                    break;
                }
            }
        } else if(tripsOne) {

            if(tripsTwo || pairOne){
                handRankAndKickers.add(7);
                if(tripsTwo){
                    handRankAndKickers.add(tripsTwoValue);
                    if(pairThree && pairThreeValue > tripsOneValue){
                        handRankAndKickers.add(pairThreeValue);
                    } else if (pairTwo && pairTwoValue > tripsOneValue) {
                        handRankAndKickers.add(pairTwoValue);
                    } else if (pairOne && pairOneValue > tripsOneValue) {
                        handRankAndKickers.add(pairOneValue);
                    } else {
                        handRankAndKickers.add(tripsOneValue);
                    }
                } else{
                    handRankAndKickers.add(tripsOneValue);
                    if(pairThree){
                        handRankAndKickers.add(pairThreeValue);
                    } else if (pairTwo) {
                        handRankAndKickers.add(pairTwoValue);
                    } else {
                        handRankAndKickers.add(pairOneValue);
                    }
                }
            
            } else {

                handRankAndKickers.add(4);
                handRankAndKickers.add(tripsOneValue);
                for(int i = 0; i < allCards.size(); i++){
                    if(value.get(i) == tripsOneValue){
                        value.set(i, 0);
                    }
                }
                int added = 0;
                for(int i = allCards.size()-1; i >= 0; i--){
                    if(value.get(i) != 0){
                        handRankAndKickers.add(value.get(i));
                        added++;
                    }
                    if(added == 2){
                        break;
                    }
                }

            }
        } else if(pairOne){

            if(pairTwo){

                handRankAndKickers.add(3);
                if(pairThree){
                    handRankAndKickers.add(pairThreeValue);
                    handRankAndKickers.add(pairTwoValue);
                    for(int i = 0; i < allCards.size(); i++){
                        if(value.get(i) == pairThreeValue || value.get(i) == pairTwoValue){
                            value.set(i, 0);
                        }
                    }
                    for(int i = allCards.size()-1; i >= 0; i--){
                        if(value.get(i) != 0){
                            handRankAndKickers.add(value.get(i));
                            break;
                        }
                    }
                } else {
                    handRankAndKickers.add(pairTwoValue);
                    handRankAndKickers.add(pairOneValue);
                    for(int i = 0; i < allCards.size(); i++){
                        if(value.get(i) == pairOneValue || value.get(i) == pairTwoValue){
                            value.set(i, 0);
                        }
                    }
                    for(int i = allCards.size()-1; i >= 0; i--){
                        if(value.get(i) != 0){
                            handRankAndKickers.add(value.get(i));
                            break;
                        }
                    }
                }
            } else {

                handRankAndKickers.add(2);
                handRankAndKickers.add(pairOneValue);
                for(int i = 0; i < allCards.size(); i++){
                    if(value.get(i) == pairOneValue){
                        value.set(i, 0);
                    }
                }
                int added = 0;
                for(int i = allCards.size()-1; i >= 0; i--){
                    if(value.get(i) != 0){
                        handRankAndKickers.add(value.get(i));
                        added++;
                    }
                    if(added == 3){
                        break;
                    }
                }
            }
        } else {

            handRankAndKickers.add(1);
            for(int i = allCards.size() - 1; i > allCards.size() - 6; i--){
                handRankAndKickers.add(value.get(i));
            }
        }
    }

    int blanks = 6 - handRankAndKickers.size();

    for(int i = 0; i < blanks; i++){
        handRankAndKickers.add(0);
    }

}

public static HashMap<Integer, Integer> handRankCountValues (ArrayList<Integer> values){

    HashMap<Integer, Integer> countedValues = new HashMap<>();
    for(int i = 0; i < values.size(); i++){
        if(countedValues.containsKey(values.get(i))){
            countedValues.put(values.get(i), countedValues.get(values.get(i)) + 1);
        } else {
            countedValues.put(values.get(i), 1);
        }
    }

    return countedValues;
}

public static HashMap<String, Integer> handRankCountSuits (ArrayList<String> suits){

    HashMap<String, Integer> countedSuits = new HashMap<>();
    for(int i = 0; i < suits.size(); i++){
        if(countedSuits.containsKey(suits.get(i))){
            countedSuits.put(suits.get(i), countedSuits.get(suits.get(i)) + 1);
        } else {
            countedSuits.put(suits.get(i), 1);
        }
    }

    return countedSuits;
}

public static ArrayList<Integer> handRankRankToValue (ArrayList<String> rank){

    ArrayList<Integer> values = new ArrayList<>();
    for(int i = 0; i < rank.size(); i++){
        switch(rank.get(i)) {
            case "A": 
                values.add(14);
                break;
            case "K": 
                values.add(13);
                break;
            case "Q": 
                values.add(12);
                break;
            case "J": 
                values.add(11);
                break;
            case "T": 
                values.add(10);
                break;
            case "9": 
                values.add(9);
                break;
            case "8": 
                values.add(8);
                break;
            case "7": 
                values.add(7);
                break;
            case "6": 
                values.add(6);
                break;
            case "5": 
                values.add(5);
                break;
            case "4": 
                values.add(4);
                break;
            case "3": 
                values.add(3);
                break;
            case "2": 
                values.add(2);
                break;
        }

    }

    return values;

}

public static String handValueToRank(int value) {

    switch (value) {
        case 14: return "Ace";
        case 13: return "King";
        case 12: return "Queen";
        case 11: return "Jack";
        case 10: return "Ten";
        case 9: return "Nine";
        case 8: return "Eight";
        case 7: return "Seven";
        case 6: return "Six";
        case 5: return "Five";
        case 4: return "Four";
        case 3: return "Three";
        case 2: return "Two";
        default: return "Unknown";
        
    }
}

public static String handDisplayDetails(String name, ArrayList<Integer> hand) {
    
    String output = name + " has ";
    int rank = hand.get(0);

    if (rank == 10) {
        output += "a royal flush";
    } else if (rank == 9) {
        output += "a straight flush, " + handValueToRank(hand.get(1)) + " to " + (handValueToRank(hand.get(1) - 4));
    } else if (rank == 8) {
        output += "four of a kind, " + handValueToRank(hand.get(1)) + "s with " + handValueToRank(hand.get(2)) + " kicker";
    } else if (rank == 7) {
        output += "a full house, " + handValueToRank(hand.get(1)) + "s full of " + handValueToRank(hand.get(2)) + "s";
    } else if (rank == 6) {
        output += "a flush, " + handValueToRank(hand.get(1)) + " high with " 
        + handValueToRank(hand.get(2)) + ", "
        + handValueToRank(hand.get(3)) + ", "
        + handValueToRank(hand.get(4)) + ", and "
        + handValueToRank(hand.get(5)) + " kickers";
    } else if (rank == 5) {
        output += "a straight, high card " + handValueToRank(hand.get(1)) + " to " + (handValueToRank(hand.get(1) - 4));
    } else if (rank == 4) {
        output += "three of a kind, " + handValueToRank(hand.get(1)) + "s with " + handValueToRank(hand.get(2)) + " and " + handValueToRank(hand.get(3)) + " kicker";
    } else if (rank == 3) {
        output += "two pair, " + handValueToRank(hand.get(1)) + "s and " + handValueToRank(hand.get(2)) + "s with " + handValueToRank(hand.get(3)) + " kicker";
    } else if (rank == 2) {
        output += "a pair of " + handValueToRank(hand.get(1)) + "s with " + handValueToRank(hand.get(2)) + ", " + handValueToRank(hand.get(3)) + ", and " + handValueToRank(hand.get(4)) + " kickers";
    } else if (rank == 1) {
        output += "high card " + handValueToRank(hand.get(1)) + " with " + handValueToRank(hand.get(2)) + ", " + handValueToRank(hand.get(3)) + ", " + handValueToRank(hand.get(4)) + ", and " + handValueToRank(hand.get(5)) + " kickers";
    } else {
        output += "an unrecognized hand";
    }

    return output;
}

}