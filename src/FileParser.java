import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public static List<SparePart> loadInventory(String path) {
        List<SparePart> list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine())!= null) {
                // skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // This file is messy:, | ; mixed
                // So replace everything withh comma
                line = line.replace("|", ",");
                line = line.replace(";", ",");

                // Now split by coma
                String[] parts = line.split(",");

                // Need at least 7 parts to r
                if (parts.length < 7) {
                    continue;
                }

                try {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    String brand = parts[2].trim();

                    // Price like "Rs. 4500.00" or "1250"
                    String priceText = parts[3].trim();
                    priceText = priceText.replace("Rs.", "");
                    priceText = priceText.replace("Rs", "");
                    priceText = priceText.trim();
                    double price = Double.parseDouble(priceText);

                    // Quantity
                    int qty = Integer.parseInt(parts[4].trim());

                    String category = parts[5].trim();
                    String date = parts[6].trim();

                    // If brand is empty, set to Unknown
                    if (brand.isEmpty()) {
                        brand = "Unknown";
                    }

                    // Make SparePart object
                    SparePart sp = new SparePart(code, name, brand, price, qty, category, date);
                    list.add(sp);

                } catch (Exception e) {
                    // If one line is bad, skip it
                    System.out.println("Skip bad line: " + line);
                }
            }
            br.close();

        } catch (Exception e) {
            // System.out.println("Cannot open: " + path);
        }

        return list;
    }
}