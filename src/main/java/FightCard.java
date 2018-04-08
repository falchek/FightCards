import java.util.ArrayList;
import java.util.List;

public class FightCard {

    private Fighter owner;
    private List<Fighter> opponents;

    public FightCard(Fighter owner){
        this.owner = owner;
    }

    public Fighter getOwner() {
        return owner;
    }

    public void setOpponents (List<Fight> fights) {
        this.opponents = new ArrayList<Fighter>();
        for(Fight fight : fights) {
            if(fight.hasFighter(owner)){
                Fighter opponent = fight.getOpponent(owner);
                this.opponents.add(opponent);
            }

        }
    }

    public String toString() {
        String str = "";

        str += "\nFIGHT CARD\n"
                + owner.getName() + " of " + owner.getLocation();
        str += "\n-------------------------------------------------\n";
        for (Fighter fighter : opponents) {
            str += fighter.getName() + " of " + fighter.getLocation() + "\n";
        }

        str += "Number of Fights: " +opponents.size() + "\n";

        return str;
    }

}
