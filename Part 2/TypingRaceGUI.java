import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TypingRaceGUI extends JFrame
{
    private TypingRace race;
    private RacePanel panel;
    private Timer timer;
    private JTextArea compareArea;
    private JButton bestBtn;
    private JButton historyBtn;
    private JButton compareBtn;
    private JButton restartBtn;
    private JButton leaderboardBtn;
    private boolean comparisonOn = false;
    private JScrollPane scroll;
    private Typist[] savedTypists;

    public TypingRaceGUI()
    {
        setupGame();
        setupFrame();
        setupUI();
        startTimer();
    }

    public static void main(String[] args)
    {
        new TypingRaceGUI();
    }

    /**
     * Create race + typists + modifiers.
     */
    private void setupGame()
    {
        int num = Integer.parseInt(JOptionPane.showInputDialog("Enter number of typists (2-6):"));
    
        int option = Integer.parseInt(JOptionPane.showInputDialog("Passage length (1:short 2:medium 3:long 4:custom)"));
    
        String passage;
    
        if (option == 4) {
            passage = JOptionPane.showInputDialog("Enter custom passage:");
        }
        else {
            passage = TypingRace.getPassageOption(option);
        }
    
        race = new TypingRace(passage, num);
    
        // first time
        if (savedTypists == null || savedTypists.length != num)
        {
            savedTypists = new Typist[num];
    
            for (int i = 0; i < num; i++)
            {
                savedTypists[i] = createTypist(i);
            }
        }
    
        // reuse old typists
        for (int i = 0; i < num; i++)
        {
            race.addTypist(savedTypists[i], i + 1);
        }
    
        int ac = JOptionPane.showConfirmDialog(null, "Enable Autocorrect?");
        int cf = JOptionPane.showConfirmDialog(null, "Enable Caffeine Mode?");
        int ns = JOptionPane.showConfirmDialog(null, "Enable Night Shift?");
    
        race.setModifiers(ac == 0, cf == 0, ns == 0);
    
        race.startRace();
    }

    /**
     * Create one typist (input + settings).
     */
    private Typist createTypist(int i)
    {
        String name = JOptionPane.showInputDialog("Typist " + (i + 1) + " name:");
        double acc = Double.parseDouble(
                JOptionPane.showInputDialog("Accuracy (0.0 - 1.0):"));

        String symbol = JOptionPane.showInputDialog("Symbol / emoji:");

        String style = choose(new String[]{
                "Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"
        }, "Choose style");

        String keyboard = choose(new String[]{
                "Mechanical", "Membrane", "Touchscreen", "Stenography"
        }, "Choose keyboard");

        String colour = choose(new String[]{
                "Red", "Orange", "Yellow", "Green", "Blue"
        }, "Choose colour");

        Color c = parseColor(colour);

        String accessory = choose(new String[]{
                "None", "Wrist Support", "Energy Drink", "Headphones"
        }, "Choose accessory");

        Typist t = new Typist(symbol, name, acc);
        t.setStyle(style);
        t.setKeyboard(keyboard);
        t.setColour(c);
        t.setAccessory(accessory);

        return t;
    }

    /**
     * Helper: selection dialog.
     */
    private String choose(String[] options, String title)
    {
        return (String) JOptionPane.showInputDialog(
                null,
                title,
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    /**
     * Convert colour string to Color.
     */
    private Color parseColor(String c)
    {
        if (c.equals("Orange")) {
            return Color.ORANGE;
        }
        if (c.equals("Yellow")) {
            return Color.YELLOW;
        }
        if (c.equals("Green")) {
            return Color.GREEN;
        }
        if (c.equals("Blue")) {
            return Color.BLUE;
        }
        return Color.RED;
    }
    
    /**
     * Setup main window layout.
     */
    private void setupFrame()
    {
        this.setTitle("Typing Race");
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
    }

    /**
     * Setup other UI area.
     */
    private void setupUI()
    {
        // game panel
        panel = new RacePanel(race);
    
        // comparison area
        compareArea = new JTextArea();
        compareArea.setEditable(false);
        compareArea.setFont(new Font("Arial", Font.PLAIN, 14));
    
        scroll = new JScrollPane(compareArea);
        scroll.setPreferredSize(new Dimension(250, 0));
        scroll.setVisible(false);
    
        // split panel
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                panel,
                scroll
        );
    
        split.setResizeWeight(1.0);
        split.setDividerLocation(900);
    
        this.add(split, BorderLayout.CENTER);
    
        // top buttons
        JPanel topBar = new JPanel();
    
        bestBtn = new JButton("Best Records");
        historyBtn = new JButton("History");
        compareBtn = new JButton("Comparison OFF");
        restartBtn = new JButton("Restart");
        leaderboardBtn = new JButton("Leaderboard");

        topBar.add(leaderboardBtn);
        topBar.add(restartBtn);
        topBar.add(bestBtn);
        topBar.add(historyBtn);
        topBar.add(compareBtn);
    
        this.add(topBar, BorderLayout.NORTH);
    
        setupButtons();
    
        this.setVisible(true);
    }
    
    /**
     * Game loop timer.
     */
    private void startTimer()
    {
        timer = new Timer(200, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                race.step();
                panel.repaint();

                if (comparisonOn) {
                    updateComparison();
                }

                if (race.isFinished()) {
                    timer.stop();
                }
            }
        });

        timer.start();
    }
    
    /**
     * Connect button actions.
     */
    private void setupButtons()
    {
        bestBtn.addActionListener(e -> showBest());
        historyBtn.addActionListener(e -> showHistory());
        compareBtn.addActionListener(e -> toggleComparison());
        restartBtn.addActionListener(e -> restartGame());
        leaderboardBtn.addActionListener(e -> showLeaderboard());
    }

    /**
     * Toggle comparison view panel.
     */
    private void toggleComparison()
    {
        comparisonOn = !comparisonOn;

        scroll.setVisible(comparisonOn);
        this.revalidate();
        this.repaint();

        if (comparisonOn) {
            compareBtn.setText("Comparison ON");
        }
        else {
            compareBtn.setText("Comparison OFF");
            compareArea.setText("");
        }
    
        this.revalidate(); 
        this.repaint();
    }

    /**
     * Update comparison panel in real time.
     */
    private void updateComparison()
    {
        String text = "";

        for (Typist t : race.getTypists()) {
            if (t == null) {
                continue;
            }                         

            text += t.getName() + "\n";
            text += "WPM: " + String.format("%.1f",
                    t.getWPM(race.getPassage().length())) + "\n";
            text += "Accuracy: " + String.format("%.1f",
                    t.getAccuracyPercent()) + "%\n\n";
        }

        compareArea.setText(text);
    }

    /**
     * Show best WPM.
     */
    private void showBest()
    {
        String result = "";

        for (Typist t : race.getTypists())
        {
            if (t == null) {
                continue;
            }

            result += t.getName() + ": "
                    + String.format("%.1f", t.getBestWPM())
                    + "\n";
        }

        JOptionPane.showMessageDialog(this, result);
    }

    /**
     * Show history records.
     */
    private void showHistory()
    {
        String result = "";

        for (Typist t : race.getTypists()) {
            if (t == null) {
                continue;
            }

            result += "=== " + t.getName() + " ===\n";

            for (String h : t.getHistory()) {
                result += h + "\n";
            }

            result += "\n";
        }

        JOptionPane.showMessageDialog(this, result);
    }
    
    /**
     * Restart game.
     */
    private void restartGame()
    {
        timer.stop();
    
        Typist[] oldTypists = race.getTypists();
    
        int num = Integer.parseInt(
            JOptionPane.showInputDialog(
                "Enter number of typists (2-6):"
            )
        );
    
        String passage = race.getPassage();
    
        race = new TypingRace(passage, num);
    
        for (int i = 0; i < num; i++) {
            Typist t;
    
            if (i < oldTypists.length && oldTypists[i] != null) {
                int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Seat " + (i + 1) +
                    ": Keep " +
                    oldTypists[i].getName() +
                    " ?",
                    "Reuse Typist",
                    JOptionPane.YES_NO_OPTION
                );
    
                if (choice == JOptionPane.YES_OPTION) {
                    t = oldTypists[i];
                }
                else {
                    t = createTypist(i);
                }
            }
            else {
                t = createTypist(i);
            }
    
            race.addTypist(t, i + 1);
        }
    
        race.startRace();
    
        this.getContentPane().removeAll();
    
        setupUI();
        startTimer();
    
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Show the leader board.
     */
    private void showLeaderboard()
    {
        Typist[] list = race.getTypists().clone();
    
        for (int i = 0; i < list.length - 1; i++)
        {
            for (int j = i + 1; j < list.length; j++)
            {
                if (list[j].getTotalPoints() > list[i].getTotalPoints())
                {
                    Typist temp = list[i];
                    list[i] = list[j];
                    list[j] = temp;
                }
            }
        }
    
        String text = "";
    
        for (int i = 0; i < list.length; i++)
        {
            text += (i + 1) + ". "
                 + list[i].getName()
                 + " - "
                 + list[i].getTotalPoints()
                 + " pts"
                 + " [" + list[i].getTitle() + "]\n";
        }
    
        JOptionPane.showMessageDialog(this, text);
    }
}