/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBMS;

import Utils.Utilities;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class DBFunctions {

    private Database dbcon;

    private void getDBConnection() {
        try {
            this.dbcon = new Database();
        } catch (Exception ex) {
           System.err.println(Utilities.stringifyStackTrace(ex)); 
        }
    }

    public JSONObject GetCustomers(String mobilenumber) {
        System.out.println("GetCustomers Phone number:"+mobilenumber);
     
        JSONObject custDetails = new JSONObject();
        try {
            String sql = "select CA.CustomerID, CA.SaccoID, CA.LoginStatus, CD.CustomerName, CA.CustomerPin from [MYSACCO].[dbo].CustomerAccount as CA inner join [MYSACCO].[dbo].CustomerDetails as CD on CD.CustomerID=CA.CustomerID where CD.PhoneNumber ='" + mobilenumber + "'";
           
            System.out.println("SQL Query:"+sql);
            dbcon = new Database();
            ResultSet resultSet = dbcon.executeStatement(sql);
            while(resultSet.next()) {

                custDetails.put("CustomerID",resultSet.getString("CustomerID")); //registered
                custDetails.put("SaccoID",resultSet.getString("SaccoID"));//menu
                custDetails.put("LoginStatus",resultSet.getString("LoginStatus"));//FTL
                 custDetails.put("CustomerName",resultSet.getString("CustomerName"));//greatings
          }
            
            resultSet.close();
            
        } catch (Exception ex) {
            System.err.println(Utilities.stringifyStackTrace(ex)); 
        } finally {
            try {
                dbcon.closeDatabaseConnection();
            } catch (Exception ex) {
               System.err.println(Utilities.stringifyStackTrace(ex));   
            }
        }
        return custDetails;
    }

    public String GetSaccos() {
        String response = "";
        ResultSet resultSet = null;
        try {
          String sql = "SELECT [SaccoID]  ,[SaccoName]  ,[CreateUserID]     FROM [MYSACCO].[dbo].[SaccoDetails]";

            dbcon = new Database();
            resultSet = dbcon.executeStatement(sql);
            while (resultSet.next()) {
                String col1 = resultSet.getString(1);
                String col2 = resultSet.getString(2);
                String col3 = resultSet.getString(3);
                response += col1 + "|" + col2 + "|" + col3 + "~";

            }

        } catch (Exception ex) {

        } finally {
            try {
                resultSet.close();
                dbcon.closeDatabaseConnection();
            } catch (Exception ex) {

            }
        }
        return response;
    }

    public JSONObject checkOTP(JSONObject receivedData) {
        JSONObject response= new JSONObject();
        String sql ="select count(AccountID)as exist  from [MYSACCO].[dbo].[CustomerAccount] as CA inner join [MYSACCO].[dbo].[CustomerDetails] as CD on CD.CustomerID=CA.CustomerID where CD.PhoneNumber ='"+receivedData.getString("PhoneNumber") +"' and CA.CustomerPin= '"+receivedData.getString("CustomerPin")+"'";
        try {
            dbcon= new Database();
            ResultSet rset = dbcon.executeStatement(sql);
            if (rset.next()){
                response.put("status", rset.getString("exist"));
            }
            rset.close();
            dbcon.closeDatabaseConnection();
        } catch (Exception e) {
        }
        return response;
       
    }

    public JSONObject changePassword(JSONObject request) {
        JSONObject response= new JSONObject();
        String sql ="update  [MYSACCO].[dbo].[CustomerAccount]  set CustomerPin= '"+request.getString("NewCustomerPin")+"', LoginStatus='1'  where (select top 1 PhoneNumber from [MYSACCO].[dbo].[CustomerDetails] where CustomerDetails.CustomerID= CustomerAccount.CustomerID) ='"+request.getString("PhoneNumber")+"' and CustomerPin= '"+request.getString("CustomerPin")+"'";
        try {
            dbcon= new Database();
           int affectedRows = dbcon.execute(sql);
            if (affectedRows>0){
                response.put("status", "1");
            }else{
                 response.put("status", "0");
            }
          
            dbcon.closeDatabaseConnection();
        } catch (Exception e) {
        }
        return response;
    }


}
