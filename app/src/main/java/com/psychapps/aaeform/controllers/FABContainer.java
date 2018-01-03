package com.psychapps.aaeform.controllers;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by pavan on 3/1/18.
 */
@CoordinatorLayout.DefaultBehavior(MoveUpwardBehavior.class)
public class FABContainer extends RelativeLayout {
    public FABContainer(Context context) {
        super(context);
    }

    public FABContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FABContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FABContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
