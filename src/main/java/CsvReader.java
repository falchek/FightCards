import net.sf.jsefa.Serializer;
import net.sf.jsefa.csv.CsvIOFactory;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader {

    public static List<Fighter> readFightersFromCsv(){
        List<Fighter> fightersListFromFile = new ArrayList<Fighter>();

        File file = new File("src/main/resources/FightCardsSignUps.csv");
        String absolutePath = file.getAbsolutePath();

        try {
            BufferedReader br = new BufferedReader(new FileReader(absolutePath));
            String line;
            while((line = br.readLine()) != null){
                List<String> dataLine = Arrays.asList(line.split(","));

                Fighter fighter = new Fighter();
                fighter.setName(dataLine.get(0));
                fighter.setLocation(dataLine.get(1));
                if(dataLine.size() > 2) {
                    fighter.setTeam(dataLine.get(2));
                }
                fightersListFromFile.add(fighter);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File not found");
        }

        return fightersListFromFile;
    }


}
