import java.util.*;

public class Search_Engine {
    static HashMap<String, Integer> queryFrequency = new HashMap<>();
    public static void updateFrequency(String query) {
        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);
        System.out.println(query + " -> Frequency: " + queryFrequency.get(query));
    }

    public static void search(String prefix) {
        List<Map.Entry<String, Integer>> matches = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {
            if (entry.getKey().startsWith(prefix.toLowerCase())) {
                matches.add(entry);
            }
        }

        matches.sort((a, b) -> b.getValue() - a.getValue());

        System.out.println("Suggestions for \"" + prefix + "\":");

        int top = Math.min(10, matches.size());
        for (int i = 0; i < top; i++) {
            System.out.println((i + 1) + ". \"" + matches.get(i).getKey() + "\" ("
                    + matches.get(i).getValue() + " searches)");
        }

        if (matches.size() == 0) {
            System.out.println("No suggestions found");
            suggestCorrection(prefix);
        }
    }

    public static void suggestCorrection(String word) {
        for (String query : queryFrequency.keySet()) {
            if (Math.abs(query.length() - word.length()) <= 1) {
                System.out.println("Did you mean: " + query + " ?");
                return;
            }
        }
    }

    public static void main(String[] args) {

        updateFrequency("java tutorial");
        updateFrequency("javascript");
        updateFrequency("java download");
        updateFrequency("java tutorial");
        updateFrequency("java 21 features");
        updateFrequency("java 21 features");
        updateFrequency("java 21 features");
        updateFrequency("java interview questions");
        updateFrequency("java tutorial");

        search("jav");

        updateFrequency("java 21 features");
    }
}
