import java.util.List;

public class TestDriver {

    public static final int FIGHTS_PER_FIGHTER = 10; // update to 10.

    public static void main (String[] args){

        long startTime = System.currentTimeMillis();
        System.out.println("Test Drive!");

        System.out.println("Fighters From List:");
        List<Fighter> fighters = CsvReader.readFightersFromCsv();
        FightSchedule schedule = new FightSchedule();

        System.out.println(schedule.toString());

        List<FightCard> fightCards = schedule.generateFightCards(fighters, FIGHTS_PER_FIGHTER);

        System.out.println();

        for(FightCard card : fightCards) {
            System.out.println(card.toString());
            if(validateFightCard(card, fightCards)){
                System.out.println("Owner is in appropriate fight cards");
            }
            else {
                System.out.println("INVALID");
            }
        }

    }

    public static boolean validateFightCard(FightCard fightCard, List<FightCard> fightCards) {
        Fighter owner = fightCard.getOwner();
        for (Fighter fighter : fightCard.getOpponents()) {
            FightCard opponentCard = findFightCardByOwner(fighter, fightCards);
            boolean ownerInFightCard = isOwnerInFightCard(owner, opponentCard);
            if(!ownerInFightCard){
                return false;
            }
        }
        return true;
    }

    public static FightCard findFightCardByOwner(Fighter fighter, List<FightCard> fightCards) {
        for(FightCard fightCard : fightCards) {
            if(fightCard.getOwner().getID().equals(fighter.getID()))
                return fightCard;
        }
        return null;
    }

    public static boolean isOwnerInFightCard(Fighter owner, FightCard fightCard){
        return (fightCard.getOpponents().indexOf(owner) > -1);
    }
}
