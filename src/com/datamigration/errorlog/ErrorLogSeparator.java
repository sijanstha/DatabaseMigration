
package com.datamigration.errorlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ErrorLogSeparator {

    public ArrayList<String> errorLogManager(String tableName) {
        ArrayList<String> columnValues = new ArrayList<>();
        try {
            //file for error_log
            File file = new File("error_log.txt");
            
            //temporary file for uncorrected error
            File tempFile = new File("file.txt");

            //reader to read error_log file line by line 
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            //writer to write into temporary file
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile)));

            String line = "";
            
            //counter variable to check repeated table names
            int count = 0;

            while ((line = reader.readLine()) != null) {
                
                if (line.contains(";")) {
                    String tokens[] = line.split("%");

                    if (tokens[0].equals(tableName)) {
                        
                        //managing repeated table to read exactly one line at a time
                        if (count == 0) {
                            String[] values = tokens[1].split(",");
                            for (int i = 0; i < values.length; i++) {
                                
                                //if values contains single quote(') at the beginning and at the end then it is discarded
                                if (values[i].contains("'")) {
                                    int len = values[i].length();
                                    columnValues.add(values[i].substring(1, len - 1));
                                } else {
                                    columnValues.add(values[i]);
                                }
                            }
                            
                            //checking whether the last item in arraylist contains ";" or not
                            //if found it is discarded
                            int colSize = columnValues.size();
                            String temp = columnValues.get(colSize - 1);
                            int tempSize = temp.length();
                            columnValues.remove(colSize - 1);
                            columnValues.add(colSize - 1, temp.substring(0, tempSize - 1));

                            //the readed line is deleted for controlling multiple errors in same table name
                            String lineToDelete = line;
                            line = line.replace(lineToDelete, "");
                            count++;
                        }

                    }
                }
                
                //the data of file is copied into tempFile 
                writer.println(line);
            }
            reader.close();
            writer.close();

            file.delete();
            
            //the same temporary file is then renamed to original filename to ensure no conflict for next errors
            tempFile.renameTo(new File("error_log.txt"));

        } catch (IOException fe) {
            fe.printStackTrace();
        }
        return columnValues;
    }

}
