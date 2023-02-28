package tw.com.obtc.financialob.activity;

import android.content.Context;
import android.widget.ImageButton;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.filter.WhereFilter;

class FilterState {

    static void updateFilterColor(Context context, WhereFilter filter, ImageButton button) {
        int color = filter.isEmpty() ? context.getResources().getColor(R.color.bottom_bar_tint) : context.getResources().getColor(R.color.holo_blue_dark);
        button.setColorFilter(color);
    }

}
