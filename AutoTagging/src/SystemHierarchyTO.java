public class SystemHierarchyTO {

    private Integer id;
    private Integer parentID;
    private String idHierarchy;
    private Boolean isSelected;
    private Boolean isIncluded;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getIdHierarchy() {
        return idHierarchy;
    }

    public void setIdHierarchy(String idHierarchy) {
        this.idHierarchy = idHierarchy;
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
}
