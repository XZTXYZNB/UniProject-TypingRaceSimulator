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

            char symbol = (char)('①' + i); 

            Typist t = new Typist(symbol, name, acc);

            race.addTypist(t, i + 1);
        }
        
        int ac = JOptionPane.showConfirmDialog(null, "Enable Autocorrect?");
        int cf = JOptionPane.showConfirmDialog(null, "Enable Caffeine Mode?");
        int ns = JOptionPane.showConfirmDialog(null, "Enable Night Shift?");
        race.setModifiers(ac == 0, cf == 0, ns == 0);

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