import net.sf.jsefa.csv.annotation.CsvField;

import java.util.UUID;

public class Fighter {

    private UUID ID;
    @CsvField(pos = 1)
    private String name;
    @CsvField(pos = 2)
    private String location;

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
}
