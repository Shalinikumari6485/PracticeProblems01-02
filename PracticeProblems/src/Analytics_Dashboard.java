import java.util.*;

public class Analytics_Dashboard {
    static HashMap<String, Integer> pageViews = new HashMap<>();
    static HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    static HashMap<String, Integer> trafficSources = new HashMap<>();

    public static void processEvent(String url, String userId, String source) {
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public static void getDashboard() {
        System.out.println("Top Pages:");
        List<Map.Entry<String, Integer>> list = new ArrayList<>(pageViews.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());

        int top = Math.min(10, list.size());

        for (int i = 0; i < top; i++) {
            String url = list.get(i).getKey();
            int views = list.get(i).getValue();
            int uniqueCount = uniqueVisitors.get(url).size();
            System.out.println((i + 1) + ". " + url + " - " + views + " views (" + uniqueCount + " unique)");
        }

        System.out.println("\nTraffic Sources:");
        int totalSourceVisits = 0;
        for (int count : trafficSources.values()) {
            totalSourceVisits += count;
        }
        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            String source = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalSourceVisits;

            System.out.printf("%s: %.2f%%\n", source, percentage);
        }
    }

    public static void main(String[] args) {
        processEvent("/article/breaking-news", "user_123", "Google");
        processEvent("/article/breaking-news", "user_456", "Facebook");
        processEvent("/sports/championship", "user_111", "Direct");
        processEvent("/article/breaking-news", "user_123", "Google");
        processEvent("/sports/championship", "user_222", "Google");
        processEvent("/tech/ai-update", "user_333", "Direct");
        processEvent("/tech/ai-update", "user_444", "Facebook");
        processEvent("/article/breaking-news", "user_555", "Google");

        getDashboard();
    }
}