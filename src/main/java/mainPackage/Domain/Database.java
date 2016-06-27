package mainPackage.Domain;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Suryaraj on 5/18/2016.
 */
public class Database {

    private Connection dbConnection;

    public Database() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost:3306/chat_app";
        String user = "root";
        String password = "";
        dbConnection = DriverManager.getConnection(url, user, password);
    }

    public Connection getDbConnection() {
        return dbConnection;
    }
}


