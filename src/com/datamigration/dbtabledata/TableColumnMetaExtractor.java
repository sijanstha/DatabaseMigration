/*
 */
package com.datamigration.dbtabledata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TableColumnMetaExtractor {

    private String columnNames = "";
    private String fileName = "table_meta_data.txt";

    public String getColumnMetaData(String tableName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[0].equalsIgnoreCase(tableName)) {

                        if (i < tokens.length - 1) {
                            columnNames += tokens[i] + " , ";
                        } else if (i == tokens.length - 1) {
                            columnNames += tokens[i];
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception ioe) {
            ioe.getMessage();
        }
        return columnNames;
    }

    public ArrayList<String> getColumnMetaDataArrayList(String tableName) {
        ArrayList<String> columnNames = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                for (int i = 1; i < tokens.length; i++) {

                    if (tokens[0].equalsIgnoreCase(tableName)) {

                        columnNames.add(tokens[i]);
                    }
                }
            }
            reader.close();
        } catch (Exception ioe) {
            ioe.getMessage();
        }
        return columnNames;
    }
    
    public ArrayList<String> getTableName(){
        
        ArrayList<String> tableNames = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                    tableNames.add(tokens[0]);
            }
            reader.close();
        } catch (Exception ioe) {
            ioe.getMessage();
        }
        return tableNames;
    }
}
