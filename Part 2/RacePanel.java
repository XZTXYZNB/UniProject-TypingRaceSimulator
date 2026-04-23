import javax.swing.*;
import java.awt.*;

public class RacePanel extends JPanel
{
    private TypingRace race;

    public RacePanel(TypingRace race)
    {
        this.race = race;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setFont(new Font("Monospaced", Font.PLAIN, 14));

        g.setColor(Color.BLUE);
        g.drawString(getStatusText(), 20, 20);
    
        Typist[] typists = race.getTypists();

        int y = 50;

        String text = race.getPassage();
        
        for (int i = 0; i < typists.length; i++)
        {
            Typist t = typists[i];

            if (t == null)
            {
                continue;
            }

            int progress = t.getProgress();

            if (progress > text.length())
            {
                progress = text.length();
            }
            
            String done = text.substring(0, progress);
            String remaining = text.substring(progress);
            
            String prefix = t.getSymbol() + " " + t.getName() + ": ";

            g.setColor(Color.BLACK);
            g.drawString(prefix, 50, y);
            
            // Mark the parts done to green
            g.setColor(Color.GREEN);
            int x = 50 + g.getFontMetrics().stringWidth(prefix);
            g.drawString(done, x, y);
            
            // Add cursor and mark part to be done to black
            g.setColor(Color.BLACK);
            g.drawString("█" + remaining, 50 + g.getFontMetrics().stringWidth(t.getSymbol() + " " + t.getName() + ": " + done), y);
            
            y = y + 40;
        }
    }
    
    /**
     * Return the mode that have been activated.
     */
    private String getStatusText()
    {
        String ac = race.isAutocorrectOn() ? "ON" : "OFF";
        String cf = race.isCaffeineMode() ? "ON" : "OFF";
        String ns = race.isNightShift() ? "ON" : "OFF";
    
        return "Autocorrect: " + ac +
               "   |   Caffeine: " + cf +
               "   |   Night Shift: " + ns;
    }
}