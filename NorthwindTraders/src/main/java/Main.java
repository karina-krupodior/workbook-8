import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/northwind";
    private static final String username = "root";
    private static final String password = "yearup24";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1) Display all customers");
            System.out.println("3) Display all categories");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");

            String option = scanner.nextLine();

            if ("0".equals(option)) {
                System.out.println("Goodbye!");
                break;
            }

            if ("1".equals(option)) {
                displayAllCustomers();
            } else if ("3".equals(option)) {
                displayCategoriesAndProducts(scanner);
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void displayCategoriesAndProducts(Scanner scanner) {
        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT categoryId, categoryName FROM Categories ORDER BY categoryId")
        ) {
            System.out.println("\nCategories:");
            while (resultSet.next()) {
                System.out.printf("%d - %s%n",
                        resultSet.getInt("categoryId"),
                        resultSet.getString("categoryName"));
            }

            System.out.print("Enter a category ID to view products: ");
            int selectedCategoryId = Integer.parseInt(scanner.nextLine());

            String productQuery = "SELECT productId, productName, unitPrice, unitsInStock " +
                    "FROM Products WHERE categoryId = ? ORDER BY productId";

            try (PreparedStatement ps = connection.prepareStatement(productQuery)) {
                ps.setInt(1, selectedCategoryId);

                try (ResultSet products = ps.executeQuery()) {
                    System.out.println("\nProducts in selected category:");
                    while (products.next()) {
                        System.out.printf("ID: %d | Name: %s | Price: %.2f | Stock: %d%n",
                                products.getInt("productId"),
                                products.getString("productName"),
                                products.getDouble("unitPrice"),
                                products.getInt("unitsInStock"));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving categories or products:");
            e.printStackTrace();
        }
    }

    private static void displayAllCustomers() {
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country");
                ResultSet rs = stmt.executeQuery()
        ) {
            System.out.printf("%-25s %-30s %-15s %-15s %-15s%n",
                    "Contact Name", "Company Name", "City", "Country", "Phone");

            while (rs.next()) {
                System.out.printf("%-25s %-30s %-15s %-15s %-15s%n",
                        rs.getString("ContactName"),
                        rs.getString("CompanyName"),
                        rs.getString("City"),
                        rs.getString("Country"),
                        rs.getString("Phone"));
            }
        } catch (SQLException e) {
            System.err.println("Error querying customers:");
            e.printStackTrace();
        }
    }
}
