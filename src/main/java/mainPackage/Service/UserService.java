package mainPackage.Service;

import mainPackage.Domain.Database;
import mainPackage.Domain.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sunil on 5/19/16.
 */



public class UserService {

        Connection connection;
        PreparedStatement statement;
        String query;
        ResultSet resultSet;

        public UserService(){
            try {
                connection = new Database().getDbConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void storeUser(Users user) throws SQLException {

        String insertUser = "INSERT INTO users (userid, name, email) VALUES (?,?,?)";

        try {
            System.out.println("checking value:" + user.getId());
            System.out.println("checking value:" + user.getName());

            System.out.println("checking value:" + user.getEmail());

            statement = connection.prepareStatement(insertUser);
            statement.setLong(1,user.getId());
            statement.setString(2,user.getName());
            statement.setString(3,user.getEmail());
            statement.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public boolean searchUserId(long id){


           query="select userid from users where userid = ?";
            boolean success=false;
            try {
                statement = connection.prepareStatement(query);

                statement.setLong(1,id);
                resultSet=statement.executeQuery();
                while (resultSet.next()){
                    success=true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        return success;

    }



    public void closeConnection(){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
