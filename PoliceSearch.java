
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PoliceSearch {

    private final Map<String, String> criminals = new HashMap<>();
    private final Map<Integer, String> names = new HashMap<>();
    private final Map<Integer, Set<String>> aliases = new HashMap<>();

    enum MatchType {
        PKM(2), // Partial Key Match
        FKM(4), // Full Key Match
        PAM(1), //Partial Alias Match
        FAM(3), // Full Alias Match
        NM(0);  // No Match

        private int priority;

        MatchType(int priority) {
            this.priority = priority;
        }
    }

    private MatchType bestMatch = MatchType.NM;
    private int matchFoundFor = -1;

    public static void main(String[] args) {
        PoliceSearch ps = new PoliceSearch();
        ps.populateCriminalsData();
        System.out.print("Enter name:");
        Scanner input = new Scanner(System.in);
        String needle = input.nextLine();
        input.close();
        ps.searchInCriminalRecords(needle.toLowerCase());
    }


/*
This method is used to populate criminals data as given by the question
 */
    private void populateCriminalsData() {
        criminals.put("Paul White", "Roger Night,Peter Llong Jr.");
        criminals.put("Roger Fedexer", "Rob Ford,Pete Lord,Roger McWire");
        criminals.put("Paul White Jr.", null);
        criminals.put("Red Fortress", "Roger Rabbit,Ross Winter");
        criminals.put("Redford Fort", "Red Strong,Red Fort");
        criminals.put("Fox Hat","Hate Fox,Fox Hater");
    }

    /*
    To separate the names or keys and aliases
     */
    private void prepareData() {
        int i = 0;
        for (Map.Entry<String, String> data : criminals.entrySet()) {

            names.put(i, data.getKey().toLowerCase());

            if (data.getValue() != null) {
                String[] csv = data.getValue().split(",");
                Set<String> al = new HashSet<>();
                for (String name : csv) {
                    al.add(name.trim().toLowerCase());
//                        System.out.println(val);
                }
                aliases.put(i, al);

            }
            i++;
        }

    }

    /*
    To update the found match type and the corresponding index of the data
     */
    private void setBestMatch(MatchType bestMatch, int index) {
        if (this.bestMatch.priority >= bestMatch.priority) {
            System.out.println("found match but ignored");
        } else {
            this.bestMatch = bestMatch;
            this.matchFoundFor = index;
//            System.out.println("printing index:" + index + ", priority : " + bestMatch.priority);
        }
    }

    private void searchInCriminalRecords(String needle) {

        if (names.size() == 0 || aliases.size() == 0) {
            prepareData();
        }

        for (Map.Entry<Integer, String> name : names.entrySet()) {
            System.out.println("key: " + name.getKey() + ", value: " + name.getValue());
            if (name.getValue().equals(needle)) {
                setBestMatch(MatchType.FKM, name.getKey());
            }
            if (name.getValue().contains(needle)) {
                setBestMatch(MatchType.PKM, name.getKey());
            }
        }


        for (Map.Entry<Integer, Set<String>> alias : aliases.entrySet()) {

            for (String name : alias.getValue()) {
                System.out.println("key: " + alias.getKey() + ", value: " + name);
                if (name.equals(needle)) {
                    setBestMatch(MatchType.FAM, alias.getKey());
                }
                if (name.contains(needle)) {
                    setBestMatch(MatchType.PAM, alias.getKey());
                }
            }
        }
//        System.out.println(bestMatch.getPriority());
//        System.out.println(matchFoundFor);
        System.out.println("Name: [" + names.get(matchFoundFor) + "]");
        System.out.println("Aliases:" + (aliases.get(matchFoundFor) != null ? aliases.get(matchFoundFor).toString() : "null"));


    }
}

