package tw.com.obtc.financialob.db;

import android.content.Context;

public class TestDatabaseAdapter extends DatabaseAdapter {

    public TestDatabaseAdapter(Context context, DatabaseHelper databaseHelper) {
        super(context);
        this.databaseHelper = databaseHelper;
    }

}
