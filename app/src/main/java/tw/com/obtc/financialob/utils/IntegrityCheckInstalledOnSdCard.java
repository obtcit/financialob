package tw.com.obtc.financialob.utils;

import android.content.Context;

import tw.com.obtc.financialob.R;

import static tw.com.obtc.financialob.utils.AndroidUtils.isInstalledOnSdCard;

public class IntegrityCheckInstalledOnSdCard implements IntegrityCheck {

    private final Context context;

    public IntegrityCheckInstalledOnSdCard(Context context) {
        this.context = context;
    }

    @Override
    public Result check() {
        if (isInstalledOnSdCard(context)) {
            return new Result(Level.WARN, context.getString(R.string.installed_on_sd_card_warning));
        }
        return Result.OK;
    }

}
