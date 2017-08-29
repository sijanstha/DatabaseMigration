package com.datamigration.dbtabledata;

import com.datamigration.dbconnection.DBConnection;
import com.ibatis.common.jdbc.ScriptRunner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;

public class SQLScriptRunner extends Thread{
    private String sqlFilePath = "dump.sql";
    private Connection con = null;

    public void runSQLScript() {
        try {
            DBConnection dbCon = new DBConnection().getConnection(0);
            if (dbCon.code != 0) {
                System.out.println(dbCon.statusMsg);
                return;
            }
            con = dbCon.dbConnect;

            //Initializing object for ScriptRunner
            ScriptRunner sr = new ScriptRunner(con, false);
            //Giving the input sql file to the Reader
            Reader reader = new BufferedReader(new FileReader(sqlFilePath));
            //Executing sql script
            sr.runScript(reader);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void run(){
        runSQLScript();
    }
}
