import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/identity_field?useSSL=false", "root", "110992");

            KeyGenerator keyGenerator = new KeyGenerator(conn, "orders", 5);
            keyGenerator.nextKey();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
