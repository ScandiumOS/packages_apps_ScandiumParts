package com.scandium.parts.bottomnav;

import android.graphics.Typeface;
import com.scandium.parts.bottomnav.BubbleNavigationChangeListener;

public interface IBubbleNavigation {
    void setNavigationChangeListener(BubbleNavigationChangeListener navigationChangeListener);

    int getCurrentActiveItemPosition();

    void setCurrentActiveItem(int position);
}
