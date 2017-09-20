package se.mah.af6589.personalfinanceapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Gustaf Bohlin on 19/09/2017.
 */

public class BarcodeTextView extends TextView {

    public BarcodeTextView(Context context) {
        super(context);
    }

    public BarcodeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BarcodeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BarcodeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private String generateBarcode(String value) {
        if (value.length() != 13)
            return "";
        int values[] = new int[13];
        for (int i = 0; i < 13; i++) {
            int digit = value.charAt(i) - '0';
            if (digit >= 0 && digit < 10)
                values[i] = digit;
            else return "";
        }

        String oddParitySet[] = {"0001101", "0011001", "0010011", "0111101", "0100011", "0110001", "0101111", "0111011", "0110111", "0001011"};
        String evenParitySet[] = {"0100111", "0110011", "0011011", "0100001", "0011101", "0111001", "0000101", "0010001", "0001001", "0010111"};
        String cSet[] = {"1110010", "1100110", "1101100", "1000010", "1011100", "1001110", "1010000", "1000100", "1001000", "1110100"};
        String guardBar = "101";
        String centerGuard = "01010";

        int n = 1;
        String productCode = "";
        productCode += guardBar;

        productCode += oddParitySet[values[n++]];
        productCode += evenParitySet[values[n++]];
        productCode += oddParitySet[values[n++]];
        productCode += evenParitySet[values[n++]];
        productCode += oddParitySet[values[n++]];
        productCode += evenParitySet[values[n++]];

        productCode += centerGuard;

        productCode += cSet[values[n++]];
        productCode += cSet[values[n++]];
        productCode += cSet[values[n++]];
        productCode += cSet[values[n++]];
        productCode += cSet[values[n++]];
        productCode += cSet[values[n++]];
        
        productCode += guardBar;
        return productCode;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String barcodeText = generateBarcode(text.toString());
        super.setText(barcodeText, type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        String code = super.getText().toString();
        int stroke = width / code.length();
        int startingpoint = 0;
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(stroke);
        if (super.getTextAlignment() == TEXT_ALIGNMENT_CENTER) {
            int calculatedWidth = stroke * code.length();
            int emptySpace = width - calculatedWidth;
            startingpoint = emptySpace / 2;
        }
        Log.v("Code", code);
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '1')
                canvas.drawLine((i * stroke) + startingpoint, 0, (i * stroke) + startingpoint, height, paint);
        }
    }
}
