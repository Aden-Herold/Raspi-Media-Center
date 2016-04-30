package raspimediacenter.GUI.Components;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import raspimediacenter.GUI.SceneManager;

public class OverviewDisplay {

    private long startTime = -1;
    private int range = 0;
    private int runningTime = 5000; //5 Seconds
    private int direction = 1;
    
    private final JTextArea overviewTextArea;
    private ItemScrollPanel overviewPanel;
    
    public OverviewDisplay (String overview)
    {
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.setOpaque(false);
        panel.setFocusable(false);
        
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        overviewTextArea = new StyledTextArea(overview, Font.PLAIN, fontSize, SwingConstants.LEFT);

        overviewPanel = new ItemScrollPanel(overviewTextArea);
        overviewPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        AutoScrollTimer.start();
    }
    
    public ItemScrollPanel getOverviewPanel()
    {
        return overviewPanel;
    }
    
    public void updateOverview (String overview)
    {
        overviewTextArea.setText(overview);
        startTime = -1;
    }
    
    //AUTOSCROLL TIMER 
    final Timer AutoScrollTimer = new Timer(40, (ActionEvent e) -> {

        if (overviewPanel.getVerticalScrollBar().isVisible())
        {
            if (startTime < 0) {
                startTime = System.currentTimeMillis();
                range = overviewPanel.getViewport().getView().getPreferredSize().height - overviewPanel.getHeight();
            }
            
            long duration = System.currentTimeMillis() - startTime;
            float progress = 1f;
            
            if (duration >= runningTime) {
                startTime = -1;
                direction *= -1;

                if (direction < 0) {
                    progress = 1f;
                } 
                else {
                    progress = 0f;
                }
            } 
            else {
                progress = (float) duration / (float) runningTime;
                if (direction < 0) {
                    progress = 1f - progress;
                }
            }

            int yPos = (int) (range * progress);
            overviewPanel.getViewport().setViewPosition(new Point(0, yPos));
        }
    });
}
