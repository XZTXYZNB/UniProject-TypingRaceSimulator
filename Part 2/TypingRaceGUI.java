import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TypingRaceGUI extends JFrame
{
    private TypingRace race;
    private RacePanel panel;
    private Timer timer;

    public TypingRaceGUI()
    {
        int num = Integer.parseInt(JOptionPane.showInputDialog("Enter number of typists (2-6):"));

        int option = Integer.parseInt(JOptionPane.showInputDialog("Enter number to choose the length of the passage (1:short 2:medium 3:long 4:custom)"));
        String passage;
        if (option == 4) {
            passage = JOptionPane.showInputDialog("Enter custom passage:");
        }
        else {
            passage = TypingRace.getPassageOption(option);
        }

        race = new TypingRace(passage, num);
        
        

        // Create typists
        for (int i = 0; i < num; i++)
        {
            String name = JOptionPane.showInputDialog("Typist " + (i + 1) + " name:");

            double acc = Double.parseDouble(
                    JOptionPane.showInputDialog("Accuracy (0.0 - 1.0):")
            );

            String symbol = JOptionPane.showInputDialog("Enter symbol or emoji for Typist " + (i + 1)); 
            
            //Choose typing style
            String[] styles = {
                "Touch Typist",
                "Hunt & Peck",
                "Phone Thumbs",
                "Voice-to-Text"
            };
            
            String style = (String) JOptionPane.showInputDialog(
                null,
                "Choose typing style:",
                "Style",
                JOptionPane.PLAIN_MESSAGE,
                null,
                styles,
                styles[0]
            );
            
            //Choose keyboard type
            String[] keyboards = {
                "Mechanical",
                "Membrane",
                "Touchscreen",
                "Stenography"
            };
            
            String keyboard = (String) JOptionPane.showInputDialog(
                null,
                "Choose keyboard type:",
                "Keyboard",
                JOptionPane.PLAIN_MESSAGE,
                null,
                keyboards,
                keyboards[0]
            );
            
            //Choose colour
            String[] colours = {
                "Red",
                "Orange",
                "Yellow",
                "Green",
                "Blue",
            };
            
            String colourChoice = (String) JOptionPane.showInputDialog(
                null,
                "Choose colour:",
                "Colour",
                JOptionPane.PLAIN_MESSAGE,
                null,
                colours,
                colours[0]
            );
            
            Color chosenColour = Color.RED;

            if (colourChoice.equals("Orange")) {
                chosenColour = Color.ORANGE;
            }
            else if (colourChoice.equals("Yellow")) {
                chosenColour = Color.YELLOW;
            }
            else if (colourChoice.equals("Green")) {
                chosenColour = Color.GREEN;
            }
            else if (colourChoice.equals("Blue")) {
                chosenColour = Color.BLUE;
            }
            
            
            //Choose accessory
            String[] accessories = {
                "None",
                "Wrist Support",
                "Energy Drink",
                "Headphones"
            };
            
            String accessory = (String) JOptionPane.showInputDialog(
                null,
                "Choose accessory:",
                "Accessory",
                JOptionPane.PLAIN_MESSAGE,
                null,
                accessories,
                accessories[0]
            );

            Typist t = new Typist(symbol, name, acc);
            t.setAccessory(accessory);
            t.setColour(chosenColour);
            t.setStyle(style);
            t.setKeyboard(keyboard);

            race.addTypist(t, i + 1);
        }
        
        int ac = JOptionPane.showConfirmDialog(null, "Enable Autocorrect?");
        int cf = JOptionPane.showConfirmDialog(null, "Enable Caffeine Mode?");
        int ns = JOptionPane.showConfirmDialog(null, "Enable Night Shift?");
        race.setModifiers(ac == 0, cf == 0, ns == 0);
        
        //Starting point
        race.startRace();

        // UI
        panel = new RacePanel(race);
        this.add(panel);

        this.setTitle("Typing Race");
        this.setSize(900, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // timer
        timer = new Timer(200, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                race.step();
                panel.repaint();

                if (race.isFinished())
                {
                    timer.stop();
                }
            }
        });

        timer.start();
    }

    public static void main(String[] args)
    {
        new TypingRaceGUI();
    }
}