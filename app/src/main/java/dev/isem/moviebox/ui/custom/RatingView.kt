package dev.isem.moviebox.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Cap
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import dev.isem.moviebox.R
import kotlin.math.max
import kotlin.math.min

private const val ANGLE_START_BACKGROUND = 0
private const val ANGLE_END_BACKGROUND = 360
private const val DEFAULT_START_ANGLE = 270

private const val DEFAULT_COLOR = Color.BLACK
private const val DEFAULT_BACKGROUND_COLOR = Color.LTGRAY
private const val DEFAULT_STROKE_WIDTH_DP = 12
private const val DEFAULT_TEXT_SIZE_SP = 16

private const val DIRECTION_CLOCKWISE = 0
private const val DIRECTION_COUNTERCLOCKWISE = 1

private const val CAP_ROUND = 0
private const val CAP_BUTT = 1


class RatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var ratingColor = DEFAULT_COLOR
    private var ratingColorLow = DEFAULT_COLOR
    private var ratingBackgroundColor = DEFAULT_BACKGROUND_COLOR
    private var ratingBackgroundColorLow = DEFAULT_BACKGROUND_COLOR
    private var ratingStrokeWidth = DEFAULT_STROKE_WIDTH_DP
    private var ratingBackgroundStrokeWidth = DEFAULT_STROKE_WIDTH_DP

    private var textColor = DEFAULT_COLOR
    private var textSize = DEFAULT_TEXT_SIZE_SP

    private var isFillBackgroundEnabled = false
    private var direction = DIRECTION_CLOCKWISE

    private var ratingStrokeCap = Cap.ROUND

    private var startAngle = DEFAULT_START_ANGLE
    private var sweepAngle = 0

    private var ratingValue = 0.0
    private var ratingValueThresholdLow = 50.0
    private var ratingValueMax = 100.0

    private var ratingText = ratingValue.toString()
    private var textX = 0f
    private var textY = 0f

    private var radius = 0f

    private val textBoundsRect = Rect()

    private var circleBounds = RectF()

    private lateinit var ratingPaint: Paint
    private lateinit var ratingBackgroundPaint: Paint
    private lateinit var ratingTextPaint: Paint

    init {
        initAttributes(context, attrs)

        ratingPaintSetup()

        ratingBackgroundPaintSetup()

        ratingTextPaintSetup()
    }

    fun setRating(currentValue: Double) {

        if (currentValue >= ratingValueThresholdLow) {
            ratingPaint.color = ratingColor
            ratingBackgroundPaint.color = ratingBackgroundColor
        } else {
            ratingPaint.color = ratingColorLow
            ratingBackgroundPaint.color = ratingBackgroundColorLow
        }

        val finalAngle: Double = if (direction == DIRECTION_CLOCKWISE) {
            currentValue / ratingValueMax * 360
        } else {
            -(currentValue / ratingValueMax * 360)
        }

        ratingValue = min(currentValue, ratingValueMax)

        setFormattedRatingText()

        calculateTextBounds()

        sweepAngle = finalAngle.toInt()

        invalidate()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingView)

            ratingValueMax =
                typedArray.getFloat(R.styleable.RatingView_ratingValueMax, ratingValueMax.toFloat())
                    .toDouble()
            ratingValueThresholdLow =
                typedArray.getFloat(
                    R.styleable.RatingView_ratingValueThresholdLow,
                    ratingValueThresholdLow.toFloat()
                ).toDouble()
            ratingColor =
                typedArray.getColor(R.styleable.RatingView_ratingColor, DEFAULT_COLOR)
            ratingColorLow =
                typedArray.getColor(R.styleable.RatingView_ratingColorLow, DEFAULT_COLOR)
            ratingBackgroundColor =
                typedArray.getColor(
                    R.styleable.RatingView_ratingBackgroundColor,
                    DEFAULT_BACKGROUND_COLOR
                )
            ratingBackgroundColorLow =
                typedArray.getColor(
                    R.styleable.RatingView_ratingBackgroundColorLow,
                    ratingBackgroundColorLow
                )
            ratingStrokeWidth =
                typedArray.getDimensionPixelOffset(
                    R.styleable.RatingView_ratingStrokeWidth,
                    DEFAULT_STROKE_WIDTH_DP
                )
            ratingBackgroundStrokeWidth =
                typedArray.getDimensionPixelOffset(
                    R.styleable.RatingView_ratingBackgroundStrokeWidth, DEFAULT_STROKE_WIDTH_DP
                )
            textColor =
                typedArray.getColor(R.styleable.RatingView_textColor, DEFAULT_COLOR)
            textSize =
                typedArray.getDimensionPixelSize(
                    R.styleable.RatingView_textSize,
                    DEFAULT_TEXT_SIZE_SP
                )
            startAngle =
                typedArray.getInt(R.styleable.RatingView_startAngle, DEFAULT_START_ANGLE)
            isFillBackgroundEnabled =
                typedArray.getBoolean(R.styleable.RatingView_fillBackground, false)
            direction =
                typedArray.getInt(R.styleable.RatingView_direction, direction)

            val cap = typedArray.getInt(R.styleable.RatingView_ratingCap, CAP_ROUND)
            ratingStrokeCap = if (cap == CAP_BUTT) Cap.BUTT else Cap.ROUND

            typedArray.recycle()
        }
    }

    private fun ratingPaintSetup() {
        ratingPaint = Paint()
        ratingPaint.strokeCap = ratingStrokeCap
        ratingPaint.strokeWidth = ratingStrokeWidth.toFloat()
        ratingPaint.style = Paint.Style.STROKE
        ratingPaint.color = ratingColor
        ratingPaint.isAntiAlias = true
    }

    private fun ratingBackgroundPaintSetup() {
        val ratingBackgroundStyle =
            if (isFillBackgroundEnabled) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
        ratingBackgroundPaint = Paint()
        ratingBackgroundPaint.style = ratingBackgroundStyle
        ratingBackgroundPaint.strokeWidth = ratingBackgroundStrokeWidth.toFloat()
        ratingBackgroundPaint.color = ratingBackgroundColor
        ratingBackgroundPaint.isAntiAlias = true
    }

    private fun ratingTextPaintSetup() {
        ratingTextPaint = TextPaint()
        (ratingTextPaint as TextPaint).strokeCap = Cap.ROUND
        (ratingTextPaint as TextPaint).color = textColor
        (ratingTextPaint as TextPaint).isAntiAlias = true
        (ratingTextPaint as TextPaint).textSize = textSize.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = MeasureSpec.getSize(heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        ratingText.length.let { ratingTextPaint.getTextBounds(ratingText, 0, it, textBoundsRect) }

        val widthCorrection = widthCorrectionCalc()

        val finalWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> measuredWidth
            MeasureSpec.AT_MOST -> min(widthCorrection, measuredWidth)
            else -> widthCorrection
        }

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> measuredHeight
            MeasureSpec.AT_MOST -> min(widthCorrection, measuredHeight)
            else -> widthCorrection
        }

        val widthWithoutPadding = finalWidth - paddingLeft - paddingRight
        val heightWithoutPadding = finalHeight - paddingTop - paddingBottom

        val smallestSide = min(heightWithoutPadding, widthWithoutPadding)
        setMeasuredDimension(smallestSide, smallestSide)
    }

    private fun widthCorrectionCalc(): Int {
        val strokeSizeOffset = max(ratingPaint.strokeWidth, ratingBackgroundPaint.strokeWidth)
        val widthCorrection = 150
        var desiredSize: Int =
            strokeSizeOffset.toInt() + dp2px(widthCorrection.toFloat()) +
                    max(paddingBottom + paddingTop, paddingLeft + paddingRight)

        // Multiply by .1f to have extra space for small padding between text and circle
        desiredSize += max(
            textBoundsRect.width(),
            textBoundsRect.height()
        ) + desiredSize * .1f.toInt()

        return desiredSize
    }

    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            .toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        calculateBounds(w, h)
    }

    private fun calculateBounds(w: Int, h: Int) {
        radius = w / 2f
        val ratingWidth = ratingPaint.strokeWidth
        val ratingBackgroundWidth = ratingBackgroundPaint.strokeWidth

        // strokeSizeOffset prevents rating from drawing over the bounds
        val strokeSizeOffset = max(ratingWidth, ratingBackgroundWidth)
        val halfOffset = strokeSizeOffset / 2f

        circleBounds.left = halfOffset
        circleBounds.top = halfOffset
        circleBounds.right = w - halfOffset
        circleBounds.bottom = h - halfOffset
        radius = circleBounds.width() / 2f

        calculateTextBounds()
    }

    override fun onDraw(canvas: Canvas?) {
        drawRatingBackground(canvas)
        drawRating(canvas)
        drawText(canvas)
    }

    private fun drawRatingBackground(canvas: Canvas?) {
        canvas!!.drawArc(
            circleBounds,
            ANGLE_START_BACKGROUND.toFloat(),
            ANGLE_END_BACKGROUND.toFloat(),
            false,
            ratingBackgroundPaint
        )
    }

    private fun drawRating(canvas: Canvas?) {
        canvas!!.drawArc(
            circleBounds,
            startAngle.toFloat(),
            sweepAngle.toFloat(),
            false,
            ratingPaint
        )
    }

    private fun drawText(canvas: Canvas?) {
        canvas!!.drawText(ratingText, textX, textY, ratingTextPaint)
    }

    private fun setFormattedRatingText() {
        ratingText = "${ratingValue.toInt()}%"
    }

    private fun calculateTextBounds(): Rect? {
        val textRect = Rect()
        ratingTextPaint.getTextBounds(ratingText, 0, ratingText.length, textRect)
        textX = circleBounds.centerX() - textRect.width() / 2f
        textY = circleBounds.centerY() + textRect.height() / 2f
        return textRect
    }

}
