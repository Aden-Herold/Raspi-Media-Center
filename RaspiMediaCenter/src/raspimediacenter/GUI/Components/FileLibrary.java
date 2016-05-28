package raspimediacenter.GUI.Components;

import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FileLibrary extends SceneMenu {

    private final double LIST_LENGTH = 0.78; //0.78 Default
    private final double LIST_HEIGHT = (int)(Math.floor(GUI.getScreenHeight()*LIST_LENGTH));
    private final int BTN_WIDTH = GUI.getScreenWidth()/5;
    private final int BTN_HEIGHT = (int)Math.floor(GUI.getScreenHeight()/28.8);
    private boolean isScrollable = true;
    private int currentScrollAmount = 0;

    private LibraryButton focusedButton;
    private int focusedButtonPos = 0;
    private int totalListItems;
    private ArrayList<LibraryButton> libraryButtons;
    
    public FileLibrary (){}
    
    // GETTERS
    @Override
    public String getFocusedButtonText()
    {
        return libraryButtons.get(focusedButtonPos).getText();
    }
    
    public int getButtonWidth()
    {
        return BTN_WIDTH;
    }
    
    @Override 
    public int getFocusedButtonPos ()
    {
        return focusedButtonPos;
    }
    
    @Override
    public boolean isScrollable() {
        return isScrollable;
    }
    
    // SETTERS
    @Override
    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }
    
    // SETUP FUNCTIONS
    @Override
     public void setupMusicList(ArrayList<String> artPaths, ArrayList<String> nameList, ArrayList<String> bioList){} // NOT NEEDED
    
    @Override
    public void setupLibraryList (ArrayList<String> list)
    {
        totalListItems = list.size();
        libraryButtons = new ArrayList<>();

        int element = 0;
        for (int x = 0; element < totalListItems; x+=BTN_HEIGHT)
        {
            LibraryButton btn;
            
            if ((x+BTN_HEIGHT > 0) && (x < LIST_HEIGHT))
            {
                btn = new LibraryButton(list.get(element), 
                      GUI.getScreenWidth()-BTN_WIDTH,
                      x,
                      BTN_WIDTH,
                      BTN_HEIGHT,
                      true
                );  
            }
            else
            {
                btn = new LibraryButton(list.get(element), 
                    GUI.getScreenWidth()-BTN_WIDTH,
                    x,
                    BTN_WIDTH,
                    BTN_HEIGHT,
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
        focusedButtonPos = 0;
        totalListItems = 0;
        
        for (LibraryButton btn : libraryButtons)
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
                if (libraryButtons.get(x).getRect().contains(mousePos))
                {
                    SceneManager.getCurrentScene().buttonClicked();
                }
            }  
        }
    }
    
    @Override
    public void focusNextButton() 
    {
        if (focusedButtonPos+1 > libraryButtons.size()-1)
        {
            scrollToTop();
            focusButton(0);
        }
        else
        {
            if (libraryButtons.get(focusedButtonPos+1).getVisible() == false)
            {
                scrollList(-1);
            }
            focusButton(focusedButtonPos+1);
        }
    }

    @Override
    public void focusPreviousButton() 
    {
        if (focusedButtonPos-1 < 0)
        {
            scrollToBottom();
            focusButton(libraryButtons.size()-1);
        }
        else
        {
            if (libraryButtons.get(focusedButtonPos-1).getVisible() == false)
            {
                scrollList(1);
            }

            focusButton(focusedButtonPos-1);
        }
    }
    
    @Override
    public void focusHoveredButton (MouseEvent e)
    {
        Point mousePos = e.getLocationOnScreen();
        
        for (int x = 0; x < libraryButtons.size(); x++)
        {
            if (libraryButtons.get(x).getRect().contains(mousePos))
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

        focusedButton = libraryButtons.get(button);
        focusedButtonPos = button;
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
            for (LibraryButton btn : libraryButtons)
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
            for (LibraryButton btn : libraryButtons)
            {
                  btn.updateY(-adjustAmount);
                  toggleButtonVisible(btn);
            }
            currentScrollAmount = adjustAmount*-1;
    }
    
    @Override
    public void scrollList (int direction)
    {
        //Ensure even number
        int scrollAmount =(int) Math.floor((BTN_HEIGHT)/10)*10;
        
        if (direction < 0) //UP
        {
            if (!(libraryButtons.get(libraryButtons.size()-1).getY() < LIST_HEIGHT))
            {
                for (LibraryButton btn : libraryButtons)
                {
                    btn.updateY(-scrollAmount);
                    toggleButtonVisible(btn);
                }
                currentScrollAmount -= scrollAmount;
            }
        }
        else //DOWN
        {
            if (!(libraryButtons.get(0).getY() >= 0))
            {
                for (LibraryButton btn : libraryButtons)
                {
                    btn.updateY(scrollAmount);
                    toggleButtonVisible(btn);
                }
                currentScrollAmount += scrollAmount;
            }
        }

        if (SceneManager.getCurrentScene() != null)
        {
            SceneManager.getCurrentScene().paintScene();
        }
    }

    private void toggleButtonVisible(LibraryButton btn)
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
    
    @Override
    public void drawMenu(Graphics2D g2d) {
        for (LibraryButton btn : libraryButtons)
        {
            if (btn.getVisible())
            {
               btn.paintSceneComponent(g2d); 
            }
        }
    }
}
