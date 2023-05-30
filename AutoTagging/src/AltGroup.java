import java.util.*;
import java.util.stream.Collectors;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;

public class AltGroup {

    // This function needs to be called on creating an Employee Group
    public void createEmployeeGroup(InputRequestTO inputRequestTO, Integer organizationID, Integer tenantID) {
        Map<String, Map<Long, List<String>>> inclusionExclusionMap = generateInclusionExclusion(inputRequestTO, organizationID, tenantID);
        // generate the records accordingly and insert to AltGroupParameterValueAgr table
        List<AltGroupParameterValueAggregate> entries = generateAggregateTableEntries(inclusionExclusionMap, inputRequestTO, organizationID, tenantID);
        // insert these values into the MariaDB
    }

    private List<AltGroupParameterValueAggregate> generateAggregateTableEntries(Map<String, Map<Long, List<String>>> inclusionExclusionMap,
                                                                                InputRequestTO inputRequestTO, Integer organizationID, Integer tenantID) {
        List<AltGroupParameterValueAggregate> resultList = new ArrayList<>();
        for (String type : inclusionExclusionMap.keySet()) {
            for (Long sysGroupParameterID : inclusionExclusionMap.get(type).keySet()) {
                AltGroupParameterValueAggregate aggregate = new AltGroupParameterValueAggregate();
                aggregate.setAltGroupID(inputRequestTO.getAltGroupID());
                aggregate.setOrganizationID(organizationID);
                aggregate.setTenantID(tenantID);
                aggregate.setIsActive(inputRequestTO.getStatus());
                aggregate.setSysGroupParameterID(sysGroupParameterID);
                Map<String, List<String>> valueMap = new HashMap<>();
                valueMap.put("values", inclusionExclusionMap.get(type).get(sysGroupParameterID));
                JSONObject jsonObject = new JSONObject(map);
                String jsonString = jsonObject.toJSONString();
                aggregate.setValueList(jsonString);
                if (type.equalsIgnoreCase("Inclusion")) {
                    aggregate.setIsInclusive(true);
                } else {
                    aggregate.setIsInclusive(false);
                }
                aggregate.setCreatedBy(1);
                aggregate.setModifiedBy(1);
                aggregate.setCreatedDate(new Date());
                aggregate.setModifiedDate(new Date());
                resultList.add(aggregate);
            }
        }
        return resultList;
    }

    // This function needs to be called on Migration of Employee Group data for an Organization
    public void startMigration(Integer organizationID, Integer tenantID) {
        List<Object[]> groupsInfo = fetchGroupsInfoByOrgID(organizationID, tenantID);
        List<InputRequestTO> groupRequestTOs = createGroupInputRequestTOs(groupsInfo);
        for (InputRequestTO inputRequestTO : groupRequestTOs) {
            createEmployeeGroup(inputRequestTO, organizationID, tenantID);
        }
    }

    public Map<String, Map<Long, List<String>>> generateInclusionExclusion(InputRequestTO inputRequestTO, Integer organizationID, Integer tenantID) {
        List<InputParameterTO> inputParameterTOS = inputRequestTO.getSelectedParameterList();
        Map<String, Map<Long, List<String>>> result = new HashMap<>();

        for (InputParameterTO inputParameterTO : inputParameterTOS) {
            List<String> inclusionList = new ArrayList<>();
            List<String> exclusionList = new ArrayList<>();
            Map<Boolean, List<String>> incExcMap = new HashMap<>();
            if (inputParameterTO.getTreeStructure()) {
                List<SystemHierarchyTO> systemHierarchyTOS = fetchSystemHierarchyTOList(inputParameterTO.getLabel(), organizationID, tenantID);
                markSelectionInSystemHierarchyTOList(systemHierarchyTOS,
                        inputRequestTO.getSelectedParameterValueMap().get(inputParameterTO.getSysGroupParameterID()));
                List<SystemHierarchyTO> rootNodesList = fetchRootNodes(systemHierarchyTOS);
                if (rootNodesList!=null && !rootNodesList.isEmpty()) {
                    for (SystemHierarchyTO systemHierarchyTO : rootNodesList) {
                        traverseTree(systemHierarchyTO, systemHierarchyTOS, inclusionList, exclusionList);
                    }
                }
            } else {
                List<InputParameterValueTO> inputParameterValueTOList = inputRequestTO.getSelectedParameterValueMap().get(inputParameterTO.getSysGroupParameterID());
                for (InputParameterValueTO inputParameterValueTO : inputParameterValueTOList) {
                    if (inclusionList==null) {
                        inclusionList = new ArrayList<>();
                    }
                    inclusionList.add(inputParameterValueTO.getId().toString());
                }
            }
            if (inclusionList!=null && !inclusionList.isEmpty()) {
                if (result.get("Inclusion")==null) {
                    result.put("Inclusion", new HashMap<>());
                }
                result.get("Inclusion").put(inputParameterTO.getSysGroupParameterID(), inclusionList);
            }
            if (exclusionList!=null && !exclusionList.isEmpty()) {
                if (result.get("Exclusion")==null) {
                    result.put("Exclusion", new HashMap<>());
                }
                result.get("Exclusion").put(inputParameterTO.getSysGroupParameterID(), exclusionList);
            }
        }
        return result;
    }

    private List<SystemHierarchyTO> fetchRootNodes(List<SystemHierarchyTO> systemHierarchyTOS) {
        List<SystemHierarchyTO> systemHierarchyTOList = new ArrayList<>();
        for (SystemHierarchyTO systemHierarchyTO : systemHierarchyTOS) {
            if (systemHierarchyTO.getParentID()==null) {
                systemHierarchyTOList.add(systemHierarchyTO);
            }
        }
        return systemHierarchyTOList;
    }

    private void markSelectionInSystemHierarchyTOList(List<SystemHierarchyTO> systemHierarchyTOS, List<InputParameterValueTO> inputParameterValueTOS) {
        for (InputParameterValueTO inputParameterValueTO : inputParameterValueTOS) {
            for (SystemHierarchyTO systemHierarchyTO : systemHierarchyTOS) {
                if (systemHierarchyTO.getId().equals(inputParameterValueTO.getId())) {
                    systemHierarchyTO.setSelected(inputParameterValueTO.getSelected());
                    break;
                }
            }
        }
    }

    public List<SystemHierarchyTO> fetchSystemHierarchyTOList(String parameterName, Integer organizationID, Integer tenantID) {
        List<Object[]> dataset = new ArrayList<>();
        List<SystemHierarchyTO> hierarchyTOList = new ArrayList<>();
        if (parameterName.equalsIgnoreCase("OrgUnit")) {
            //fetch Data from HrOrgUnit (unitID, parentID)
            hierarchyTOList = createSystemHierarchyTO(dataset);
        } else if (parameterName.equalsIgnoreCase("WorkSite")) {
            //fetch Data from HrWorkSite (WorksiteID, parentID)
            hierarchyTOList = createSystemHierarchyTO(dataset);
        } else if (parameterName.equalsIgnoreCase("BusinessUnit")) {
            //fetch Data from BusinessUnit (BuID, parentID)
            hierarchyTOList = createSystemHierarchyTO(dataset);
        } else if (parameterName.equalsIgnoreCase("Division")) {
            //fetch Data from Division (DivisionID, parentID)
            hierarchyTOList = createSystemHierarchyTO(dataset);
        } else if (parameterName.equalsIgnoreCase("Division")) {
            //fetch Data from Division (DepartmentID, parentID)
            hierarchyTOList = createSystemHierarchyTO(dataset);
        }
        return hierarchyTOList;
    }

    private List<SystemHierarchyTO> createSystemHierarchyTO(List<Object[]> dataset) {
        List<SystemHierarchyTO> result = new ArrayList<>();
        for (Object[] objects : dataset) {
            SystemHierarchyTO systemHierarchyTO = new SystemHierarchyTO();
            Integer id = objects[0]!=null?(Integer) objects[0] : null;
            Integer parentID = objects[0]!=null?(Integer) objects[0] : null;
            systemHierarchyTO.setId(id);
            systemHierarchyTO.setParentID(parentID);
            systemHierarchyTO.setSelected(false);
            systemHierarchyTO.setIncluded(false);
            result.add(systemHierarchyTO);
        }
        return result;
    }

    public List<Object[]> fetchGroupsInfoByOrgID(Integer organizationID, Integer tenantID) {
        // fetch data from AltGroup, AltGroupParameter, SysGroupParameter, AltGroupParameterValue
        return null;
    }

    public List<InputRequestTO> createGroupInputRequestTOs(List<Object[]> groupsData) {
        List<InputRequestTO> groupInputRequestTOList = new ArrayList<>();
        Map<Integer, InputRequestTO> groupRequestToMap = new HashMap<>();
        for (Object[] objects : groupsData) {
            Integer groupID = (Integer) objects[0];
            Integer organizationID = (Integer) objects[1];
            Integer tenantID = (Integer) objects[2];
            Integer entityID = (Integer) objects[3];
            Long sysGroupParameterID = (Long) objects[4];
            String sysGroupParameterName = (String) objects[5];
            Boolean isTreeStructure = (Boolean) objects[6];
            Integer valueID = (Integer) objects[7];
            Integer contextID = (Integer) objects[8];
            Boolean isActive = (Boolean) objects[9];

            if (groupRequestToMap.get(groupID)==null) {
                groupRequestToMap.put(groupID, new InputRequestTO());
            }

            InputRequestTO inputRequestTO = groupRequestToMap.get(groupID);
            InputParameterTO inputParameterTO = new InputParameterTO();
            inputParameterTO.setLabel(sysGroupParameterName);
            inputParameterTO.setSysGroupParameterID(sysGroupParameterID);
            inputParameterTO.setTreeStructure(isTreeStructure);
            inputParameterTO.setSelected(true);

            InputParameterValueTO inputParameterValueTO = new InputParameterValueTO();
            inputParameterValueTO.setId(valueID);
            inputParameterValueTO.setSelected(true);

            if (inputRequestTO.getSelectedParameterList()==null) {
                inputRequestTO.setSelectedParameterList(new ArrayList<>());
            }
            inputRequestTO.getSelectedParameterList().add(inputParameterTO);
            if (inputRequestTO.getSelectedParameterValueMap().get(sysGroupParameterID)==null) {
                inputRequestTO.setSelectedParameterValueMap(new HashMap<>());
            }
            if (inputRequestTO.getSelectedParameterValueMap().get(sysGroupParameterID)==null) {
                inputRequestTO.getSelectedParameterValueMap().put(sysGroupParameterID, new ArrayList<>());
            }
            inputRequestTO.getSelectedParameterValueMap().get(sysGroupParameterID).add(inputParameterValueTO);
            inputRequestTO.setGroupContextID(contextID);
            inputRequestTO.setStatus(isActive);
            inputRequestTO.setEntityID(entityID);

            groupInputRequestTOList.add(inputRequestTO);
        }
        return null;
    }

    /*
    Algorithm :
        Input : Node (any random node, List of all system level nodes with selection status)
        Approach :
            1. Fetch the parent node
            2. If the node is selected -
                if the parent is null, add the node in inclusion and change the inclusion status of the node
                if the parent is not null -
                    if the parent is included - then change the node inclusion status to true
                    if the parent is not included - then add the node in inclusion and change the node inclusion status
            3. If the node is not selected -
                If the parent is not null -
                    If the parent is included - then add the node in exclusion
            4. Modify the node properties (Inclusion status) in system level nodes
            5. Fetch the child nodes of the input node
            6. If the child nodes are not empty, for each node - go to line 1
            7. If there are no child nodes continue
            8. Return the inclusion exclusion lists
     */
    private List<SystemHierarchyTO> traverseTree(SystemHierarchyTO node, List<SystemHierarchyTO> systemNodes, List<String> inclusionList, List<String> exclusionList) {
        List<SystemHierarchyTO> childNodes = fetchChildNodes(node, systemNodes);
        SystemHierarchyTO parentNode = fetchParentNode(node, systemNodes);
        // Input node can be selected or not selected and the node inclusion or exclusion depends on the parent inclusion status
        if (node.getSelected()) {
            if (parentNode==null) {
                if (inclusionList==null) {
                    inclusionList = new ArrayList<>();
                }
                inclusionList.add(node.getIdHierarchy());
            } else {
                if (!parentNode.getIncluded())  {
                    if (inclusionList==null) {
                        inclusionList = new ArrayList<>();
                    }
                    inclusionList.add(node.getIdHierarchy());
                }
                node.setIncluded(true);
            }
        } else {
            if (parentNode.getIncluded()) {
                if (exclusionList==null) {
                    exclusionList = new ArrayList<>();
                }
                exclusionList.add(node.getIdHierarchy());
            }
        }
        for (SystemHierarchyTO systemHierarchyTO : systemNodes) {
            if (systemHierarchyTO.getId().equals(node.getId())) {
                systemHierarchyTO.setIncluded(node.getIncluded());
                break;
            }
        }
        if (childNodes!=null && !childNodes.isEmpty()) {
            for (SystemHierarchyTO childNode : childNodes) {
                systemNodes = traverseTree(childNode, systemNodes, inclusionList, exclusionList);
            }
        }
        return systemNodes;
    }

    private SystemHierarchyTO fetchParentNode(SystemHierarchyTO node, List<SystemHierarchyTO> systemNodes) {
        if (node.getParentID()==null) {
            return null;
        }
        for (SystemHierarchyTO systemHierarchyTO : systemNodes) {
            if (systemHierarchyTO.getId().equals(node.getParentID())) {
                return systemHierarchyTO;
            }
        }
        return null;
    }

    private List<SystemHierarchyTO> fetchChildNodes(SystemHierarchyTO node, List<SystemHierarchyTO> orgUnits) {
        return orgUnits.stream().filter(temp -> (
                temp!=null &&
                        temp.getParentID()!=null &&
                        temp.getParentID().equals(node.getId()))
        ).collect(Collectors.toList());
    }

}