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
 * I don't want to bore you with the details, to be honest I don't even want to be here, all I want to do is make enough money
 * so that my present and future family live the greatest lifestyle. Then I can go in peace. Because no one on this earth wants a
 * guy like me, the only people that would hopefully love me are in another life.
 * But before that, I have to escape these shit-holes we call a 9 to 5 and school, and to
 * do that I must become the best version of myself. Coding will not accomplish this goal. And besides what's the fucking
 * point if I could just use AI to make this 10 times better. What a fucking joke. I earn more money trading and affiliate
 * marketing then the fucking teacher or whoever is marking this paper.
 * Why do I even try. Besides I fucking suck at coding haha, fuck my life.
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
    val mainWindow = MainWindow(app)         // Create and show the UI, using the app model
    val mapWindow = SubWindow()
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
    private lateinit var map: SubWindow

    /**
     * Configure the UI and display it
     */
    init {
        configureWindow()               // Configure the window
        addControls()                   // Build the UI
        setLocationRelativeTo(null)     // Centre the window
        updateView()
        isVisible = true
    }

    /**
     * Configure the main window
     */
    private fun configureWindow() {
        title = "Last Breath"
        contentPane.preferredSize = Dimension(600, 350)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null
        pack()
    }

    /**
     * Populate the UI with UI controls
     */
    private fun addControls() {
        val baseFont = Font(Font.SANS_SERIF, Font.BOLD, 16)
        val bigFont = Font(Font.SANS_SERIF, Font.BOLD, 20)

        map = SubWindow()

        roomLabel = JLabel("Why")
        roomLabel.horizontalAlignment = SwingConstants.LEFT
        roomLabel.bounds = Rectangle(25, 100, 325, 50)
        roomLabel.font = baseFont
        add(roomLabel)

        // Description of the rooms
        descriptionLabel = JLabel().apply {
            bounds = Rectangle(30, 70, 540, 100)
            font = baseFont

        }
        add(descriptionLabel)

        mapButton = JButton("Map").apply {
            bounds = Rectangle(410, 260, 150, 60)
            font = baseFont
        }
        add(mapButton)
        mapButton.addActionListener(this)

        //=Navigation buttons======================================================//

        // North Button
        northButton = JButton("North")
        northButton.bounds = Rectangle(250, 190, 100, 30)
        northButton.font = baseFont
        northButton.addActionListener(this)     // Handle any clicks
        northButton.isFocusable = false            // Prevent from capturing key events
        add(northButton)

        // South Button
        southButton = JButton("South")
        southButton.bounds = Rectangle(250, 250, 100, 30)
        southButton.font = baseFont
        southButton.addActionListener(this)     // Handle any clicks
        southButton.isFocusable = false            // Prevent from capturing key events
        add(southButton)

        // East Button
        eastButton = JButton("East")
        eastButton.bounds = Rectangle(360, 220, 100, 30)
        eastButton.font = baseFont
        eastButton.addActionListener(this)     // Handle any clicks
        eastButton.isFocusable = false            // Prevent from capturing key events
        add(eastButton)


        // West Button
        westButton = JButton("Down")
        westButton.bounds = Rectangle(140, 220, 100, 30)
        westButton.font = baseFont
        westButton.addActionListener(this)     // Handle any clicks
        westButton.isFocusable = false            // Prevent from capturing key events
        add(westButton)

    }

    /**
     * Update the UI controls based on the current state
     * of the application model
     */
    fun updateView() {
        roomLabel.text = app.currentRoom.name
        descriptionLabel.text = "<html>${app.currentRoom.description}</html>"  // HTML allows text wrapping

        // Enable/disable buttons based on available exits
        northButton.isEnabled = app.currentRoom.north != null
        southButton.isEnabled = app.currentRoom.south != null
        eastButton.isEnabled = app.currentRoom.east != null
        westButton.isEnabled = app.currentRoom.west != null
    }

    /**
     * Handle any UI events (e.g. button clicks)
     * Usually this involves updating the application model
     * then refreshing the UI view
     */
    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
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

class SubWindow() : JFrame() {
    // Fields to hold the UI elements
    private lateinit var startMap: JLabel
    private lateinit var securityMap: JLabel
    private lateinit var weaponsMap: JLabel
    private lateinit var labMap: JLabel
    //========================================//
    private lateinit var comsMap: JLabel
    private lateinit var trashMap: JLabel
    private lateinit var oxygenMap: JLabel
    private lateinit var gardenMap: JLabel
    //========================================//
    private lateinit var hangerMap: JLabel
    private lateinit var engineMap: JLabel
    private lateinit var storeroomMap: JLabel
    private lateinit var cafeMap: JLabel
    //========================================//
    private lateinit var airlockMap: JLabel
    private lateinit var crewMap: JLabel
    private lateinit var medBayMap: JLabel
    private lateinit var observeMap: JLabel

    /**
     * Configure the UI and display it
     */
    init {
        configureWindow()               // Configure the window
        addControls()                   // Build the UI
        setLocationRelativeTo(null)     // Centre the window
        isVisible = false              // Make it invisible initially
    }

    /**
     * Configure the MAP window
     */
    private fun configureWindow() {
        title = "Map Of Ship"
        contentPane.preferredSize = Dimension(750, 450)
        isResizable = false
        layout = null
        pack()
    }

    /**
     * Populate the UI with UI controls
     */
    private fun addControls() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 10)

        securityMap = JLabel("Security").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(35, 50, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        startMap = JLabel("Start").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(35, 150, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        weaponsMap = JLabel("Weapons").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(35, 250, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        labMap = JLabel("Lab").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(35, 350, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        //=====================================================================//

        comsMap = JLabel("Communications").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(170, 50, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        trashMap = JLabel("Trash Unit").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(170, 150, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        oxygenMap = JLabel("Oxygen").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(170, 250, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        gardenMap = JLabel("Gardens").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(170, 350, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        //=====================================================================//

        hangerMap = JLabel("Hanger").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(305, 50, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        engineMap = JLabel("Engine").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(305, 150, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        storeroomMap = JLabel("Storage").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(305, 250, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        cafeMap = JLabel("Cafe").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(305, 350, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        //=====================================================================//

        airlockMap = JLabel("Airlock").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(440, 50, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        crewMap = JLabel("Crew Quarters").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(440, 150, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        medBayMap = JLabel("Med Bay").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(440, 250, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }

        observeMap = JLabel("Observatories").apply {
            horizontalAlignment = SwingConstants.CENTER
            bounds = Rectangle(440, 350, 100, 50)
            border = BorderFactory.createLineBorder(Color.GRAY, 3)
            font = baseFont
            add(this)
        }
    }
}

