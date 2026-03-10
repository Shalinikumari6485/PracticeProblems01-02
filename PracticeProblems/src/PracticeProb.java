//Problem : 1
//Practice Problem - Social Media Username Availability Checker

import java.util.*;
public class PracticeProb {

    static HashMap<String, Integer> usernameToUserId = new HashMap<>();
    static HashMap<String, Integer> attemptFrequency = new HashMap<>();
    public static boolean checkAvailability(String username) {
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);

        if (usernameToUserId.containsKey(username)) {
            return false;
        }
        return true;
    }

    public static List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String newName = username + i;
            if (!usernameToUserId.containsKey(newName)) {
                suggestions.add(newName);
            }
        }
        if (username.contains("_")) {
            suggestions.add(username.replace("_", "."));
        }
        return suggestions;
    }

    public static String getMostAttempted() {
        String name = "";
        int max = 0;
        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                name = entry.getKey();
            }
        }
        return name + " (" + max + " attempts)";
    }

    public static void main(String[] args) {
        usernameToUserId.put("john_doe", 1);
        usernameToUserId.put("admin", 2);

        System.out.println("checkAvailability(\"john_doe\") -> " + checkAvailability("john_doe"));
        System.out.println("checkAvailability(\"jane_smith\") -> " + checkAvailability("jane_smith"));

        System.out.println("suggestAlternatives(\"john_doe\") -> " + suggestAlternatives("john_doe"));

        checkAvailability("admin");
        checkAvailability("admin");
        checkAvailability("admin");

        System.out.println("getMostAttempted() -> " + getMostAttempted());
    }
}
