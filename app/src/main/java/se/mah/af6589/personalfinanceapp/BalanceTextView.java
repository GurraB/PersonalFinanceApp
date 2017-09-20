package se.mah.af6589.personalfinanceapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Gustaf Bohlin on 20/09/2017.
 */

public class BalanceTextView extends TextView {
    public BalanceTextView(Context context) {
        super(context);
    }

    public BalanceTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BalanceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BalanceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            float balance = Float.parseFloat(text.toString());
            super.setTextColor((balance >= 0 ? getContext().getColor(R.color.colorPrimary) : Color.RED));
            super.setText(String.valueOf(balance) + " kr", type);
        } catch (Exception e) {
            super.setText("NaN", type);
        }
    }
}
