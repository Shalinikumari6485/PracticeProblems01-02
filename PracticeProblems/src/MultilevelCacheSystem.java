import java.util.*;

public class MultilevelCacheSystem {

    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        int capacity;
        LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    static LRUCache<String, String> l1Cache = new LRUCache<>(3);
    static LRUCache<String, String> l2Cache = new LRUCache<>(5);
    static HashMap<String, String> l3Database = new HashMap<>();
    static HashMap<String, Integer> accessCount = new HashMap<>();
    static int l1Hits = 0, l2Hits = 0, l3Hits = 0;
    public static String getVideo(String videoId) {
        long start = System.nanoTime();

        if (l1Cache.containsKey(videoId)) {
            l1Hits++;
            long end = System.nanoTime();
            return "L1 Cache HIT -> " + l1Cache.get(videoId) +
                    " | Time: " + ((end - start) / 1000000.0) + " ms";
        }
        if (l2Cache.containsKey(videoId)) {
            l2Hits++;
            String data = l2Cache.get(videoId);
            l1Cache.put(videoId, data);
            long end = System.nanoTime();
            return "L2 Cache HIT -> Promoted to L1 -> " + data +
                    " | Time: " + ((end - start) / 1000000.0) + " ms";
        }
        if (l3Database.containsKey(videoId)) {
            l3Hits++;
            String data = l3Database.get(videoId);
            l2Cache.put(videoId, data);
            accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);
            long end = System.nanoTime();
            return "L3 Database HIT -> Added to L2 -> " + data +
                    " | Time: " + ((end - start) / 1000000.0) + " ms";
        }

        return "Video not found";
    }

    public static void updateVideo(String videoId, String newData) {
        l3Database.put(videoId, newData);
        l1Cache.remove(videoId);
        l2Cache.remove(videoId);
        System.out.println("Cache invalidated for " + videoId);
    }
    public static void getStatistics() {
        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("L1 Hits: " + l1Hits);
        System.out.println("L2 Hits: " + l2Hits);
        System.out.println("L3 Hits: " + l3Hits);

        if (total > 0) {
            System.out.printf("Overall Hit Rate: %.2f%%\n", (total * 100.0) / total);
        }
    }
    public static void main(String[] args) {
        l3Database.put("video123", "Movie A");
        l3Database.put("video999", "Movie B");
        l3Database.put("video555", "Movie C");

        System.out.println(getVideo("video123"));
        System.out.println(getVideo("video123"));
        System.out.println(getVideo("video999"));

        updateVideo("video123", "Movie A Updated");

        System.out.println(getVideo("video123"));
        getStatistics();
    }
}
