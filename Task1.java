package project2;

import org.apache.commons.csv.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Task1 {

    public static void main(String[] args) {
        String csvFilePath = "VAERS_COVID_DataDecember2024.csv";
        int degree = 3;
        String treeDir = "/Users/sushoonleikhaing/Desktop/365/Covid19/src/project2/tree";

        try {
            System.out.println("Step 1: Building in-memory B+ tree with IDs...");

            BufferedReader reader1 = new BufferedReader(new FileReader(csvFilePath, StandardCharsets.UTF_8));
            CSVParser parser1 = new CSVParser(reader1, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            BPlusTree tree = new BPlusTree(degree);
            int recordIndex = 0;

            for (CSVRecord record : parser1) {
                String vaersId = record.get("VAERS_ID").trim();
                tree.insert(vaersId, recordIndex++);
            }

            parser1.close();
            System.out.println("Indexed " + recordIndex + " records.");

            // Step 2: Write full records to disk using B+ Tree routing
            System.out.println("Step 2: Writing full records to folder structure...");

            BufferedReader reader2 = new BufferedReader(new FileReader(csvFilePath, StandardCharsets.UTF_8));
            CSVParser parser2 = new CSVParser(reader2, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord record : parser2) {
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

                // Use the updated path generation method
                String folderPath = DiskTreePathBuilder.getPath(tree, vaersId);
                File dir = new File(treeDir + "/" + folderPath);
                dir.mkdirs();

                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(dir, vaersId + ".dat")))) {
                    out.writeObject(rec);
                }
            }

            parser2.close();
            System.out.println("Finished writing all records.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
