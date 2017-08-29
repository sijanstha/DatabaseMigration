
/*
 This Class Fetchs the table details and dumps the table contents into .sql file
 Created by: Sijan Shrestha
 Created On: 23 july, 2017
 Modified On: 24 july, 2017
 */
package com.datamigration.dbtabledata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import com.datamigration.dbconnection.DBConnection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TableDataExtractor extends Thread {

    private Connection con = null;
    private String table[] = {"TABLE"};
    private ArrayList<String> ar = new ArrayList<String>();

    public boolean databaseMigrator() {

        try {
            DBConnection dbObj = new DBConnection().getConnection(1);
            if (dbObj.code != 0) {
                System.out.println(dbObj.statusMsg);
                return false;
            }
            con = dbObj.dbConnect;
            String dbName = con.getCatalog();

            //opening file for dumping database records
            File file = new File("dump.sql");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileout = new FileOutputStream(file);
            OutputStreamWriter outsw = new OutputStreamWriter(fileout, "UTF-8");
            BufferedWriter fw = new BufferedWriter(outsw);
            DatabaseMetaData meta = con.getMetaData();
            ResultSet res = meta.getTables(null, null, null, table);

            while (res.next()) {
                ar.add(res.getString(3));	//3 TOGET Table NAME 
            }
            res.close();

            //writing table columns in file
            fw.write("/* Project By Team: \n Sijan Shrestha \n Divash Adhikari \n Sudeep Bhandari \n BEIT(VI)\n\n Dumping Tables From Database\n*/\n\n");
            fw.append("DROP DATABASE IF EXISTS " + dbName + ";\n");
            fw.append("CREATE DATABASE " + dbName + ";\n");
            fw.append("USE " + dbName + ";\n\n");

            for (int i = 0; i < ar.size(); i++) {
                String tableColumnsMeta = "";
                fw.append("DROP TABLE IF EXISTS " + ar.get(i) + ";\n");
                fw.append("CREATE TABLE " + ar.get(i) + "( ");
                res = meta.getColumns(null, null, ar.get(i), null);

                //fetching columns type,name,size
                while (res.next()) {
                    tableColumnsMeta += res.getString("COLUMN_NAME") + " ";
                    if (res.getString("TYPE_NAME").equalsIgnoreCase("TIMESTAMP")) {
                        tableColumnsMeta += "DATETIME";
                    } else {
                        tableColumnsMeta += res.getString("TYPE_NAME");
                    }
                    if (res.getString("TYPE_NAME").equalsIgnoreCase("TIMESTAMP")) {
                        tableColumnsMeta += "(5),";
                    } else if (res.getString("TYPE_NAME").equalsIgnoreCase("DOUBLE")) {
                        tableColumnsMeta += "(12,2),";
                    } else if (res.getString("TYPE_NAME").equalsIgnoreCase("BOOLEAN")) {
                        tableColumnsMeta += ",";
                    } else {
                        tableColumnsMeta += "(" + res.getInt("COLUMN_SIZE") + ")";
                        if (res.getString("IS_AUTOINCREMENT").equals("YES")) {
                            tableColumnsMeta += " AUTO_INCREMENT, ";
                        } else {
                            tableColumnsMeta += ",";
                        }
                    }
                }

                //fetching primary keys from table
                ResultSet primaryKeys = meta.getPrimaryKeys(null, null, ar.get(i));
                if (primaryKeys.next()) {
                    fw.append(tableColumnsMeta);
                    fw.append(" PRIMARY KEY(" + primaryKeys.getString("COLUMN_NAME") + ")");
                }else{
                    fw.append(tableColumnsMeta.substring(0, tableColumnsMeta.length() - 1));
                }
                fw.append(");");
                fw.append("\n\n");

                con.setAutoCommit(false);
                //for insert statement
                String selectSql = "Select * FROM " + ar.get(i);
                Statement rst = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                        java.sql.ResultSet.CONCUR_READ_ONLY);
                rst.setFetchSize(500);
                ResultSet rs = rst.executeQuery(selectSql);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                // String columnData = "";
                String columnFields = new TableColumnMetaExtractor().getColumnMetaData(ar.get(i));

                while (rs.next()) {
                    fw.append("INSERT\tINTO\t" + ar.get(i) + "\t( " + columnFields + " ) " + " VALUES ");
                    fw.append("(\t");
                    for (int j = 1; j <= columnsNumber; j++) {
                        if (rs.getString(j) == null || rs.getString(j) == "") {
                            if (j < columnsNumber) {
                                fw.append(" " + null + ", ");
                            } else if (j == columnsNumber) {
                                fw.append(" " + null);
                            }

                        } else if (rs.getString(j).contains("'")) {
                            int index = rs.getString(j).indexOf("'");
                            if (j < columnsNumber) {
                                fw.append(" '" + rs.getString(j).substring(0, index) + "\\" + rs.getString(j).substring(index) + "', ");
                            } else if (j == columnsNumber) {
                                fw.append(" '" + rs.getString(j).substring(0, index) + "\\" + rs.getString(j).substring(index) + "'");
                            }

                        } 
                         else if (rsmd.getColumnTypeName(j).equalsIgnoreCase("varchar") || rsmd.getColumnTypeName(j).equalsIgnoreCase("text") || rsmd.getColumnTypeName(j).equalsIgnoreCase("timestamp")) {
                            if (j < columnsNumber) {
                                fw.append(" '" + rs.getString(j) + "', ");
                            } else if (j == columnsNumber) {
                                fw.append(" '" + rs.getString(j) + "'");
                            }
                        } else {
                            if (j < columnsNumber) {
                                fw.append(rs.getString(j) + ",");
                            } else if (j == columnsNumber) {
                                fw.append(rs.getString(j));
                            }
                        }
                    }
                    fw.append("\t);\n");
                }
                fw.append("\n");
                //  fw.append(columnData);
                fw.append("\n");
                fw.append("\n\n");
                rst.close();
            }

            fw.flush();
            fw.close();
            con.close();
            System.out.println("Data Written SuccessFul");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public void run() {
        boolean b = databaseMigrator();
        if (b) {
            new SQLScriptRunner().start();
        }
    }
}
