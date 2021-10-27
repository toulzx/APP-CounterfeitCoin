package com.toulzx.counterfeitcoins.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.toulzx.counterfeitcoins.R;

public class DecimalEditText extends AppCompatEditText {


    /**
     * 保留小数点前多少位，默认三位，既到千位
     */
    private int mDecimalStarNumber = 3;

    /**
     * 保留小数点后多少位，默认两位
     */
    private int mDecimalEndNumber = 2 ;

    public DecimalEditText(Context context) {
        this(context, null);
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public DecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DecimalEditText);

        mDecimalStarNumber = typedArray.getInt(R.styleable.DecimalEditText_decimalStarNumber, mDecimalStarNumber);
        mDecimalEndNumber = typedArray.getInt(R.styleable.DecimalEditText_decimalEndNumber, mDecimalEndNumber);
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String lastInputContent = dest.toString();


                if (source.equals(".") && lastInputContent.length() == 0) {
                    return "0.";
                }

                if (!source.equals(".") && !source.equals("") && lastInputContent.equals("0")) {
                    return ".";
                }

                if (source.equals(".") && lastInputContent.contains(".")) {
                    return "";
                }

                if (lastInputContent.contains(".")) {
                    int index = lastInputContent.indexOf(".");
                    if (dend - index >= mDecimalEndNumber + 1) {
                        return "";
                    }
                } else {
                    if (!source.equals(".") && lastInputContent.length() >= mDecimalStarNumber) {
                        return "";
                    }
                }

                return null;
            }
        }});
    }


    public int getDecimalStarNumber() {
        return mDecimalStarNumber;
    }

    public void setDecimalStarNumber(int decimalStarNumber) {
        mDecimalStarNumber = decimalStarNumber;
    }

    public int getDecimalEndNumber() {
        return mDecimalEndNumber;
    }

    public void setDecimalEndNumber(int decimalEndNumber) {
        mDecimalEndNumber = decimalEndNumber;
    }
}