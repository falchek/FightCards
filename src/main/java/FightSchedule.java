import java.util.*;

public class FightSchedule {

    private List<Fight> generatedFights;
    private List<Fighter> registeredFighters;
    private List<String> locationsList;
    private HashMap<String, List<Fighter>> parkToFightersMapping;
    private HashMap<Fighter, Integer> occurrencesByFighter;
    private int targetFightsCount;
    private int targetFightsPerFighter;

    public FightSchedule(){
        generatedFights = new ArrayList<Fight>();
        locationsList = new ArrayList<String>();
    }

    //main method - generates all of the cards.
    public List<FightCard> generateFightCards(List<Fighter> fighters, int fightsPerFighter) {
        targetFightsPerFighter = fightsPerFighter;
        targetFightsCount = fighters.size() * fightsPerFighter / 2;
        registeredFighters = new ArrayList<Fighter>(fighters);
        occurrencesByFighter = generateOccurrencesByFighter();
        parkToFightersMapping = generateParkToFightersMapping(registeredFighters);

        generateFights();

        //boolean fightsCanBeRedistributedByPark = true;
        while(redistributeFightsByPark());

        List<FightCard> fightCards = createFightCardsFromGeneratedFights();
        return fightCards;
    }

    //after having a completed fights list, redistribute them to lower the park differentials as much as possible.
    public boolean redistributeFightsByPark(){
        //Identify breakable candidates;
        List<Fight> fightsFromSamePark = new ArrayList<Fight>();
        for(Fight fight : generatedFights){
            if(fight.fightersFromSamePark()){
                fightsFromSamePark.add(fight);
            }
        }

        //For each candidate, try to find a breakable candidate that helps both of them out.
        for(Fight fightFromSamePark : fightsFromSamePark) {
            //get fighters who need a swap.
            Fighter needsSwapA = fightFromSamePark.getFighters().get(0);
            Fighter needsSwapB = fightFromSamePark.getOpponent(needsSwapA);
            List<Fight> fightsToAdd = new ArrayList<Fight>();
            List<Fight> fightsToRemove = new ArrayList<Fight>();


            for (Fight fight : generatedFights) {
                Fight newFight1 = null;
                Fight newFight2 = null;
                //candidateFightForSwap = null;
                Fighter candidateSwapC = fight.getFighters().get(0);
                Fighter candidateSwapD = fight.getOpponent(candidateSwapC);

                //these items are swappable if the fighters don't have have a fighter already, and if they're not from the same park.
                boolean swappableAandC = !fighterPairHasFight(needsSwapA, candidateSwapC)
                        && !needsSwapA.getLocation().equals(candidateSwapC.getLocation());
                boolean swappableBandD = !fighterPairHasFight(needsSwapB, candidateSwapD)
                        && !needsSwapB.getLocation().equals(candidateSwapD.getLocation());
                boolean swappableAandD = !fighterPairHasFight(needsSwapA, candidateSwapD)
                        && !needsSwapA.getLocation().equals(candidateSwapD.getLocation());
                boolean swappableBandC = !fighterPairHasFight(needsSwapB, candidateSwapC)
                        && !needsSwapB.getLocation().equals(candidateSwapC.getLocation());
                //Swap 1 and 1 with 2 and 2 respectively

                if (swappableAandC && swappableBandD) {
                    newFight1 = new Fight(needsSwapA, candidateSwapC);
                    newFight2 = new Fight(needsSwapB, candidateSwapD);
                }
                //Swap 1 with 2 and 2 with 1.
                else if (swappableAandD && swappableBandC) {
                    newFight1 = new Fight(needsSwapA, candidateSwapD);
                    newFight2 = new Fight(needsSwapB, candidateSwapC);
                }
                //prepare for removal.
                if(newFight1 != null && newFight2 != null) {
                    //candidateFightForSwap = fight;
                    System.out.println("Swapping for " + fightFromSamePark.toString() + " and " + fight.toString());
                }

                //perfrorm the swap.
                if(newFight1 != null && newFight2 != null){
                    System.out.println("Removing " + fightFromSamePark.toString() + " and " + fight.toString());
                    generatedFights.remove(fightFromSamePark);
                    generatedFights.remove(fight);
                    System.out.println("Generated fights after removal: " + generatedFights.size());
                    generatedFights.add(newFight1);
                    generatedFights.add(newFight2);
                    System.out.println("Generated fights after adding: " + generatedFights.size());
                    return true;
                }

            }


        }
        System.out.println("We can't redistribute anymore.");
        return false;
    }


    //generates the list of registered fights.
    public void generateFights() {
        //empty out generated fights.
        while(generatedFights.size() < targetFightsCount) {
            Fight fight = generateUniqueFight();
            if (fight != null)
            {
                generatedFights.add(fight);
                updateOccurrencesMapping();
                System.out.println("Fights count" + generatedFights.size());
                //sortAttempts = 0;
            }
            else if (fight == null) {
                System.out.println("We're stuck");
                attemptToSwapFight();
            }
        }
        this.toString();
    }

    //puts the whole schedule to string.
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

    private void attemptToSwapFight() {
        //let's see what's wrong:
        int fewestNumberOfFights = getFewestFightsPerFighter();
        List<Fighter> fighterPool = getFightersWithFewestFights(fewestNumberOfFights);

        if (fighterPool.size() == 1){
            System.out.println("Odd number of fighters remaining, just give me someone new. ");

            Fighter loneFighter = fighterPool.get(0);
            List<Fighter> opponentsForLoneFighter = getListOfOpponentsForFighter(loneFighter);

            Collections.shuffle(opponentsForLoneFighter);
            Fighter candidateFighter = null;
            for(Fighter candidate: registeredFighters) {
                boolean loneFighterCanFightCandidate = opponentsForLoneFighter.indexOf(candidate) < 0;
                if(loneFighterCanFightCandidate) {
                    candidateFighter = candidate;
                }
            }

            if(candidateFighter != null) {
                Fight fight = new Fight(loneFighter, candidateFighter);
                generatedFights.add(fight);
                updateOccurrencesMapping();
            }

        }

        if (fighterPool.size() == 2) {
            System.out.println("Even number of fighters remaining, so we execute a swap for both fighters");
            //get fighters that require swap
            Fighter fighterA = fighterPool.get(0);
            Fighter fighterB = fighterPool.get(1);

            List<Fighter> opponentsForA = getListOfOpponentsForFighter(fighterA);
            List<Fighter> opponentsForB = getListOfOpponentsForFighter(fighterB);

            Collections.shuffle(opponentsForA);
            Collections.shuffle(opponentsForB);

            Fighter fighterForSwapFromA = null;
            Fighter fighterForSwapFromB = null;

            //Find an opponent of A that B has not fought.
            for(Fighter opponent : opponentsForA) {
                boolean bHasNotFoughtOpponent = !fighterPairHasFight(opponent, fighterB);
                if(bHasNotFoughtOpponent) {
                    fighterForSwapFromA = opponent;
                }
            }

            //Find an opponent of B that A has not fought.
            for(Fighter opponent : opponentsForB) {
                boolean aHasNotFoughtOpponent = !fighterPairHasFight(opponent, fighterA);
                if(aHasNotFoughtOpponent) {
                    fighterForSwapFromB = opponent;
                }
            }

            if(fighterForSwapFromA != null && fighterForSwapFromB != null) {
//                Fight fightToDelete1 = getFightFromFighterPair(fighterForSwapFromB, fighterB);
//                generatedFights.remove(fightToDelete1);
//                Fight fightToDelete2 = getFightFromFighterPair(fighterForSwapFromA, fighterA);
//                generatedFights.remove(fightToDelete2);

                Fight newFight1 = new Fight(fighterForSwapFromA, fighterB);
                generatedFights.add(newFight1);
                //if(generatedFights.size() < targetFightsCount) {
                    Fight newFight2 = new Fight(fighterForSwapFromB, fighterA);
                    generatedFights.add(newFight2);
                //}
                updateOccurrencesMapping();
                System.out.println("Successfully swapped these fighters");
            }

        }
    }

    private List<Fighter> getListOfOpponentsForFighter(Fighter fighter) {
        List<Fighter> opponents = new ArrayList<Fighter>();
        for(Fight fight : generatedFights){
            if(fight.hasFighter(fighter)){
                opponents.add(fight.getOpponent(fighter));
            }
        }
        return opponents;
    }

    //returns whether or not a pair of fighters have a fight
    private boolean fighterPairHasFight(Fighter a, Fighter b) {
        for(Fight fight : generatedFights) {
            if(fight.hasFighter(a) && fight.hasFighter(b)){
                return true;
            }
        }
        return false;
    }

    private Fight generateUniqueFight() {
        int fewestNumberOfFights = getFewestFightsPerFighter();
        List<Fighter> fighterPool = getFightersWithFewestFights(fewestNumberOfFights);

        //get a random target
        Fighter randomFighter = fighterPool.get(new Random().nextInt(fighterPool.size()));
        fighterPool.remove(randomFighter); //remove this fighter from the pool, so he can't select himself.

        String park = randomFighter.getLocation(); //TODO:  Weight by park.
        String name = randomFighter.getName();

        List<Fighter> validOpponents = getValidOpponentsForFighter(randomFighter, fighterPool);
        if(validOpponents.size() > 0) {
            Fighter randomOpponent = validOpponents.get(new Random().nextInt(validOpponents.size()));
            return new Fight(randomFighter, randomOpponent);
        }
        return null;
    }

    //updates the occurences mapping.  this is used to help generate unique fights
    private void updateOccurrencesMapping() {
        //reset mapping
        occurrencesByFighter = generateOccurrencesByFighter();
        for(Fighter fighter : occurrencesByFighter.keySet()){
            int occurrences = 0;
            for(Fight fight : generatedFights) {
                if(fight.hasFighter(fighter)){
                    occurrences++;
                }
            }
            occurrencesByFighter.put(fighter, occurrences);
        }
        //int occurrences = occurrencesByFighter.get(fighter);
        //occurrences++;
        //occurrencesByFighter.put(fighter, occurrences);
    }

    //removes invalid opponents to prevent repetition
    private List<Fighter> getValidOpponentsForFighter(Fighter fighter, List<Fighter> fighterPool){
        List<Fighter> validOpponents = new ArrayList<Fighter>(fighterPool);

        //we've had this fighter before
        for(Fight fight : generatedFights) {
            if(fight.hasFighter(fighter)){
                Fighter invalidOpponent = fight.getOpponent(fighter);
                validOpponents.remove(invalidOpponent);
                //System.out.println("Check here");
            }
        }

        //This fighter currently has too many fights.  throws concurrent modification exception
        List<Fighter> fightersAtMaxFights = new ArrayList<Fighter>();
        for(Fighter opponent : validOpponents) {
            int numberOfFights = occurrencesByFighter.get(opponent);
            if (numberOfFights >= targetFightsPerFighter) { //>= //TODO: FIX
                fightersAtMaxFights.add(opponent);
            }
        }
        validOpponents.removeAll(fightersAtMaxFights);


        return validOpponents;
    }

    //return a list of fighters with the fewest fights.
    private List<Fighter> getFightersWithFewestFights(int fightsCount) {
        List<Fighter> fightersWithFewestFights = new ArrayList<Fighter>();
        for(Fighter fighter : occurrencesByFighter.keySet()) {
            if (occurrencesByFighter.get(fighter) <= fightsCount){ // + 1){ //<= // < +1 works.
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


    public List<FightCard> createFightCardsFromGeneratedFights() {
        List<FightCard> generatedFightCards = new ArrayList<FightCard>();
        for (Fighter fighter : registeredFighters) {
            FightCard fightCard = new FightCard(fighter);
            fightCard.setOpponents(generatedFights);
            generatedFightCards.add(fightCard);
        }
        return generatedFightCards;
    }

    //takes in two fighters, and returns that fight.
    public Fight getFightFromFighterPair(Fighter a, Fighter b){
        for(Fight fight : generatedFights) {
            if (fight.hasFighter(a) && fight.hasFighter(b)) {
                return fight;
            }
        }
        return null;
    }

}