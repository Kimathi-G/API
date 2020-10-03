/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBMS;

import Utils.Utilities;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author User
 */
public class Database {
        private DataSource dataSource;
    private InitialContext initialContext;

    private java.sql.Connection conn = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private Statement statement = null;
    private String _sqlString;
    
    public Database(){    
      try {
            initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:jboss/datasource/MYSACCO");
            conn = dataSource.getConnection();
        } catch (NamingException ex) {
             System.err.println(Utilities.stringifyStackTrace(ex));
        } catch (SQLException ex) {
             System.err.println(Utilities.stringifyStackTrace(ex));
        }
      
      
    }
    
      public java.sql.Connection getDatabaseConnection() throws Exception {
        return this.conn;
    }

    /**
     * Release all database locks to allow for creation of a new connection
     *
     * @throws Exception - Thrown if the connection cannot be released, This
     * exception must be recoverable
     */
    public void closeDatabaseConnection() throws Exception {
        if (statement != null) {
            statement.close();
        }
        if (resultSet != null) {
            resultSet.close();
        }
        if (conn != null) {
            this.conn.close();
        }
        dataSource = null;
        if (initialContext != null) {
            initialContext.close();
        }
    }
    
     /**
     * Execute statement
     * <p>
     * This executes an SQL query and returns a resultSet of data after a select
     *
     * @param _sqlString the sql string
     * @return the result set
     */
    public ResultSet executeStatement(String _sqlString) {
        try {
            conn = getDatabaseConnection();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(_sqlString);
        } catch (SQLException ex) {
             System.err.println(Utilities.stringifyStackTrace(ex));
        } catch (Exception ex) {
             System.err.println(Utilities.stringifyStackTrace(ex));
        }

        return resultSet;
    }

    /**
     * Execute statements eg. DELETE, UPDATE.
     *
     * @param _sqlString the sql string
     * @return the int
     */
    public int execute(String _sqlString) {
        int affectedRows = 0;
      
        try {
            conn = getDatabaseConnection();
            statement = conn.createStatement();
            affectedRows = statement.executeUpdate(_sqlString);

        } catch (Exception ex) {
             System.err.println(Utilities.stringifyStackTrace(ex));
        }
        return affectedRows;
    }

    /**
     * Sets sql string.
     * <p>
     * This will pass the sql query string to prepared statement
     *
     * @param _sqlString the sql string
     */
    public void set_sqlString(String _sqlString) {
        this._sqlString = _sqlString;
    }
    
    
    
}
