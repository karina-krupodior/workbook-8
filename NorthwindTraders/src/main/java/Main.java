import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/northwind";
        String username = "root";
        String password = "yearup24";

        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery("SELECT productId, productName, unitPrice, unitsInStock FROM products")
        ) {
            System.out.println("Connected to the database!");
            System.out.printf("%-5s %-30s %-10s %-10s%n", "Id", "Name", "Price", "Stock");
            System.out.println("------------------------------------------------------------");

            while (results.next()) {
                int id = results.getInt("productId");
                String name = results.getString("productName");
                double price = results.getDouble("unitPrice");
                int stock = results.getInt("unitsInStock");

                // Nicely formatted row
                System.out.printf("%-5d %-30s %-10.2f %-10d%n", id, name, price, stock);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
