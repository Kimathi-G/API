/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DBMS.DBFunctions;
import Loan.SMSEngine;
import javax.json.Json;
import javax.servlet.http.HttpServlet;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class Processor {

    private DBFunctions func;

    public JSONObject processRequests(JSONObject request) {

        JSONObject res = new JSONObject();
        if (request.isNull("menu")) {
         res.put("Message", "Invalid Request1");
            return res;
        }
        
        String menu = request.getString("menu");

        DBFunctions db = new DBFunctions();
        switch (menu) {

            //1-10 login
            case "1": // verify customer

                res = db.GetCustomers(request);

                break;
            case "2": // login
                res = db.checkOTP(request); // if 1 means OTP correct
                   /* menu
                 loan score satus
                 borrow loan
                 repayloan
                 check balance*/
                break;
            case "3": // OTP and old password
                res = db.changePassword(request); // if 1 means change successful 

                break;

            //loan
            case "11":
                res = db.GetLoanScore(request); // if 1 means change successful 

                break;

            //Borrow loan
            case "12":
                res = db.BorrowLoan(request); // if 1 means change successful 

                break;

            // loan balance
            case "13":
                res = db.LoanBalance(request); // 

                break;
            // loan Repayment
            case "14":
                res = db.LoanRepayment(request); // if 1 means  successful 

                break;
            // loan statements
            case "15":
                res = db.Statements(request); // if 1 means change successful 

                break;

            case "21":
                func = new DBFunctions();
                // loan balance
                String saccos = func.GetSaccos();
                res.put("saccos", saccos);

                break;

            default:
                // default statements
                res.put("Message", "Invalid Request");
        }
        return res;
    }

}
