package com.pasha.android.wallpaper3d

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.pasha.android.wallpaper3d.drawer.Drawer
import com.pasha.android.wallpaper3d.drawer.DrawerShark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = WallpaperEngine()

    inner class WallpaperEngine : Engine() {
        private var surfaceHolder: SurfaceHolder? = null

        var coroutineScope = CoroutineScope(Dispatchers.IO)
        val drawer: Drawer = DrawerShark()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        private fun run() {
            coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                while (true) {
                    draw()
                    delay(10)
                }
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
            this@WallpaperEngine.surfaceHolder = surfaceHolder
        }

        override fun onDestroy() {
            super.onDestroy()
        }

        override fun onTouchEvent(event: MotionEvent?) {
            super.onTouchEvent(event)
//            if (event != null) {
//                drawer.onEvent(event)
//            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                if (!coroutineScope.isActive) {
                    run()
                }
            } else {
                coroutineScope.cancel("VisibilityChanged")
            }
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder?,
            format: Int,
            width: Int,
            height: Int
        ) {
            super.onSurfaceChanged(holder, format, width, height)
            surfaceHolder = holder
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder?) {
            super.onSurfaceRedrawNeeded(holder)
            surfaceHolder = holder
            draw()
        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            surfaceHolder = holder
            run()
            sensorManager.registerListener(drawer, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            coroutineScope.cancel("SurfaceDestroyed")
            sensorManager.unregisterListener(drawer)
        }

        private fun draw() {
            surfaceHolder?.let { surfaceHolder ->
                try {
                    val canvas = surfaceHolder.lockCanvas()

                    canvas?.apply {
                        drawer.draw(this)
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas)
                } catch (exception: Exception) {
                    Log.e("Main", exception.message, exception)
                }
            }
        }
    }
}
