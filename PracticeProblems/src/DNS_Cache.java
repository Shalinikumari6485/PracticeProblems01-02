import java.util.*;

public class DNS_Cache {

    static class DNSEntry {
        String ipAddress;
        long expiryTime;

        DNSEntry(String ipAddress, long ttlSeconds) {
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    static HashMap<String, DNSEntry> cache = new HashMap<>();
    static LinkedHashMap<String, String> lruMap = new LinkedHashMap<>(16, 0.75f, true);
    static int MAX_CACHE_SIZE = 3;

    static int hits = 0;
    static int misses = 0;

    public static String resolve(String domain) {
        long startTime = System.nanoTime();

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                lruMap.get(domain); // update LRU
                long endTime = System.nanoTime();
                double timeMs = (endTime - startTime) / 1000000.0;
                return "Cache HIT -> " + entry.ipAddress + " (retrieved in " + timeMs + " ms)";
            } else {
                cache.remove(domain);
                lruMap.remove(domain);
                System.out.println("Cache EXPIRED for " + domain);
            }
        }

        misses++;
        String newIp = queryUpstreamDNS(domain);

        if (cache.size() >= MAX_CACHE_SIZE) {
            String oldestKey = lruMap.keySet().iterator().next();
            cache.remove(oldestKey);
            lruMap.remove(oldestKey);
            System.out.println("LRU Evicted: " + oldestKey);
        }

        cache.put(domain, new DNSEntry(newIp, 5)); // TTL = 5 seconds
        lruMap.put(domain, newIp);

        return "Cache MISS -> Query upstream -> " + newIp + " (TTL: 5s)";
    }

    public static String queryUpstreamDNS(String domain) {
        if (domain.equals("google.com")) return "172.217.14.206";
        if (domain.equals("youtube.com")) return "142.250.183.46";
        if (domain.equals("facebook.com")) return "157.240.22.35";
        return "192.168.1.1";
    }
    public static void removeExpiredEntries() {
        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> entry = it.next();
            if (entry.getValue().isExpired()) {
                lruMap.remove(entry.getKey());
                it.remove();
            }
        }
    }

    public static String getCacheStats() {
        int total = hits + misses;
        double hitRate = 0;

        if (total > 0) {
            hitRate = (hits * 100.0) / total;
        }
        return "Hit Rate: " + hitRate + "%";
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("resolve(\"google.com\") -> " + resolve("google.com"));
        System.out.println("resolve(\"google.com\") -> " + resolve("google.com"));

        Thread.sleep(6000); // wait for TTL to expire
        removeExpiredEntries();

        System.out.println("resolve(\"google.com\") -> " + resolve("google.com"));

        System.out.println("resolve(\"youtube.com\") -> " + resolve("youtube.com"));
        System.out.println("resolve(\"facebook.com\") -> " + resolve("facebook.com"));
        System.out.println("resolve(\"instagram.com\") -> " + resolve("instagram.com"));

        System.out.println("getCacheStats() -> " + getCacheStats());
    }
}