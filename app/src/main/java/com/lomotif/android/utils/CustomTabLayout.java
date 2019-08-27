package com.lomotif.android.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.lomotif.android.R;

public class CustomTabLayout extends TabLayout {

    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        super.addTab(tab, setSelected);
        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTextSize(getContext().getResources().getDimension(R.dimen.font_size_medium));
            }
        }

        this.setVisibility(getTabCount() == 1 ? View.GONE : View.VISIBLE);
        if (getTabCount() <= 3) {
            this.setTabMode(TabLayout.MODE_FIXED);
            this.setTabGravity(TabLayout.GRAVITY_FILL);
        } else {
            this.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    public void setFixedMode(boolean isFixed) {
        this.setTabMode(isFixed ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
    }
}
