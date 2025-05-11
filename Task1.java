package project2;

import org.apache.commons.csv.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Task1 {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("VAERS_COVID_DataDecember2024.csv", StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            Iterator<CSVRecord> iterator = csvParser.iterator();

            System.out.println("Starting CSV parsing...");

            BPlusTree tree = new BPlusTree(3); // Single tree
            Map<String, Record> allRecords = new HashMap<>();

            int index = 0;
            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();
                String vaersId = record.get("VAERS_ID").trim();

                Record rec = new Record(
                        vaersId,
                        record.get("RECVDATE"),
                        record.get("STATE"),
                        record.get("AGE_YRS"),
                        record.get("SEX"),
                        record.get("DIED"),
                        record.get("VAX_TYPE"),
                        record.get("VAX_NAME"),
                        record.get("SYMPTOM1"),
                        record.get("SYMPTOM2")
                );

                allRecords.put(vaersId, rec);
                tree.insert(vaersId, Integer.parseInt(vaersId));

                index++;
                if (index % 10000 == 0) {
                    System.out.println("  Processed " + index + " records");
                }
            }

            // Save to disk (single files)
            System.out.println("Serializing full BPlusTree...");
            try (ObjectOutputStream treeOut = new ObjectOutputStream(new FileOutputStream("tree_full.dat"))) {
                treeOut.writeObject(tree);
            }

            System.out.println("Serializing all records...");
            try (ObjectOutputStream recordOut = new ObjectOutputStream(new FileOutputStream("records_full.dat"))) {
                recordOut.writeObject(allRecords);
            }


            System.out.println("All data processed and saved to disk.");

            // Search logic
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter VAERS_ID to search: ");
            String searchKey = sc.nextLine();

            try (ObjectInputStream treeIn = new ObjectInputStream(new FileInputStream("tree_full.dat"))) {
                BPlusTree loadedTree = (BPlusTree) treeIn.readObject();
                Integer key = loadedTree.search(searchKey);
                if (key != null) {
                    try (ObjectInputStream recordIn = new ObjectInputStream(new FileInputStream("records_full.dat"))) {
                        Map<String, Record> loadedRecords = (Map<String, Record>) recordIn.readObject();
                        System.out.println("Found record:");
                        System.out.println(loadedRecords.get(searchKey));
                    }
                } else {
                    System.out.println("Record not found.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
