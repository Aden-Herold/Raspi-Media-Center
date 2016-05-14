package raspimediacenter.GUI.Components.Video.VideoPlayer;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;

public class VideoPlayerMenu extends SceneMenu {

    private final int width = GUI.getScreenWidth();
    private final int height = GUI.getScreenHeight();
    
    private final int overlayHeight = height/10;
    
    private VideoPlayerButton focusedButton;
    private int focusedButtonPos = 0;
    private ArrayList<VideoPlayerButton> menuButtons;
    
    public VideoPlayerMenu () {}
    
    // GETTERS
    @Override 
    public int getFocusedButtonPos ()
    {
        return focusedButtonPos;
    }
    
    @Override
    public boolean isScrollable() {
        return false;
    }
    
    // SETTERS
    @Override
    public void setScrollable(boolean isScrollable) {
        //NOT SCROLLABLE
    }
    
    // SETUP FUNCTIONS
    @Override
    public void setupLibraryList (ArrayList<String> list)
    {
        menuButtons = new ArrayList<>();
        
        int playSize = width/25;
        PlayButton play = new PlayButton(width/2-playSize/2, overlayHeight/2-playSize/2, playSize, playSize);
        play.setState(true);
        menuButtons.add(play);
        
        focusedButton = play;
        focusedButton.setFocused(true);
    }
    
    @Override
    public void unloadMenu()
    {
        focusedButton = null;
        focusedButtonPos = 0;
        
        for (VideoPlayerButton btn : menuButtons)
        {
            btn = null;
        }
    }
    
    // EVENT FUNCTIONS
    @Override
    public void clickedButton()
    {
        focusedButton.performAction();
        SceneManager.getCurrentScene().buttonClicked();
        SceneManager.getCurrentScene().paintScene();
    }
    
    @Override
    public void clickedButton(MouseEvent e)
    {
        Point mousePos = e.getLocationOnScreen();

        if (menuButtons != null)
        {
            for (int x = 0; x < menuButtons.size(); x++)
            {
                if (menuButtons.get(x).getRect().contains(mousePos))
                {
                    SceneManager.getCurrentScene().buttonClicked();
                    menuButtons.get(x).performAction();

                    SceneManager.getCurrentScene().paintScene();
                }
            }
        }
        
    }
    
    @Override
    public void focusNextButton() 
    {
        if (focusedButtonPos+1 > menuButtons.size()-1)
        {
            focusButton(0);
        }
        else
        {
            focusButton(focusedButtonPos+1);
        }
    }

    @Override
    public void focusPreviousButton() 
    {
        if (focusedButtonPos-1 < 0)
        {
            focusButton(menuButtons.size()-1);
        }
        else
        {
            focusButton(focusedButtonPos-1);
        }
    }
    
    @Override
    public void focusHoveredButton (MouseEvent e)
    {
        Point mousePos = e.getLocationOnScreen();
        
        for (int x = 0; x < menuButtons.size(); x++)
        {
            if (menuButtons.get(x).getRect().contains(mousePos))
            {
                focusButton(x);
            }
        }
    }
    
    @Override
    protected void focusButton (int button)
    {
        SceneManager.getCurrentScene().updateBackground(button);
        SceneManager.getCurrentScene().updatePreviewImage(button);
        SceneManager.getCurrentScene().updateInformationLabels(button);

        if (focusedButton != null)
        {
            focusedButton.setFocused(false);
        }

        focusedButton = menuButtons.get(button);
        focusedButtonPos = button;
        menuButtons.get(button).setFocused(true);

        if (SceneManager.getCurrentScene() != null)
        {
            SceneManager.getCurrentScene().paintScene();
        }

        SceneManager.getCurrentScene().paintScene();
    }
    
    @Override
    public void scrollList (int direction, int amount)
    {
        //MENU CANNOT SCROLL
    }

    @Override
    public void drawMenu(Graphics2D g2d) 
    {
        g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
        g2d.setPaint(SceneManager.getMenuColor());
        g2d.fillRect(0, 0, width, overlayHeight);
        
        //DRAW MENU BUTTONS
        for (VideoPlayerButton btn : menuButtons)
        {
            btn.drawButton(g2d); 
        }
    }
}
