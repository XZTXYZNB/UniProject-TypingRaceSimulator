import javax.swing.*;
import java.awt.*;

public class RacePanel extends JPanel
{
    private TypingRace race;
    
    private Font uiFont = new Font("Arial", Font.BOLD, 16);
    private Font typeFont = new Font("Monospaced", Font.PLAIN, 14);
    
    private int leftX = 50;
    private int baseY = 50;
    private int rowGap = 80;

    public RacePanel(TypingRace race)
    {
        this.race = race;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    
        Typist[] typists = race.getTypists();
        String text = race.getPassage();
        
        // modes
        g.setFont(uiFont);
        g.setColor(Color.BLUE);
        g.drawString(getStatusText(), 20, 25);
    
        for (int i = 0; i < typists.length; i++)
        {
            Typist t = typists[i];
    
            if (t == null) {
                continue;
            }
    
            int y = baseY + i * rowGap;
    
            int progress = Math.min(t.getProgress(), text.length());
    
            String done = text.substring(0, progress);
            String remaining = text.substring(progress);
    
            // header
            g.setFont(uiFont);
            g.setColor(Color.BLACK);
    
            String prefix = t.getSymbol() + " " + t.getName();
            String info = " [" + t.getStyle() + " | " + t.getKeyboard() + " | " + t.getAccessory() + "]";
    
            g.drawString(prefix + info, leftX, y);
    
            // impact
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.setColor(Color.GRAY);
            g.drawString(getImpactText(t), leftX + 20, y + 18);
    
            // typing
            g.setFont(typeFont);
    
            int textX = leftX + 50;
    
            g.setColor(t.getColour());
            g.drawString(done, textX, y + 45);
    
            g.setColor(Color.BLACK);
            g.drawString("█" + remaining,
                    textX + g.getFontMetrics(typeFont).stringWidth(done),
                    y + 45);
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
    
        return "Autocorrect: " + ac + "   |   Caffeine: " + cf + "   |   Night Shift: " + ns;
    }
    
    /**
     * Return inofrmation of the impact.
     */
    private String getImpactText(Typist t)
    {
        String speed = "Normal";
        String burnout = "Medium";
        String accuracy = "Medium";
    
        if (t.getKeyboard().equals("Mechanical")) {
            speed = "Fast";
        }
    
        if (t.getKeyboard().equals("Touchscreen")) {
            speed = "Slow";
        }
    
        if (t.getKeyboard().equals("Stenography")) {
            speed = "Very Fast";
        }
    
        if (t.getStyle().equals("Touch Typist")) {
            accuracy = "High";
        }
    
        if (t.getStyle().equals("Voice-to-Text")) {
            accuracy = "Very High";
        }
    
        if (t.getAccessory().equals("Wrist Support")) {
            burnout = "Low";
        }
    
        if (t.getAccessory().equals("Energy Drink")) {
            burnout = "High";
        }
    
        return "Accuracy: " + accuracy + "   Speed: " + speed + "   Burnout Risk: " + burnout;
    }
}