import java.util.*;

public class Fight {

    private UUID ID;
    private HashMap<UUID, Fighter> fighters;

    public Fight(Fighter fighter1, Fighter fighter2) {
        ID = UUID.randomUUID();
        fighters = new HashMap<UUID, Fighter>();
        fighters.put(fighter1.getID(), fighter1);
        fighters.put(fighter2.getID(), fighter2);
    }

    public Fighter getFighterByID(UUID id) {
        return fighters.get(id);  //returns null?
    }

    public boolean hasFighter(Fighter fighter) {
        if(fighters.get(fighter.getID()) != null){
            return true;
        }
        return false;
    }



    //returns the opponent of the fighter
    public Fighter getOpponent(Fighter fighter) {
        if(this.hasFighter(fighter)){
            for(UUID id: fighters.keySet()){
                if(!id.equals(fighter.getID())){
                    return getFighterByID(id);
                }
            }
        }
        return null;
    }

    //this is a bullshit hack and is inexcusable
    public String toString(){
        int vs = 0;
        String outString = "";
        for(UUID key: fighters.keySet()){
            Fighter fighter = getFighterByID(key);
            outString += fighter.getName() + " from " + fighter.getLocation();
            if(vs == 0){
                outString += " vs ";
                vs++;
            }
        }
        return outString;
    }

    public ArrayList<Fighter> getFighters() {
        return new ArrayList<Fighter>(fighters.values());
    }

    public boolean fightersFromSamePark() {
        List<Fighter> fightersInMatch = getFighters();
        Fighter fighter1 = fightersInMatch.get(0);
        Fighter fighter2 = fightersInMatch.get(1);
        if(fighter1.getLocation().equals(fighter2.getLocation())){
            return true;
        }
        else{
            return false;
        }

    }

}
