package tw.com.obtc.financialob.activity;

import android.content.Context;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import tw.com.obtc.financialob.R;

public class UiUtils {

    public static void applyTheme(Context context, DatePickerDialog d) {
        d.setAccentColor(context.getResources().getColor(R.color.colorPrimary));
        d.setThemeDark(true);
    }

    public static void applyTheme(Context context, TimePickerDialog d) {
        d.setAccentColor(context.getResources().getColor(R.color.colorPrimary));
        d.setThemeDark(true);
    }

}
