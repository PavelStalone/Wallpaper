package com.pasha.android.wallpaper3d.engine3d

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import com.pasha.android.wallpaper3d.toRadians
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class Camera(val scene: Scene, val position: Point3D = Point3D(5.0, 40.0, 5.0)) {

    val offsetY = 400.0
    val offsetX = 10.0
    val angleW = 20.0
    val angleH = 20.0
    val angle = Angle3D(0.0, 0.0, 180.0)
    var minSize: Double = 0.0
    var maxSize: Double = 0.0

    fun scan(canvas: Canvas, paint: Paint) {
        minSize = min(canvas.width, canvas.height).toDouble()
        maxSize = max(canvas.width, canvas.height).toDouble()

        val models = scene.models
        models.sortWith { m1, m2 ->
            (getDistance(m2.position) - getDistance(m1.position)).toInt()
        }

        for (model in models) {
            for ((start, end) in model.lines) {
                val relativePointStart: Point3D = toRelativeForCamera(start)
                val relativePointEnd: Point3D = toRelativeForCamera(end)
                if (isVisible(relativePointStart) && isVisible(relativePointEnd)) {
                    val pointStart: Point = getPoint(relativePointStart)
                    val pointEnd: Point = getPoint(relativePointEnd)
                    canvas.drawLine(
                        (offsetX + pointStart.x).toFloat(), (offsetY + pointStart.y).toFloat(),
                        (offsetX + pointEnd.x).toFloat(), (offsetY + pointEnd.y).toFloat(),
                        paint
                    )
                }
            }
            for (point in model.points) {
                point.hide = false
            }
        }
    }

    private fun getDistance(point: Point3D): Double {
        val (x, y, z) = toRelativeForCamera(point)
        return sqrt(x * x + y * y + z * z)
    }

    private fun toRelativeForCamera(absolut: Point3D): Point3D {
        var relativePoint =
            Point3D(absolut.x - position.x, absolut.y - position.y, absolut.z - position.z)
        if (angle.angleZ != 0.0) relativePoint =
            cameraRotateForZ(angle.angleZ, relativePoint, Point3D(0.0, 0.0, 0.0))
        if (angle.angleY != 0.0) relativePoint =
            cameraRotateForY(angle.angleY, relativePoint, Point3D(0.0, 0.0, 0.0))
        if (angle.angleX != 0.0) relativePoint =
            cameraRotateForX(angle.angleX, relativePoint, Point3D(0.0, 0.0, 0.0))
        relativePoint.x = -relativePoint.x
        relativePoint.z = -relativePoint.z
        return relativePoint
    }

    private fun cameraRotateForX(angle: Double, point: Point3D, centerForRotate: Point3D): Point3D {
        var rotatedPoints: Point3D = point.toRelative(centerForRotate)
        rotatedPoints = Point3D(
            rotatedPoints.x + centerForRotate.x,
            rotatedPoints.y * cos(angle.toRadians()) - rotatedPoints.z * sin(angle.toRadians()) + centerForRotate.y,
            rotatedPoints.y * sin(angle.toRadians()) + rotatedPoints.z * cos(angle.toRadians()) + centerForRotate.z
        )
        return rotatedPoints
    }

    private fun cameraRotateForY(angle: Double, point: Point3D, centerForRotate: Point3D): Point3D {
        var rotatedPoints: Point3D = point.toRelative(centerForRotate)
        rotatedPoints = Point3D(
            rotatedPoints.x * cos(angle.toRadians()) - rotatedPoints.z * sin(angle.toRadians()) + centerForRotate.x,
            rotatedPoints.y + centerForRotate.y,
            rotatedPoints.x * sin(angle.toRadians()) + rotatedPoints.z * cos(angle.toRadians()) + centerForRotate.z
        )
        return rotatedPoints
    }

    private fun cameraRotateForZ(angle: Double, point: Point3D, centerForRotate: Point3D): Point3D {
        var rotatedPoints: Point3D = point.toRelative(centerForRotate)
        rotatedPoints = Point3D(
            rotatedPoints.x * cos(angle.toRadians()) - rotatedPoints.y * sin(angle.toRadians()) + centerForRotate.x,
            rotatedPoints.x * sin(angle.toRadians()) + rotatedPoints.y * cos(angle.toRadians()) + centerForRotate.y,
            rotatedPoints.z + centerForRotate.z
        )
        return rotatedPoints
    }

    private fun getPoint(pointInCam: Point3D): Point {
        return Point(
            (minSize / 2 / (tan(angleW.toRadians()) * pointInCam.y) * pointInCam.x + minSize / 2).toInt(),
            (minSize / 2 / (tan(angleH.toRadians()) * pointInCam.y) * pointInCam.z + minSize / 2).toInt()
        )
    }

    private fun isVisible(point: Point3D): Boolean {
        var visible = point.y >= 0
        if (visible) {
            visible = (point.x <= tan(angleW.toRadians()) * point.y
                    && point.z <= tan(angleH.toRadians()) * point.y)
        }
        return visible
    }
}