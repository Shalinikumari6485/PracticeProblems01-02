import java.util.*;

public class Plagarism_Detector {

    static HashMap<String, String> documents = new HashMap<>();

    static HashMap<String, Set<String>> nGramIndex = new HashMap<>();
    static int N = 5; // 5-gram

    public static void addDocument(String docId, String text) {
        documents.put(docId, text);
        List<String> ngrams = getNGrams(text, N);
        for (String gram : ngrams) {
            nGramIndex.putIfAbsent(gram, new HashSet<>());
            nGramIndex.get(gram).add(docId);
        }
    }

    public static List<String> getNGrams(String text, int n) {
        List<String> list = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder gram = new StringBuilder();
            for (int j = 0; j < n; j++) {
                gram.append(words[i + j]);
                if (j < n - 1) {
                    gram.append(" ");
                }
            }
            list.add(gram.toString());
        }
        return list;
    }

    public static void analyzeDocument(String docId, String text) {
        List<String> newDocNGrams = getNGrams(text, N);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : newDocNGrams) {
            if (nGramIndex.containsKey(gram)) {
                for (String oldDocId : nGramIndex.get(gram)) {
                    matchCount.put(oldDocId, matchCount.getOrDefault(oldDocId, 0) + 1);
                }
            }
        }

        System.out.println("Analyzing Document: " + docId);
        System.out.println("Extracted " + newDocNGrams.size() + " n-grams");

        String mostSimilarDoc = "";
        int maxMatch = 0;

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            String oldDocId = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / newDocNGrams.size();

            System.out.println("Found " + matches + " matching n-grams with \""
                    + oldDocId + "\"");
            System.out.printf("Similarity: %.2f%%", similarity);

            if (similarity >= 50) {
                System.out.println(" (PLAGIARISM DETECTED)");
            } else if (similarity >= 10) {
                System.out.println(" (Suspicious)");
            } else {
                System.out.println(" (Safe)");
            }
            if (matches > maxMatch) {
                maxMatch = matches;
                mostSimilarDoc = oldDocId;
            }
        }

        if (!mostSimilarDoc.equals("")) {
            System.out.println("Most similar document: " + mostSimilarDoc);
        } else {
            System.out.println("No matching document found");
        }
    }

    public static void main(String[] args) {

        addDocument("essay_089.txt",
                "Artificial intelligence is changing the world very quickly and many industries are using it");
        addDocument("essay_092.txt",
                "Machine learning is a part of artificial intelligence and it is changing the world very quickly");

        String newEssay = "Artificial intelligence is changing the world very quickly and many industries are using it today";
        analyzeDocument("essay_123.txt", newEssay);
    }
}