package xyz.mengxy.bpb

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Mengxy on 2019/11/11.
 */
class BothwayProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mPosPath: Path = Path()
    private var mNegPath: Path = Path()
    private var mPosPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var mNegPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var mPosStartColor = 0
    private var mPosEndColor = 0
    private var mNegStartColor = 0
    private var mNegEndColor = 0

    private var mWidth = 0f
    private var mHeight = 0f
    private var mPosWidth = 0f
    private var mNegWidth = 0f

    private var mLeftRectF: RectF? = null
    private var mRightRectF: RectF? = null
    private var mRadius = 0f
    private var mPosPercent = 0f
    private var mNegPercent = 0f

    private var isAllPositive = false
    private var isAllNegative = false

    companion object {
        const val GAP = 10f
    }

    init {
        mPosStartColor = ContextCompat.getColor(getContext(), R.color.c_ff552e)
        mPosEndColor = ContextCompat.getColor(getContext(), R.color.c_ccff552e)
        mNegStartColor = ContextCompat.getColor(getContext(), R.color.c_cc556ccd)
        mNegEndColor = ContextCompat.getColor(getContext(), R.color.c_556ccd)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = h.div(2f)
        mLeftRectF = RectF(0f, 0f, mHeight, mHeight)
        mRightRectF = RectF(mWidth.minus(mHeight), 0f, mWidth, mHeight)
        configureProgress()
    }

    fun setPositivePercent(percent: Float) {
        configurePercent(percent)
        configureProgress()
        invalidate()
    }

    fun setPositivePercentWithAnim(percent: Float) {
        configurePercent(percent)
        configureProgress()
        AnimatorSet().apply {
            playTogether(ValueAnimator.ofFloat(0f, mPosPercent).apply {
                duration = 1000
                interpolator = LinearInterpolator()
                addUpdateListener {
                    mPosWidth = max(min(mWidth.times(it.animatedValue as Float), mWidth.minus(mRadius).minus(GAP)), mRadius.plus(
                        GAP))
                }
            }, ValueAnimator.ofFloat(0f, mNegPercent).apply {
                duration = 1000
                interpolator = LinearInterpolator()
                addUpdateListener {
                    mNegWidth = max(min(mWidth.times(it.animatedValue as Float), mWidth.minus(mRadius).minus(GAP)), mRadius.plus(
                        GAP))
                    invalidate()
                }
            })
        }.start()
    }

    private fun configurePercent(percent: Float) {
        isAllPositive = percent >= 1f
        isAllNegative = percent <= 0f
        mPosPercent = percent
        mNegPercent = 1f.minus(percent)
    }

    private fun configureProgress() {
        when {
            isAllPositive -> {
                mPosWidth = mWidth
                mNegWidth = 0f
            }
            isAllNegative -> {
                mPosWidth = 0f
                mNegWidth = mWidth
            }
            else -> {
                mPosWidth = max(min(mWidth.times(mPosPercent), mWidth.minus(mRadius).minus(GAP)), mRadius.plus(GAP))
                mNegWidth = max(min(mWidth.times(mNegPercent), mWidth.minus(mRadius).minus(GAP)), mRadius.plus(GAP))
            }
        }
        mPosPaint.shader = LinearGradient(0f, 0f, mPosWidth, 0f, mPosStartColor, mPosEndColor, Shader.TileMode.MIRROR)
        mNegPaint.shader = LinearGradient(0f, 0f, mNegWidth, 0f, mNegStartColor, mNegEndColor, Shader.TileMode.MIRROR)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mPosPercent <= 0 && mNegPercent <= 0) {
            return
        }
        mPosPath.reset()
        mNegPath.reset()
        if (isAllPositive || isAllNegative) {
            with(mPosPath) {
                moveTo(mRadius, 0f)
                arcTo(mLeftRectF, 270f, -180f)
                lineTo(mWidth.minus(mRadius), mHeight)
                arcTo(mRightRectF, 90f, -180f)
                close()
            }
        } else {
            with(mPosPath) {
                moveTo(mRadius, 0f)
                arcTo(mLeftRectF, 270f, -180f)
                lineTo(mPosWidth.minus(GAP), mHeight)
                lineTo(mPosWidth, 0f)
                close()
            }
            with(mNegPath) {
                moveTo(mWidth.minus(mRadius), 0f)
                arcTo(mRightRectF, 270f, 180f)
                lineTo(mWidth.minus(mNegWidth), mHeight)
                lineTo(mWidth.minus(mNegWidth).plus(GAP), 0f)
                close()
            }
        }

        when {
            isAllPositive -> {
                canvas?.drawPath(mPosPath, mPosPaint)
            }
            isAllNegative -> {
                canvas?.drawPath(mPosPath, mNegPaint)
            }
            else -> {
                canvas?.drawPath(mPosPath, mPosPaint)
                canvas?.drawPath(mNegPath, mNegPaint)
            }
        }
    }
}
