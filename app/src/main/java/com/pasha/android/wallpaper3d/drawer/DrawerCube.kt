package com.pasha.android.wallpaper3d.drawer

import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.view.MotionEvent
import com.pasha.android.wallpaper3d.engine3d.Angle3D
import com.pasha.android.wallpaper3d.engine3d.Camera
import com.pasha.android.wallpaper3d.engine3d.Scene

class DrawerCube : Drawer() {
    val scene = Scene()
    val camera = Camera(scene)

    init {
        paint.color = Color.WHITE
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        camera.scan(canvas, paint)
    }

    override fun onEvent(event: MotionEvent) {
        //TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            scene.models[0].rotate(
                Angle3D(
                    event.values[0].toDouble() * 5,
                    event.values[2].toDouble() * 5,
                    event.values[1].toDouble() / 10
                )
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO("Not yet implemented")
    }
}
