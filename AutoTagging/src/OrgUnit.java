import java.util.List;
import java.util.stream.Collectors;

public class OrgUnit {

    private Integer ID;
    private String name;
    private Integer parentID;
    private Boolean isSelected;
    private Boolean isIncluded;

    public OrgUnit(Integer ID, String name, Integer parentID, Boolean isSelected) {
        this.ID = ID;
        this.name = name;
        this.parentID = parentID;
        this.isSelected = isSelected;
        this.isIncluded = false;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean getIncluded() {
        return isIncluded;
    }

    public void setIncluded(Boolean included) {
        isIncluded = included;
    }

    @Override
    public String toString() {
        return "OrgUnit{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", parentID=" + parentID +
                ", isSelected=" + isSelected +
                '}';
    }

}
