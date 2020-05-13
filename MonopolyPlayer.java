public class MonopolyPlayer{
    public static void main(String[] args){
        MonopolyGame game = new MonopolyGame(false);
        for(int i = 0; i < 1000000; i++){
         game.playGame();
        }
        game.calculateProbabilities();
    }
}