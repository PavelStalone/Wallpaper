package com.pasha.android.wallpaper3d.engine3d

data class Angle3D(var angleX: Double = 0.0, var angleY: Double = 0.0, var angleZ: Double = 0.0) {

    operator fun plus(angle3D: Angle3D): Angle3D =
        this.copy(
            this.angleX + angle3D.angleX,
            this.angleY + angle3D.angleY,
            this.angleZ + angle3D.angleZ
        )

    operator fun minus(angle3D: Angle3D): Angle3D =
        this.copy(
            this.angleX - angle3D.angleX,
            this.angleY - angle3D.angleY,
            this.angleZ - angle3D.angleZ
        )

    operator fun remAssign(double: Double){
        this.angleX %= double
        this.angleY %= double
        this.angleZ %= double
    }
}
