package raspimediacenter.GUI.Components.Music;

import java.awt.Graphics2D;
import java.util.ArrayList;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.Logic.Utilities.ColorUtils;

public class Equalizer extends SceneComponent
{
    private final int EQUALIZER_WIDTH = (int)Math.floor(GUI.getScreenWidth()/8);
    private final int EQUALIZER_HEIGHT= GUI.getScreenHeight();
    private final int EQUALIZER_COL_WIDTH = EQUALIZER_WIDTH;
    private final int EQUALIZER_COL_HEIGHT = (int)Math.floor(GUI.getScreenHeight()*0.05);
    
    ArrayList<EqualizerColumn> columns;
    
    public Equalizer ()
    {
        columns = new ArrayList<>();
        setupEqualizer();
    }
    
    private void setupEqualizer()
    {
        int maxCols = EQUALIZER_HEIGHT / EQUALIZER_COL_HEIGHT;
        double hueShift = 1f/maxCols;
        
        int yPos = 0;
        double hue = 0;
        
        for (int x = 0; x < maxCols; x++)
        {
            EqualizerColumn col = new EqualizerColumn(
                                                                                    GUI.getScreenWidth()-EQUALIZER_COL_WIDTH,
                                                                                    yPos,
                                                                                    EQUALIZER_COL_WIDTH,
                                                                                    EQUALIZER_COL_HEIGHT,
                                                                                    ColorUtils.getColor(hue)
                                                                                    );
            columns.add(col);
            
            yPos += EQUALIZER_COL_HEIGHT;
            hue += hueShift;
        }
    }
    
    public void randomiseColumns ()
    {
        for (EqualizerColumn col : columns)
        {
            col.updateColumn();
        }
    }
    
    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        
        randomiseColumns();
        
        for (EqualizerColumn col : columns)
        {
            col.paintColumn(g2d);
        }
    }
}
