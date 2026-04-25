import java.awt.Color;
import java.util.ArrayList;

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
    private double oldAccuracy;
    private double baseAccuracy;
    
    private int mistypeCount;
    private int burnoutCount;
    private int correctKeystrokes;
    private long startTime;
    private long finishTime;
    
    private double bestWPM = 0;
    
    private ArrayList<String> history = new ArrayList<String>();
    
    private int totalPoints = 0;
    private int consecutiveWins = 0;
    private int noBurnoutStreak = 0;
    private String title = "Rookie";

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
        this.baseAccuracy = typistAccuracy;
        this.accuracy = typistAccuracy;
        this.style = "";
        this.keyboard = "";
        this.colour = Color.GREEN;
        this.accessory = "";
    }

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
            this.burnoutCount++;
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
        this.mistypeCount = 0;
        this.burnoutCount = 0;
        this.correctKeystrokes = 0;
        this.startTime = System.currentTimeMillis();
        this.oldAccuracy = this.accuracy;
        this.accuracy = this.baseAccuracy;
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
            this.correctKeystrokes++;
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
        this.mistypeCount++;
        this.progress -= amount;

        if (this.progress < 0) {
            this.progress = 0;
        }
    }
    
    /**
     * Record fisish time.
     */
    public void finishRace()
    {
        this.finishTime = System.currentTimeMillis();
    }
    
    public void updateAccuracyAfterRace()
    {
        double percent = getAccuracyPercent();
    
        if (percent >= 90) {
            this.accuracy += 0.02;
        }
        else if (percent <= 70) {
            this.accuracy -= 0.02;
        }
    
        if (this.accuracy > 1.0) {
            this.accuracy = 1.0;
        }
    
        if (this.accuracy < 0.0) {
            this.accuracy = 0.0;
        }
    }
    
    /**
     * Update the best personal record.
     */
    public void updateBestWPM(int passageLength)
    {
        double current = getWPM(passageLength);
    
        if (current > bestWPM) {
            bestWPM = current;
        }
    }
    
    /**
     * Add race record.
     */
    public void addRaceHistory(int position, int passageLength)
    {
        String record =
            "Position: " + position +
            " | WPM: " + String.format("%.1f", getWPM(passageLength)) +
            " | Accuracy: " + String.format("%.1f", getAccuracyPercent()) + "%" +
            " | Burnouts: " + burnoutCount;
    
        history.add(record);
    }
    
    /**
     * Accumulate award points.
     */
    public void awardPoints(int pts)
    {
        totalPoints += pts;
        
        if (totalPoints < 0) {
            totalPoints = 0;
        }
    }
    
    /**
     * Update title for typists.
     */
    public void updateTitle()
    {
        if (consecutiveWins >= 3)
        {
            title = "Speed Demon";
        }
    
        if (noBurnoutStreak >= 5)
        {
            title = "Iron Fingers";
        }
    }
    
    /**
     * Calculate words per minute.
     */
    public double getWPM(int passageLength)
    {
        long endTime;
    
        if (finishTime > 0)
        {
            endTime = finishTime;
        }
        else
        {
            endTime = System.currentTimeMillis();
        }
    
        double minutes = (endTime - startTime) / 60000.0;
    
        if (minutes <= 0)
        {
            return 0;
        }
    
        double words = progress / 5.0;
    
        return words / minutes;
    }
    
    /**
     * Calculate accuracy percentage.
     */
    public double getAccuracyPercent()
    {
        int total = correctKeystrokes + mistypeCount;
    
        if (total == 0)
        {
            return 100.0;
        }
    
        return (correctKeystrokes * 100.0) / total;
    }
    
    /**
     * Return number of times the typist burnt out.
     */
    public int getBurnoutCount()
    {
        return burnoutCount;
    }
    
    /**
     * Return the change in accuracy.
     */
    public double getAccuracyChange()
    {
        return accuracy - oldAccuracy;
    }
    
    /**
     * Return personal best.
     */
    public double getBestWPM()
    {
        return bestWPM;
    }
    
    /**
     * Return race history.
     */
    public ArrayList<String> getHistory()
    {
        return history;
    }
    
    /**
     * Return total points.
     */
    public int getTotalPoints()
    {
        return totalPoints;
    }
    
    /**
     * Return the title for the typist.
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * Return accuracy for race.
     */
    public double getRaceAccuracy()
    {
        double acc = baseAccuracy;
    
        if (burnoutState)
        {
            acc -= 0.2;
        }
    
        if (acc > 1.0) {
            acc = 1.0;
        }
        
        if (acc < 0.0) {
            acc = 0.0;
        }
    
        return acc;
    }
    
    /**
     * Return the base accuracy.
     */
    public double getBaseAccuracy()
    {
        return baseAccuracy;
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
    
        if (style.equals("Touch Typist")) {
            this.baseAccuracy += 0.15;
        }
        else if (style.equals("Hunt & Peck")) {
            this.baseAccuracy -= 0.10;
        }
        else if (style.equals("Phone Thumbs")) {
            this.baseAccuracy -= 0.05;
        }
        else if (style.equals("Voice-to-Text")) {
            this.baseAccuracy += 0.20;
        }
    
        if (this.baseAccuracy > 1.0) {
            this.baseAccuracy = 1.0;
        }
    
        if (this.baseAccuracy < 0.0) {
            this.baseAccuracy = 0.0;
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
    
    /**
     * Set the base accuracy.
     */
    public void setBaseAccuracy(double acc)
    {
        this.baseAccuracy = acc;
        this.accuracy = acc;
    }
    
    /**
     * Add number of wins.
     */
    public void addConsecutiveWin()
    {
        consecutiveWins++;
    }
    
    /**
     * Reset number of wins.
     */
    public void resetConsecutiveWins()
    {
        consecutiveWins = 0;
    }
    
    /**
     * Add number of burnout.
     */
    public void addNoBurnoutStreak()
    {
        noBurnoutStreak++;
    }
}