import java.util.*;

public class Deck {

    ArrayList<Card> deck = new ArrayList<Card>();
    //creates an arraylist called deck that stores cards

    public Deck() {
        
        deckCreate();
        //create deck on initialization

    }

    //GETTERS

    public String deckGetCardString(int index) {
        return deck.get(index).getCard();
        //gets card string
    }

    public Card deckGetCardObject(int index) {
        return deck.get(index);
        //gets card object
    }

    //DISPLAY

    public void deckDisplay() {

        System.out.print("\n");
        System.out.print("Deck: ");
        for(int i = 0; i < deck.size(); i++){
            System.out.print(deck.get(i).getCard() + " ");
        }
        
    }

    //SHUFFLE

    public void deckShuffle() {

        for(int i = 0; i < 10000; i++){
            int index1 = (int)(Math.random() * 52);
            int index2 = (int)(Math.random() * 52);
            Card temp = deckGetCardObject(index2);
            deck.set(index2, deck.get(index1));
            deck.set(index1, temp);
        }

    }

    //DEAL

    public Card deckDeal() {

        Card temp = deckGetCardObject(0);
        deck.remove(0);
        return temp;
        //returns a card and then removes it from the deck

    }

    //RESET

    public void deckReset() {

        while(!deck.isEmpty()){
            deck.remove(0);
        }
        deckCreate();
        //emptys then creates an ordered deck

    }

    //CREATE

    public void deckCreate() {

        String[] ranks = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] suits = {"s", "h", "d", "c"};
        for(int j = 0; j < suits.length; j++){
            for(int i = 0; i < ranks.length; i++){
                deck.add(new Card(ranks[i], suits[j]));
            }
        }
        //adds all 52 cards of the deck by adding the cards by suits

    }

    
}
