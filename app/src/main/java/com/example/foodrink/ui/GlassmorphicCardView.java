package com.example.foodrink.ui;

import android.content.Context;
import android.util.AttributeSet;
import androidx.cardview.widget.CardView;

// Add this import to fix the error
import com.example.foodrink.R;

public class GlassmorphicCardView extends CardView {

    public GlassmorphicCardView(Context context) {
        super(context);
        init();
    }

    public GlassmorphicCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlassmorphicCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setCardElevation(0);
        setBackground(getResources().getDrawable(R.drawable.glassmorphism_background, null));
        setClipToOutline(true);
    }
}