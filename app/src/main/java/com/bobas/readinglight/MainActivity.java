package com.bobas.readinglight;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;

public class MainActivity extends Activity {
    private static final int MIN_BRIGHTNESS_PERCENT = 5;
    private static final int MAX_BRIGHTNESS_PERCENT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        enterImmersiveMode();

        FrameLayout screen = new FrameLayout(this);
        screen.setBackgroundColor(0xFFFFFFFF);

        SeekBar brightness = new SeekBar(this);
        brightness.setMax(MAX_BRIGHTNESS_PERCENT - MIN_BRIGHTNESS_PERCENT);
        brightness.setProgress(initialBrightnessPercent() - MIN_BRIGHTNESS_PERCENT);
        brightness.setContentDescription("Screen brightness");

        FrameLayout.LayoutParams sliderParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        int horizontalPadding = dp(28);
        sliderParams.setMargins(horizontalPadding, 0, horizontalPadding, dp(18));
        screen.addView(brightness, sliderParams);

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setScreenBrightness((progress + MIN_BRIGHTNESS_PERCENT) / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        setContentView(screen);
    }

    private int initialBrightnessPercent() {
        int brightness = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 128);
        return Math.max(MIN_BRIGHTNESS_PERCENT,
                Math.min(MAX_BRIGHTNESS_PERCENT, Math.round(brightness * 100f / 255f)));
    }

    private void setScreenBrightness(float brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.screenBrightness = brightness;
        window.setAttributes(params);
    }

    private void enterImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
