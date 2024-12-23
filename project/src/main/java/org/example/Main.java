import java.io.*;
import java.util.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

public class Main {
    public static void main(String[] args) {
        try {
            // Load data from CSV file in resources
            List<String[]> data = loadCSV("Игры.csv");

            // Group data by platform and calculate average global sales
            Map<String, Double> platformSales = new HashMap<>();
            Map<String, Integer> platformCounts = new HashMap<>();

            for (int i = 1; i < data.size(); i++) { // Skip header row
                String[] row = data.get(i);

                // Check if the row has enough columns
                if (row.length > 10) {
                    String platform = row[2]; // Platform column
                    double globalSales = row[10].isEmpty() ? 0.0 : Double.parseDouble(row[10]); // Global_Sales column

                    if (globalSales > 0) { // Only include games with sales > 0
                        platformSales.put(platform, platformSales.getOrDefault(platform, 0.0) + globalSales);
                        platformCounts.put(platform, platformCounts.getOrDefault(platform, 0) + 1);
                    }
                }
            }

            // Calculate average sales per platform
            List<String> platforms = new ArrayList<>(platformSales.keySet());
            List<Double> averageSales = new ArrayList<>();

            for (String platform : platforms) {
                averageSales.add(platformSales.get(platform) / platformCounts.get(platform));
            }

            // Sort platforms by average sales and select top 28 for better visualization
            List<String> sortedPlatforms = new ArrayList<>();
            List<Double> sortedAverageSales = new ArrayList<>();

            platforms.stream()
                    .sorted(Comparator.comparingDouble(platform -> -platformSales.get(platform) / platformCounts.get(platform)))
                    .limit(28)
                    .forEach(platform -> {
                        sortedPlatforms.add(platform);
                        sortedAverageSales.add(platformSales.get(platform) / platformCounts.get(platform));
                    });

            // Create a chart for average global sales by platform
            CategoryChart chart = new CategoryChartBuilder()
                    .width(1200)
                    .height(600)
                    .title("Top 28 Average Global Sales by Platform")
                    .xAxisTitle("Platform")
                    .yAxisTitle("Average Global Sales (in millions)")
                    .build();

            // Customize chart for better appearance
            chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
            chart.getStyler().setHasAnnotations(false); // Disable annotations
            chart.getStyler().setXAxisLabelRotation(45);
            chart.getStyler().setPlotGridVerticalLinesVisible(false);
            chart.getStyler().setChartFontColor(new java.awt.Color(50, 50, 50));
            chart.getStyler().setChartBackgroundColor(java.awt.Color.WHITE);
            chart.getStyler().setAxisTickLabelsFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));

            // Add data
            chart.addSeries("Average Sales", sortedPlatforms, sortedAverageSales);

            // Show the chart
            new SwingWrapper<>(chart).displayChart();

            // Additional outputs from separate analysis class
            SalesAnalysis analysis = new SalesAnalysis();
            analysis.findHighestSalesInEurope2000(data);
            analysis.findHighestSportsGameSalesJapan2000To2006(data);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static List<String[]> loadCSV(String resourcePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("Файл не найден: " + resourcePath);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    data.add(line.split(","));
                }
            }
        }
        return data;
    }
}

class SalesAnalysis {

    public void findHighestSalesInEurope2000(List<String[]> data) {
        String highestSalesGame = "";
        double highestSales = 0.0;

        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length > 10) {
                int year = parseInt(row[3]);
                double euSales = parseDouble(row[7]);

                if (year == 2000 && euSales > highestSales) {
                    highestSales = euSales;
                    highestSalesGame = row[1];
                }
            }
        }

        System.out.println("Game with highest sales in Europe in 2000: " + highestSalesGame);
    }

    public void findHighestSportsGameSalesJapan2000To2006(List<String[]> data) {
        String highestSalesGame = "";
        double highestSales = 0.0;

        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length > 10) {
                int year = parseInt(row[3]);
                String genre = row[4];
                double jpSales = parseDouble(row[8]);

                if (year >= 2000 && year <= 2006 && genre.equalsIgnoreCase("Sports") && jpSales > highestSales) {
                    highestSales = jpSales;
                    highestSalesGame = row[1];
                }
            }
        }

        System.out.println("Highest sports game sales in Japan (2000-2006): " + highestSalesGame);
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
