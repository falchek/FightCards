import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FightSchedule {

    private List<Fight> generatedFights;
    private List<Fighter> registeredFighters;
    private List<String> locationsList;
    private HashMap<String, List<Fighter>> parkToFightersMapping;
    private HashMap<Fighter, Integer> occurrencesByFighter;
    private int targetFightsCount;

    public FightSchedule(){
        generatedFights = new ArrayList<Fight>();
        locationsList = new ArrayList<String>();
    }


    public void generateFights(List<Fighter> fighters, int fightsPerFighter) {
        targetFightsCount = (fighters.size() / 2) * fightsPerFighter;
        registeredFighters = new ArrayList<Fighter>(fighters);

        occurrencesByFighter = generateOccurrencesByFighter();
        parkToFightersMapping = generateParkToFightersMapping(registeredFighters);

        while(generatedFights.size() < targetFightsCount){
            Fight fight = generateUniqueFight();
            if (fight != null) {
                generatedFights.add(fight);
            }
        }

    }

    public String toString() {
        int fightNum = 0;
        String str = "";
        str += "Generated Fights: \n";
        for(Fight fight : generatedFights) {
            fightNum++;
            str += fightNum + ": ";
            str += fight.toString();
            str += "\n";
        }

        str += "Expected Total Fights: " + targetFightsCount + "\n";
        str += "Actual Fights: " + fightNum + "\n";
        return str;
    }

    private Fight generateUniqueFight() {
        int fewestNumberOfFights = getFewestFightsPerFighter();
        List<Fighter> fighterPool = getFightersWithFewestFights(fewestNumberOfFights);

        //get a random target
        Fighter randomFighter = fighterPool.get(new Random().nextInt(fighterPool.size()));
        fighterPool.remove(randomFighter); //remove this fighter from the pool, so he can't select himself.

        String park = randomFighter.getLocation(); //TODO:  Weight by park.

        List<Fighter> validOpponents = getValidOpponentsForFighter(randomFighter, new ArrayList<Fighter>(fighterPool));
        if(validOpponents.size() > 0) {
            Fighter randomOpponent = validOpponents.get(new Random().nextInt(validOpponents.size()));
            updateOccurrencesMapping(randomFighter);
            updateOccurrencesMapping(randomOpponent);
            return new Fight(randomFighter, randomOpponent);
        }
        return null;
    }

    private void updateOccurrencesMapping(Fighter fighter) {
        int occurrences = occurrencesByFighter.get(fighter);
        occurrences++;
        occurrencesByFighter.put(fighter, occurrences);
    }

    //removes invalid opponents to prevent repetition
    private List<Fighter> getValidOpponentsForFighter(Fighter fighter, List<Fighter> opponents){
        for(Fight fight : generatedFights) {
            if(fight.hasFighter(fighter)){
                Fighter invalidOpponent = fight.getOpponent(fighter);
                opponents.remove(invalidOpponent);
            }
        }
        for(Fighter opponent : opponents) {
            int numberOfFights = occurrencesByFighter.get(opponent);
            if (numberOfFights >= targetFightsCount) {
                opponents.remove(opponent);
            }
        }

        return new ArrayList<Fighter>(opponents);
    }

    //return a list of fighters with the fewest fights.
    private List<Fighter> getFightersWithFewestFights(int fightsCount) {
        List<Fighter> fightersWithFewestFights = new ArrayList<Fighter>();
        for(Fighter fighter : occurrencesByFighter.keySet()) {
            if (occurrencesByFighter.get(fighter) <= fightsCount){
                fightersWithFewestFights.add(fighter);
            }
        }
        return fightersWithFewestFights;
    }

    //gets the lowest possible number of fights.
    private int getFewestFightsPerFighter(){
        int lowest = targetFightsCount;
        for(Fighter fighter : occurrencesByFighter.keySet()){
            int occurrences = occurrencesByFighter.get(fighter);
             if (lowest > occurrences) {
                 lowest = occurrences;
             }
        };
        return lowest;
    }

    //contains occurrences by fighter.
    private HashMap<Fighter, Integer> generateOccurrencesByFighter() {
        HashMap<Fighter, Integer> mapping = new HashMap<Fighter, Integer>();
        for(Fighter fighter : registeredFighters) {
            mapping.put(fighter, 0);
        }
        return mapping;
    }

    //creates a mapping of fighters to the park that they're from.
    private HashMap<String, List<Fighter>>  generateParkToFightersMapping(List<Fighter> fighters) {
        HashMap<String, List<Fighter>> mapping = new HashMap<String, List<Fighter>>();
        //For all of the fighters
        for(Fighter fighter : fighters) {
            String fighterLocation = fighter.getLocation();

            List<Fighter> fightersByLocation = mapping.get(fighterLocation);

            //If we don't have fighters by this location, add it to the mapping.
            if(!mapping.containsKey(fighterLocation)) {
                List<Fighter> newList = new ArrayList<Fighter>();
                newList.add(fighter);
                mapping.put(fighterLocation, newList);

                locationsList.add(fighterLocation); //Add location to location list; TODO: refactor
            }
            //We have this mapping, add the fighter
            else {
                List<Fighter> fightersInLocation = mapping.get(fighterLocation);
                fightersInLocation.add(fighter);
                mapping.put(fighterLocation, fightersInLocation);
            }

        }

        return mapping;
    }
    
    public List<FightCard> generateFightCards() {
        List<FightCard> generatedFightCards = new ArrayList<FightCard>();
        for (Fighter fighter : registeredFighters) {
            FightCard fightCard = new FightCard(fighter);
            fightCard.setOpponents(generatedFights);
            generatedFightCards.add(fightCard);
        }

        return generatedFightCards;
    }

}
