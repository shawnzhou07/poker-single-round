import java.util.ArrayList;

public class Board {
    
    ArrayList<Card> board = new ArrayList<Card>();
    //creates an arraylist called board that stores cards

    private int pauseTime = 200;

    public Board() {

    }

    //DISPLAY

    public void boardDisplay(String str) {

        System.out.print("~~~ BOARD (" + str + ") ~~~");
        println("");
        for(int i = 0; i < board.size(); i++){
            print(board.get(i).getCard() + " ");
        }
        //uses a for loop to print the cards on the board
        println("");
        

    }

    //FLOP, TURN, RIVER

    public void boardFlop(Deck deck) {

        board.add(deck.deckDeal());
        board.add(deck.deckDeal());
        board.add(deck.deckDeal());
        //deals 3 cards
        boardDisplay("FLOP");

    }

    public void boardTurn(Deck deck) {

        board.add(deck.deckDeal());
        //deals 1 card
        boardDisplay("TURN");

    }

    public void boardRiver(Deck deck) {

        board.add(deck.deckDeal());
        //deals 1 card
        boardDisplay("RIVER");

    }

    //RESET

    public void boardReset() {

        while(!board.isEmpty()){
            board.remove(0);
        }

    }

    //SIZE
    
    public int size() {
        return board.size();
    }

    //GET CARD
    
    public Card get(int index) {
        return board.get(index);
    }

    //DISPLAY

    public void print(String str){

        System.out.print(str);
        Game.pause(pauseTime);

    }

    public void println(String str){

        System.out.println(str);
        Game.pause(pauseTime);

    }

}
