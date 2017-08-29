/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datamigration.main;

import com.datamigration.dbconnection.DBConnection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
 
   
    private Connection con = null;
    private String table[] = {"TABLE"};
    private ArrayList<String> tableArray = new ArrayList<String>();

    public void getTableMetadata() {
        
        try {
            DBConnection dbObj = new DBConnection().getConnection(1);
            if (dbObj.code != 0) {
                System.out.println(dbObj.statusMsg);
           
            }
            con = dbObj.dbConnect;

            DatabaseMetaData meta = con.getMetaData();
            ResultSet res = meta.getTables(null, null, "tbl%", table);

            while (res.next()) {
                tableArray.add(res.getString(3));
                System.out.println(res.getString(3));
            }
            res.close();

            File file = new File("table_meta_data.txt");
            if (file.exists()) {
                file.delete();
            }
             BufferedWriter fw = new BufferedWriter(new FileWriter(file));
             
            for (int i = 0; i < tableArray.size(); i++) {
              //  FileWriter fw = new FileWriter(file, true); // to append ",true"
                String table_name = tableArray.get(i);
                fw.write(table_name);
                res = meta.getColumns(null, null, table_name, null);
                while (res.next()) {
                    fw.append("%" + res.getString("COLUMN_NAME"));
                }
                fw.append("\n");
            }
            fw.close();
            System.out.println("Tables Metadata Written Successfully");
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        
        }
    }
    
    public static void main(String args[]){
        new Test().getTableMetadata();
    }
}

