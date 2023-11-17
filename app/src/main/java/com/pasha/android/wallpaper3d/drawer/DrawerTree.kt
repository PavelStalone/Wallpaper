package com.pasha.android.wallpaper3d.drawer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Shader
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.MotionEvent
import com.pasha.android.wallpaper3d.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class DrawerTree : Drawer() {
    val treeDepth = 15

    val foliagePaint = Paint()

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 7f

        foliagePaint.color = Color.CYAN
        foliagePaint.strokeWidth = 5f
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        foliagePaint.shader = LinearGradient(canvas.width/3f,canvas.height/3f,canvas.width.toFloat(), canvas.height/2f, Color.CYAN, Color.BLACK, Shader.TileMode.CLAMP)
        canvas.drawLine(canvas.width/2f, canvas.height - canvas.height/4f, canvas.width/2f, canvas.height - canvas.height/3f, paint)
        trees(treeDepth, 200f, canvas, canvas.width/2f, canvas.height - canvas.height/3f, 90.0)
    }

    private fun trees(depth: Int, distance: Float, canvas: Canvas, prePositionX: Float, prePositionY: Float, currentAngle: Double) {
        val leftAngle = Random.nextDouble(10.0, 25.0)
        val rightAngle = Random.nextDouble(10.0, 25.0)

        if (depth > 0) {
            val leftCurrentPositionX = prePositionX + (cos((currentAngle + leftAngle).toRadians()) * distance).toFloat()
            val leftCurrentPositionY = prePositionY - (sin((currentAngle + leftAngle).toRadians()) * distance).toFloat()
            canvas.drawLine(
                prePositionX,
                prePositionY,
                leftCurrentPositionX,
                leftCurrentPositionY,
                paint
            )

            val rightCurrentPositionX = prePositionX + (cos((currentAngle - rightAngle).toRadians()) * distance).toFloat()
            val rightCurrentPositionY = prePositionY - (sin((currentAngle - rightAngle).toRadians()) * distance).toFloat()
            canvas.drawLine(
                prePositionX,
                prePositionY,
                rightCurrentPositionX,
                rightCurrentPositionY,
                paint
            )
            trees(depth - 1, distance * Random.nextDouble(0.6, 0.8).toFloat(), canvas, leftCurrentPositionX, leftCurrentPositionY, currentAngle + leftAngle)
            trees(depth - 1, distance * Random.nextDouble(0.6, 0.8).toFloat(), canvas, rightCurrentPositionX, rightCurrentPositionY, currentAngle - rightAngle)
        } else {
            canvas.drawCircle(prePositionX, prePositionY, 6f, foliagePaint)
        }
    }

    override fun onEvent(event: MotionEvent) {
        //TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO("Not yet implemented")
    }
}
