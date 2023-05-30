import java.util.List;
import java.util.Map;

public class InputRequestTO {

    private Integer altGroupID;
    private Integer entityID;
    private String groupName;
    private String policyName;
    private String module;
    private List<InputParameterTO> selectedParameterList;
    private Integer groupContextID;
    private Integer groupID;
    private Map<Long, List<InputParameterValueTO>> selectedParameterValueMap;
    private Boolean status;

    public Integer getAltGroupID() {
        return altGroupID;
    }

    public void setAltGroupID(Integer altGroupID) {
        this.altGroupID = altGroupID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<InputParameterTO> getSelectedParameterList() {
        return selectedParameterList;
    }

    public void setSelectedParameterList(List<InputParameterTO> selectedParameterList) {
        this.selectedParameterList = selectedParameterList;
    }

    public Integer getGroupContextID() {
        return groupContextID;
    }

    public void setGroupContextID(Integer groupContextID) {
        this.groupContextID = groupContextID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Map<Long, List<InputParameterValueTO>> getSelectedParameterValueMap() {
        return selectedParameterValueMap;
    }

    public void setSelectedParameterValueMap(Map<Long, List<InputParameterValueTO>> selectedParameterValueMap) {
        this.selectedParameterValueMap = selectedParameterValueMap;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
