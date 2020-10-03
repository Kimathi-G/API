/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import DBMS.DBFunctions;
import Loan.Customer;
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

        String menu = request.getString("menu");
        Customer cus = new Customer();
        switch (menu) {

            //1-10 login
            case "1": // verify customer

                res = cus.verifyUser(request);

                break;
            case "2": // OTP
                res = cus.Login(request); // if 1 means OTP correct

                break;
            case "3": // OTP and old password
                res = cus.ChangePassword(request); // if 1 means change successful

                break;
              

            case "11":
                func = new DBFunctions();
                // loan balance
                String saccos = func.GetSaccos();
                res.put("saccos", saccos);

                break;

            default:
            // default statements
            }
        return res;
    }

}
