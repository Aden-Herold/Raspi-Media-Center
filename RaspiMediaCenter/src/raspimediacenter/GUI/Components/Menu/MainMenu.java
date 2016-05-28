package raspimediacenter.GUI.Components.Menu;

import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MainMenu extends SceneMenu {

    private int MENU_Y_POSITION = (int)Math.floor(GUI.getScreenHeight()*0.8); //80% down the screen 
    private final int MENU_HEIGHT = (int)(Math.floor(GUI.getScreenHeight()/14.4));
    private boolean isScrollable = false;

    private MainMenuButton focusedButton;
    private int focusedButtonPos = 0;
    private int totalListItems;
    private ArrayList<MainMenuButton> menuButtons;
    
    public MainMenu () {}
    
    // GETTERS
    @Override
    public String getFocusedButtonText()
    {
        return menuButtons.get(focusedButtonPos).getText();
    }
    
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
        this.isScrollable = false;
    }
    
    // SETUP FUNCTIONS
    @Override
     public void setupMusicList(ArrayList<String> artPaths, ArrayList<String> nameList, ArrayList<String> bioList){} // NOT NEEDED
    
    @Override
    public void setupLibraryList (ArrayList<String> list)
    {
        totalListItems = list.size();
        menuButtons = new ArrayList<>();
        int btnWidth = GUI.getScreenWidth()/6;
        int element = 0;
        
        int menuStartPoint = (btnWidth*list.size())/4;
        
        for (int x = 0; element < totalListItems; x+=btnWidth)
        {
            MainMenuButton btn;
            
            btn = new MainMenuButton(list.get(element), 
                      menuStartPoint+x,
                      MENU_Y_POSITION,
                      btnWidth,
                      MENU_HEIGHT
                ); 
            
            menuButtons.add(btn);
            element++;
        }  
        
        focusedButton = menuButtons.get(0);
        focusedButton.setFocused(true);
    }
    
    @Override
    public void unloadMenu()
    {
        focusedButton = null;
        focusedButtonPos = 0;
        totalListItems = 0;
        
        for (MainMenuButton btn : menuButtons)
        {
            btn = null;
        }
    }
    
    // EVENT FUNCTIONS
    @Override
    public void clickedButton(){
        SceneManager.getCurrentScene().buttonClicked();
    }
    
    @Override
    public void clickedButton(MouseEvent e)
    {
        Point mousePos = e.getLocationOnScreen();
        
        if (menuButtons != null && e != null)
        {
            for (int x = 0; x < menuButtons.size(); x++)
            {
                if (menuButtons.get(x).getRect().contains(mousePos))
                {
                    SceneManager.getCurrentScene().buttonClicked();
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
    }
    
    @Override
    public void scrollList (int direction)
    {
        //MENU CANNOT SCROLL
    }

    @Override
    public void drawMenu(Graphics2D g2d) {
        
        //DRAW MENU SHADOW
        Color invis = new Color(0, 0, 0, 0);
        final float[] shadowFrac = {0f, 0.07f, 0.15f, 0.86f, 0.92f, 1f};
        final Color[] shadowGrad = {invis, Color.BLACK, invis, invis, Color.BLACK, invis};
        LinearGradientPaint shadowGradPaint = new LinearGradientPaint(
                                                            new Point2D.Double(0, MENU_Y_POSITION-10),
                                                            new Point2D.Double(0, MENU_Y_POSITION+MENU_HEIGHT+10),
                                                            shadowFrac,
                                                            shadowGrad);
        g2d.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()));
        g2d.setPaint(shadowGradPaint);
        g2d.fillRect(0, MENU_Y_POSITION-10, GUI.getScreenWidth(), MENU_HEIGHT+20);

        //DRAW MENU
        final float[] menuFrac = {0f, 0.3f, 0.7f, 1f};
        Color menuColor = SceneManager.getMenuColor();
        Color darkerMenuColor = ColorUtils.darken(menuColor, 3);

        final Color[] menuGrad = {darkerMenuColor, menuColor, menuColor, darkerMenuColor};
        LinearGradientPaint menuGradPaint = new LinearGradientPaint(
                                                            new Point2D.Double(0, MENU_Y_POSITION),
                                                            new Point2D.Double(GUI.getScreenWidth(), MENU_Y_POSITION),
                                                            menuFrac,
                                                            menuGrad);
        
        
        g2d.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()));
        g2d.setPaint(menuGradPaint);
        g2d.fillRect(0, MENU_Y_POSITION, GUI.getScreenWidth(), MENU_HEIGHT);
        
        //DRAW MENU BUTTONS
        for (MainMenuButton btn : menuButtons)
        {
            if (btn.getVisible())
            {
               btn.paintSceneComponent(g2d); 
            }
        }
    }
}
