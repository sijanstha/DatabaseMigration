/*
flag = 1 for MS-Access Database
flag = 0 for MySql Database

Created by : Sudeep Bhandari
Created On: 22 july 2017
Modified by: Sijan Shrestha
Modifeid On: 23 july 2017
 */
package com.datamigration.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public Connection dbConnect = null;
    public int code = 0;
    public String statusMsg = null;

    public DBConnection getConnection(int flag) {

        DBConnection dbcon = new DBConnection();

        if (flag == 1) {
            try {
                dbcon.dbConnect = DriverManager.getConnection("jdbc:ucanaccess://db_hospital.mdb;password=1234;memory=false");
                dbcon.code = 0;
                dbcon.statusMsg = "MS-Access Database Connected";
            } catch (SQLException ex) {
                dbcon.code = 101;
                dbcon.statusMsg = ex.getMessage();
            }
        } else if (flag == 0) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                dbcon.dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
                dbcon.code = 0;
                dbcon.statusMsg = "Mysql Database Connected";
            } catch (Exception ex) {
                dbcon.code = 101;
                dbcon.statusMsg = ex.toString();
            }

        } else {
            dbcon.code = 102;
            dbcon.statusMsg = "No Valid Flag Value";
        }

        return dbcon;
    }
}
