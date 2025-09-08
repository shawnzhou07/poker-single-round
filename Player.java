import java.util.*;

public class Player {
    
    Scanner in = new Scanner(System.in);

    private int stack;
    private int bet;
    private boolean foldStatus;
    private int position;
    private boolean userStatus;
    private String playerName;
    private int raise;
    private boolean wonRoundStatus;
    private boolean allInStatus;
    
    private int pauseTime = 200;

    Hand hand = new Hand(); 

    public Player(String name){

        stack = 100;
        playerName = name;
        userStatus = false;
        wonRoundStatus = false;
        foldStatus = false;
        allInStatus = false;

    }

    //GETTERS

    public String playerGetNameString(){
        return playerName; 
    }

    public boolean playerGetAllInStatus(){
        return allInStatus;
    }

    public int playerGetStack(){
        return stack; 
    }

    public int playerGetPosition(){
        return position; 
    }

    public Hand playerGetHand(){
        return hand; 
    }

    public int playerGetBet(){
        return bet; 
    }

    public boolean playerGetWonRoundStatus(){
        return wonRoundStatus; 
    }

    public boolean playerGetFoldStatus(){
        return foldStatus; 
    }

    public boolean playerGetUserStatus(){
        return userStatus; 
    }

    public String playerGetHandString(){
        return hand.handDisplay();
    }

    public String playerGetPlayerInfoFormatted(){
        
        String text = "Position " + position + ", " + playerName + ": ";
        return text;

    }

    //SETTERS

    public void playerSetPosition(int p){
        position = p;
    }

    public void playerSetAllInStatus(boolean a){
        allInStatus = a;
    }

    public void playerSetWonRoundStatus(boolean w){
        wonRoundStatus = w;
    }

    public void playerSetBet(int b){
        bet = b;
    }

    public void playerSetStack(int s){
        stack = s;
    }

    public void playerSetFoldStatus(boolean f){
        foldStatus = f;
    }

    public void playerSetPlayerName(String p){
        playerName = p;
    }

    public void playerSetUserStatus(boolean u){
        userStatus = u;
    }

    //POSITION

    public void playerPositionMove(int numberOfPlayers){

        if(position == (numberOfPlayers - 1)){
            position = 0;
        } else {
            position++;
        }

    }

    //CARDS

    public void playerDealHand(Deck deck){

        hand.handDeal(deck);

    }

    public void playerResetHand(){

        hand.handReset();

    }

    //PLAYER ACTION

    public int playerAction(int highestBet){
        raise = 0;

        if(userStatus){
            highestBet = playerUserAction(highestBet);
        } else{
            if(stack != 0){
                highestBet = playerComputerAction(highestBet);
            } 
        }
        
        if(stack == 0){
            allInStatus = true;
        }

        return highestBet;

    }

    //USER ACTION

    public int playerUserAction(int highestBet){
        String playerAction = "";
        if(highestBet == 0 || highestBet == bet){
            print(playerGetPlayerInfoFormatted() + "Action (raise/check): ");
            playerAction = in.nextLine();

            while(!playerAction.equalsIgnoreCase("raise") && !playerAction.equalsIgnoreCase("check")){
                print("Invalid action. Try again: ");
                playerAction = in.nextLine();
            }
        } else {
            print(playerGetPlayerInfoFormatted() + "Action (raise/call/fold): ");
            playerAction = in.nextLine();

            while(!playerAction.equalsIgnoreCase("raise") && !playerAction.equalsIgnoreCase("call") && !playerAction.equalsIgnoreCase("fold")){
                print("Invalid action. Try again: ");
                playerAction = in.nextLine();
            }
        }
        switch (playerAction) {
            case "raise", "Raise": highestBet = playerUserActionRaise(highestBet); break;
            case "check", "Check": playerUserActionCheck(); break;
            case "fold", "Fold": playerUserActionFold(); break;
            case "call", "Call": playerUserActionCall(highestBet); break;
        }

        return highestBet;

    }

    public int playerUserActionRaise(int highestBet){

        boolean validInput = false;

        while (!validInput) {
            try {
                print("Raise amount: ");
                raise = in.nextInt();
                in.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                println("Invalid input. Enter a valid integer.");
                in.nextLine();
            }
        }
        
        while(!playerCheckValidRaise(highestBet, raise)){
            validInput = false;
            while (!validInput) {
                try {
                    print("Invalid raise amount. Raise amount: ");
                    raise = in.nextInt();
                    in.nextLine();
                    validInput = true;
                } catch (InputMismatchException e) {
                    println("Invalid input. Enter a valid integer.");
                    in.nextLine(); 
                }
            }
        }

        println(playerGetPlayerInfoFormatted() + "Raise " + raise);
        
        highestBet = raise;   
        stack = stack - raise + bet;
        bet = raise;

        return highestBet;

    }

    public void playerUserActionCall(int highestBet){

        println(playerGetPlayerInfoFormatted() + "Call " + highestBet);
        stack = stack - highestBet + bet;
        bet = highestBet;
        

    }

    public void playerUserActionCheck(){

        println(playerGetPlayerInfoFormatted() + "Check");

    }

    public void playerUserActionFold(){

        println(playerGetPlayerInfoFormatted() + "Fold");
        foldStatus = true;

    }
    
    public boolean playerCheckValidRaise(int highestBet, int raise){

        if(raise > (bet + stack)){
            return false;
        }
        if(raise <= highestBet){
            return false;
        }

        if (raise < (highestBet * 2) ) {
            return raise == (stack + bet);
        } else {
            return true;
        }

    }

    //COMPUTER ACTION

public int playerComputerAction(int highestBet){
    
        Random rand = new Random();
        
        if(highestBet == 0){
            // No bet to match - can only check or raise
            if(rand.nextInt(5) < 2){ // 40% chance to raise
                int raise = generateRandomRaise(highestBet);
                println(playerGetPlayerInfoFormatted() + "Raise " + raise);
                highestBet = raise;   
                stack = stack - raise + bet;
                bet = raise;   
            } else {
                // 60% chance to check
                println(playerGetPlayerInfoFormatted() + "Check");
            }
        } else if(highestBet == bet) {
            // Big blind option: already matched the bet, can raise or check
            if(rand.nextInt(5) < 1){ // 20% chance to raise
                int raise = generateRandomRaise(highestBet);
                if(playerCheckValidRaise(highestBet, raise)){
                    println(playerGetPlayerInfoFormatted() + "Raise " + raise);
                    highestBet = raise;
                    stack = stack - raise + bet;
                    bet = raise;
                } else {
                    // Can't raise, just check
                    println(playerGetPlayerInfoFormatted() + "Check");
                    bet = highestBet;
                }
            } else {
                // 80% chance to check
                println(playerGetPlayerInfoFormatted() + "Check");
            }
        } else {
            // There's a bet to match - try actions until one is valid
            boolean actionTaken = false;
            
            while(!actionTaken){
                int action = rand.nextInt(5); // 0-4
                
                if(action < 2){ // 40% chance to call
                    if(highestBet <= (stack + bet)){
                        // Normal call - can cover the full bet
                        println(playerGetPlayerInfoFormatted() + "Call " + highestBet);
                        stack = stack - highestBet + bet;
                        bet = highestBet;
                        actionTaken = true;
                    } else if(stack + bet > 0){
                        // All-in call - can't cover full bet but has chips left
                        int allInAmount = stack + bet;
                        println(playerGetPlayerInfoFormatted() + "Call " + allInAmount + " (All-in)");
                        bet = allInAmount;
                        stack = 0;
                        actionTaken = true;
                    }
                    // If stack + bet <= 0, this action is invalid, try another
                } else if(action < 3){ // 20% chance to raise
                    int raise = generateRandomRaise(highestBet);
                    if(playerCheckValidRaise(highestBet, raise)){
                        println(playerGetPlayerInfoFormatted() + "Raise " + raise);
                        highestBet = raise;
                        stack = stack - raise + bet;
                        bet = raise;
                        actionTaken = true;
                    }
                } else { // 40% chance to fold
                    println(playerGetPlayerInfoFormatted() + "Fold");
                    foldStatus = true;
                    actionTaken = true;
                }
            }
        }
        
        return highestBet;
    }

    // Helper method to generate unpredictable raise sizes
    private int generateRandomRaise(int highestBet) {
        Random rand = new Random();
        int baseRaise;
        
        // If there's no current bet, start with a base amount
        if(highestBet == 0) {
            baseRaise = 2; // Minimum raise when no bet exists
        } else {
            baseRaise = highestBet; // Current bet to call
        }
        
        // Generate different raise patterns with varying probabilities
        int raisePattern = rand.nextInt(10); // 0-9 for different raise sizes
        
        if(raisePattern < 3) { // 30% - Small raise (1.5x to 2.5x)
            double multiplier = 1.5 + (rand.nextDouble() * 1.0); // 1.5 to 2.5
            return (int)(baseRaise * multiplier);
            
        } else if(raisePattern < 6) { // 30% - Medium raise (2.5x to 4x)
            double multiplier = 2.5 + (rand.nextDouble() * 1.5); // 2.5 to 4.0
            return (int)(baseRaise * multiplier);
            
        } else if(raisePattern < 8) { // 20% - Large raise (4x to 6x)
            double multiplier = 4.0 + (rand.nextDouble() * 2.0); // 4.0 to 6.0
            return (int)(baseRaise * multiplier);
            
        } else if(raisePattern < 9) { // 10% - Massive raise (6x to 10x)
            double multiplier = 6.0 + (rand.nextDouble() * 4.0); // 6.0 to 10.0
            return (int)(baseRaise * multiplier);
            
        } else { // 10% - All-in or near all-in (50% to 100% of stack)
            int availableChips = stack + bet;
            double stackPercentage = 0.5 + (rand.nextDouble() * 0.5); // 50% to 100%
            int stackBasedRaise = (int)(availableChips * stackPercentage);
            
            // Make sure it's at least a valid raise
            int minRaise = Math.max(baseRaise * 2, baseRaise + 2);
            return Math.max(stackBasedRaise, minRaise);
        }
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
