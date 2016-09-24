package com.wooplr.spotlight.utils;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wooplr.spotlight.SpotlightView;

/**
 * Created by jitender on 10/06/16.
 */

public interface SpotlightListener {

    void onUserClicked(@NonNull SpotlightView l, @Nullable String spotlightViewId);
    void onAborted(@NonNull SpotlightView l, @Nullable String spotlightViewId);
}
