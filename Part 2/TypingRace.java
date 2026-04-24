import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus, who left this project to "focus on his
 * two-finger technique". He assured us the code was "basically done".
 * We have found evidence to the contrary.
 *
 * @author TyPosaurus
 * @version 0.7 (the other 0.3 is left as an exercise for the reader)
 */
public class TypingRace
{
    private boolean autocorrectOn = false;
    private boolean caffeineMode = false;
    private boolean nightShift = false;
    
    private int caffeineTurns = 0;
    
    private boolean finished;
    private Typist winner;
    
    private String passage;
    private int passageLength;   // Total characters in the passage to type
    private Typist[] typists;

    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;
    
    private boolean[] mistyped;

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(String passage, int number)
    {
        if (number < 2 || number > 6) {
            throw new IllegalArgumentException(
                "Number of typists must be between 2 and 6."
            );
        }
        
        this.passage = passage;
        this.passageLength = passage.length();
        this.typists = new Typist[number];
        this.mistyped = new boolean[number];
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
    public void addTypist(Typist theTypist, int seatNumber)
    {
        if (seatNumber >= 1 && seatNumber <= typists.length) {
            typists[seatNumber - 1] = theTypist;
        }
        else {
            System.out.println("The seat number is invalid.");
        }
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     *
     * Note from Ty: "I didn't bother printing the winner at the end,
     * you can probably figure that out yourself."
     */
    public void startRace()
    {
        finished = false;
        winner = null;

        for (Typist t : typists) {
            if (t == null) {
                throw new IllegalStateException("Missing typist");
            }
            
            if (t != null) {
                t.resetToStart();
            }
        }
    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn's worth and skip typing.
     * Otherwise:
     *   - They may type a character (advancing progress) based on their accuracy.
     *   - They may mistype (sliding back) — the chance of a mistype should decrease
     *     for more accurate typists.
     *   - They may burn out — more likely for very high-accuracy typists
     *     who are pushing themselves too hard.
     *
     * @param theTypist the typist to advance
     */
    private void advanceTypist(Typist theTypist, int seatNumber)
    {
        double acc = theTypist.getAccuracy();
        
        double progressRatio = (double) theTypist.getProgress() / passageLength;
        
        if (theTypist.getAccessory().equals("Energy Drink")){
            if (progressRatio < 0.5) {
                acc += 0.15;
            }
            else {
                acc -= 0.10;
            }
        }

        if (nightShift)
        {
            acc = acc - 0.1;
            if (acc < 0) {
                acc = 0;
            }
        }
        
        double speedMultiplier = 1.0;

        if (caffeineMode && caffeineTurns > 0)
        {
            speedMultiplier = 1.3;
        }
        
        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }

        // Attempt to type a character
        if (Math.random() < acc * speedMultiplier * theTypist.getSpeedMultiplier())
        {
            theTypist.typeCharacter();
        }

        // Mistype check — the probability should reflect the typist's accuracy
        double mistypeChance = (1 - acc) * MISTYPE_BASE_CHANCE;

        if (theTypist.getAccessory().equals("Headphones"))
        {
            mistypeChance *= 0.5;
        }
        
        if (Math.random() < mistypeChance)
        {
            int slide = SLIDE_BACK_AMOUNT;
            
            if (autocorrectOn)
            {
                slide = Math.max(1, slide / 2); 
            }
            
            theTypist.slideBack(slide);
            
            mistyped[seatNumber] = true;
        }

        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
        
        double burnoutChance = 0.05 * acc * acc;

        if (caffeineMode && caffeineTurns == 0)
        {
            burnoutChance *= 1.5;
        }
        
        if (Math.random() < burnoutChance)
        {
            int burnoutTurns = BURNOUT_DURATION;

            if (theTypist.getAccessory().equals("Wrist Support"))
            {
                burnoutTurns = 1;
            }
            
            theTypist.burnOut(burnoutTurns);
        }
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        // Ty was confident this condition was correct
        if (theTypist.getProgress() >= passageLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
    private void printRace()
    {
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        for (int i = 0; i < typists.length; i++) {
            printSeat(typists[i], i);
            System.out.println();
        }

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [~] = burnt out    [<] = just mistyped");
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    [zz]              | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * Note: Ty forgot to show when a typist has just mistyped. That would
     * be a nice improvement — perhaps a [<] marker after their symbol.
     *
     * @param theTypist the typist whose lane to print
     */
    private void printSeat(Typist theTypist, int seatNumber)
    {
        int progress = theTypist.getProgress();

        if (progress > passageLength) {
            progress = passageLength;
        }
    
        String typed = passage.substring(0, progress);
        String remaining = passage.substring(progress);
    
        System.out.print(theTypist.getSymbol() + " ");
        System.out.print(typed);
        System.out.print("|");
        System.out.print(remaining);
        
        System.out.print("   ");

        if (theTypist.isBurntOut())
        {
            System.out.print(theTypist.getName()
                + " BURNT OUT ("
                + theTypist.getBurnoutTurnsRemaining()
                + ")");
        }
        else
        {
            System.out.print(theTypist.getName());
        }
    }

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
    
    /**
     * Manage the passage for different options.
     */
    public static String getPassageOption(int choice)
    {
        if (choice == 1) {
            return "hello world";
        }
        if (choice == 2) {
            return "the quick brown fox jumps over the lazy dog";
        }
        if (choice == 3) {
            return "Stray birds of summer come to my window to sing and fly away. And yellow leaves of autumn, which have no songs, flutter and fall there with a sign.";
        }
    
        throw new IllegalArgumentException("Invalid passage choice");
    }
    
    /**
     * Record each stage of the race.
     */
    public void step() 
    {
        if (caffeineTurns > 0) {
            caffeineTurns--;
        }
    
        if (finished) {
            return;
        }
    
        // reset flags
        for (int i = 0; i < typists.length; i++) {
            mistyped[i] = false;
        }
    
        // move typists
        for (int i = 0; i < typists.length; i++) {
            if (typists[i] != null) {
                advanceTypist(typists[i], i);
            }
        }
    
        // check winner
        for (Typist t : typists) {
            if (t != null && raceFinishedBy(t)) {
                winner = t;
                finished = true;
                break;
            }
        }
    
        // ONLY ONCE when finished
        if (finished) {
            for (int i = 0; i < typists.length; i++) {
                Typist ty = typists[i];
    
                if (ty == null) {
                    continue;
                }
    
                if (ty == winner) {
                    ty.finishRace();
                }
                ty.updateAccuracyAfterRace();
                ty.updateBestWPM(passageLength);
    
                int position = (ty == winner) ? 1 : i + 1;
    
                ty.addRaceHistory(position, passageLength);
            }
        }
    }
    
    /**
     * Return the typists.
     */
    public Typist[] getTypists()
    {
        return typists;
    }
    
    /**
     * Return the passage.
     */
    public String getPassage()
    {
        return passage;
    }
    
    /**
     * Set difficulty modifiers.
     */
    public void setModifiers(boolean ac, boolean cf, boolean ns)
    {
        this.autocorrectOn = ac;
        this.caffeineMode = cf;
        this.nightShift = ns;
    
        if (cf)
        {
            caffeineTurns = 10;
        }
    }
    
    /**
     * Return if finished.
     */
    public boolean isFinished()
    {
        return finished;
    }
    
    /**
     * Return Autocorrect On/Off. 
     */
    public boolean isAutocorrectOn()
    {
        return autocorrectOn;
    }
    
    /**
     * Return Caffeine Mode On/Off.
     */
    public boolean isCaffeineMode()
    {
        return caffeineMode;
    }
    
    /**
     * Return Night Shift On/Off.
     */
    public boolean isNightShift()
    {
        return nightShift;
    }
}