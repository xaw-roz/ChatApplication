import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Suryaraj on 5/18/2016.
 */
public class Database {
    java.sql.Statement stmt;
    Connection conn;

    private Connection dbConnection;

    public Database() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost:3306/chat_app";
        String user = "root";
        String password = "";
        dbConnection = DriverManager.getConnection(url, user, password);

        stmt = conn.createStatement();

        String sql = "INSERT INTO  chat " +
                "VALUES ('100', '101', '102')";
        stmt.executeUpdate(sql);
        String sql1 = "INSERT INTO user " +
                "VALUES (101, 'surya, 'surya@gmail.com')";
        stmt.executeUpdate(sql1);





    }

    public Connection getDbConnection() {
        return dbConnection;
    }
}


