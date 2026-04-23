import java.awt.Color;

/**
 * The class Typist represents a typing race competitor with accuracy, progress,
 * and a temporary burnout state.
 *
 * Starter code generously abandoned by Ty Posaurus, your predecessor,
 * who typed with two fingers and considered that "good enough".
 * He left a sticky note: "the slide-back thing is optional probably".
 * It is not optional. Good luck.
 *
 * @author Yuwen Zeng
 * @version 1.0
 */
public class Typist
{
    private String style;
    private String keyboard;
    private Color colour;
    private String accessory;
    
    private String name;
    private String symbol;
    private int progress;
    private boolean burnoutState;
    private int burnoutTurnsRemaining;
    private double accuracy;
    




    // Constructor of class Typist
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given symbol, name, and accuracy rating.
     *
     * @param typistSymbol  a single Unicode character representing this typist (e.g. '①', '②', '③')
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public Typist(String typistSymbol, String typistName, double typistAccuracy)
    {
        this.symbol = typistSymbol;
        this.name = typistName;
        this.accuracy = typistAccuracy;
    
        this.style = "";
        this.keyboard = "";
        this.colour = Color.GREEN;
        this.accessory = "";
    }


    // Methods of class Typist

    /**
     * Sets this typist into a burnout state for a given number of turns.
     * A burnt-out typist cannot type until their burnout has worn off.
     *
     * @param turns the number of turns the burnout will last
     */
    public void burnOut(int turns)
    {
        if (turns > 0) {
            this.burnoutState = true;
            this.burnoutTurnsRemaining = turns;
        }
    }

    /**
     * Reduces the remaining burnout counter by one turn.
     * When the counter reaches zero, the typist recovers automatically.
     * Has no effect if the typist is not currently burnt out.
     */
    public void recoverFromBurnout()
    {
        this.burnoutTurnsRemaining--;

        if (this.burnoutTurnsRemaining <= 0) {
            this.burnoutState = false;
            this.burnoutTurnsRemaining = 0;
        }
    }
    
    /**
     * Return the typing style.
     */
    public String getStyle()
    {
        return style;
    }
    
    /**
     * Return the keyboard type.
     */
    public String getKeyboard()
    {
        return keyboard;
    }
    
    /**
     * Return the accessory.
     */
    public String getAccessory()
    {
        return accessory;
    }

    /**
     * Returns the typist's accuracy rating.
     *
     * @return accuracy as a double between 0.0 and 1.0
     */
    public double getAccuracy()
    {
        return this.accuracy; 
    }

    /**
     * Returns the typist's current progress through the passage.
     * Progress is measured in characters typed correctly so far.
     * Note: this value can decrease if the typist mistypes.
     *
     * @return progress as a non-negative integer
     */
    public int getProgress()
    {
        return this.progress; 
    }

    /**
     * Returns the name of the typist.
     *
     * @return the typist's name as a String
     */
    public String getName()
    {
        return this.name; 
    }

    /**
     * Returns the character symbol used to represent this typist.
     *
     * @return the typist's symbol as a char
     */
    public String getSymbol()
    {
        return this.symbol; 
    }
    
    /**
     * Return speed multiplier according to the chosen keyboard type.
     */
    public double getSpeedMultiplier()
    {
        if (keyboard.equals("Mechanical")) {
            return 1.2;
        }
    
        if (keyboard.equals("Touchscreen")) {
            return 0.8;
        }
    
        if (keyboard.equals("Stenography")) {
            return 1.5; 
        }
    
        return 1.0;
    }

    /**
     * Returns the number of turns of burnout remaining.
     * Returns 0 if the typist is not currently burnt out.
     *
     * @return burnout turns remaining as a non-negative integer
     */
    public int getBurnoutTurnsRemaining()
    {
        if (!this.burnoutState){
            return 0;
        }
        return this.burnoutTurnsRemaining; 
    }
    
    /**
     * Return the chosen colour.
     */
    public Color getColour()
    {
        return colour;
    }

    /**
     * Resets the typist to their initial state, ready for a new race.
     * Progress returns to zero, burnout is cleared entirely.
     */
    public void resetToStart()
    {
        this.progress = 0;
        this.burnoutState = false;
        this.burnoutTurnsRemaining = 0;
    }

    /**
     * Returns true if this typist is currently burnt out, false otherwise.
     *
     * @return true if burnt out
     */
    public boolean isBurntOut()
    {
        return this.burnoutState; 
    }

    /**
     * Advances the typist forward by one character along the passage.
     * Should only be called when the typist is not burnt out.
     */
    public void typeCharacter()
    {
        if (!this.burnoutState) {
            this.progress++;
        }
    }

    /**
     * Moves the typist backwards by a given number of characters (a mistype).
     * Progress cannot go below zero — the typist cannot slide off the start.
     *
     * @param amount the number of characters to slide back (must be positive)
     */
    public void slideBack(int amount)
    {
        this.progress -= amount;

        if (this.progress < 0) {
            this.progress = 0;
        }
    }

    /**
     * Sets the accuracy rating of the typist.
     * Values below 0.0 should be set to 0.0; values above 1.0 should be set to 1.0.
     *
     * @param newAccuracy the new accuracy rating
     */
    public void setAccuracy(double newAccuracy)
    {
        this.accuracy = newAccuracy;
        
        if (this.accuracy <= 0.0) {
            this.accuracy = 0.0;
        }
        
        if (this.accuracy >= 1.0) {
            this.accuracy = 1.0;
        }
    }

    /**
     * Sets the symbol used to represent this typist.
     *
     * @param newSymbol the new symbol character
     */
    public void setSymbol(String newSymbol)
    {
        this.symbol = newSymbol;
    }

    /**
     * Set variable style.
     */
    public void setStyle(String style)
    {
        this.style = style;
        
        if (style.equals("Touch Typist"))
        {
            this.accuracy += 0.15;
        }
        else if (style.equals("Hunt & Peck"))
        {
            this.accuracy -= 0.10;
        }
        else if (style.equals("Phone Thumbs"))
        {
            this.accuracy -= 0.05;
        }
        else if (style.equals("Voice-to-Text"))
        {
            this.accuracy += 0.20;
        }
    
        if (this.accuracy > 1.0)
        {
            this.accuracy = 1.0;
        }
    
        if (this.accuracy < 0.0)
        {
            this.accuracy = 0.0;
        }
    }
    
    /**
     * Set variable keyboard.
     */
    public void setKeyboard(String keyboard)
    {
        this.keyboard = keyboard;
    }
    
    /**
     * Set variable color.
     */
    public void setColour(Color colour)
    {
        this.colour = colour;
    }
    
    /**
     * Set variable accessory.
     */
    public void setAccessory(String accessory)
    {
        this.accessory = accessory;
    }
}