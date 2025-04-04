/**
 * =====================================================================
 * Programming Project for NCEA Level 3, Standard 91906
 * ---------------------------------------------------------------------
 * Project Name: Last Breath
 * Project Author: James Black
 * GitHub Repo: https://github.com/waimea-jmblack/level-3-programming-assesment
 * ---------------------------------------------------------------------
 * Notes:
 * This is a game is about escaping an alien who has boarded your ship, and has killed every last person who once
 * called this ship home, so you have to escape before you lose all your air.
 * =====================================================================
 */

import com.formdev.flatlaf.FlatDarkLaf
import java.awt.*
import java.awt.event.*
import javax.swing.*

/**
 * Launch the application
 */
fun main() {
    FlatDarkLaf.setup()     // Flat, dark look-and-feel
    val app = App()         // Create the app/game model
    MainWindow(app)         // Create and show the UI, using the app model
    SubWindow()
}

/**
 * The application class (model)
 * This is the place where any application data should be
 * stored, plus any application logic functions
 */

/**
 * Creating a class
 */
class Room(
    val name: String,
    val description: String,
) {
    // Connections to other rooms
    var north: Room? = null
    var south: Room? = null
    var east: Room? = null
    var west: Room? = null

    fun connectNorth(room: Room) {
        this.north = room
        room.south = this
    }

    fun connectSouth(room: Room) {
        this.south = room
        room.north = this
    }

    fun connectEast(room: Room) {
        this.east = room
        room.west = this
    }

    fun connectWest(room: Room) {
        this.west = room
        room.north = this
    }
}

class App {
    // Player state
    var currentRoom: Room
    var gameOver = false

    // All rooms in the game
    private val rooms = mutableListOf<Room>()

    init {
        // Initialize/ making the rooms
        val securityRoom = Room(
            "Security",
            "Blood spills from the body's of fallen comrades, this apex predator lives for the hunt",
        )

        val startRoom = Room(
            "The beginning...",
            "You wake up in the bridge shrouded in darkness, you know your not alone.",
        )

        val weaponsRoom = Room(
            "Weapons Room",
            "Horror fills you head as you come to the realisation theres no weapons."
        )

        val labRoom = Room(
            "Lab Room",
            "Glass and dangerous chemical cover the now battered floor, creating a foul odor."
        )

        //=====================================================================================================//

        val comsRoom = Room(
            "Communications",
            "You hope fades as you enter this room. " +
                    "The alien made sure to destroy any hope of communicating with any other survivors."
        )

        val trashRoom = Room(
            "Trash Room",
            "The stench of blood and trash floats through the air, you " +
                    "do not dare to explore for anything of use."
        )

        val oRoom = Room(
            "Oxygen Room",
            "Thankfully the backup generators and the oxygen are still operational otherwise i'd be out of air. "
        )

        val gardenRoom = Room(
            "Gardens",
            "All our food has been torn up and revenged by the lifeform after it finished its hunt"
        )

        //=====================================================================================================//

        val engineRoom = Room(
            "Engineering Bay",
            "Silence and the cover of darkness envelopes the once lively room of engineers",
        )

        val alienRoom = Room(
            "Alien entry room",
            "This is the darkest room of them all. Blood and destruction are the only things filling this room.",
        )

        // Connect rooms
        securityRoom.connectEast(comsRoom)
        securityRoom.connectSouth(startRoom)

        startRoom.connectNorth(securityRoom)
        startRoom.connectSouth(weaponsRoom)
        startRoom.connectEast(trashRoom)

        weaponsRoom.connectNorth(startRoom)
        weaponsRoom.connectSouth(labRoom)

        labRoom.connectNorth(weaponsRoom)
        labRoom.connectEast(gardenRoom)
        //==================================//
        comsRoom.connectNorth(securityRoom)

        trashRoom.connectWest(startRoom)
        trashRoom.connectEast(engineRoom)
        trashRoom.connectSouth(oRoom)

        oRoom.connectNorth(trashRoom)

        oRoom.connectSouth(gardenRoom)


        // Add to rooms list
        rooms.addAll(listOf(startRoom, securityRoom, weaponsRoom,
            gardenRoom, engineRoom, alienRoom, trashRoom, labRoom, oRoom))

        // Set starting room
        currentRoom = startRoom
    }

    fun move(direction: String): String {
        val newRoom = when(direction.lowercase()) {
            "north" -> currentRoom.north
            "south" -> currentRoom.south
            "east" -> currentRoom.east
            "west" -> currentRoom.west
            else -> null
        }

        return if (newRoom != null) {
            currentRoom = newRoom
            "Moved to ${newRoom.name}"
        } else {
            "You can't go that way"
        }
    }

    fun restartGame() {
        gameOver = false
        currentRoom = rooms.first() // Reset to starting room
    }
}

/**
 * Main UI window (view)
 * Defines the UI and responds to events
 * The app model should be passed as an argument
 */
class MainWindow(val app: App) : JFrame(), ActionListener {
    private lateinit var roomLabel: JLabel
    private lateinit var descriptionLabel: JLabel
    private lateinit var mapButton: JButton
    private lateinit var northButton: JButton
    private lateinit var eastButton: JButton
    private lateinit var southButton: JButton
    private lateinit var westButton: JButton
    private val map = SubWindow()

    init {
        configureWindow()
        addControls()
        setLocationRelativeTo(null)
        updateView()
        isVisible = true
    }

    private fun configureWindow() {
        title = "Last Breath"
        contentPane.preferredSize = Dimension(600, 350)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null
        pack()
    }

    private fun addControls() {
        val baseFont = Font(Font.SANS_SERIF, Font.BOLD, 16)
        val bigFont = Font(Font.SANS_SERIF, Font.BOLD, 20)

        // Room Label
        roomLabel = JLabel("Room Name")
        roomLabel.bounds = Rectangle(25, 20, 550, 30)
        roomLabel.font = bigFont
        add(roomLabel)

        // Description Label (fixed position)
        descriptionLabel = JLabel("Description")
        descriptionLabel.bounds = Rectangle(25, 60, 550, 80)
        descriptionLabel.font = baseFont
        add(descriptionLabel)

        // Navigation Buttons
        northButton = JButton("North")
        northButton.bounds = Rectangle(250, 180, 100, 30)
        northButton.font = baseFont
        northButton.addActionListener(this)
        add(northButton)

        southButton = JButton("South")
        southButton.bounds = Rectangle(250, 220, 100, 30)
        southButton.font = baseFont
        southButton.addActionListener(this)
        add(southButton)

        eastButton = JButton("East")
        eastButton.bounds = Rectangle(360, 200, 100, 30)
        eastButton.font = baseFont
        eastButton.addActionListener(this)
        add(eastButton)

        westButton = JButton("West")  // Fixed from "Down" to "West"
        westButton.bounds = Rectangle(140, 200, 100, 30)
        westButton.font = baseFont
        westButton.addActionListener(this)
        add(westButton)

        // Map Button
        mapButton = JButton("Map")
        mapButton.bounds = Rectangle(410, 260, 150, 60)
        mapButton.font = bigFont
        mapButton.addActionListener(this)
        add(mapButton)
    }

    fun updateView() {
        roomLabel.text = app.currentRoom.name
        descriptionLabel.text = "<html>${app.currentRoom.description}</html>"

        northButton.isEnabled = app.currentRoom.north != null
        southButton.isEnabled = app.currentRoom.south != null
        eastButton.isEnabled = app.currentRoom.east != null
        westButton.isEnabled = app.currentRoom.west != null
    }

    override fun actionPerformed(e: ActionEvent) {
        when (e.source) {
            mapButton -> map.isVisible = true
            northButton -> {
                app.move("north")
                updateView()
            }
            southButton -> {
                app.move("south")
                updateView()
            }
            eastButton -> {
                app.move("east")
                updateView()
            }
            westButton -> {
                app.move("west")
                updateView()
            }
        }
    }
}
class SubWindow : JFrame() {
    init {
        configureWindow()
        addControls()
        setLocationRelativeTo(null)
        isVisible = false
    }

    private fun configureWindow() {
        title = "Map Of Ship"
        contentPane.preferredSize = Dimension(750, 450)
        isResizable = false
        layout = null
        pack()
    }

    private fun addControls() {
        val securityMap = JLabel("Security")
        securityMap.bounds = Rectangle(35, 50, 100, 50)
        securityMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(securityMap)

        val startMap = JLabel("Start")
        startMap.bounds = Rectangle(35, 150, 100, 50)
        startMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(startMap)

        val weaponsMap = JLabel("Weapons")
        weaponsMap.bounds = Rectangle(35, 250, 100, 50)
        weaponsMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(weaponsMap)

        val labMap = JLabel("Lab")
        labMap.bounds = Rectangle(35, 350, 100, 50)
        labMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(labMap)

        //=======================================================================================//

        val comsMap = JLabel("Communications")
        comsMap.bounds = Rectangle(170, 50, 100, 50)
        comsMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(comsMap)

        val trashMap = JLabel("Trash Unit")
        trashMap.bounds = Rectangle(170, 150, 100, 50)
        trashMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(trashMap)

        val oxygenMap = JLabel("Oxygen")
        oxygenMap.bounds = Rectangle(170, 250, 100, 50)
        oxygenMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(oxygenMap)

        val gardenMap = JLabel("Gardens")
        gardenMap.bounds = Rectangle(170, 350, 100, 50)
        gardenMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        add(gardenMap)

        //=======================================================================================//

    }
}