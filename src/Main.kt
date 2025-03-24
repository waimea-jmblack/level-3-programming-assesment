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
    val app = App()         // Create the app model
    val popUp = SubWindow(app)
    MainWindow(app, popUp)         // Create and show the UI, using the app model
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
    val name:String,
    val description:String
){
    var north: Room? = null
    var south: Room? = null
    var east: Room? = null
    var west: Room? = null
}

class App() {

    val alienRoom = Room(
        "Alien entry room",
        "This room is the entry point for the Alien who broke through the hull of the ship",
    )

    val shipRoom = Room(
        "Engineering Bay",
        "Silence envelopes the once lively room of engineers",
    )


}

/**
 * Main UI window (view)
 * Defines the UI and responds to events
 * The app model should be passwd as an argument
 */
class MainWindow(val app: App, val popUp: SubWindow) : JFrame(), ActionListener {

    // Fields to hold the UI elements
    private lateinit var roomPannel: JLabel
    private lateinit var clicksLabel: JLabel
    private lateinit var clickButton: JButton
    private lateinit var oxygenBar: JPanel

    /**
     * Configure the UI and display it
     */
    init {
        configureWindow()               // Configure the window
        addControls()                   // Build the UI

        setLocationRelativeTo(null)     // Centre the window
        isVisible = true                // Make it visible

        updateView()                    // Initialise the UI
    }

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
     * Populate the UI with UI controls
     */
    private fun addControls() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        roomPannel = JLabel("Hello, World!")
        roomPannel.horizontalAlignment = SwingConstants.CENTER
        roomPannel.bounds = Rectangle(30, 50, 500, 75)
        roomPannel.font = baseFont
        add(roomPannel)

        clicksLabel = JLabel("")
        clicksLabel.horizontalAlignment = SwingConstants.CENTER
        clicksLabel.bounds = Rectangle(10, 50, 500, 100)
        clicksLabel.font = baseFont
        add(clicksLabel)

        clickButton = JButton("Map")
        clickButton.bounds = Rectangle(410,260,150,60)
        clickButton.font = baseFont
        clickButton.addActionListener(this)     // Handle any clicks
        add(clickButton)

        oxygenBar = JPanel()
        oxygenBar.bounds = Rectangle(20, 100, 130, 75)
        oxygenBar.font = baseFont
        add(oxygenBar)
    }


    /**
     * Update the UI controls based on the current state
     * of the application model
     */
    fun updateView() {

    }

    /**
     * Handle any UI events (e.g. button clicks)
     * Usually this involves updating the application model
     * then refreshing the UI view
     */
    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
            clickButton -> {
                popUp.isVisible = true
            }
        }
    }

}


class SubWindow(val app: App) : JFrame() {

    // Fields to hold the UI elements
    private lateinit var clicksLabel: JLabel

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
        contentPane.preferredSize = Dimension(300, 200)
        isResizable = false
        layout = null

        pack()
    }

    /**
     * Populate the UI with UI controls
     */
    private fun addControls() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 36)

        clicksLabel = JLabel("Map")
        clicksLabel.horizontalAlignment = SwingConstants.CENTER
        clicksLabel.bounds = Rectangle(50, 50, 200, 100)
        clicksLabel.font = baseFont
        add(clicksLabel)

    }

}


