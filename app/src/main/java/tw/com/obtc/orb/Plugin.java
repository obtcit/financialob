package tw.com.obtc.orb;

import android.content.ContentValues;

public interface Plugin {

    void withContentValues(String tableName, ContentValues values);

}
