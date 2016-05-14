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
    private final int BUTTON_WIDTH = GUI.getScreenWidth()/5;
    private boolean isScrollable = true;

    private LibraryButton focusedButton;
    private int focusedButtonPos = 0;
    private int totalListItems;
    private ArrayList<LibraryButton> libraryButtons;
    
    public FileLibrary ()
    {
    }
    
    // GETTERS
    public int getButtonWidth()
    {
        return BUTTON_WIDTH;
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
    public void setupLibraryList (ArrayList<String> list)
    {
        totalListItems = list.size();
        libraryButtons = new ArrayList<>();
        int btnWidth = GUI.getScreenWidth()/5;
        int btnHeight = (int)Math.floor(GUI.getScreenHeight()/28.8);
        int element = 0;
        for (int x = 0; element < totalListItems; x+=btnHeight)
        {
            LibraryButton btn;
            
            if (x-45 < LIST_HEIGHT)
            {
                btn = new LibraryButton(list.get(element), 
                      GUI.getScreenWidth()-btnWidth,
                      x,
                      btnWidth,
                      btnHeight,
                      true
                );  
            }
            else
            {
                btn = new LibraryButton(list.get(element), 
                    GUI.getScreenWidth()-btnWidth,
                    x,
                    btnWidth,
                    btnHeight,
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
            focusButton(libraryButtons.size()-1);
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
    
    @Override
    public void scrollList (int direction, int amount)
    {
        if (direction < 0) //UP
        {
            if (!(libraryButtons.get(libraryButtons.size()-1).getY() < LIST_HEIGHT))
            {
                for (LibraryButton btn : libraryButtons)
                {
                    btn.updateY(-amount);
                    if (((btn.getY()+btn.getHeight())+5 > 0) && (btn.getY()-45 < LIST_HEIGHT))
                    {
                        btn.setVisible(true);
                    }
                    else
                    {
                        btn.setVisible(false);
                    }
                }
            }
        }
        else //DOWN
        {
            if (!(libraryButtons.get(0).getY() >= 0))
            {
                for (LibraryButton btn : libraryButtons)
                {
                    btn.updateY(amount);
                    if (((btn.getY()+btn.getHeight())+5 > 0) && (btn.getY()-45 < LIST_HEIGHT))
                    {
                        btn.setVisible(true);
                    }
                    else
                    {
                        btn.setVisible(false);
                    }
                }
            }
        }

        if (SceneManager.getCurrentScene() != null)
        {
            SceneManager.getCurrentScene().paintScene();
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
