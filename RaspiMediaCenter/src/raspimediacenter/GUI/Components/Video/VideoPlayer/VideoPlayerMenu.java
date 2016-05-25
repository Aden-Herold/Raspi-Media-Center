package raspimediacenter.GUI.Components.Video.VideoPlayer;

import raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons.PlayButton;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons.FastForwardButton;
import raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons.NextChapterButton;
import raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons.PreviousChapterButton;
import raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons.RewindButton;
import raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons.StopButton;
import raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons.SubtitlesButton;
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
     public void setupMusicList(ArrayList<String> artPaths, ArrayList<String> nameList, ArrayList<String> tagList, ArrayList<String> bioList){} // NOT NEEDED
    
    @Override
    public void setupLibraryList (ArrayList<String> list)
    {
        menuButtons = new ArrayList<>();
        int btnSize = width/25;

        //Create buttons
        PlayButton play = new PlayButton(width/2-btnSize/2, overlayHeight/2-btnSize/2, btnSize, btnSize);
        StopButton stop = new StopButton(width/2-btnSize*2, overlayHeight/2-btnSize/2, btnSize, btnSize);
        SubtitlesButton subtitles = new SubtitlesButton(width/2+btnSize, overlayHeight/2-btnSize/2, btnSize, btnSize);
        FastForwardButton ffButton = new FastForwardButton(width/2+btnSize*2+btnSize/2, overlayHeight/2-btnSize/2, btnSize, btnSize, play);
        RewindButton rewind = new RewindButton(width/2-btnSize*3-btnSize/2, overlayHeight/2-btnSize/2, btnSize, btnSize, play);
        PreviousChapterButton pvChapter = null;
        NextChapterButton nxChapter = null;
        
        if (SceneManager.getCurrentScene().getPlayer().getChapterCount() > 0)
        {
             pvChapter= new PreviousChapterButton(width/2-btnSize*5, overlayHeight/2-btnSize/2, btnSize, btnSize, play);
             nxChapter= new NextChapterButton(width/2+btnSize*4, overlayHeight/2-btnSize/2, btnSize, btnSize, play);
        }
        
        //Add buttons in correct order
        if (pvChapter != null)
        {
            menuButtons.add(pvChapter);
        }
        
        menuButtons.add(rewind);
        menuButtons.add(stop);
        menuButtons.add(play);
        menuButtons.add(subtitles);
        menuButtons.add(ffButton);
        
        if (nxChapter != null)
        {
            menuButtons.add(nxChapter);
        }
        
        play.setState(true);
        focusedButton = play;
        focusedButtonPos = menuButtons.indexOf(play);
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
        if (SceneManager.getCurrentScene().getPlayer().getControlsVisible())
        {
            focusedButton.performAction();
        }
        else
        {
            SceneManager.getCurrentScene().getPlayer().toggleControls();
        }
        
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
