// DealerManager - handles loading and sorting dealers

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DealerManager {

    public static List<Dealer> loadDealers(String path) {
        List<Dealer> list = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine())!= null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                line = line.replace("|", ",");
                line = line.replace(";", ",");

                String[] parts = line.split(",");

                if (parts.length < 4) {
                    continue;
                }

                try {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    String phone = parts[2].trim();
                    String location = parts[3].trim();

                    if (phone.isEmpty()) {
                        phone = "Not Provided";
                    }
                    if (location.isEmpty()) {
                        location = "Unknown";
                    }

                    Dealer d = new Dealer(code, name, phone, location);
                    list.add(d);

                } catch (Exception e) {
                    System.out.println("Skip bad dealer line: " + line);
                }
            }
            br.close();

        } catch (Exception e) {
        }

        return list;
    }

    // Random select 4 unique dealers without duplicates - manual check
    public static List<Dealer> getRandomFour(List<Dealer> allDealers) {
        List<Dealer> result = new ArrayList<>();
        Random rand = new Random();

        if (allDealers.size() <= 4) {
            for (int i = 0; i < allDealers.size(); i++) {
                result.add(allDealers.get(i));
            }
            return result;
        }

        while (result.size() < 4) {
            int index = rand.nextInt(allDealers.size());
            Dealer picked = allDealers.get(index);

            boolean alreadyExists = false;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).getCode().equals(picked.getCode())) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                result.add(picked);
            }
        }

        return result;
    }

// Uses bubble sort for location sorting - manual implementation
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                String loc1 = list.get(j).getLocation().toLowerCase();
                String loc2 = list.get(j + 1).getLocation().toLowerCase();

                if (loc1.compareTo(loc2) > 0) {
                    Dealer temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}