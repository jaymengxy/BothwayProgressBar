package xyz.mengxy.bpb;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by Mengxy on 2019/11/12.
 */
public class PKLinearProgress extends View {
    // Pos Positive
    // Neg Negative
    private static final int GAP = 10;

    private Path mPosPath;
    private Path mNegPath;
    private Paint mPosPaint;
    private Paint mNegPaint;

    private int mPosStartColor;
    private int mPosEndColor;
    private int mNegStartColor;
    private int mNegEndColor;

    private int mWidth;
    private int mHeight;
    private float mPosWidth;
    private float mNegWidth;

    private RectF mLeftRectF;
    private RectF mRightRectF;
    private float mRadius;
    private float mPosPercent = 0.5f;
    private float mNegPercent = 0.5f;

    private boolean isAllPositive = false;
    private boolean isAllNegative = false;

    public PKLinearProgress(Context context) {
        this(context, null);
    }

    public PKLinearProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PKLinearProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPosPath = new Path();
        mNegPath = new Path();

        mPosPaint = new Paint();
        mPosPaint.setAntiAlias(true);
        mPosPaint.setStyle(Paint.Style.FILL);

        mNegPaint = new Paint();
        mNegPaint.setAntiAlias(true);
        mNegPaint.setStyle(Paint.Style.FILL);

        mPosStartColor = ContextCompat.getColor(getContext(), R.color.c_ff552e);
        mPosEndColor = ContextCompat.getColor(getContext(), R.color.c_ccff552e);
        mNegStartColor = ContextCompat.getColor(getContext(), R.color.c_cc556ccd);
        mNegEndColor = ContextCompat.getColor(getContext(), R.color.c_556ccd);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = h / 2f;
        mLeftRectF = new RectF(0, 0, h, h);
        mRightRectF = new RectF(w - h, 0, w, h);
        if (isAllPositive) {
            mPosWidth = w;
            mNegWidth = 0;
        } else if (isAllNegative) {
            mPosWidth = 0;
            mNegWidth = w;
        } else {
            mPosWidth = Math.max(Math.min(w * mPosPercent, w - mRadius - GAP), mRadius + GAP);
            mNegWidth = Math.max(Math.min(w * mNegPercent, w - mRadius - GAP), mRadius + GAP);
        }
        LinearGradient mPosGradient = new LinearGradient(0, 0, mPosWidth, 0, mPosStartColor, mPosEndColor, Shader.TileMode.MIRROR);
        LinearGradient mNegGradient = new LinearGradient(0, 0, mNegWidth, 0, mNegStartColor, mNegEndColor, Shader.TileMode.MIRROR);
        mPosPaint.setShader(mPosGradient);
        mNegPaint.setShader(mNegGradient);
    }

    public void setPositivePercent(float percent) {
        if (percent <= 0f) {
            isAllNegative = true;
        } else if (percent >= 1f) {
            isAllPositive = true;
        } else {
            mPosPercent = percent;
            mNegPercent = 1f - percent;
        }
        invalidate();
    }

    public void setPositivePercentWithAnim(final float percent) {
        if (percent <= 0f) {
            isAllNegative = true;
        } else if (percent >= 1f) {
            isAllPositive = true;
        }
        ValueAnimator valuePos = ValueAnimator.ofFloat(0, percent);
        valuePos.setDuration(4000);
        valuePos.setInterpolator(new LinearInterpolator());
        valuePos.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPosPercent = (float) animation.getAnimatedValue();
//                mPosWidth = Math.max(Math.min(mWidth * percent, mWidth - mRadius - GAP), mRadius + GAP);
            }
        });
        ValueAnimator valueNeg = ValueAnimator.ofFloat(0, 1f - percent);
        valueNeg.setDuration(4000);
        valueNeg.setInterpolator(new LinearInterpolator());
        valueNeg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                float percent = (float) animation.getAnimatedValue();
//                mNegWidth = Math.max(Math.min(mWidth * percent, mWidth - mRadius - GAP), mRadius + GAP);
                mNegPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(valuePos, valueNeg);
        set.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        // only animator
        mPosWidth = Math.max(Math.min(mWidth * mPosPercent, mWidth - mRadius - GAP), mRadius + GAP);
        mNegWidth = Math.max(Math.min(mWidth * mNegPercent, mWidth - mRadius - GAP), mRadius + GAP);
        if (isAllPositive || isAllNegative) {
            mPosPath.moveTo(mRadius, 0);
            mPosPath.arcTo(mLeftRectF, 270, -180);
            mPosPath.lineTo(mWidth - mRadius, mHeight);
            mPosPath.arcTo(mRightRectF, 90, -180);
            mPosPath.close();
        } else {
            mPosPath.moveTo(mRadius, 0);
            mPosPath.arcTo(mLeftRectF, 270, -180);
            mPosPath.lineTo(mPosWidth - GAP, mHeight);
            mPosPath.lineTo(mPosWidth, 0);
            mPosPath.close();

            mNegPath.moveTo(mWidth - mRadius, 0);
            mNegPath.arcTo(mRightRectF, 270, 180);
            mNegPath.lineTo(mWidth - mNegWidth, mHeight);
            mNegPath.lineTo(mWidth - mNegWidth + GAP, 0);
            mNegPath.close();
        }

        if (isAllPositive) {
            canvas.drawPath(mPosPath, mPosPaint);
        } else if (isAllNegative) {
            canvas.drawPath(mPosPath, mNegPaint);
        } else {
            canvas.drawPath(mPosPath, mPosPaint);
            canvas.drawPath(mNegPath, mNegPaint);
        }
    }

}
