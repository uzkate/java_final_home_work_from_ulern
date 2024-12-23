
# Average Global Sales by Platform - Project

## Overview
This project processes data about video game sales to generate a visualization of the average global sales by platform. The data is loaded from a CSV file, and for each platform, we calculate the average global sales. The results are then displayed on a chart.

## Technologies Used
- Java
- XChart (for charting)
- BufferedReader (for reading CSV data)
- Collections (for data processing)

## File Structure
- `Main.java` : The main program to load, process, and visualize the data.
- `Игры.csv` : The CSV file containing video game sales data.
- `SalesAnalysis.java` : A class with methods for additional analysis, including finding the highest sales in Europe in 2000 and the highest sports game sales in Japan between 2000-2006.

## Instructions

1. **Import Data**: The program loads data from a CSV file (`Игры.csv`). Ensure the file is present in the resources folder of the project.

2. **Data Processing**: The program processes the data by grouping it by platform and calculating the average global sales for each platform.

3. **Visualization**: The program generates a bar chart displaying the average global sales per platform, with platforms sorted by sales.

4. **Additional Analysis**: The program also includes two analysis methods:
   - `findHighestSalesInEurope2000`: Finds the game with the highest sales in Europe in the year 2000.
   - `findHighestSportsGameSalesJapan2000To2006`: Finds the highest-selling sports game in Japan from 2000 to 2006.

## Running the Project

1. Compile the Java files:

   ```bash
   javac Main.java SalesAnalysis.java
   ```

2. Run the program:

   ```bash
   java Main
   ```

3. The program will display a bar chart showing the average global sales per platform.

## Requirements

- Java 8 or higher
- XChart library (add XChart dependency to your project)

## Notes
- Ensure that `Игры.csv` contains the necessary data with the correct structure (with at least 11 columns).
- The program assumes the CSV file has a header, which is skipped during processing.

## License
This project is open-source and free to use. Modify or distribute as needed.
