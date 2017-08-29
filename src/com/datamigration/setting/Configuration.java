/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datamigration.setting;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author divash
 */
public class Configuration {

    private String value = "";
    private String settingFile = "C:\\Users\\divash\\Downloads\\setting.0.ini";

    public String getSettingValue(String field) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(settingFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                //for (int i = 1; i < tokens.length; i++) {
                    if (tokens[0].equals(field)) {
                        value += tokens[1];
                    }
                //}
            }
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return value;
    }
    
    public static void main(String[] args) {
        String field = "securitykey";
        System.out.println(new Configuration().getSettingValue(field));
    }

}
