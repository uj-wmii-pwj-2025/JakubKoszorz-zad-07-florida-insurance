package uj.wmii.pwj.w7.insurance;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipFile;
import java.util.stream.Collectors;

public class FloridaInsurance {

    public static void main(String[] args) throws IOException {
        String zipPath = "FL_insurance.csv.zip";
        String csvFileName = "FL_insurance.csv";
        List<InsuranceEntry> entries = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile("FL_insurance.csv.zip")) {
            zipFile.stream()
                    .filter(e -> e.getName().equals("FL_insurance.csv"))
                    .findFirst()
                    .ifPresent(entry -> {
                        try (InputStream is = zipFile.getInputStream(entry);
                             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                            String header = br.readLine();
                            String line;
                            while ((line = br.readLine()) != null) {
                                try {
                                    entries.add(InsuranceEntry.fromCsvLine(line));
                                } catch (Exception e) {
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        long count = entries.stream()
                .map(InsuranceEntry::getCounty)
                .distinct()
                .count();
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("count.txt")))) {
            pw.println(Long.toString(count));
        }

        double sumTiv2012 = entries.stream()
                .mapToDouble(InsuranceEntry::getTiv2012)
                .sum();
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("tiv2012.txt")))) {
            pw.printf(Locale.US, "%.2f%n", sumTiv2012);
        }

        Map<String, Double> byCounty = entries.stream()
                .collect(Collectors.groupingBy(
                        InsuranceEntry::getCounty,
                        Collectors.summingDouble(e -> e.getTiv2012() - e.getTiv2011())
                ));

        List<Map.Entry<String, Double>> top10 =
                byCounty.entrySet().stream()
                        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                        .limit(10)
                        .collect(Collectors.toList());

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("most_valuable.txt")))) {
            pw.println("country,value");
            for (Map.Entry<String, Double> entry : top10) {
                pw.printf(Locale.US, "%s,%.2f%n", entry.getKey(), entry.getValue());
            }
        }
    }
}
