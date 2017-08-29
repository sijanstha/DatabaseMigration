package com.datamigration.main;

import com.datamigration.dbtabledata.TableColumnMetadata;
import com.datamigration.dbtabledata.TableDataExtractor;

public class Main {

    public static void main(String args[]) {

        if (new TableColumnMetadata().getTableMetadata()) {
           System.out.println("Dumping Database Records into dump.sql file............");
            TableDataExtractor obj = new TableDataExtractor();
            obj.start();
            
        } else {
            System.out.println("Something went wrong");
        }
    }
}
    