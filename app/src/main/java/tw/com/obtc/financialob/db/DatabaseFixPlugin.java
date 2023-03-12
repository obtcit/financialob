package tw.com.obtc.financialob.db;

import android.content.ContentValues;

import tw.com.obtc.orb.Plugin;

import static tw.com.obtc.financialob.db.DatabaseHelper.LOCATIONS_TABLE;

public class DatabaseFixPlugin implements Plugin {

    @Override
    public void withContentValues(String tableName, ContentValues values) {
        if (LOCATIONS_TABLE.equals(tableName)) {
            // since there is no easy way to drop a column in SQLite
            values.put("name", values.getAsString("title"));
        }
    }

}
