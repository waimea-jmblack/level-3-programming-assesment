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
            "The security team's last stand is painted across the walls in broad strokes of crimson. Smashed rifles " +
                    "and dented armor tell the story—they fought hard. They lost harder. The predator didn't just kill them; " +
                    "it arranged the corpses, like some grotesque trophy display. The emergency lockdown button lies just out of " +
                    "reach... beneath a slowly spreading pool of blood",
        )

        val ammunitionDepotRoom = Room(
            "Ammunition Depot",
            "The armory is a tomb of wasted firepower—shattered racks of rifles line the walls, their barrels bent into " +
                    "unnatural shapes. Crates of grenades sit unopened, their seals intact... and covered in a web of sticky, translucent " +
                    "fibers. The scent of gunpowder mixes with something muskier, something alive. A single undamaged shotgun rests on " +
                    "the counter—its ejection port clogged with a throbbing, vein-like growth. The alien didn’t just raid this place. " +
                    "It claimed it"
        )

        val startRoom = Room(
            "The beginning...",
            "Your head throbs as you stir in the pitch-black command center. Emergency lights flicker, revealing " +
                    "splattered control panels and toppled chairs. The acrid scent of burnt wiring mixes with something... " +
                    "organic. A distant, wet shuffling echoes from the corridor outside. You're not supposed to be alive. " +
                    "And you're definitely not alone.",
        )

        val weaponsRoom = Room(
            "Weapons Room",
            "Cold dread seeps in as you stare at the empty racks—not just cleared, but peeled open with terrifying precision. " +
                    "The reinforced steel is curled back like flower petals, the few remaining sidearms crushed into useless scrap. A single " +
                    "survival knife juts from the wall, embedded to the hilt in a dark, coagulated stain. Whatever did this knew exactly how to disarm you."
        )

        val labRoom = Room(
            "Lab Room",
            "The containment pods lie shattered—not from the inside, but from something breaking in. Thick, " +
                    "viscous fluid drips from empty embryo tanks, their stolen contents evident by the drag marks leading " +
                    "toward the vents. A fractured security feed shows a single frame: curled, leathery husks being hauled " +
                    "away into darkness."
        )

        //=====================================================================================================//

        val comsRoom = Room(
            "Communications",
            "Your hope fades as you enter. The alien clawed through every console, wires ripped out like tendons. " +
                    "Shattered screens dangle by their cables, swinging gently—as if something just left. The emergency " +
                    "beacon lies in pieces, its transmitter crushed underfoot. No signals are getting out."
        )

        val gymRoom = Room(
            "Gym Room",
            "The gym’s lights flicker to life as you enter, revealing a space frozen mid-workout. Treadmills still " +
                    "run at full speed, their belts stained with dark fluids. A workout playlist crackles through broken speakers—just " +
                    "upbeat enough to make the claw marks on the lockers feel surreal. The only thing out of place? Every single mirror " +
                    "is angled just wrong. You never get a full reflection. Just glimpses of something moving when you turn your head."
        )
        val trashRoom = Room(
            "Trash Room",
            "The stench of blood and rotting waste hits you like a wall. Something big died here—recently. " +
                    "The compactor’s teeth gleam with gristle, jammed mid-cycle. You step back as a crushed ration tin " +
                    "clinks underfoot. No. Nothing here is worth touching."
        )

        val cargoRoom = Room(
            "Cargo Bay",
            "Crates lie broken open, their contents spilled across the floor. Some were torn apart with brute " +
                    "force, others with terrifying precision. Something was searching for supplies… or hiding among them."
        )

        val gardenRoom = Room(
            "Gardens",
            "The ship’s garden is a ravaged wasteland—smashed grow-pods, half-eaten plants, and gnawed ration packs litter " +
                    "the floor. Claw marks tear through the soil where the alien dug up every edible root and berry. A crewmate’s " +
                    "boot lies in the corner, filled with squirming tendrils. It didn’t just kill here… it feasted."
        )

        //=====================================================================================================//

        val alienRoom = Room(
            "Alien Entry Point",
            "The ship’s hull is split like a wound stitched shut—jagged metal fused in unnatural patterns, Nearby, a maintenance drone " +
                    "lies crushed against the wall. soldiers body's lay motionless, a futile human attempt to patch what the alien had already closed behind it. " +
                    "Not to keep the void out… but to keep you in.",
        )

        val meetingRoom = Room(
            "Meeting Room",
            "The holographic projector still hums, casting a ghostly agenda list onto the smoke-stained table. 'Emergency Protocols' sits unchecked at the bottom. " +
                    "Chairs are overturned—not in panic, but in some unnatural pattern, like a grotesque mockery of a discussion circle. At the head of the table, a coffee cup " +
                    "sits untouched, its surface rippling faintly. Not from steam. From the thing that's breathing into it."
        )

        val oRoom = Room(
            "Oxygen Room",
            "Thankfully the backup O2 generators are still running—weak, sputtering, but alive. The main systems" +
                    " are fried, vents clogged with something dark and fibrous. A flickering screen reports steady depletion: " +
                    "23% and falling. Every breath feels like my last.  "
        )

        val engineRoom = Room(
            "Engineering Bay",
            "The deafening hum of the reactors has been replaced by an eerie silence, broken only by the occasional " +
                    "drip of leaking coolant. The engineers' tools lie scattered in the dark, some still clutched in skeletal hands. " +
                    "The emergency lights cast long shadows from the mangled machinery—something didn’t just disable " +
                    "the engines… it savaged them.",
        )

        val cafeRoom = Room(
            "Cafeteria",
            "The stench of rotting food and copper hangs thick. A chair lies toppled, tray upended—someone " +
                    "abandoned their last meal in terror. Behind the kitchen’s broken door, something shifts in the darkness. " +
                    "Metal creaks. A wet, rhythmic sound follows. Whatever’s in there… it’s feeding."
        )

        //=====================================================================================================//

        val airLockRoom = Room(
            "Air Lock",
            "The reinforced door is grotesquely warped—massive, clawed dents punched inward as if something colossal " +
                    "tried to brute-force its way inside. Frost still clings to the edges where the alien’s patience ran out. " +
                    "A trail of frozen, tar-like droplets leads away… toward a different entry point. It’s already in."
        )

        val spaceStorageRoom = Room(
            "Space Storage Room",
            "The emergency suits hang in tatters—not torn off, but peeled from the inside out, their visors cracked " +
                    "like eggshells. Tools and oxygen tanks lie scattered in congealing pools of something too dark to be just " +
                    "hydraulic fluid. The flickering worklight catches movement in a half-open storage locker—just a loose glove swaying. " +
                    "Probably. The air reeks of copper and that sour, greasy stench the vents keep pumping through the ship."
        )

        val medBayRoom = Room(
            "Med Bay",
            "The smell hits first—antiseptic and bile undercut by something sweetly rotten. The emergency lights " +
                    "bathe the room in a sickly glow, illuminating the dark arcs of blood sprayed across surgical trays. A " +
                    "medical drone still whirs on the floor, its robotic arm repeatedly stitching—and restitching—a gaping " +
                    "wound in empty air. The last patient’s vitals flash CRITICAL on the screen... but the bed holds only a " +
                    "curled-up, hollowed-out biosuit."
        )



        val crewQuarterRoom = Room(
            "Crew Quarter",
            "The room is a graveyard of interrupted lives—family photos trampled underfoot, a child’s holotoy " +
                    "still chirping a distorted lullaby. The bunks are shredded, mattresses gutted with surgical precision. " +
                    "Something wasn’t just hunting here… it was studying. The sobbing from Locker C hitches—a wet, gurgling sound. " +
                    "That’s not a human voice anymore"
        )

        val cryogenicRoom = Room(
            "Cryogenic Room",
            "The reinforced pods are scarred with claw marks—not random, but methodical, like a predator testing each cell for " +
                    "worthy prey. Most remain sealed, their occupants deemed unworthy. Pod-12’s shattered exterior tells a different story: " +
                    "frost-coated bones and mangled combat armor spill from the rupture. The terminal’s last log glitches on repeat: ‘COMBAT " +
                    "SPECIMEN REJECTED. FAILURE: ████ MINUTES.’ The alien didn’t just hunt here… it held auditions."
        )

        //=====================================================================================================//

        val maintenceRoom = Room(
            "Maintence",
            "The drones never stood a chance. Their shattered chassis litter the floor—some torn in half, others crushed into the " +
                    "grating with terrifying force. A few still twitch, their broken servos whining as they pointlessly drag themselves in circles. " +
                    "The emergency repair terminal flickers with a single, repeating alert: 'CRITICAL DAMAGE DETECTED " +
                    "IN SECTOR—' before cutting to static."
        )

        val auxilaryRoom = Room(
            "Auxilary Engineering",
            "The backup systems hum—wrong. Too smooth. Too quiet. The panels are flawless. No damage. No blood. Just... rewired. " +
                    "Your name blinks on the maintenance logs for work you never did. The temperature drops exactly when you notice the new " +
                    "piping—shaped like veins. The ship’s AI whispers: 'All systems nominal.' The lie echoes in the dark. You don’t remember " +
                    "this place. But it remembers you."
        )


        val reactorRoom = Room(
            "Reactor Chamber",
            "The core hums at 12% capacity—just enough to keep the lights dying slowly. something clawed through three-foot-thick " +
                    "shielding like it was wet paper. the walls are streaked with radioactive coolant and... thicker fluids, all leading to a " +
                    "service pit where the maintenance team vanished. the radiation alarm chirps calmly, like it's given up."
        )

        val powerDistributionRoom = Room(
            "Power Distribution",
            "The breaker panels are split open like gutted animals, copper wiring coiled in blood-soaked tangles. the metallic " +
                    "stink of scorched circuits mixes with rotting meat—some poor bastard got fused to the main conduit. their blackened " +
                    "fingers still clutch a fried voltage meter. sparks spit from the corpse whenever the backup generators stutter to life."
        )


        val hangerRoom = Room(
            "Hanger Room",
            "The lockdown protocols were meant to seal out intruders—now they've caged you both inside. The blast doors stand unyielding, " +
                    "their reinforced frames dented from the inside by desperate, panicked blows. The override panel isn't just broken—it's been " +
                    "systematically destroyed, each component clawed apart with terrifying precision. Escape pods sit half-crushed under collapsed scaffolding. " +
                    "The air smells of ionized metal and stale fear. The remaining escape pods are so close—just past the reactor chamber, through " +
                    " the power distribution room. Screams of the Alien eco, a cruel reminder of how little stands between you and survival."

        )

        //=====================================================================================================//

        val podsRoom = Room(
            "Escape Pods",
            "The pod’s hatch glows just ahead—safety. But the thunder of claws on metal shakes the walls. Closer. " +
                    "Louder. You slam the release button. The door hisses shut. A shadow fills the viewport. Not a monster. " +
                    "Something worse. Something knowing. Its eyes lock onto yours as the pod detaches. The last thing you see isn’t " +
                    "teeth or claws—it’s the thing smiling as you escape. Like this was always the plan."
        )



        // Connect rooms

        securityRoom.connectEast(comsRoom)
        securityRoom.connectSouth(ammunitionDepotRoom)

        startRoom.connectNorth(ammunitionDepotRoom)
        startRoom.connectEast(trashRoom)
        startRoom.connectSouth(weaponsRoom)

        labRoom.connectNorth(weaponsRoom)
        labRoom.connectEast(gardenRoom)

        //================================================//

        comsRoom.connectEast(alienRoom)
        comsRoom.connectSouth(gymRoom)

        trashRoom.connectNorth(gymRoom)
        trashRoom.connectEast(oRoom)

        cargoRoom.connectEast(engineRoom)
        cargoRoom.connectSouth(gardenRoom)
        cargoRoom.connectWest(weaponsRoom)

        //================================================//

        alienRoom.connectEast(airLockRoom)

        meetingRoom.connectEast(spaceStorageRoom)
        meetingRoom.connectSouth(oRoom)
        meetingRoom.connectWest(gymRoom)

        engineRoom.connectNorth(oRoom)
        engineRoom.connectSouth(cafeRoom)

        cafeRoom.connectEast(cryogenicRoom)
        cafeRoom.connectWest(gardenRoom)

        //note gardens north go to cafe MUST FIX

        //================================================//

        airLockRoom.connectEast(hangerRoom)
        airLockRoom.connectSouth(spaceStorageRoom)

        spaceStorageRoom.connectEast(auxilaryRoom)
        spaceStorageRoom.connectSouth(medBayRoom)

        crewQuarterRoom.connectNorth(medBayRoom)
        crewQuarterRoom.connectEast(powerDistributionRoom)
        crewQuarterRoom.connectSouth(cryogenicRoom)

        //================================================//

        auxilaryRoom.connectNorth(hangerRoom)
        auxilaryRoom.connectSouth(reactorRoom)

        reactorRoom.connectEast(podsRoom)

        powerDistributionRoom.connectNorth(reactorRoom)
        powerDistributionRoom.connectSouth(maintenceRoom)



        // Add to rooms list
        rooms.addAll(listOf(securityRoom, ammunitionDepotRoom, startRoom, weaponsRoom, labRoom, comsRoom, gymRoom,
            trashRoom, cargoRoom, gardenRoom, alienRoom, meetingRoom, oRoom, engineRoom, cafeRoom, airLockRoom, spaceStorageRoom,
            medBayRoom, crewQuarterRoom, cryogenicRoom, hangerRoom, auxilaryRoom, reactorRoom, powerDistributionRoom, maintenceRoom,
            podsRoom))

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
        val baseFont = Font(Font.SANS_SERIF, Font.BOLD, 11)
        val bigFont = Font(Font.SANS_SERIF, Font.BOLD, 15)

        // Room Label
        roomLabel = JLabel("Room Name")
        roomLabel.bounds = Rectangle(100, 20, 550, 30)
        roomLabel.font = bigFont
        add(roomLabel)

        // Description Label (fixed position)
        descriptionLabel = JLabel("Description")
        descriptionLabel.bounds = Rectangle(100, 20, 480, 200)
        descriptionLabel.font = baseFont
        add(descriptionLabel)

        // Navigation Buttons
        northButton = JButton("North")
        northButton.bounds = Rectangle(170, 220, 110, 40)
        northButton.font = baseFont
        northButton.addActionListener(this)
        add(northButton)

        southButton = JButton("South")
        southButton.bounds = Rectangle(170, 280, 110, 40)
        southButton.font = baseFont
        southButton.addActionListener(this)
        add(southButton)

        eastButton = JButton("East")
        eastButton.bounds = Rectangle(240, 250, 110, 40)
        eastButton.font = baseFont
        eastButton.addActionListener(this)
        add(eastButton)

        westButton = JButton("West")
        westButton.bounds = Rectangle(100, 250, 110, 40)
        westButton.font = baseFont
        westButton.addActionListener(this)
        add(westButton)

        // Map Button
        mapButton = JButton("Map")
        mapButton.bounds = Rectangle(410, 280, 100, 40)
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