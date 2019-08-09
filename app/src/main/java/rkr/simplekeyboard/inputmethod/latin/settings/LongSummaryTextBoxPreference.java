package rkr.simplekeyboard.inputmethod.latin.settings;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class LongSummaryTextBoxPreference extends Preference
{
    public LongSummaryTextBoxPreference(Context ctx, AttributeSet attrs, int defStyle)
    {
        super(ctx, attrs, defStyle);
    }

    public LongSummaryTextBoxPreference(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }


    LongSummaryTextBoxPreference(Context ctx)
    {
        super(ctx);
    }

    @Override
    protected void onBindView(View view)
    {
        super.onBindView(view);

        TextView summary= (TextView)view.findViewById(android.R.id.summary);
        summary.setMaxLines(100);
    }
}