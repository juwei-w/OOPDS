import java.util.List;
import java.util.ArrayList;

public class Player {
    protected List<Card> playerCards;
    protected boolean turn;
    protected int score;
    protected boolean playableCard;

    public Player() {
        playerCards = new ArrayList<>();
        turn = false;
        score = 0;
        playableCard = true;
    }

    // Return player cards
    public List<Card> getPlayerCards() {
        return playerCards;
    }    
}
