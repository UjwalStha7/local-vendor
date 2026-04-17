package com.learninglog.dao;

import com.learninglog.entity.User;
import com.learninglog.util.DatabaseConnection;

import java.awt.dnd.DropTarget;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//implementing userdau which is interference(inherit) from userDao class
public class UserDaoImp implements UserDao {

    @Override
    public boolean insertUser(User user) {
//        Since each user must have unique names so we are validating names and email
        if (findByEmail(user.getEmail())!=null){
            System.out.printf("User Email already Exist : "+user.getEmail());
            return  false;
        }
        if(findByUsername(user.getUsername()) != null){
            System.out.printf("User Name already exist : "+user.getUsername());
            return  false;
        }

//                Using Class DatabaseConnection to connect Db
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql_query = "INSERT INTO users (Username,email,password,phone,role,is_active) Values (?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql_query);
//            1,2,3 mean  the (?,..) positiona and setting value in that positon
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.setString(4,user.getPhone());
            statement.setString(5,user.getRole());
            statement.setBoolean(6,user.isIsactive());

//            Now exicuting query insert
            statement.execute();
            return true;

        }
        catch (SQLException ex){
            System.out.printf("Error Inserting Data into Database : "+ex.getMessage());
            return false;
        }
        finally {
//            Closing Database
            DatabaseConnection.closeConnection(conn);
        }
    }

//    This 2 method retrive the databased on the given parameter and convert into java object
    @Override
    public User findByEmail(String email) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql_query= "Select * from users where lower(email) = lower(?)";
            PreparedStatement statement = conn.prepareStatement(sql_query);
            statement.setString(1,email);
//            ResultSet is an object that holds the data returned from a SQL query. and return datya when exicute
            ResultSet res = statement.executeQuery();

//            Error-1 Differnt Name in constructor and Db may cause confusion and error
            if (res.next()) {
                return new User(res.getInt("id"),
                        res.getString("username"),
                        res.getString("email"),
                        res.getString("password"),
                        res.getString("phone"),
                        res.getString("role"),
                        res.getBoolean("is_active"),
                        res.getTimestamp("createdAt"),
                        res.getTimestamp("UpdatedAt")
                );
            }


        } catch (SQLException ex) {
            System.out.printf("Error Retrivind data from Database using (Email) : " + ex.getMessage());
//            return false;
        }
        finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql_query = "Select  * from users where lower(username) = lower(?)";
            PreparedStatement statement = conn.prepareStatement(sql_query);

            ResultSet res = statement.executeQuery();

            if (res.next()) {
                return new User(res.getInt("id"),
                        res.getString("username"),
                        res.getString("email"),
                        res.getString("password"),
                        res.getString("phone"),
                        res.getString("role"),
                        res.getBoolean("is_active"),
                        res.getTimestamp("createdAt"),
                        res.getTimestamp("UpdatedAt")
                );
            }

        }
        catch (SQLException ex){
            System.out.printf("Error Retriving data from Database using (Name) : "+ex.getMessage());
        }
        finally {
         DatabaseConnection.closeConnection(conn);
        }
        return null;  //null if data doesnt found
    }
}
