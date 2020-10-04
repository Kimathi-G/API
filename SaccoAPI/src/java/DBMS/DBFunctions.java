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

    public JSONObject GetCustomers(JSONObject receivedData) {
        System.out.println("GetCustomers Phone number:" + receivedData.getString("PhoneNumber"));

        JSONObject custDetails = new JSONObject();
        try {
            String sql = "select CA.CustomerID, CA.SaccoID, CA.LoginStatus, CD.CustomerName, CA.CustomerPin from [MYSACCO].[dbo].CustomerAccount as CA inner join [MYSACCO].[dbo].CustomerDetails as CD on CD.CustomerID=CA.CustomerID where CD.PhoneNumber ='" + receivedData.getString("PhoneNumber") + "'";

            System.out.println("SQL Query:" + sql);
            dbcon = new Database();
            ResultSet resultSet = dbcon.executeStatement(sql);
            while (resultSet.next()) {

                custDetails.put("CustomerID", resultSet.getString("CustomerID")); //registered
                custDetails.put("SaccoID", resultSet.getString("SaccoID"));//menu
                custDetails.put("LoginStatus", resultSet.getString("LoginStatus"));//FTL
                custDetails.put("CustomerName", resultSet.getString("CustomerName"));//greatings
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
        JSONObject response = new JSONObject();
        String sql = "select count(AccountID)as exist  from [MYSACCO].[dbo].[CustomerAccount] as CA inner join [MYSACCO].[dbo].[CustomerDetails] as CD on CD.CustomerID=CA.CustomerID where CD.PhoneNumber ='" + receivedData.getString("PhoneNumber") + "' and CA.CustomerPin= '" + receivedData.getString("CustomerPin") + "'";
        try {
            dbcon = new Database();
            ResultSet rset = dbcon.executeStatement(sql);
            if (rset.next()) {
                response.put("status", rset.getString("exist"));
            }
            rset.close();
            dbcon.closeDatabaseConnection();
        } catch (Exception e) {
        }
        return response;

    }

    public JSONObject changePassword(JSONObject request) {
        JSONObject response = new JSONObject();
        String sql = "update  [MYSACCO].[dbo].[CustomerAccount]  set CustomerPin= '"
                + request.getString("NewCustomerPin")
                + "', LoginStatus='1'  where CustomerID = "
                + request.getString("CustomerID") + " and CustomerPin= '"
                + request.getString("CustomerPin") + "'";
        dbcon = new Database();
        try {
            if (dbcon.updateRecords(sql)) {
                response.put("status", "1");
            } else {
                response.put("status", "0");
            }
        } catch (Exception e) {
        }
        return response;
    }

    public JSONObject GetLoanScore(JSONObject request) {
        JSONObject response = new JSONObject();
        String sql = "select  LoanScore from [MYSACCO].[dbo].[CustomerAccount]  where CustomerID ='" + request.getString("CustomerID") + "'";
        try {
            dbcon = new Database();
            ResultSet res = dbcon.executeStatement(sql);
            if (res.next()) {
                response.put("LoanScore", res.getString("LoanScore"));
            }
            dbcon.closeDatabaseConnection();
        } catch (Exception e) {
        }
        return response;
    }

    public JSONObject BorrowLoan(JSONObject request) {

        JSONObject response = new JSONObject();
        String transid = Utilities.GenarateTransactionID();
        String sql1 = "Insert into [MYSACCO].[dbo].CustomerLoan (LoanAmount, CustomerID,LoanID,SaccoID,LoanInterest, LoanRepaymentTime,LoanAccount, OutstandingAmount) VALUES ("
                + request.getString("LoanAmount") + " , "
                + request.getString("CustomerID") + ","
                + request.getString("LoanID") + ","
                + request.getString("SaccoID") + ","
                + request.getString("LoanInterest") + ", '"
                + request.getString("LoanRepaymentTime") + "', '"
                + transid + "',"
                + request.getString("LoanAmount") + ");";

        String sql2 = "update [MYSACCO].[dbo].CustomerAccount set LoanStatus =1 where CustomerID = " + request.getString("CustomerID");
        String sql3 = "delete from [MYSACCO].[dbo].CustomerLoan where  LoanAccount='" + transid + "' AND CustomerID = '" + request.getString("CustomerID") + "'";
        try {
            dbcon = new Database();
            int affectedRows = dbcon.execute(sql1);
            if (affectedRows > 0) {
                int affectedRows2 = dbcon.execute(sql2);
                if (affectedRows2 > 0) {
                    response.put("status", "1");
                } else {

                    int affectedRows3 = dbcon.execute(sql3);  //delete usse customerID and loanID from request
                    if (affectedRows3 < 0) {
                        response.put("status", "-2");
                    }
                    response.put("status", "-1");
                }
            } else {
                response.put("status", "0");
            }
            dbcon.closeDatabaseConnection();

        } catch (Exception e) {
        }

        return response;
    }

    public JSONObject LoanBalance(JSONObject request) {
        JSONObject response = new JSONObject();
        String sql = "select OutstandingAmount from CustomerLoan where CustomerID ='"
                + request.getString("CustomerID") + "' and LoanAccount='" + request.getString("LoanAccount") + "'";
        try {
            dbcon = new Database();
            ResultSet res = dbcon.executeStatement(sql);
            if (res.next()) {
                response.put("LoanBalance", res.getString("OutstandingAmount"));
            }
            dbcon.closeDatabaseConnection();
        } catch (Exception e) {
        }
        return response;
    }

    public JSONObject LoanRepayment(JSONObject request) {
        JSONObject response = new JSONObject();
        String transid = Utilities.GenarateTransactionID();
        double repaid = Double.parseDouble(request.getString("RepaidAmount"));
        String sql1 = "update CustomerLoan set RepaidAmount = RepaidAmount+" + repaid + ", OutstandingAmount= OutstandingAmount -" + repaid + "  where LoanAccount= '" + request.getString("LoanAccount") + "';";

        dbcon = new Database();
        try {
            if (dbcon.updateRecords(sql1)) {
                response.put("status", "1");
            } else {
                response.put("status", "0");
            }
        } catch (Exception e) {
        }

        return response;
    }

    public JSONObject Statements(JSONObject request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
