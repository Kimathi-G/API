/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loan;

import DBMS.DBFunctions;
import org.json.JSONObject;

/**
 *
 * @author User
 */

public class Customer {
    DBFunctions dbFunctions = new DBFunctions();
    public JSONObject verifyUser(JSONObject receivedData){ // if unregistered, first time or regular customer
    JSONObject customer = new JSONObject();
  //  System.out.println("Received Phone number:"+receivedData.getString("PhoneNumber"));
    customer = dbFunctions.GetCustomers(receivedData.getString("PhoneNumber"));
   
    
    return customer;
    }
    
    //first time customer //from ussd use OTP
    //enter OTP if login status is 0
    
    public JSONObject Login(JSONObject receivedData){ // if unregistered, first time or regular customer
    JSONObject customer = new JSONObject();
    customer = dbFunctions.checkOTP(receivedData);
   
    
    return customer;
    }

    public JSONObject ChangePassword(JSONObject request) {
    JSONObject customer = new JSONObject();
    customer = dbFunctions.changePassword(request);   
    
    return customer;
    }

   
    
}
