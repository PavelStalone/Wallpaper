package com.pasha.android.wallpaper3d.drawer

import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.MotionEvent
import com.pasha.android.wallpaper3d.toRadians
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class DrawerCircle : Drawer() {
    val numsPlanet = 5
    val radPlanet = 10f
    var velocity = 800.0
    val velocityRange = -3000.0..3000.0
    val numTrajectory = 5
    val paddingOrbits = 200
    val innerPaddingOrbits = 100
    val coordinate =
        MutableList<Double>(numsPlanet) { index -> 0.0 + Random.nextDouble(0.0, 360.0) }

    init {
        paint.color = Color.WHITE
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        val minRad = min(canvas.width, canvas.height)

        for (i in coordinate.indices) {
            val radiusOrbit = (((minRad - paddingOrbits - innerPaddingOrbits) / 2) / numsPlanet) * (i + 1) + innerPaddingOrbits / 2
            coordinate[i] = (coordinate[i] + velocity / radiusOrbit) % 360.0

            canvas.drawCircle(
                (canvas.width.toFloat() / 2) + (cos(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                (canvas.height.toFloat() / 2) + (sin(coordinate[i].toRadians()) * radiusOrbit).toFloat(),
                radPlanet,
                paint
            )

            val shiftTrajectory = velocity / radiusOrbit

            for (j in 1..numTrajectory) {
                canvas.drawCircle(
                    (canvas.width.toFloat() / 2) + (cos((coordinate[i] - j * shiftTrajectory).toRadians()) * radiusOrbit).toFloat(),
                    (canvas.height.toFloat() / 2) + (sin((coordinate[i] - j * shiftTrajectory).toRadians()) * radiusOrbit).toFloat(),
                    (radPlanet - j * (radPlanet / numTrajectory)),
                    paint
                )
            }
        }
    }

    var oldY: Float? = null

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
