package com.example.customview

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class FaceView(context: Context, attrs: AttributeSet): View(context, attrs) {
    companion object {
        private const val DEFAULT_FACE_COLOR = Color.YELLOW
        private const val DEFAULT_EYES_COLOR = Color.BLACK
        private const val DEFAULT_MOUTH_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_WIDTH = 4.0f

        const val USUAL = 0L
        const val WITHOUTMOUTH = 1L
    }

    private var faceColor = DEFAULT_FACE_COLOR
    private var eyesColor = DEFAULT_EYES_COLOR
    private var mouthColor = DEFAULT_MOUTH_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    private val paint = Paint()
    private val mouth = Path()
    private var size = 0

    var faceState = USUAL
        set(state) {
            field = state
            invalidate()
        }

    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.FaceView,
            0, 0)

        faceState = typedArray.getInt(R.styleable.FaceView_state, USUAL.toInt()).toLong()
        faceColor = typedArray.getColor(R.styleable.FaceView_faceColor, DEFAULT_FACE_COLOR)
        eyesColor = typedArray.getColor(R.styleable.FaceView_eyesColor, DEFAULT_EYES_COLOR)
        mouthColor = typedArray.getColor(R.styleable.FaceView_mouthColor, DEFAULT_MOUTH_COLOR)
        borderColor = typedArray.getColor(R.styleable.FaceView_borderColor,
            DEFAULT_BORDER_COLOR)
        borderWidth = typedArray.getDimension(R.styleable.FaceView_borderWidth,
            DEFAULT_BORDER_WIDTH)

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawFaceBackground(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    private fun drawFaceBackground(canvas: Canvas) {
        paint.color = faceColor
        paint.style = Paint.Style.FILL

        val radius = size / 2f

        canvas.drawCircle(size / 2f, size / 2f, radius, paint)

        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

        canvas.drawCircle(size / 2f, size / 2f, radius - borderWidth / 2f, paint)

    }

    private fun drawEyes(canvas: Canvas) {
        paint.color = eyesColor
        paint.style = Paint.Style.FILL

        canvas.drawCircle(size * 0.35f, size / 2.7f, size * 0.1f, paint)

        canvas.drawCircle(size * 0.65f, size / 2.7f, size * 0.1f, paint)

    }

    private fun drawMouth(canvas: Canvas) {

        mouth.reset()

        mouth.moveTo(size * 0.22f, size * 0.7f)

        if (faceState == USUAL) {
            mouth.quadTo(size * 0.50f, size * 0.80f, size * 0.78f, size * 0.70f)
            mouth.quadTo(size * 0.50f, size * 1f, size * 0.22f, size * 0.70f)
        }


        paint.color = mouthColor
        paint.style = Paint.Style.FILL

        canvas.drawPath(mouth, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = Math.min(measuredWidth, measuredHeight)

        setMeasuredDimension(size, size)
    }
}