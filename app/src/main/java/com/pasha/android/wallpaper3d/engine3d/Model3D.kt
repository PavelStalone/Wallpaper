package com.pasha.android.wallpaper3d.engine3d

abstract class Model3D {
    var height: Double = 0.0
    var widht: Double = 0.0
    var depth: Double = 0.0
    abstract var position: Point3D
    var points: MutableList<Point3D> = mutableListOf()
    var lines: List<Line> = listOf()
    var centerForRotate: Point3D = Point3D(0.0,0.0,0.0)
    var angle: Angle3D = Angle3D()

    fun rotate(angle: Angle3D) {
        this.angle += angle
        this.angle %= 360.0

        points.forEach {
            if (angle.angleX != 0.0) it.rotateForX(angle.angleX, centerForRotate)
            if (angle.angleY != 0.0) it.rotateForY(angle.angleY, centerForRotate)
            if (angle.angleZ != 0.0) it.rotateForZ(angle.angleZ, centerForRotate)
        }
    }
}
