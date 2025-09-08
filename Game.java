import java.util.*;
public class Game {

    Scanner in = new Scanner(System.in);
    private int pot;
    private int numberOfPlayers;
    private int highestBet;
    private int firstToAct;
    private int bigBlindValue = 2;
    private int userIndex = 0;
    private int pauseTime = 200;
    private int actionOn;
    private int activePlayers;
    private int round = 0;
    //creates global variables for game state

    ArrayList<Player> players = new ArrayList<Player>();
    Deck deck = new Deck(); 
    Board board = new Board();
    //creates players, deck and board objects

    public Game() {

        gameInitialize();      
        // do{
            gameRound();
        //     if(players.get(userIndex).playerGetStack() == 0){
        //         break;
        //     }
        // } while (gameContinue());
        gameEnd();     

    }

    //GAME INITIALIZATION
    
    public void gameInitialize(){

        println("~~~ WELCOME TO POKER ~~~");
        gameCreatePlayers();
        gameDrawSeats();
        gameCreateUser();
        //sets up the game
        
    }

    public void gameCreatePlayers(){

        numberOfPlayers = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                print("Enter the number of players (2-9): ");
                numberOfPlayers = in.nextInt();
                
                if (numberOfPlayers >= 2 && numberOfPlayers <= 9) {
                    validInput = true;
                    //if input is within valid range, exit the validation loop
                } else {
                    println("Invalid input. Enter a number between " + 2 + " and " + 9 + ".");
                    //if input is outside valid range, display error message and continue loop
                }
            } catch (InputMismatchException e) {
                print("Invalid input. ");
                in.nextLine();
                //if input is not an integer, catch exception and clear scanner buffer
            }
        }

        in.nextLine();
        //clear any remaining newline characters from scanner buffer

        for(int i = 0; i < numberOfPlayers; i++){
            String playerName = "Computer " + (i + 1);
            players.add(new Player(playerName));
            //create computer players with default names and add them to the players arraylist
        }
        //creates computer players

    }

    public void gameCreateUser(){

        String username = "";

        print("Enter your username: ");
        username = in.nextLine();

        players.get(userIndex).playerSetUserStatus(true);
        players.get(userIndex).playerSetPlayerName(username);
        //sets up the human player

    }

    public void gameDrawSeats(){

        int drawnSeat = (int)(Math.random() * numberOfPlayers);
        //generate random starting position for seat assignment
        for(int i = 0; i < numberOfPlayers; i++){
            players.get(i).playerSetPosition(drawnSeat);
            //assign current position to player i
            if(drawnSeat == (numberOfPlayers - 1)){
                drawnSeat = 0;
                //if at last position, wrap around to position 0
            } else {
                drawnSeat++;
                //otherwise increment to next position
            }
        }
        //randomly assigns player positions

    }

    //ROUND INITIALIZATION

    public void gameRoundInitialize() {

        gameRoundIncrement();
        System.out.println();
        gameResetHands();
        board.boardReset();
        deck.deckReset();
        deck.deckShuffle();
        gameDealHands();
        gameFindFirstToAct();
        gameResetFoldStatus();
        gameResetHandRank();
        gameResetWonRoundStatus();   
        gameResetAllInStatus();
        gameDisplayPlayerInformation(userIndex); 
        //resets everything for new round

    }

    public void gameResetHands(){

        for(int i = 0; i < numberOfPlayers; i++){
            players.get(i).playerResetHand();
        }
        //clears all player hands

    }

    public void gameDealHands(){

        for(int i = 0; i < numberOfPlayers; i++){
            players.get(i).playerDealHand(deck);
        }
        //deals 2 cards to each player

    }

    public void gameFindFirstToAct(){

        for(int i = 0; i < numberOfPlayers; i++){
            if(players.get(i).playerGetPosition() == 0){
                firstToAct = i;
                //when player at position 0 is found, set them as first to act and store their index
            }
        }
        //finds player in position 0

    }

    public void gameResetFoldStatus(){

        for(int i = 0; i < numberOfPlayers; i++){
            if(players.get(i).playerGetStack() != 0){
                players.get(i).playerSetFoldStatus(false);
                //only unfold players who still have chips remaining
            }
        }
        //unfolds all players with chips

    }

    public void gameResetHandRank(){

        for(int i = 0; i < numberOfPlayers; i++){
            players.get(i).playerGetHand().handRankAndKickersReset();
        }
        //clears hand rankings

    }

    public void gameResetWonRoundStatus(){

        for(int i = 0; i < numberOfPlayers; i++){
            players.get(i).playerSetWonRoundStatus(false);
        }
        //resets round winners

    }

    public void gameResetAllInStatus(){

        for(int i = 0; i < numberOfPlayers; i++){
            players.get(i).playerSetAllInStatus(false);
        }
        //resets all-in status

    }

    public void gameCountActivePlayers(){

        activePlayers = 0;
        for(int i = 0; i < numberOfPlayers; i++){
            if(!players.get(i).playerGetFoldStatus()){
                activePlayers++;
                //increment count if player has not folded
            }
        }
        //counts players still in hand

    }

    public int gameCountActiveAndNotAllInPlayers(){

        int activeAndNotAllInPlayers = 0;
        for(int i = 0; i < numberOfPlayers; i++){
            if(!players.get(i).playerGetFoldStatus() && !players.get(i).playerGetAllInStatus()){
                activeAndNotAllInPlayers++;
                //increment count if player has not folded and is not all-in
            }
        }

        return activeAndNotAllInPlayers;
        //counts players who can still bet

    }
    
    //ROUND

    public void gameRound(){

        gameRoundInitialize();

        gamePreflop();
        //handle preflop betting round with blinds

        gameCountActivePlayers();
        if(activePlayers != 1){
            board.boardFlop(deck);
            //only deal flop if more than one player remains
        }
        if(activePlayers != 1 && gameCountActiveAndNotAllInPlayers() >= 2){
            gameFlopTurnRiver();
            //only have betting round if multiple active players who can still bet remain
        }

        gameCountActivePlayers();
        if(activePlayers != 1){
            board.boardTurn(deck);
            //only deal turn if more than one player remains
        }
        if(activePlayers != 1 && gameCountActiveAndNotAllInPlayers() >= 2){
            gameFlopTurnRiver();
            //only have betting round if multiple active players who can still bet remain
        }

        gameCountActivePlayers();
        if(activePlayers != 1){
            board.boardRiver(deck);
            //only deal river if more than one player remains
        }
        if(activePlayers != 1 && gameCountActiveAndNotAllInPlayers() >= 2){
            gameFlopTurnRiver();
            //only have betting round if multiple active players who can still bet remain
        }

        gameRoundEnd();
        //runs a complete poker round

    }

    public void gamePreflop(){

        gameDisplayAllPlayersStack();
        gameCountActivePlayers();
        boolean allActed = false;
        int acted = 0;
        //initialize variables to track if all players have had opportunity to act

        println("~~~ BLINDS ~~~");

        actionOn = gameFindPosition(firstToAct, 2);
        //set action to start with player 2 positions after first to act (big blind position)
        gamePreflopSmallBlind();
        gamePreflopBigBlind();
        //post small and big blinds

        println("~~~ ACTION ~~~");

        do {
            if(!players.get(actionOn).playerGetFoldStatus()){
                acted++;
                highestBet = players.get(actionOn).playerAction(highestBet);
                //if player hasn't folded, let them act and update highest bet if needed
            }

            if(acted >= activePlayers){
                allActed = true;
                //if number of players who have acted equals or exceeds active players, all have acted
            }

            if(actionOn == (numberOfPlayers - 1)){
                actionOn = 0;
                //if at last player position, wrap around to first player
            } else {
                actionOn++;
                //otherwise move to next player position
            }

        } while(!gameMatchOrFolded() || !allActed);
        //continue until all bets match or players fold, and all have had chance to act

        gameBetToPot();
        //handles preflop betting round

    }

    public void gameFlopTurnRiver(){

        gameDisplayAllPlayersStack();
        boolean allActed = false;
        int acted = 0;
        //initialize variables to track if all players have had opportunity to act

        println("~~~ ACTION ~~~");

        actionOn = firstToAct;
        //start action with first to act player (position 0)
        do {
            if(!players.get(actionOn).playerGetFoldStatus()){
                acted++;
                highestBet = players.get(actionOn).playerAction(highestBet);
                //if player hasn't folded, let them act and update highest bet if needed
            }

            if(acted >= activePlayers){
                allActed = true;
                //if number of players who have acted equals or exceeds active players, all have acted
            }

            if(actionOn == (numberOfPlayers - 1)){
                actionOn = 0;
                //if at last player position, wrap around to first player
            } else {
                actionOn++;
                //otherwise move to next player position
            }
        } while (!gameMatchOrFolded() || !allActed);
        //continue until all bets match or players fold, and all have had chance to act

        gameBetToPot();
        //handles post-flop betting rounds
        
    }

    //ROUND END

    public void gameRoundEnd(){

        gameShowdown();
        gameDistributePot();
        gameMoveAllPlayersPosition();
        //ends the round

    }

    public void gameRoundIncrement(){
        round++;
    }

    public void gameShowdown(){

        gameCountActivePlayers();

        if(activePlayers == 1){
            println("~~~ ROUND END ~~~");
            
            for(int i = 0; i < numberOfPlayers; i++){
                if(!players.get(i).playerGetFoldStatus()){
                    int playerRemaining = i;
                    players.get(playerRemaining).playerSetWonRoundStatus(true);
                    println(gameGetPlayerInfoFormatted(playerRemaining) + "Won round: All other players folded");
                    //if at the end there is only one player remaining, set that players won round status to true and display they have won due to all other players folding
                }
            }
        } else {

            println("~~~ SHOWDOWN ~~~");
            gameDisplayAllPlayersHand();

            for(int i = 0; i < numberOfPlayers; i++){
            if(!players.get(i).playerGetFoldStatus()){
                players.get(i).playerGetHand().handGetHandRank(board);
            }
            //if at the end there is not only one remaining player, get all the active players hand rankings
        }   

        int indexBeingCompared = 0;
        int highest = 0;

        ArrayList<Boolean> strongestHand = new ArrayList<Boolean>();

        for(int i = 0; i < numberOfPlayers; i++){
            strongestHand.add(true);
            //creates an array that initializes by considering that all players hands could potentially be the strongest hand by adding true
        }

        while(true){
            highest = 0;
            for(int i = 0; i < numberOfPlayers; i++){
                if(!players.get(i).playerGetFoldStatus() && strongestHand.get(i)){
                    if(players.get(i).playerGetHand().handGetHandRankIndex(indexBeingCompared) > highest){
                        highest = players.get(i).playerGetHand().handGetHandRankIndex(indexBeingCompared);
                    }
                }
                //if a player is not folded and still in consideration for the strongest hand then decide the highest number of the arraylist at that index and store it as highest
            }

            for(int i = 0; i < numberOfPlayers; i++){
                if(!players.get(i).playerGetFoldStatus() && strongestHand.get(i)){
                    if(players.get(i).playerGetHand().handGetHandRankIndex(indexBeingCompared) != highest){
                        strongestHand.set(i, false);
                    }
                }
                //if a player is not folded and still in consideration for the strongest hand then compare their current index to the highest, if it not the highest then remove them from consideration for the strongest hand
            }

            indexBeingCompared++;
            //shift index being compared

            if(indexBeingCompared > 5){
                break;
            }
            //if the index being compared is greater than 5, break, the arraylist is always of size 6 so index 5 is always last to be compared
        }

        for(int i = 0; i < numberOfPlayers; i++){
            if(!players.get(i).playerGetFoldStatus() && strongestHand.get(i)){
                players.get(i).playerSetWonRoundStatus(true);
                println(gameGetPlayerInfoFormatted(i) + "Won round - " + players.get(i).playerGetHand().handDisplayDetails(players.get(i).playerGetNameString(), players.get(i).playerGetHand().handGetHandRank(board)));
            }
            //displays winners (could be multiple) of the round which should have strongest hand status as true and not be folded
            //sets their wonround status to true
        }

        }
        //determines winner by comparing hands        
    }
    
    public void gameDistributePot(){

        int numberOfWinners = 0;
        for(int i = 0; i < numberOfPlayers; i++){
            if(players.get(i).playerGetWonRoundStatus()){
                numberOfWinners++;
                //count how many players won the round to determine pot split
            }
        }
        for(int i = 0; i < numberOfPlayers; i++){
            if(players.get(i).playerGetWonRoundStatus()){
                players.get(i).playerSetStack(players.get(i).playerGetStack() + (pot/numberOfWinners));
                println(gameGetPlayerInfoFormatted(i) + "Won pot of " + pot + " split among " + numberOfWinners + " player(s) - Amount Won: " + (pot/numberOfWinners) + " - Updated Stack: " + players.get(i).playerGetStack());
                //add equal share of pot to each winner's stack and display winnings information
            }
        }
        pot = 0;
        //reset pot to zero for next round
        //splits pot among winners

    }

    public void gameMoveAllPlayersPosition(){

        for(int i = 0; i < numberOfPlayers; i++){
            players.get(i).playerPositionMove(numberOfPlayers);
            //move each player's position forward by one for next round (rotating dealer button)
        }
        //rotates player positions

    }

    public boolean gameContinue(){
        
        print("Play again (yes/no): ");
        String input = in.nextLine();

        while(!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")){
                print("Invalid input. Try again: ");
                input = in.nextLine();
                //continue asking for input until user enters valid response
            }

        if(input.equalsIgnoreCase("yes")){
            return true;
            //return true if player wants to continue
        } else {
            return false;
            //return false if player wants to quit
        }
        //asks if player wants another round

    }

    public void gameEnd(){

        println("\n~~~ GAME END | THANKS FOR PLAYING ~~~\n");
        println("Stack: " + players.get(userIndex).playerGetStack());
        println("Profit: " + (players.get(userIndex).playerGetStack() - 100));
        //displays final game stats

    }

    //BETTING

    public void gamePreflopBigBlind(){

        int bigBlindPosition = gameFindPosition(firstToAct, 1);
        //find position that is 1 seat after first to act for big blind
        players.get(bigBlindPosition).playerSetBet(bigBlindValue);
        println(gameGetPlayerInfoFormatted(bigBlindPosition) + "Big blind bet of " + players.get(bigBlindPosition).playerGetBet());
        highestBet = bigBlindValue;
        //set highest bet to big blind value since this is first bet of round
        players.get(bigBlindPosition).playerSetBet(bigBlindValue);
        players.get(bigBlindPosition).playerSetStack(players.get(bigBlindPosition).playerGetStack() - (bigBlindValue));
        //deduct big blind amount from player's stack
        //posts big blind bet

    }

    public void gamePreflopSmallBlind(){

        players.get(firstToAct).playerSetBet(bigBlindValue/2);
        //set small blind bet to half of big blind value
        println(gameGetPlayerInfoFormatted(firstToAct) + "Small blind bet of " + players.get(firstToAct).playerGetBet());
        players.get(firstToAct).playerSetBet(bigBlindValue/2);
        players.get(firstToAct).playerSetStack(players.get(firstToAct).playerGetStack() - (bigBlindValue/2));
        //deduct small blind amount from player's stack
        //posts small blind bet
        
    }

    public void gameBetToPot(){

        for(int i = 0; i < numberOfPlayers; i++){
            pot = pot + players.get(i).playerGetBet();
            players.get(i).playerSetBet(0);
            //add each player's current bet to pot then reset their bet to zero
        }
        highestBet = 0;
        //reset highest bet for next betting round
        gameDisplayPot();
        //adds all bets to pot

    }

    public boolean gameMatchOrFolded(){

        boolean allMatchOrFolded = false;
        int numMatchOrFolded = 0;
        for(int i = 0; i < numberOfPlayers; i++){
            if(players.get(i).playerGetFoldStatus() || players.get(i).playerGetBet() == highestBet){
                numMatchOrFolded++;
                //increment counter if player has folded or their bet matches the highest bet
            }
        }
        if(numMatchOrFolded == numberOfPlayers){
            allMatchOrFolded = true;
            //if all players have either folded or matched highest bet, betting round is complete
        }

        return allMatchOrFolded;
        //checks if betting round is complete

    }

    public int gameFindPosition(int position, int moves){

        int newPosition = position + moves;
        if(newPosition > (numberOfPlayers - 1)){
            newPosition = newPosition - numberOfPlayers;
            //if new position exceeds maximum position, wrap around by subtracting total number of players
        }
        return newPosition;
        //calculates position after moving seats

    }

    //DISPLAY

    public void gameDisplayAllPlayersInformaion(){

        for(int i = 0; i < numberOfPlayers; i++){
            gameDisplayPlayerInformation(i);
        }

    }

    public void gameDisplayPlayerInformation(int i){
        println("~~~ PLAYER INFORMATION ~~~");
        gameDisplayPlayerName(i);
        gameDisplayPlayerHand(i);
        gameDisplayPlayerStack(i);
        gameDisplayPlayerPosition(i);
    }

    public void gameDisplayPlayerHand(int i){
        println("Hand: " + gameGetHandString(i));
    }

    public void gameDisplayPlayerStack(int i){
        println("Stack: " + gameGetStack(i));
    }

    public void gameDisplayPlayerPosition(int i){
        println("Position: " + gameGetPosition(i));
    }

    public void gameDisplayPlayerUserStatus(int i){
        println("User Status: " + gameGetUserStatus(i));
    }

    public void gameDisplayPlayerFoldStatus(int i){
        println("Fold Status: " + gameGetFoldStatus(i));
    }

    public void gameDisplayPlayerName(int i){
        println("Player Name: " + gameGetPlayerName(i));
    }

    public void gameDisplayAllPlayers(){

        println("~~~ PLAYERS ~~~");
        for(int i = 0; i < numberOfPlayers; i++){
            println(gameGetNameString(i));
        }

    }

    public void gameDisplayAllPlayersHand() {

        displayHandRecursive(0); // start from the first player

    }

    private void displayHandRecursive(int index) {
        if (index >= numberOfPlayers) {
            return; // base case: stop when we've checked all players
        }

        if (!players.get(index).playerGetFoldStatus()) {
            println(gameGetNameString(index) + " Hand: " + gameGetHandString(index));
        }

        displayHandRecursive(index + 1); // recursive step: move to next player
    }


    public void gameDisplayAllPlayersStack(){

        println("~~~ PLAYER STACKS ~~~");
        for(int i = 0; i < numberOfPlayers; i++){
            if(!players.get(i).playerGetFoldStatus()){
                println(gameGetNameString(i) + " Stack: " + gameGetStack(i));
                //only display stacks of players who haven't folded
            }
        }
        //shows chip stacks of active players

    }

    public void gameDisplayPlayersPosition(){

        println("~~~ PLAYER POSITIONS ~~~");
        for(int i = 0; i < numberOfPlayers; i++){
            println(gameGetNameString(i) + ": " + gameGetPosition(i));
        }

    }

    public void gameDisplayPlayersBet(){

        println("~~~ PLAYER BETS ~~~");
        for(int i = 0; i < numberOfPlayers; i++){
            println(gameGetNameString(i) + ": " + gameGetBet(i));
        }

    }

    public void gameDisplayPot(){

        println("~~~ POT ~~~");
        println("Pot: " + pot);

    }

    public void print(String str){

        System.out.print(str);
        pause(pauseTime);

    }

    public void println(String str){

        System.out.println(str);
        pause(pauseTime);

    }

    public static void pause(int milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

    }
}

    //GETTERS

    public String gameGetPlayerInfoFormatted(int i){
        
        String text = "Position " + players.get(i).playerGetPosition() + ", " + players.get(i).playerGetNameString() + ": ";
        return text;

    }

    public ArrayList<Integer> gameGetHandRank(int index){
        return players.get(index).playerGetHand().handGetHandRank(board);
    }

    public String gameGetNameString(int index){
        return players.get(index).playerGetNameString();
    }

    public int gameGetStack(int index){
        return players.get(index).playerGetStack();
    }
    
    public int gameGetPosition(int index){
        return players.get(index).playerGetPosition();
    }

    public Hand gameGetHand(int index){
        return players.get(index).playerGetHand();
    }

    public String gameGetHandString(int index){
        return players.get(index).playerGetHandString();
    }

    public int gameGetBet(int index){
        return players.get(index).playerGetBet();
    }

    public boolean gameGetFoldStatus(int index){
        return players.get(index).playerGetFoldStatus();
    }

    public boolean gameGetUserStatus(int index){
        return players.get(index).playerGetUserStatus();
    }

    public String gameGetPlayerName(int index){
        return players.get(index).playerGetNameString();
    }

}