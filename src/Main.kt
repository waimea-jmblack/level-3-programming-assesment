/**
 * =====================================================================
 * Programming Project for NCEA Level 3, Standard 91906
 * ---------------------------------------------------------------------
 * Project Name: Last Breath
 * Project Author: James Black
 * GitHub Repo: https://github.com/waimea-jmblack/level-3-programming-assesment
 * ---------------------------------------------------------------------
 * Notes:
 * PROJECT NOTES HERE
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
    val game = GameState()         // Create the app/game model
    val mapWindow = SubWindow(game)
    val mainWindow = MainWindow(game, mapWindow)         // Create and show the UI, using the app model

    val oxygenSystem = OxygenSystem(game) {
        mainWindow.updateView() // This is for the oxygen system for the character (nudge, nudge, cough 'Last Breath')
    }
    oxygenSystem.start()
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
    var hasOxygen: Boolean = true  // Some rooms might have low oxygen
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
    var oxygenLevel = 100
    var inventory = mutableListOf<String>()
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
        startRoom.connectNorth(securityRoom)
        startRoom.connectSouth(weaponsRoom)
        startRoom.connectEast(trashRoom)

        weaponsRoom.connectNorth(startRoom)
        weaponsRoom.connectSouth(labRoom)

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
            if (newRoom.hasOxygen) {
                currentRoom = newRoom
                oxygenLevel -= 10  // Decrease oxygen if the character is moving
                "Moved to ${newRoom.name}"
            } else {
                "Can't enter - ${newRoom.name} has no oxygen!"
            }
        } else {
            "You can't go that way"
        }
    }

    fun findRoomByName(name: String): Room? {
        return rooms.find { it.name.equals(name, ignoreCase = true) }
    }

    fun restartGame() {
        oxygenLevel = 100
//        inventory.clear()
        gameOver = false
        currentRoom = rooms.first() // Reset to starting room
    }
}

    /**
     * Main UI window (view)
     * Defines the UI and responds to events
     * The app model should be passwd as an argument
     */

    private fun addControls() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        class MainWindow(val game: GameState, val popUp: SubWindow) : JFrame(), ActionListener {
            private lateinit var roomLabel: JLabel
            private lateinit var descriptionLabel: JLabel
            private lateinit var oxygenBar: JProgressBar
            private lateinit var mapButton: JButton
            private lateinit var northButton: JButton


            /**
             * Configure the main window
             */
            private fun configureWindow() {
                title = "Kotlin Swing GUI Demo"
                contentPane.preferredSize = Dimension(600, 350)
                defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
                isResizable = false
                layout = null

                pack()
            }

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
             * Populate the UI with UI controls
             */
            private fun addControls() {
                // Name of the room the character is in.
                roomLabel = JLabel().apply {
                    bounds = Rectangle(30, 30, 540, 30)
                    font = Font(Font.SANS_SERIF, Font.BOLD, 20)
                    add(this)
                }

                // Description of the rooms
                descriptionLabel = JLabel().apply {
                    bounds = Rectangle(30, 70, 540, 100)
                    font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
                    add(this)
                }

                mapButton = JButton("Map")
                mapButton.bounds = Rectangle(410,260,150,60)
                mapButton.font = baseFont
                mapButton.addActionListener(this)     // Handle any clicks
                add(mapButton)

                // Oxygen bar
                oxygenBar = JProgressBar(0, 100).apply {
                    bounds = Rectangle(30, 180, 540, 20)
                    add(this)
                }

                //=Navigation buttons======================================================//

                // North Button
                northButton = JButton("North").apply {
                    bounds = Rectangle(250, 220, 100, 30)
                    addActionListener(this@MainWindow)
                    add(this)
                }

            }

            /**
             * Update the UI controls based on the current state
             * of the application model
             */
            fun updateView() {
                roomLabel.text = game.currentRoom.name
                descriptionLabel.text = "<html>${game.currentRoom.description}</html>"  // HTML allows text wrapping
                oxygenBar.value = game.oxygenLevel

                // Enable/disable buttons based on available exits
                northButton.isEnabled = game.currentRoom.north != null

            }

            /**
             * Handle any UI events (e.g. button clicks)
             * Usually this involves updating the application model
             * then refreshing the UI view
             */

            override fun actionPerformed(e: ActionEvent?) {
                when (e?.source) {
                    mapButton -> {
                        popUp.isVisible = true
                    }
                    northButton -> {
                        game.move("north")
                        updateView()
                    }
                    // Handle other directions...

            }
        }
    }

}


class SubWindow(val app: App) : JFrame() {

    // Fields to hold the UI elements
//    private lateinit var titleLabel: JLabel

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
    //========================================//

    /**
     * Configure the UI and display it
     */
    init {
        configureWindow()               // Configure the window
        addControls()                   // Build the UI

        setLocationRelativeTo(null)     // Centre the window
        isVisible = false                // Make it visible

    }


    //=====================================================================//


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

//        titleLabel = JLabel("Map")
//        titleLabel.horizontalAlignment = SwingConstants.CENTER
//        titleLabel.bounds = Rectangle(50, 50, 40, 20)
//        titleLabel.font = baseFont
//        add(titleLabel)

        securityMap = JLabel("Security")
        securityMap.horizontalAlignment = SwingConstants.CENTER
        securityMap.bounds = Rectangle(35, 50, 100, 50)
        securityMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        securityMap.font = baseFont
        add(securityMap)

        startMap = JLabel("Start")
        startMap.horizontalAlignment = SwingConstants.CENTER
        startMap.bounds = Rectangle(35, 150, 100, 50)
        startMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        startMap.font = baseFont
        add(startMap)

        weaponsMap = JLabel("Weapons")
        weaponsMap.horizontalAlignment = SwingConstants.CENTER
        weaponsMap.bounds = Rectangle(35, 250, 100, 50)
        weaponsMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        weaponsMap.font = baseFont
        add(weaponsMap)

        labMap = JLabel("Lab")
        labMap.horizontalAlignment = SwingConstants.CENTER
        labMap.bounds = Rectangle(35, 350, 100, 50)
        labMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        labMap.font = baseFont
        add(labMap)

        //=====================================================================//

        comsMap = JLabel("Communications")
        comsMap.horizontalAlignment = SwingConstants.CENTER
        comsMap.bounds = Rectangle(170, 50, 100, 50)
        comsMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        comsMap.font = baseFont
        add(comsMap)

        trashMap = JLabel("Trash Unit")
        trashMap.horizontalAlignment = SwingConstants.CENTER
        trashMap.bounds = Rectangle(170, 150, 100, 50)
        trashMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        trashMap.font = baseFont
        add(trashMap)

        oxygenMap = JLabel("Oxygen")
        oxygenMap.horizontalAlignment = SwingConstants.CENTER
        oxygenMap.bounds = Rectangle(170, 250, 100, 50)
        oxygenMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        oxygenMap.font = baseFont
        add(oxygenMap)

        gardenMap = JLabel("Gardens")
        gardenMap.horizontalAlignment = SwingConstants.CENTER
        gardenMap.bounds = Rectangle(170, 350, 100, 50)
        gardenMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        gardenMap.font = baseFont
        add(gardenMap)

        //=====================================================================//


        hangerMap = JLabel("Hanger")
        hangerMap.horizontalAlignment = SwingConstants.CENTER
        hangerMap.bounds = Rectangle(305, 50, 100, 50)
        hangerMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        hangerMap.font = baseFont
        add(hangerMap)

        engineMap = JLabel("Engine")
        engineMap.horizontalAlignment = SwingConstants.CENTER
        engineMap.bounds = Rectangle(305, 150, 100, 50)
        engineMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        engineMap.font = baseFont
        add(engineMap)


        storeroomMap = JLabel("Storage")
        storeroomMap.horizontalAlignment = SwingConstants.CENTER
        storeroomMap.bounds = Rectangle(305, 250, 100, 50)
        storeroomMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        storeroomMap.font = baseFont
        add(storeroomMap)

        cafeMap = JLabel("Cafe")
        cafeMap.horizontalAlignment = SwingConstants.CENTER
        cafeMap.bounds = Rectangle(305, 350, 100, 50)
        cafeMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        cafeMap.font = baseFont
        add(cafeMap)

        //=====================================================================//

        airlockMap = JLabel("Airlock")
        airlockMap.horizontalAlignment = SwingConstants.CENTER
        airlockMap.bounds = Rectangle(440, 50, 100, 50)
        airlockMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        airlockMap.font = baseFont
        add(airlockMap)

        crewMap = JLabel("Crew Quarters")
        crewMap.horizontalAlignment = SwingConstants.CENTER
        crewMap.bounds = Rectangle(440, 150, 100, 50)
        crewMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        crewMap.font = baseFont
        add(crewMap)

        medBayMap = JLabel("Med Bay")
        medBayMap.horizontalAlignment = SwingConstants.CENTER
        medBayMap.bounds = Rectangle(440, 250, 100, 50)
        medBayMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        medBayMap.font = baseFont
        add(medBayMap)

        observeMap = JLabel("Observatories")
        observeMap.horizontalAlignment = SwingConstants.CENTER
        observeMap.bounds = Rectangle(440, 350, 100, 50)
        observeMap.border = BorderFactory.createLineBorder(Color.GRAY, 3)
        observeMap.font = baseFont
        add(observeMap)

        //=====================================================================//


    }

}


