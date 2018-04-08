import java.util.List;

public class TestDriver {

    public static final int FIGHTS_PER_FIGHTER = 10; // update to 10.

    public static void main (String[] args){

        long startTime = System.currentTimeMillis();
        System.out.println("Test Drive!");

        System.out.println("Fighters From List:");
        List<Fighter> fighters = CsvReader.readFightersFromCsv();
        FightSchedule schedule = new FightSchedule();

        schedule.generateFights(fighters, FIGHTS_PER_FIGHTER);


        System.out.println(schedule.toString());

        List<FightCard> fightCards = schedule.generateFightCards();

        System.out.println();

        for(FightCard card : fightCards) {
            System.out.println(card.toString());
        }

    }
}
