import java.util.*;

public class FightCard {

    private Fighter owner;
    private List<Fighter> opponents;
    private double differential;

    public FightCard(Fighter owner){
        this.owner = owner;
    }

    public Fighter getOwner() {
        return owner;
    }

    public List<Fighter> getOpponents() {
        return opponents;
    }

    public void setOpponents (List<Fight> fights) {
        this.opponents = new ArrayList<Fighter>();
        for(Fight fight : fights) {
            if(fight.hasFighter(owner)){
                Fighter opponent = fight.getOpponent(owner);
                this.opponents.add(opponent);
            }
        }
        calculateDifferential();
    }

    public boolean isAllFightsUnique(List<Fighter> input) {
        Set tempSet = new HashSet();
        for (Fighter fighter : input) {
            if (!tempSet.add(fighter)) {
                return false;
            }
        }
        return true;
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
        if(isAllFightsUnique(opponents)){
            str += "All opponents are unique";
        }
        else {
            str += "Not Unique";
        }
        str += "Park differential: " + getDifferential();

        return str;
    }

    public void calculateDifferential() {
        int opponentsFromSamePark = 0;
        for (Fighter fighter : opponents) {
            if(fighter.getLocation().equals(owner.getLocation())){
                opponentsFromSamePark++;
            }
        }

        differential = (double)opponentsFromSamePark / (double)opponents.size();
    }

    public double getDifferential() {
        return differential;
    }


}
