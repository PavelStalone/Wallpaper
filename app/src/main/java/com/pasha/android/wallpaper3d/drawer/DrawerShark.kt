package com.pasha.android.wallpaper3d.drawer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.MotionEvent
import com.pasha.android.wallpaper3d.toRadians
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

class DrawerShark : Drawer() {
    val numsShark = 4
    var velocity = 550.0
    val velocityRange = -3000.0..3000.0
    val paddingSharks = 200
    val innerPaddingSharks = 150
    val coordinate =
        MutableList(numsShark) { _ -> 0.0 + Random.nextDouble(0.0, 360.0) }
    val minSizeShark = 25
    val maxSizeShark = 45

    init {
        paint.color = Color.WHITE
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        val minRad = min(canvas.width, canvas.height)
        velocity = (minRad / 3).toDouble()

        for (i in coordinate.indices) {
            val radiusOrbit =
                (((minRad - paddingSharks - innerPaddingSharks) / 2) / numsShark) * (i + 1) + innerPaddingSharks / 2
            coordinate[i] = (coordinate[i] + velocity / radiusOrbit) % 360.0

            canvas.drawRipple(
                (canvas.width.toFloat() / 2) + (cos(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                (canvas.height.toFloat() / 2) + (sin(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                coordinate[i].toFloat(),
                radiusOrbit
            )
            canvas.drawShark(
                (canvas.width.toFloat() / 2) + (cos(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                (canvas.height.toFloat() / 2) + (sin(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                (i * ((maxSizeShark - minSizeShark) / numsShark) + minSizeShark),
                coordinate[i].toFloat()
            )
        }
    }

    private var oldY: Float? = null

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

    private fun Canvas.drawRipple(x: Float, y: Float, angle: Float, rad: Int) {
        val wallpaint = Paint()
        wallpaint.color = Color.WHITE
        wallpaint.strokeWidth = 3f
        wallpaint.style = Paint.Style.STROKE
        wallpaint.shader =
            RadialGradient(x, y, rad.toFloat() / 3, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP)

        this.rotate(angle, x, y)
        this.drawArc(x - rad * 2, y - rad, x, y + rad, 0f, -20f, false, wallpaint)
        this.rotate(-angle, x, y)
    }

    private fun Canvas.drawShark(x: Float, y: Float, size: Int, angle: Float = 0f) {
        val minScaled = 0.1f
        val scaleFactor = (size - size * minScaled) / 90f
        val koef: Float = when {
            angle >= 270 -> (90 - angle % 90) * scaleFactor + size * minScaled
            angle >= 180 -> (angle % 90) * scaleFactor + size * minScaled
            angle >= 90 -> (90 - angle % 90) * scaleFactor + size * minScaled
            angle >= 0 -> (angle % 90) * scaleFactor + size * minScaled
            else -> size.toFloat()
        }

        val list = mutableListOf(x - 2, y - (size / 2f))
        var t = 0.0
        while (t <= 1.0) {
            list += bezie(x - 2, x - 2 + koef / 3, x - 2 + koef, t)
            list += bezie(y - size / 2f, y - size / 6f, y - size / 2f, t)
            t += 0.1
        }
        t = 0.0
        while (t <= 1.0) {
            list += bezie(x - 2 + koef, x - 2 + koef * 0.9f, x - 2, t)
            list += bezie(y - size / 2f, y + size / 3f, y - (size / 2f) + size.toFloat(), t)
            t += 0.1
        }
        list += x - 2
        list += y - (size / 2f) + size.toFloat()

        this.rotate(angle, x, y)
        this.drawPolygon(
            list.toFloatArray()
        )
        this.rotate(-angle, x, y)
    }

    private fun bezie(point0: Float, point1: Float, point2: Float, t: Double): Float =
        ((1 - t).pow(2.0) * point0 + 2 * t * (1 - t) * point1 + t.pow(2.0) * point2).toFloat()

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
