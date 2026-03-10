import java.util.*;

public class ECommerceSale {

    static HashMap<String, Integer> stockMap = new HashMap<>();

    static HashMap<String, LinkedList<Integer>> waitingListMap = new HashMap<>();

    public static void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingListMap.put(productId, new LinkedList<>());
    }

    public static String checkStock(String productId) {
        int stock = stockMap.getOrDefault(productId, 0);
        return stock + " units available";
    }

    public static synchronized String purchaseItem(String productId, int userId) {
        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            LinkedList<Integer> waitingList = waitingListMap.get(productId);
            waitingList.add(userId);
            return "Added to waiting list, position #" + waitingList.size();
        }
    }

    public static void main(String[] args) {
        addProduct("IPHONE15_256GB", 100);

        System.out.println("checkStock(\"IPHONE15_256GB\") -> " + checkStock("IPHONE15_256GB"));

        System.out.println("purchaseItem(\"IPHONE15_256GB\", 12345) -> "
                + purchaseItem("IPHONE15_256GB", 12345));

        System.out.println("purchaseItem(\"IPHONE15_256GB\", 67890) -> "
                + purchaseItem("IPHONE15_256GB", 67890));

        stockMap.put("IPHONE15_256GB", 0);

        System.out.println("purchaseItem(\"IPHONE15_256GB\", 99999) -> "
                + purchaseItem("IPHONE15_256GB", 99999));
    }
}