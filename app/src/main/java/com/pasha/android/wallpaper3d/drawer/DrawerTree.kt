package com.pasha.android.wallpaper3d.drawer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.MotionEvent
import com.pasha.android.wallpaper3d.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class DrawerTree : Drawer() {

    init {
        paint.color = Color.WHITE
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        trees(25, 200f, canvas, canvas.width/2f, canvas.height - canvas.height/3f, 90.0)
    }

    private fun trees(depth: Int, distance: Float, canvas: Canvas, prePositionX: Float, prePositionY: Float, currentAngle: Double) {
        val leftAngle = Random.nextDouble(10.0, 45.0)
        val rightAngle = Random.nextDouble(10.0, 45.0)

        if (depth > 0) {
            val leftCurrentPositionX = prePositionX - (cos((currentAngle + leftAngle).toRadians()) * distance).toFloat()
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
            trees(depth - Random.nextInt(1, 4), distance/2, canvas, leftCurrentPositionX, leftCurrentPositionY, currentAngle + leftAngle)
            trees(depth - Random.nextInt(1, 4), distance/2, canvas, rightCurrentPositionX, rightCurrentPositionY, currentAngle - rightAngle)
        } else {
            canvas.drawCircle(prePositionX, prePositionY, 10f, paint)
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
