package com.yatoooon.screenadaptation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yatoooon on 2018/2/6.
 */

public class LoadViewHelper {

    private ActualScreen actualScreen;
    private Context context;
    private int designwidth;
    private int designdpi;
    private float fontsize;
    private String unit;

    public LoadViewHelper(Context context, int designwidth, int designdpi, float fontsize, String unit) {
        this.context = context;
        this.designwidth = designwidth;
        this.designdpi = designdpi;
        this.fontsize = fontsize;
        this.unit = unit;
        actualScreen = new ActualScreen(context);
    }

    public void loadView(ViewGroup viewGroup) {
        loadView(viewGroup, new Conversion(context));
    }

    public void loadView(ViewGroup viewGroup, Conversion conversion) {
        conversion.transform(viewGroup, this);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                loadView((ViewGroup) viewGroup.getChildAt(i), conversion);
            } else {
                conversion.transform(viewGroup.getChildAt(i), this);
            }
        }
    }

    public void loadWidthHeightFont(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.width > 0) {
            layoutParams.width = setWidthHeight(layoutParams.width);
        }
        if (layoutParams.height > 0) {
            layoutParams.height = setWidthHeight(layoutParams.height);
        }
        loadViewFont(view);
    }

    private int setWidthHeight(int value) {
        return (int) calculateValue(value);
    }

    private void loadViewFont(View view) {
        if ((view instanceof TextView)) {
            TextView textView = (TextView) view;
            ((TextView) view).setTextSize(0, setFontSize(textView));
        }
    }

    private float setFontSize(TextView textView) {
        return calculateValue(textView.getTextSize() * fontsize);
    }


    public void loadPadding(View view) {
        view.setPadding(setPadding(view.getPaddingLeft()), setPadding(view.getPaddingTop()), setPadding(view.getPaddingRight()), setPadding(view.getPaddingBottom()));
    }

    private int setPadding(int paddingValue) {
        return (int) calculateValue(paddingValue);
    }


    public void loadLayoutMargin(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.leftMargin = setLayoutMargin(marginLayoutParams.leftMargin);
        marginLayoutParams.topMargin = setLayoutMargin(marginLayoutParams.topMargin);
        marginLayoutParams.rightMargin = setLayoutMargin(marginLayoutParams.rightMargin);
        marginLayoutParams.bottomMargin = setLayoutMargin(marginLayoutParams.bottomMargin);
    }

    public int setLayoutMargin(int layoutmarginvalue) {
        return (int) calculateValue(layoutmarginvalue);
    }

    private float calculateValue(float value) {
        if (unit.equals("px")) {
            return value * ((float) actualScreen.width / (float) designwidth);
        } else if (unit.equals("dp")) {
            int dip = dp2pxutils.px2dip(context, value);
            value = ((float) designdpi / 160) * dip;
            return value * ((float) actualScreen.width / (float) designwidth);

        }
        return 0;
    }


}
