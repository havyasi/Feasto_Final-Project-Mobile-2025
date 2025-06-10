package com.example.foodrink;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GlassmorphismActivity extends AppCompatActivity {

    private LinearLayout backgroundLayout;
    private View glassCard;
    private static final float BLUR_RADIUS = 25f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glassmorphism_card_layout);

        backgroundLayout = findViewById(R.id.background_layout);
        glassCard = findViewById(R.id.glass_card);

        // Apply blur effect when layout is ready
        backgroundLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        backgroundLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        applyBlurEffect();
                    }
                });
    }

    private void applyBlurEffect() {
        try {
            // Create bitmap from background
            Bitmap backgroundBitmap = createBitmapFromView(backgroundLayout);

            // Create blurred bitmap
            Bitmap blurredBitmap = blurBackground(backgroundBitmap);

            // Set as background to glass card
            glassCard.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Bitmap blurBackground(Bitmap bitmap) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap);

        RenderScript renderScript = RenderScript.create(this);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        Allocation tmpIn = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        blurScript.setRadius(BLUR_RADIUS);
        blurScript.setInput(tmpIn);
        blurScript.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        renderScript.destroy();
        return outputBitmap;
    }
}