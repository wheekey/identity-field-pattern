import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KeyGenerator {

    private Connection conn;
    private String keyName;
    private long nextId;
    private long maxId;
    private int incrementBy;

    public KeyGenerator(Connection conn, String keyName, int incrementBy) {
        this.conn = conn;
        this.keyName = keyName;
        this.incrementBy = incrementBy;
        nextId = maxId = 0;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException exc) {
            System.out.println("Unable to turn off autocommit");
        }
    }

    public synchronized Long nextKey() {
        if (nextId == maxId) {
            reserveIds();
        }
        return new Long(nextId++);
    }

    private void reserveIds() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long newNextId = 0;
        try {
            stmt = conn.prepareStatement("SELECT nextId FROM `keyss` WHERE `name` = ?");
            stmt.setString(1, keyName);
            rs = stmt.executeQuery();
            rs.next();
            newNextId = rs.getLong(1);
        } catch (SQLException exc) {
            exc.printStackTrace();
            System.out.println("Unable to generate ids");
        } finally {
            //DB.cleanUp(stmt, rs);
        }
        long newMaxId = newNextId + incrementBy;
        stmt = null;
        try {
            stmt = conn.prepareStatement("UPDATE keys SET nextId = ? WHERE name = ?");
            stmt.setLong(1, newMaxId);
            stmt.setString(2, keyName);
            stmt.executeUpdate();
            conn.commit();
            nextId = newNextId;
            maxId = newMaxId;
        } catch (SQLException exc) {
            System.out.println("Unable to generate ids");
        } finally {
            //DB.cleanUp(stmt);
        }
    }
}
