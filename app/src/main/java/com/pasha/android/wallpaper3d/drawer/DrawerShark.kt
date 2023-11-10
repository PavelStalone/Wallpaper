package com.pasha.android.wallpaper3d.drawer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.MotionEvent
import com.pasha.android.wallpaper3d.toRadians
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class DrawerShark : Drawer() {
    val numsShark = 5
    var velocity = 800.0
    val velocityRange = -3000.0..3000.0
    val paddingSharks = 200
    val innerPaddingSharks = 100
    val coordinate =
        MutableList<Double>(numsShark) { index -> 0.0 + Random.nextDouble(0.0, 360.0) }

    init {
        paint.color = Color.WHITE
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        val minRad = min(canvas.width, canvas.height)

        for (i in coordinate.indices) {
            val radiusOrbit =
                (((minRad - paddingSharks - innerPaddingSharks) / 2) / numsShark) * (i + 1) + innerPaddingSharks / 2
            coordinate[i] = (coordinate[i] + velocity / radiusOrbit) % 360.0

            canvas.drawBitmap(
                drawShark(50, coordinate[i].toFloat() + 45),
                (canvas.width.toFloat() / 2) + (cos(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                (canvas.height.toFloat() / 2) + (sin(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                paint
            )

            val shiftTrajectory = velocity / radiusOrbit

//            for (j in 1..numTrajectory) {
//                canvas.drawCircle(
//                    (canvas.width.toFloat() / 2) + (cos((coordinate[i] - j * shiftTrajectory).toRadians()) * radiusOrbit).toFloat(),
//                    (canvas.height.toFloat() / 2) + (sin((coordinate[i] - j * shiftTrajectory).toRadians()) * radiusOrbit).toFloat(),
//                    (radPlanet - j * (radPlanet / numTrajectory)),
//                    paint
//                )
//            }
        }
    }

    var oldY: Float? = null

    private fun Canvas.drawPolygon(points: FloatArray) {
        val wallpaint = Paint()
        wallpaint.color = Color.WHITE
        wallpaint.style = Paint.Style.FILL

        val wallpath = Path()
        wallpath.reset()
        wallpath.moveTo(points[0], points[1])
        for (i in points.indices step 2) {
            wallpath.lineTo(points[i], points[i + 1])
        }
        wallpath.lineTo(points[0], points[1])

        this.drawPath(wallpath, wallpaint)
    }

    private fun drawShark(size: Int, angle: Float = 0f): Bitmap {
        val bitmapSize = (size * 1.2f).toInt()
        val bitmap = Bitmap.createBitmap(
            bitmapSize,
            bitmapSize,
            Bitmap.Config.ARGB_8888
        )

        val scaleFactor = size / 90

        val koef: Float = when {
            angle > 270 -> (90 - angle % 90) * scaleFactor
            angle > 180 -> (angle % 90) * scaleFactor
            angle > 90 -> (90 - angle % 90) * scaleFactor
            angle > 0 -> (angle % 90) * scaleFactor
            else -> 0.8f
        }

        val canvasShark = Canvas(bitmap)
        canvasShark.rotate(angle, bitmapSize / 2f, bitmapSize / 2f)
        canvasShark.drawPolygon(
            floatArrayOf(
                bitmapSize * 0.2f,
                bitmapSize * 0.2f,
                bitmapSize * 0.8f,
                bitmapSize * 0.2f,
                bitmapSize * 0.8f,
                bitmapSize * koef
            )
        )
        return bitmap
    }

    override fun onEvent(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                oldY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                oldY?.let {
                    if (velocity + (it - event.y) in velocityRange) {
                        velocity = velocity + (it - event.y)
                    }
                }
                oldY = event.y
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //TODO("Not yet implemented")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO("Not yet implemented")
    }
}
