import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    // Replace this with your actual API key
    private static final String API_KEY = "YOUR_API_KEY";

    public static double getExchangeRate(String base, String target) {
        try {
            String urlStr = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + base;
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Simple string parsing (unsafe for production but works in limited environments)
            String searchKey = "\"" + target + "\":";
            int index = response.indexOf(searchKey);
            if (index == -1) {
                System.out.println("Currency not found in API response.");
                return -1;
            }
            int startIndex = index + searchKey.length();
            int endIndex = response.indexOf(",", startIndex);
            if (endIndex == -1) endIndex = response.indexOf("}", startIndex);

            String valueStr = response.substring(startIndex, endIndex).trim();
            return Double.parseDouble(valueStr);

        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Supported currencies: USD, INR, EUR, GBP, JPY, AUD, CAD...");
        System.out.print("Enter base currency: ");
        String base = scanner.next().toUpperCase();

        System.out.print("Enter target currency: ");
        String target = scanner.next().toUpperCase();

        System.out.print("Enter amount in " + base + ": ");
        double amount = scanner.nextDouble();

        double rate = getExchangeRate(base, target);
        if (rate != -1) {
            double converted = amount * rate;
            System.out.printf("Exchange rate: 1 %s = %.2f %s\n", base, rate, target);
            System.out.printf("Converted amount: %.2f %s\n", converted, target);
        } else {
            System.out.println("Conversion failed. Try again later.");
        }
    }
}
