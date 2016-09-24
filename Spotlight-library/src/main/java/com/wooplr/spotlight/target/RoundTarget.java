package com.wooplr.spotlight.target;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by jitender on 10/06/16.
 */

public class RoundTarget
    implements Target
{
    private final Point point;
    private final int radius;

    public RoundTarget(Point point, int radius) {
        this.point = point;
        this.radius = radius;
    }

    public RoundTarget(View v, Point point, int radius) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        this.point = new Point(location[0] + point.x, location[1] + point.y);
        this.radius = radius;
    }

    @Override public Point getPoint() { return point; }

    @Override public Rect getRect() {
        return new Rect(point.x - radius, point.y - radius, point.x + radius, point.y + radius);
    };

    @Override public int getViewLeft() { return point.x - radius; }

    @Override public int getViewRight() { return point.x + radius; }

    @Override public int getViewTop() { return point.y - radius; }

    @Override public int getViewBottom() { return point.y + radius; }

    @Override public int getViewWidth() { return 2*radius; };

    @Override public int getViewHeight() { return 2*radius; };

    @Override public void setPressed(boolean pressed) { }

    @Override public void invalidate() { }

    @Override public boolean performClick() { return false; }

    @Override public String toString() { return "["+point+"|"+radius+"]"; }
}
