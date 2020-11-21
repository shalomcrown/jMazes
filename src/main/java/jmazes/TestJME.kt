package jmazes

import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.font.BitmapText
import com.jme3.material.Material
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Box

class RenderableMaze(
                    val application: SimpleApplication,
                    rows: Int, cols: Int,
                     entry: RowCol = RowCol(0,0),
                     exit: RowCol = RowCol(rows, cols),
                     entrySide : Directions = Directions.WEST,
                     exitSide : Directions = Directions.EAST,
                            ) : Maze(rows, cols, entry, exit, entrySide, exitSide) {

    private var floor : Geometry? = null
    var cellWalls = List(rows * cols) { HashMap<Directions, Geometry>() }
    var floorNode = Node("floor")

    var geometry : Node? = null
        get() = this.floorNode

    fun getWall(row: Int, col: Int, direction: Directions): Geometry? = cellWalls[row * rows + col][direction]
    fun setWall(wall: Geometry, row: Int, col: Int, direction: Directions) {
        cellWalls[row * cols + col] [direction] =  wall
    }

    init {
        updateModel()
    }

    // ====================================================================

    fun updateModel() {
        var cellWidth = 1f / cols
        var cellHeight = 1f / rows
        var wallWidth = 0.01f
        var wallHeight = 0.4f

        if (floor == null) {
            var base = Box(0.5f, 0.2f, 0.5f)
            floor = Geometry("Box", base)

            var mat = Material(application.assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
            mat.setTexture("ColorMap",
                    application.assetManager.loadTexture("andrew-buchanan-O5vuEWpsSxw-unsplash.jpg"))

            floor?.material = mat // set the cube's material
            application.rootNode.attachChild(floorNode)
            floorNode.attachChild(floor)
        }

        var wallMaterial = Material(application.assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
        wallMaterial.setTexture( "ColorMap",application.assetManager.loadTexture( "keith-misner-h0Vxgz5tyXA-unsplash.jpg"))

        for (rowIndex in 0 until rows) {
            for (cellIndex in 0 until cols) {
                var cellTopY = cellWidth * rowIndex - 1f
                var cellLeftX = cellHeight * cellIndex -1f

                if (getWall(rowIndex, cellIndex, Directions.EAST) == null) {
                    var wall = Geometry("${cellIndex}-${rowIndex}-EAST",
                            Box(wallWidth / 2f, wallHeight, cellHeight / 2f))

                    wall.material = wallMaterial
                    wall.move(cellLeftX + 0.5f, wallHeight, cellTopY  + 0.5f)
                    floorNode.attachChild(wall)

                    setWall(wall, rowIndex, cellIndex, Directions.EAST)
                }

                if (getWall(rowIndex, cellIndex, Directions.WEST) == null) {
                    var wall = Geometry("${cellIndex}-${rowIndex}-EAST",
                            Box(wallWidth / 2f, wallHeight, cellHeight / 2f))

                    wall.material = wallMaterial
                    wall.move(cellLeftX + cellWidth + 0.5f, wallHeight, cellTopY  + 0.5f)
                    floorNode.attachChild(wall)

                    setWall(wall, rowIndex, cellIndex, Directions.EAST)
                }

                if (getWall(rowIndex, cellIndex, Directions.NORTH) == null) {
                    var wall = Geometry("${cellIndex}-${rowIndex}-EAST",
                            Box(cellWidth / 2f, wallHeight, wallWidth / 2f))

                    wall.material = wallMaterial
                    wall.move(cellLeftX + 0.5f + cellWidth / 2, wallHeight, cellTopY  + 0.5f + cellHeight / 2)
                    floorNode.attachChild(wall)

                    setWall(wall, rowIndex, cellIndex, Directions.EAST)
                }

                if (getWall(rowIndex, cellIndex, Directions.SOUTH) == null) {
                    var wall = Geometry("${cellIndex}-${rowIndex}-EAST",
                            Box(cellWidth / 2f, wallHeight, wallWidth / 2f))

                    wall.material = wallMaterial
                    wall.move(cellLeftX + 0.5f + cellWidth / 2, wallHeight, cellTopY  + 0.5f - cellHeight / 2)
                    floorNode.attachChild(wall)

                    setWall(wall, rowIndex, cellIndex, Directions.EAST)
                }
            }
        }
    }
}

// ====================================================================

public class TestJMEKT : SimpleApplication() {

    private lateinit var maze: RenderableMaze

    override fun simpleInitApp() {

        maze = RenderableMaze(this,10, 10)

        rootNode.attachChild(maze.geometry)

        var font = assetManager.loadFont("Interface/Fonts/Default.fnt")
        var helloText = BitmapText(font, false)
        helloText.size = 1f
        helloText.text = "Cube"
        helloText.setLocalTranslation(1.3f, 1f, 2f)
        rootNode.attachChild(helloText)

        camera.location = Vector3f(0f, 10f, 10f)
        camera.lookAt(Vector3f(0f, 0f, 0f), Vector3f(0f, 1f, 0f))

    }

    override fun simpleUpdate(tpf: Float) {
        //maze.geometry?.rotate(0f, 2 * tpf, tpf)
        super.simpleUpdate(tpf)

    }
}

fun main() = TestJMEKT().start()
