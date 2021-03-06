package com.ramijemli.percentagechartview;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.ramijemli.percentagechartview.annotation.AdaptiveMode;
import com.ramijemli.percentagechartview.annotation.ChartMode;
import com.ramijemli.percentagechartview.annotation.ProgressBarStyle;
import com.ramijemli.percentagechartview.annotation.ProgressOrientation;
import com.ramijemli.percentagechartview.annotation.TextStyle;
import com.ramijemli.percentagechartview.renderer.BaseModeRenderer;
import com.ramijemli.percentagechartview.renderer.PieModeRenderer;
import com.ramijemli.percentagechartview.renderer.RingModeRenderer;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static com.ramijemli.percentagechartview.renderer.BaseModeRenderer.MODE_PIE;
import static com.ramijemli.percentagechartview.renderer.BaseModeRenderer.MODE_RING;

public class PercentageChartView extends View implements IPercentageChartView {

    private BaseModeRenderer renderer;

    @ChartMode
    private int mode;

    @Nullable
    private OnProgressChangeListener onProgressChangeListener;

    public PercentageChartView(Context context) {
        super(context);
        init(context, null);
    }

    public PercentageChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PercentageChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public PercentageChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray attrs = context.getTheme().obtainStyledAttributes(
                    attributeSet,
                    R.styleable.PercentageChartView,
                    0, 0
            );

            try {
                //CHART MODE (DEFAULT PIE MODE)
                mode = attrs.getInt(R.styleable.PercentageChartView_pcv_mode, MODE_PIE);
                switch (mode) {
                    case MODE_RING:
                        renderer = new RingModeRenderer(this, attrs);
                        break;
                    case MODE_PIE:
                        renderer = new PieModeRenderer(this, attrs);
                        break;
                }

            } finally {
                attrs.recycle();
                attrs = null;
            }

        } else {
            renderer = new PieModeRenderer(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        renderer.mesure(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        renderer.destroy();
        renderer = null;

        if (onProgressChangeListener != null) {
            onProgressChangeListener = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        renderer.draw(canvas);
    }

    //RENDERER CALLBACKS
    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void onProgressUpdated(float progress) {
        if (onProgressChangeListener != null)
            onProgressChangeListener.onProgressChanged(progress);
    }

    //##############################################################################################   STYLE MODIFIERS
    //PROGRESS
    @FloatRange(from = -1f, to = 100f)
    public float getProgress() {
        return renderer.getProgress();
    }

    /**
     * Sets a new progress value. Passing true in animate will result in an animated progress update.
     *
     * @param progress New progress float value to set.
     * @param animate  Animation boolean value to set whether to animate progress change or not.
     */
    public void setProgress(@FloatRange(from = 0f, to = 100f) float progress, boolean animate) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        renderer.setProgress(progress, animate);
    }

    //ORIENTATION
    @ProgressOrientation
    public int getOrientation() {
        return renderer.getOrientation();
    }

    public void setOrientation(@ProgressOrientation int orientation) {
        this.renderer.setOrientation(orientation);
    }

    //DRAW BACKGROUND STATE
    public boolean isDrawBackgroundEnabled() {
        return renderer.isDrawBackgroundEnabled();
    }

    public void setDrawBackgroundEnabled(boolean enabled) {
        this.renderer.setDrawBackgroundEnabled(enabled);
    }

    //START ANGLE
    @FloatRange(from = 0f, to = 360f)
    public float getStartAngle() {
        return renderer.getStartAngle();
    }

    public void setStartAngle(@FloatRange(from = 0f, to = 360f) float startAngle) {
        if (startAngle < 0) startAngle = 0;
        if (startAngle > 360) startAngle = 360;
        this.renderer.setStartAngle(startAngle);
    }

    //BACKGROUND COLOR
    @ColorInt
    public int getBackgroundColor() {
        return renderer.getBackgroundColor();
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.renderer.setBackgroundColor(backgroundColor);
    }

    //BACKGROUND OFFSET
    public float getBackgroundOffset() {
        return renderer.getBackgroundOffset();
    }

    public void setBackgroundOffset(int backgroundOffset) {
        this.renderer.setBackgroundOffset(backgroundOffset);
    }

    //PROGRESS COLOR
    @ColorInt
    public int getProgressColor() {
        return renderer.getProgressColor();
    }

    public void setProgressColor(@ColorInt int progressColor) {
        this.renderer.setProgressColor(progressColor);
    }

    //ADAPTIVE BACKGROUND
    public boolean isAdaptiveBackgroundEnabled() {
        return renderer.isAdaptiveBackgroundEnabled();
    }

    @FloatRange(from = -1f, to = 1f)
    public float getAdaptiveBackgroundRatio() {
        return renderer.getAdaptiveBackgroundRatio();
    }

    @AdaptiveMode
    public int getAdaptiveBackgroundMode() {
        return renderer.getAdaptiveBackgroundMode();
    }

    public void setAdaptiveBgEnabled(boolean enable) {
        renderer.setAdaptiveBgEnabled(enable);
    }

    public void setAdaptiveBackground(@FloatRange(from = 0f, to = 1f) float ratio, @AdaptiveMode int adaptiveMode) {
        renderer.setAdaptiveBackground(ratio, adaptiveMode);
    }

    //ADAPTIVE BACKGROUND BAR
    public boolean isAdaptiveBackgroundBarEnabled() {
        if (renderer instanceof PieModeRenderer) return false;
        return ((RingModeRenderer) renderer).isAdaptiveBackgroundBarEnabled();
    }

    @FloatRange(from = -1f, to = 1f)
    public float getAdaptiveBackgroundBarRatio() {
        if (renderer instanceof PieModeRenderer) return -1f;
        return ((RingModeRenderer) renderer).getAdaptiveBackgroundBarRatio();
    }

    public int getAdaptiveBackgroundBarMode() {
        if (renderer instanceof PieModeRenderer) return -1;
        return ((RingModeRenderer) renderer).getAdaptiveBackgroundBarMode();
    }

    public void setAdaptiveBgBarEnabled(boolean enable) {
        if (renderer instanceof PieModeRenderer) return;
        ((RingModeRenderer) renderer).setAdaptiveBgBarEnabled(enable);
    }

    public void setAdaptiveBackgroundBar(@FloatRange(from = 0f, to = 1f) float ratio, @AdaptiveMode int adaptiveMode) {
        if (renderer instanceof PieModeRenderer) return;
        ((RingModeRenderer) renderer).setAdaptiveBackgroundBar(ratio, adaptiveMode);
    }

    //ADAPTIVE TEXT
    public boolean isAdaptiveTextEnabled() {
        return renderer.isAdaptiveTextEnabled();
    }

    @FloatRange(from = -1f, to = 1f)
    public float getAdaptiveTextRatio() {
        return renderer.getAdaptiveTextRatio();
    }

    public int getAdaptiveTextMode() {
        return renderer.getAdaptiveTextMode();
    }

    public void setAdaptiveTextEnabled(boolean enable) {
        renderer.setAdaptiveTextEnabled(enable);
    }

    public void setAdaptiveText(@FloatRange(from = 0f, to = 1f) float ratio, @AdaptiveMode int adaptiveMode) {
        renderer.setAdaptiveText(ratio, adaptiveMode);
    }

    //ANIMATION DURATION
    @IntRange(from = 0)
    public int getAnimationDuration() {
        return renderer.getAnimationDuration();
    }

    public void setAnimationDuration(@IntRange(from = 50) int duration) {
        renderer.setAnimationDuration(duration);
    }

    //ANIMATION INTERPOLATOR
    public TimeInterpolator getAnimationInterpolator() {
        return renderer.getAnimationInterpolator();
    }

    public void setAnimationInterpolator(@NonNull TimeInterpolator interpolator) {
        if (interpolator == null) {
            throw new NullPointerException("Animation interpolator cannot be null");
        }

        renderer.setAnimationInterpolator(interpolator);
    }

    //TEXT COLOR
    @ColorInt
    public int getTextColor() {
        return renderer.getTextColor();
    }

    public void setTextColor(@ColorInt int textColor) {
        renderer.setTextColor(textColor);
    }

    //TEXT SIZE
    public float getTextSize() {
        return renderer.getTextSize();
    }

    public void setTextSize(float textSize) {
        renderer.setTextSize(textSize);
    }

    //TEXT TYPEFACE
    public Typeface getTypeface() {
        return renderer.getTypeface();
    }

    public void setTypeface(@NonNull Typeface typeface) {
        if (typeface == null) {
            throw new NullPointerException("Text TypeFace cannot be null");
        }
        renderer.setTypeface(typeface);
    }

    //TEXT STYLE
    @TextStyle
    public int getTextStyle() {
        return renderer.getTextStyle();
    }

    public void setTextStyle(@TextStyle int textStyle) {
        renderer.setTextStyle(textStyle);
    }

    //TEXT SHADOW
    @ColorInt
    public int getTextShadowColor() {
        return renderer.getTextShadowColor();
    }

    public float getTextShadowRadius() {
        return renderer.getTextShadowRadius();
    }

    public float getTextShadowDistY() {
        return renderer.getTextShadowDistY();
    }

    public float getTextShadowDistX() {
        return renderer.getTextShadowDistX();
    }

    public void setTextShadow(@ColorInt int shadowColor, @FloatRange(from = 0) float shadowRadius, @FloatRange(from = 0) float shadowDistX, @FloatRange(from = 0) float shadowDistY) {
        renderer.setTextShadow(shadowColor, shadowRadius, shadowDistX, shadowDistY);
    }

    //DRAW BACKGROUND BAR STATE
    public boolean isDrawBackgroundBarEnabled() {
        if (renderer instanceof PieModeRenderer) return false;
        return ((RingModeRenderer) renderer).isDrawBackgroundBarEnabled();
    }

    public void setDrawBackgroundBarEnabled(boolean enabled) {
        if (renderer instanceof PieModeRenderer) return;
        ((RingModeRenderer) renderer).setDrawBackgroundBarEnabled(enabled);
    }

    //BACKGROUND BAR COLOR
    public int getBackgroundBarColor() {
        if (renderer instanceof PieModeRenderer) return -1;
        return ((RingModeRenderer) renderer).getBackgroundBarColor();
    }

    public void setBackgroundBarColor(@ColorInt int backgroundBarColor) {
        ((RingModeRenderer) renderer).setBackgroundBarColor(backgroundBarColor);
    }

    //BACKGROUND BAR THICKNESS
    public float getBackgroundBarThickness() {
        if (renderer instanceof PieModeRenderer) return -1;
        return ((RingModeRenderer) renderer).getBackgroundBarThickness();
    }

    public void setBackgroundBarThickness(@FloatRange(from = 0) float backgroundBarThickness) {
        if (renderer instanceof PieModeRenderer) return;
        ((RingModeRenderer) renderer).setBackgroundBarThickness(backgroundBarThickness);
    }

    //PROGRESS BAR THICKNESS
    public float getProgressBarThickness() {
        if (renderer instanceof PieModeRenderer) return -1;
        return ((RingModeRenderer) renderer).getProgressBarThickness();
    }

    public void setProgressBarThickness(@FloatRange(from = 0) float progressBarThickness) {
        if (renderer instanceof PieModeRenderer) return;
        ((RingModeRenderer) renderer).setProgressBarThickness(progressBarThickness);
    }

    //PROGRESS BAR STYLE
    public int getProgressBarStyle() {
        if (renderer instanceof PieModeRenderer) return -1;
        return ((RingModeRenderer) renderer).getProgressBarStyle();
    }

    public void setProgressBarStyle(@ProgressBarStyle int progressBarStyle) {
        if (renderer instanceof PieModeRenderer) return;
        ((RingModeRenderer) renderer).setProgressBarStyle(progressBarStyle);
    }

    //##############################################################################################   ADAPTIVE COLOR PROVIDER
    public void setAdaptiveColorProvider(@Nullable PercentageChartView.AdaptiveColorProvider adaptiveColorProvider) {
        this.renderer.setAdaptiveColorProvider(adaptiveColorProvider);
    }

    //##############################################################################################   LISTENER
    public void setOnProgressChangeListener(@Nullable OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public interface AdaptiveColorProvider {
        int getColor(float value);
    }

    public interface OnProgressChangeListener {
        void onProgressChanged(float progress);
    }
}
