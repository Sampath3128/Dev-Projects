public class InputParameterTO {

    private String label;
    private Integer sysGroupParameterID;
    private Boolean isSelected;
    private Boolean isTreeStructure;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getSysGroupParameterID() {
        return sysGroupParameterID;
    }

    public void setSysGroupParameterID(Integer sysGroupParameterID) {
        this.sysGroupParameterID = sysGroupParameterID;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Boolean getTreeStructure() {
        return isTreeStructure;
    }

    public void setTreeStructure(Boolean treeStructure) {
        isTreeStructure = treeStructure;
    }
}
