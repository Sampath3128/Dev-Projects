public class InputParameterTO {

    private String label;
    private Long sysGroupParameterID;
    private Boolean isSelected;
    private Boolean isTreeStructure;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getSysGroupParameterID() {
        return sysGroupParameterID;
    }

    public void setSysGroupParameterID(Long sysGroupParameterID) {
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
