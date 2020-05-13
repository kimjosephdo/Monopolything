import java.util.*;

public class MonopolyGame{
    
    static int[] utilities = {12, 28};
    static int[] railroads = {5, 15, 25, 35};

    private int turns;
    private int doubles;
    boolean comments;

    private int[] communityChestDeck;
    private int[] chanceDeck;
    
    //this tracks the player's current position. Go is 0.
    private int position;
    
    /*this is the variable that tracks how many times each square has been landed on,
     * with the index corresponding to parts of the monopoly board
     */
    private int[] squares;
    
    public MonopolyGame(boolean comm){
        this.comments = comm;
        this.turns = 40;
        this.doubles = 0;
        squares = new int[40];
        communityChestDeck = new int[16];
        chanceDeck = new int[16];
    }
    
    public void initGame(){
        doubles = 0;
        position = 0;
        shuffleDeck(communityChestDeck);
        // for(int i = 0; i < communityChestDeck.length; i++){
        //     System.out.print(communityChestDeck[i] + " ");
        // }
        //System.out.println("----------------");
        shuffleDeck(chanceDeck);

    }

    public void playGame(){
        initGame();
        for(int i = 0; i < turns; i++){
            rollDice();
        }
    }

    public void shuffleDeck(int[] arr){
        ArrayList<Integer> preShuffle = new ArrayList<Integer>();
        for(int i = 0; i < 16; i++){
            preShuffle.add(i);
        }
        for(int i = 0; i < 16; i++){
            arr[i] = preShuffle.remove((int)(Math.random() * preShuffle.size()));
        }
    }
    
    public void rollDice(){
        int diceRoll1 = (int)(Math.random() * 6) + 1;
        int diceRoll2 = (int)(Math.random() * 6) + 1;
        if(comments)
            System.out.println("Dice 1: " + diceRoll1 + "\nDice 2: " + diceRoll2);
        if(diceRoll1 == diceRoll2){
            doubles++;
            if(comments)
                System.out.println("Doubles: " + doubles);
            if(doubles == 3){
                goToJail();
                doubles = 0;
            }else{
                movePlayer(diceRoll1 + diceRoll2);
                rollDice();
            }
        }else{
            movePlayer(diceRoll1 + diceRoll2);
            doubles = 0;
        }

    }

    //This is used when a dice roll is used to move a player
    public void movePlayer(int spaces){
        position = (position + spaces) % 40;
        squares[position]++;
        if(comments)
            System.out.println("You are now at position " + position);
        if(position == 30){
            squares[position]--;
            goToJail();
        }
        if(position == 2 || position == 17 || position == 33){
            if(comments)
                System.out.println("COMMUNITY CHEST :D");
            communityChest();
        }
        if(position == 7 || position == 22 || position == 36){
            if(comments)
                System.out.println("CHANCE :D");
            chance();
        }
    }

    public int pickCard(int[] deck){
        int temp = deck[0];
        for (int i = 1; i < 16; i++){
            deck[i-1] = deck[i];
        }
        deck[15] = temp;
//        for(int i = 0; i < deck.length; i++){
//            System.out.print(deck[i] + " ");
//        }
//        System.out.println("");
        return temp;
    }

    public void communityChest(){
        int randNum = pickCard(communityChestDeck);
        if(comments)
            System.out.println("Got a " + randNum);
        if(randNum == 0){
            squares[position]--;
            trackPosition(0);
        }
        if(randNum == 1){
            squares[position]--;
            goToJail();
        }
    }

    public void chance(){
        int randNum = pickCard(chanceDeck);
        if(comments)
            System.out.println("Got a " + randNum);
        switch(randNum){
            case 0:
                squares[position]--;
                trackPosition(0);
                break;
            case 1:
                squares[position]--;
                trackPosition(24);
                break;
            case 2:
                squares[position]--;
                trackPosition(11);
                break;
            case 3:
                squares[position]--;
                nearestThing(utilities);
                break;
            case 4:
                squares[position]--;
                nearestThing(railroads);
                break;
            case 5:
                squares[position]--;
                trackPosition(position - 3);
                break;
            case 6:
                squares[position]--;
                goToJail();
                break;
            case 7:
                squares[position]--;
                trackPosition(5);
                break;
            case 8:
                squares[position]--;
                trackPosition(39);
                break;

        }
    }

    private void nearestThing(int[] arr){
        if(position > arr[arr.length - 1]){
            trackPosition(arr[0]);
            return;
        }
        for(int i = 0; i < arr.length; i++){
            if(position < arr[i]){
                trackPosition(arr[i]);
                return;
            }
        }
        System.out.println("THIS SHOULD NOT HAPPEN");
    }

    public void goToJail(){
        if(comments)
            System.out.println("GO TO JAIL :(");
        trackPosition(10);
    }

    //This is used when a player goes to a space due to an effect
    public void trackPosition(int pos){
        position = pos;
        squares[pos]++;
        if(comments)
            System.out.println("You are now at position " + position);
    }
    
    public int getPosition(){
        return position;
    }

    public void printSquares(){
        for(int i = 0; i < squares.length; i++){
            System.out.println("Position " + i + " was landed on " + squares[i] + " times");
        }
    }

    public void calculateProbabilities(){
        int totalMoves = 0;
        for(int i = 0; i < squares.length; i++){
            totalMoves += squares[i];
        }
        for(int i = 0; i < squares.length; i++){
            int probability = Math.round((long)100000 * squares[i] / totalMoves);
            double roundedProbability = (double)probability / 1000;
            System.out.println("Position " + i + " was landed on " + roundedProbability + "% of the time");
        }
    }
    
}