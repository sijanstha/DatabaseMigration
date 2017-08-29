package com.datamigration.dbtabledata;

/*
 This Class Fetchs the tableName and tableColumns from Database and
 writes the metadata in file using FileWriter
 Created by: Sijan Shrestha
 Created On: 21 july, 2017
 Modified On: 24 july, 2017
 */
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;

import com.datamigration.dbconnection.DBConnection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;

public class TableColumnMetadata {

    private Connection con = null;
    private String table[] = {"TABLE"};
    private ArrayList<String> tableArray = new ArrayList<String>();

    public boolean getTableMetadata() {
        boolean sts = true;
        try {
            DBConnection dbObj = new DBConnection().getConnection(1);
            if (dbObj.code != 0) {
                System.out.println(dbObj.statusMsg);
                sts = false;
                return sts;
            }
            con = dbObj.dbConnect;

            DatabaseMetaData meta = con.getMetaData();
            ResultSet res = meta.getTables(null, null, "%", table);

            while (res.next()) {
                tableArray.add(res.getString(3));
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
            sts = false;
        }

        return sts;
    }
}
