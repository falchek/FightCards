import net.sf.jsefa.csv.annotation.CsvField;

import java.util.UUID;

public class Fighter implements Cloneable{

    private UUID ID;
    @CsvField(pos = 1)
    private String name;
    @CsvField(pos = 2)
    private String location;
    @CsvField(pos = 3)
    private String team;

    //copy constructor I guess.
    public Fighter(Fighter fighter) {
        this.ID = fighter.getID();
        this.name = fighter.getName();
        this.location = fighter.getLocation();
        this.team = fighter.getTeam(); 
    }

    public Fighter() {
        ID = UUID.randomUUID();
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID id) {
        this.ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
