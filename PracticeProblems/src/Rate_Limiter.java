import java.util.*;

public class Rate_Limiter {
    static class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefillTime;

        TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }

    static HashMap<String, TokenBucket> clientBuckets = new HashMap<>();
    static int LIMIT = 1000; // 1000 requests per hour
    static long REFILL_TIME = 60 * 60 * 1000; // 1 hour in milliseconds

    public static synchronized String checkRateLimit(String clientId) {
        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            bucket = new TokenBucket(LIMIT);
            clientBuckets.put(clientId, bucket);
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - bucket.lastRefillTime >= REFILL_TIME) {
            bucket.tokens = LIMIT;
            bucket.lastRefillTime = currentTime;
        }

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            long retryAfter = (REFILL_TIME - (currentTime - bucket.lastRefillTime)) / 1000;
            return "Denied (0 requests remaining, retry after " + retryAfter + "s)";
        }
    }

    public static String getRateLimitStatus(String clientId) {
        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            return "{used: 0, limit: " + LIMIT + ", reset: not started}";
        }
        int used = LIMIT - bucket.tokens;
        long resetTime = (bucket.lastRefillTime + REFILL_TIME) / 1000;
        return "{used: " + used + ", limit: " + LIMIT + ", reset: " + resetTime + "}";
    }

    public static void main(String[] args) {

        System.out.println("checkRateLimit(\"abc123\") -> " + checkRateLimit("abc123"));
        System.out.println("checkRateLimit(\"abc123\") -> " + checkRateLimit("abc123"));
        System.out.println("checkRateLimit(\"abc123\") -> " + checkRateLimit("abc123"));
        clientBuckets.get("abc123").tokens = 0;
        System.out.println("checkRateLimit(\"abc123\") -> " + checkRateLimit("abc123"));
        System.out.println("getRateLimitStatus(\"abc123\") -> " + getRateLimitStatus("abc123"));
    }
}