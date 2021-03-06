package shared;

/**
 * Skabelon på et studie
 */
public class StudyDTO {

    private  int id;
    private  String name;
    private  String shortname;

    // Variabler skal defineres efterfølgende via public setters
    public StudyDTO() {

    }

    // Variabler defineres under initiering
    public StudyDTO (int id, String name, String shortname){
        this.id = id;
        this.shortname = shortname;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * En tekstbeskrivelse af et studie
     * @return String repræsentationen på et studie
     */
    @Override
    public String toString() {
        return "StudyDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortname='" + shortname + '\'' +
                '}';
    }
}
