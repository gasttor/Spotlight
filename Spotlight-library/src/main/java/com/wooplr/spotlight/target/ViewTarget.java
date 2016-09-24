package com.wooplr.spotlight.target;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by jitender on 10/06/16.
 */
public class ViewTarget implements Target {

    private final View view;
    private final int[] location = new int[2];


    public ViewTarget(View view) {
        this.view = view;
    }

    @Override
    public Point getPoint() {
        view.getLocationInWindow(location);
        return new Point(location[0] + (view.getWidth() / 2), location[1] + (view.getHeight() / 2));
    }

    @Override
    public Rect getRect() {
        view.getLocationInWindow(location);
        return new Rect(
                location[0],
                location[1],
                location[0] + view.getWidth(),
                location[1] + view.getHeight()
        );
    }

    @Override
    public int getViewLeft() {
        return getRect().left;
    }

    @Override
    public int getViewRight() {
        return getRect().right;
    }

    @Override
    public int getViewBottom() {
        return getRect().bottom;
    }

    @Override
    public int getViewTop() {
        return getRect().top;
    }

    @Override
    public int getViewWidth() {
        return view.getWidth();
    }

    @Override
    public int getViewHeight() {
        return view.getHeight();
    }

    @Override
    public void setPressed(boolean pressed) {
        view.setPressed(pressed);
    }

    @Override
    public void invalidate() { view.invalidate(); }

    @Override
    public boolean performClick() { return view.performClick(); }
}
