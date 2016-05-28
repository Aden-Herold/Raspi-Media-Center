package raspimediacenter.GUI.Components.Music;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;

public class MusicLibrary extends SceneMenu{

    private final int BTN_HEIGHT = (int)Math.floor(GUI.getScreenHeight()*0.20);
    private final double LIST_HEIGHT = GUI.getScreenHeight();
    private boolean isScrollable = true;
    private int currentScrollAmount = 0;

    private MusicLibraryLabel focusedButton;
    private int focusedLabelPos = 0;
    private int totalListItems;
    private ArrayList<MusicLibraryLabel> libraryButtons;
    
    // GETTERS
    @Override
    public String getFocusedButtonText()
    {
        return libraryButtons.get(focusedLabelPos).getText();
    }
    
    @Override
    public boolean isScrollable() {
        return isScrollable;
    }

    @Override
    public int getFocusedButtonPos() {
        return focusedLabelPos;
    }

    // SETTERS
    @Override
    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    // SETUP FUNCTIONS
    @Override
    public void setupLibraryList (ArrayList<String> list){} // NOT NEEDED
    
    @Override
     public void setupMusicList(ArrayList<String> artPaths, ArrayList<String> nameList, ArrayList<String> bioList)
     {
        totalListItems = nameList.size();
        libraryButtons = new ArrayList<>();

        int element = 0;
        
        for (int yPos = 0; element < totalListItems; yPos+=BTN_HEIGHT)
        {
            MusicLibraryLabel btn;
            if (yPos < LIST_HEIGHT)
            {
                btn = new MusicLibraryLabel(
                        artPaths.get(element),
                        nameList.get(element), 
                        bioList.get(element),
                        0,
                        yPos,
                        true
                );  
            }
            else
            {
                btn = new MusicLibraryLabel(
                        artPaths.get(element),
                        nameList.get(element), 
                        bioList.get(element),
                        0,
                        yPos,
                        false
                );  
            }
            libraryButtons.add(btn);
            element++;
        }  
        
        focusedButton = libraryButtons.get(0);
        focusedButton.setFocused(true);
    }

     @Override
    public void unloadMenu()
    {
        focusedButton = null;
        focusedLabelPos = 0;
        totalListItems = 0;
        
        for (MusicLibraryLabel btn : libraryButtons)
        {
            btn = null;
        }
    }
     
     // EVENT FUNCTIONS
    @Override
    public void clickedButton()
    {
        SceneManager.getCurrentScene().buttonClicked();
    }
    
    @Override
    public void clickedButton(MouseEvent e)
    {
        Point mousePos = e.getLocationOnScreen();
        
        if (libraryButtons != null)
        {
            for (int x = 0; x < libraryButtons.size(); x++)
            {
                if (libraryButtons.get(x).getBounds().contains(mousePos))
                {
                    SceneManager.getCurrentScene().buttonClicked();
                }
            }  
        }
    }
    
    @Override
    public void focusNextButton() 
    {
        if (focusedLabelPos+1 > libraryButtons.size()-1)
        {
            scrollToTop();
            focusButton(0);
        }
        else
        {
            if (libraryButtons.get(focusedLabelPos+1).getVisible() == false)
            {
                scrollList(-1);
            }
            focusButton(focusedLabelPos+1);
        }
    }

    @Override
    public void focusPreviousButton() 
    {
        if (focusedLabelPos-1 < 0)
        {
            scrollToBottom();
            focusButton(libraryButtons.size()-1);
        }
        else
        {
            if (libraryButtons.get(focusedLabelPos-1).getVisible() == false)
            {
                scrollList(1);
            }

            focusButton(focusedLabelPos-1);
        }
    }
    
    @Override
    public void focusHoveredButton (MouseEvent e)
    {
        Point mousePos = e.getLocationOnScreen();
        
        for (int x = 0; x < libraryButtons.size(); x++)
        {
            if (libraryButtons.get(x).getBounds().contains(mousePos))
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

        focusedButton = libraryButtons.get(button);
        focusedLabelPos = button;
        libraryButtons.get(button).setFocused(true);

        if (SceneManager.getCurrentScene() != null)
        {
            SceneManager.getCurrentScene().paintScene();
        }
    }
    
    private void scrollToTop()
    {
        if (currentScrollAmount < 0)
        {
            for (MusicLibraryLabel btn : libraryButtons)
            {
                  btn.updateY(currentScrollAmount*-1);
                  toggleButtonVisible(btn);
            }
            currentScrollAmount = 0;
        }
    }
    
    private void scrollToBottom()
    {
            int btnsInvis = 0;
            for (int x = libraryButtons.size()-1; x >= 0; x--)
            {
                if (libraryButtons.get(x).getVisible() == false)
                {
                    btnsInvis++;
                }
                else
                {
                    break;
                }
            }
            
            int adjustAmount = btnsInvis*BTN_HEIGHT;
            for (MusicLibraryLabel btn : libraryButtons)
            {
                  btn.updateY(-adjustAmount);
                  toggleButtonVisible(btn);
            }
            currentScrollAmount = adjustAmount*-1;
    }
    
    @Override
    public void scrollList (int direction)
    {
        if (direction < 0) //UP
        {
            if (!(libraryButtons.get(libraryButtons.size()-1).getY() < LIST_HEIGHT))
            {
                for (MusicLibraryLabel btn : libraryButtons)
                {
                    btn.updateY(-BTN_HEIGHT);
                    toggleButtonVisible(btn);
                }
                currentScrollAmount -= BTN_HEIGHT;
            }
        }
        else //DOWN
        {
            if (!(libraryButtons.get(0).getY() >= 0))
            {
                for (MusicLibraryLabel btn : libraryButtons)
                {
                    btn.updateY(BTN_HEIGHT);
                    toggleButtonVisible(btn);
                }
                currentScrollAmount += BTN_HEIGHT;
            }
        }

        if (SceneManager.getCurrentScene() != null)
        {
            SceneManager.getCurrentScene().paintScene();
        }
    }

    private void toggleButtonVisible(MusicLibraryLabel btn)
    {
         if (((btn.getY()+btn.getHeight()) > 0) && (btn.getY() < LIST_HEIGHT))
        {
            btn.setVisible(true);
        }
        else
        {
            btn.setVisible(false);
        }
    }
    
    // DRAW FUNCTIONS
    @Override
    public void drawMenu(Graphics2D g2d) {
        Iterator<MusicLibraryLabel> iter = libraryButtons.iterator();
        
        while (iter.hasNext())
        {
            MusicLibraryLabel label = iter.next();
            
            if (label.getVisible())
            {
                label.paintSceneComponent(g2d);
            }
        }
    }
}
