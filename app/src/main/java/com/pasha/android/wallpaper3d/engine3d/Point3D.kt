package com.pasha.android.wallpaper3d.engine3d

import com.pasha.android.wallpaper3d.toRadians
import kotlin.math.cos
import kotlin.math.sin


data class Point3D(var x: Double, var y: Double, var z: Double, var hide: Boolean = false) {

    operator fun plus(point3D: Point3D): Point3D =
        this.copy(this.x + point3D.x, this.y + point3D.y, this.z + point3D.z)

    operator fun plusAssign(point3D: Point3D) {
        this.x += point3D.x
        this.y += point3D.y
        this.z += point3D.z
    }

    operator fun minus(point3D: Point3D): Point3D =
        this.copy(this.x - point3D.x, this.y - point3D.y, this.z - point3D.z)

    operator fun minusAssign(point3D: Point3D) {
        this.x -= point3D.x
        this.y -= point3D.y
        this.z -= point3D.z
    }

    fun rotateForZ(angle: Double, centerForRotate: Point3D) {
        val rotatedPoints = this.toRelative(centerForRotate)
        this.x = (rotatedPoints.x * cos(angle) + rotatedPoints.y * sin(angle)) + centerForRotate.x
        this.y = (-rotatedPoints.x * sin(angle) + rotatedPoints.y * cos(angle)) + centerForRotate.y
        this.z = rotatedPoints.z + centerForRotate.z
    }

    fun rotateForX(angle: Double, centerForRotate: Point3D) {
        val rotatedPoints = this.toRelative(centerForRotate)
        this.x = rotatedPoints.x + centerForRotate.x
        this.y = rotatedPoints.y * cos(angle.toRadians()) + rotatedPoints.z * sin(angle.toRadians()) + centerForRotate.y
        this.z = -rotatedPoints.y * sin(angle.toRadians()) + rotatedPoints.z * cos(angle.toRadians()) + centerForRotate.z
    }

    fun rotateForY(angle: Double, centerForRotate: Point3D) {
        val rotatedPoints = this.toRelative(centerForRotate)
        this.x = rotatedPoints.x * cos(angle.toRadians()) + rotatedPoints.z * sin(angle.toRadians()) + centerForRotate.x
        this.y = rotatedPoints.y + centerForRotate.y
        this.z = -rotatedPoints.x * sin(angle.toRadians()) + rotatedPoints.z * cos(angle.toRadians()) + centerForRotate.z
    }
}

fun Point3D.toRelative(newSystem: Point3D): Point3D =
    Point3D(this.x - newSystem.x, this.y - newSystem.y, this.z - newSystem.z)