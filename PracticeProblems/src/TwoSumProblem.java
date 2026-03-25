import java.util.*;

public class TwoSumProblem {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        int time;

        Transaction(int id, int amount, String merchant, String account, int time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    static List<Transaction> transactions = new ArrayList<>();

    public static void addTransaction(int id, int amount, String merchant, String account, int time) {
        transactions.add(new Transaction(id, amount, merchant, account, time));
    }

    public static void findTwoSum(int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);
                System.out.println("Two Sum Found: (" + other.id + ", " + t.id + ")");
                return;
            }

            map.put(t.amount, t);
        }

        System.out.println("No pair found");
    }

    public static void findTwoSumWithinOneHour(int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= 60) {
                    System.out.println("Two Sum within 1 hour: (" + other.id + ", " + t.id + ")");
                    return;
                }
            }

            map.put(t.amount, t);
        }

        System.out.println("No pair found within 1 hour");
    }

    public static void detectDuplicates() {
        HashMap<String, List<String>> map = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "-" + t.merchant;
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t.account);
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("Duplicate detected: " + entry.getKey() +
                        ", Accounts: " + entry.getValue());
            }
        }
    }

    public static void findKSum(int k, int target) {
        List<Integer> result = new ArrayList<>();
        if (kSumHelper(0, k, target, result)) {
            System.out.println("K-Sum Found: " + result);
        } else {
            System.out.println("No K-Sum found");
        }
    }

    public static boolean kSumHelper(int start, int k, int target, List<Integer> result) {
        if (k == 0 && target == 0) {
            return true;
        }
        if (k == 0 || target < 0) {
            return false;
        }
        for (int i = start; i < transactions.size(); i++) {
            result.add(transactions.get(i).id);

            if (kSumHelper(i + 1, k - 1, target - transactions.get(i).amount, result)) {
                return true;
            }
            result.remove(result.size() - 1);
        }
        return false;
    }
    public static void main(String[] args) {
        addTransaction(1, 500, "StoreA", "acc1", 600);
        addTransaction(2, 300, "StoreB", "acc2", 615);
        addTransaction(3, 200, "StoreC", "acc3", 630);
        addTransaction(4, 500, "StoreA", "acc4", 650);

        findTwoSum(500);
        findTwoSumWithinOneHour(500);
        detectDuplicates();
        findKSum(3, 1000);
    }
}
