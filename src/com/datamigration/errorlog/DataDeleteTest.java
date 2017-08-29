package com.datamigration.errorlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class DataDeleteTest {

    public static void main(String[] args) {
        try {
            File file = new File("error_log.txt");
            File tempFile = new File("file.txt");

            BufferedReader reader = new BufferedReader(new FileReader(file));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile)));
            
            String line = "";
            String lineToDelete = "aaa;";
            int c = 0;
            while((line = reader.readLine())!=null){
                if(line.equalsIgnoreCase(lineToDelete)){
                if(c==0){
                line = line.replace(lineToDelete, "");
                 c++;
                 }
                }
                writer.println(line);
               }
            
            reader.close();
            writer.close();
            
            file.delete();
            tempFile.renameTo(file);
            

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
