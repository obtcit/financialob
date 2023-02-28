package tw.com.obtc.financialob.adapter.async;

import android.database.Cursor;
import tw.com.obtc.financialob.db.DatabaseAdapter;
import tw.com.obtc.financialob.model.SmsTemplate;

public class SmsTemplateListSource extends CursorItemSource<SmsTemplate> {
    
    private final DatabaseAdapter db;
    private volatile String filter;
    
    public SmsTemplateListSource(DatabaseAdapter db, boolean prepareCursor) {
        this.db = db;

        if (prepareCursor) prepareCursor();
    }

    @Override
    public Cursor initCursor() {
        return db.getSmsTemplatesWithFullInfo(filter);
    }

    @Override
    protected SmsTemplate loadItem() {
        return SmsTemplate.fromListCursor(cursor);
    }

    @Override
    public Class<SmsTemplate> clazz() {
        return SmsTemplate.class;
    }

    @Override
    public void setConstraint(CharSequence constraint) {
        filter = constraint == null ? null : constraint.toString();
    }
}
