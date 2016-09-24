package com.wooplr.spotlight.target;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by jitender on 10/06/16.
 */

public interface Target {

    Point getPoint();

    Rect getRect();

    int getViewLeft();

    int getViewRight();

    int getViewTop();

    int getViewBottom();

    int getViewWidth();

    int getViewHeight();

    void setPressed(boolean pressed);

    void invalidate();

    boolean performClick();
}
