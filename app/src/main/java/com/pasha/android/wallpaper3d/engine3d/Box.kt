package com.pasha.android.wallpaper3d.engine3d

class Box(
    override var position: Point3D
) : Model3D() {
    init {
        height = 10.0
        widht = 10.0
        depth = 10.0

        centerForRotate = Point3D(position.x + widht / 2, position.y + height / 2, position.z + depth / 2)
        angle = Angle3D(0.0, 0.0, 0.0)
        points = mutableListOf(
            Point3D(position.x, position.y, position.z),
            Point3D(position.x + widht, position.y, position.z),
            Point3D(position.x + widht, position.y + height, position.z),
            Point3D(position.x, position.y + height, position.z),
            Point3D(position.x, position.y, position.z + depth),
            Point3D(position.x + widht, position.y, position.z + depth),
            Point3D(position.x + widht, position.y + height, position.z + depth),
            Point3D(position.x, position.y + height, position.z + depth)
        )

        lines = listOf(
            Line(points[0], points[1]),
            Line(points[1], points[2]),
            Line(points[2], points[3]),
            Line(points[3], points[0]),
            Line(points[0], points[4]),
            Line(points[3], points[7]),
            Line(points[2], points[6]),
            Line(points[1], points[5]),
            Line(points[7], points[4]),
            Line(points[7], points[6]),
            Line(points[4], points[5]),
            Line(points[5], points[6]),
            Line(points[5], points[4])
        )
    }
}