package com.pasha.android.wallpaper3d.drawer

import android.graphics.Canvas
import android.graphics.Paint
import android.hardware.SensorEventListener
import android.view.MotionEvent

abstract class Drawer : SensorEventListener {
    protected val paint = Paint()

    abstract fun draw(canvas: Canvas)
    abstract fun onEvent(event: MotionEvent)
}
