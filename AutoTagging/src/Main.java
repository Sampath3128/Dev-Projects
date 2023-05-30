import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final Logger LOGGER_ = LoggerFactory.getLogger(Main.class);

    public static List<OrgUnit> createOrgUnits() {
        List<OrgUnit> orgUnits = new ArrayList<>();

        orgUnits.add(new OrgUnit(2, "Board", null, false));
        orgUnits.add(new OrgUnit(3, "Financial Center", 2, false));
        orgUnits.add(new OrgUnit(4, "Legal Department", 2, false));
        orgUnits.add(new OrgUnit(5, "Director", 2, true));
        orgUnits.add(new OrgUnit(6, "Production", 5, false));
        orgUnits.add(new OrgUnit(7, "Investment", 5, false));
        orgUnits.add(new OrgUnit(8, "Marketing", 5, false));
        orgUnits.add(new OrgUnit(9, "Technical", 5, false));
        orgUnits.add(new OrgUnit(10, "Human Resources", 5, false));
        orgUnits.add(new OrgUnit(11, "Production Planner", 6, false));
        orgUnits.add(new OrgUnit(12, "Technical Planner", 6, false));
        orgUnits.add(new OrgUnit(13, "Production", 6, false));
        orgUnits.add(new OrgUnit(14, "QA Manager", 6, false));
        orgUnits.add(new OrgUnit(15, "Maintenance", 6, false));
        orgUnits.add(new OrgUnit(16, "Research", 7, false));
        orgUnits.add(new OrgUnit(17, "Planning", 7, false));
        orgUnits.add(new OrgUnit(18, "Engineering", 7, false));
        orgUnits.add(new OrgUnit(19, "Brand", 8, false));
        orgUnits.add(new OrgUnit(20, "Trade", 8, false));
        orgUnits.add(new OrgUnit(21, "Promotion", 8, false));
        orgUnits.add(new OrgUnit(22, "Public", 8, false));
        orgUnits.add(new OrgUnit(23, "Market", 8, false));
        orgUnits.add(new OrgUnit(24, "Product", 9, true));
        orgUnits.add(new OrgUnit(25, "Research", 9, true));
        orgUnits.add(new OrgUnit(26, "Technical Audit", 9, true));
        orgUnits.add(new OrgUnit(27, "Technical Service", 9, true));
        orgUnits.add(new OrgUnit(28, "Computer Science", 9, true));
        orgUnits.add(new OrgUnit(29, "Training", 10, false));
        orgUnits.add(new OrgUnit(30, "Recruit", 10, false));
        orgUnits.add(new OrgUnit(31, "Office Support", 10, false));
        orgUnits.add(new OrgUnit(32, "Payroll", 10, false));

        return orgUnits;
    }

    private static List<OrgUnit> traverseTree(OrgUnit node, List<OrgUnit> orgUnits, Map<Integer, Boolean> incExcMap) {

        List<OrgUnit> childOrgUnits = fetchChildNodes(node, orgUnits);

        Integer selectedNodesCount = childOrgUnits.stream().filter(orgUnit -> (
                orgUnit!=null &&
                orgUnit.getSelected())
        ).collect(Collectors.toList()).size();

        if (selectedNodesCount > childOrgUnits.size()/2) {
            if (!checkIfParentIncluded(node, orgUnits)) {
                for (OrgUnit orgUnit : orgUnits) {
                    if (orgUnit.getID().equals(node.getID())) {
                        orgUnit.setIncluded(true);
                        incExcMap.put(orgUnit.getID(), true);
                        break;
                    }
                }
            }
            for (OrgUnit orgUnit : childOrgUnits) {
                Boolean isOrgUnitSelected = orgUnit.getSelected();
                if (!isOrgUnitSelected) {
                    incExcMap.put(orgUnit.getID(), false);
                }
                if (isOrgUnitSelected) {
                    orgUnit.setIncluded(true);
                }
            }
        } else if (selectedNodesCount>0) {
            incExcMap.remove(node.getID());
            for (OrgUnit orgUnit : childOrgUnits) {
                if (orgUnit.getSelected()) {
                    Boolean tempIsParentInc = Main.checkIfParentIncluded(orgUnit, orgUnits);
                    if (!tempIsParentInc) {
                        incExcMap.put(orgUnit.getID(), true);
                    }
                    orgUnit.setIncluded(true);
                }
            }
            if (node.getIncluded()) {
                for (OrgUnit orgUnit : childOrgUnits) {
                    if (!orgUnit.getSelected()) {
                        incExcMap.put(orgUnit.getID(), false);
                        orgUnit.setIncluded(true);
                    }
                }
            }
        } else {
            for (OrgUnit orgUnit : childOrgUnits)  {
                Boolean tempIsParentInc = Main.checkIfParentIncluded(orgUnit, orgUnits);
                if (!orgUnit.getSelected() && tempIsParentInc) {
                    incExcMap.put(orgUnit.getID(), false);
                }
            }
        }

        for (OrgUnit orgUnit : childOrgUnits) {
            orgUnits = traverseTree(orgUnit, orgUnits, incExcMap);
        }

        return orgUnits;
    }

    private static boolean checkIfParentIncluded(OrgUnit node, List<OrgUnit> orgUnits) {
        if (node.getParentID()==null) {
            return false;
        }
        List<OrgUnit> parentOrgUnits = orgUnits.stream().filter(temp -> (
                temp!=null &&
                temp.getID().equals(node.getParentID()))
        ).collect(Collectors.toList());

        if (!parentOrgUnits.isEmpty()) {
            return parentOrgUnits.get(0).getIncluded();
        }
        return false;
    }

    private static List<OrgUnit> fetchChildNodes(OrgUnit node, List<OrgUnit> orgUnits) {
        return orgUnits.stream().filter(temp -> (
                temp!=null &&
                temp.getParentID()!=null &&
                temp.getParentID().equals(node.getID()))
        ).collect(Collectors.toList());
    }

    public static void main(String[]args) {
        List<OrgUnit> orgUnits = createOrgUnits();

        OrgUnit root = orgUnits.stream().filter(temp -> (temp!=null && temp.getParentID()==null)).collect(Collectors.toList()).get(0);
        Map<Integer, Boolean> incExcMap = new HashMap<>();
        traverseTree(root, orgUnits, incExcMap);
        System.out.println("Inclusion Exclusion Map : " + incExcMap);
    }

}
