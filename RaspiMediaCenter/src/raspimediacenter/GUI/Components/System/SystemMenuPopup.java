package raspimediacenter.GUI.Components.System;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import raspimediacenter.Logic.Utilities.TextUtils;

public class SystemMenuPopup extends SceneMenu {

    private final int BTN_HEIGHT = (int)Math.floor(GUI.getScreenHeight()*0.03);
    private final int WIDTH = (int)Math.floor(GUI.getScreenWidth()*0.2);
    private final int LIST_HEIGHT;
    
    private final Rectangle bounds;
    private final ArrayList<String> options;
    private ArrayList<SystemMenuButton> btns;
    private SystemMenuButton focusedButton;
    
    private boolean isScrollable = false;
    private int focusedButtonPos = 0;
    private int currentScrollAmount = 0;
    
    public SystemMenuPopup (int x, int y, ArrayList<String> options)
    {
        LIST_HEIGHT = (BTN_HEIGHT*options.size());
        
        bounds = new Rectangle (x-WIDTH/2, y-LIST_HEIGHT/2, WIDTH, LIST_HEIGHT*2);
        this.options = options;
    }
    
    // GETTERS
    public int getButtonWidth()
    {
        return WIDTH;
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
    public void setScrollable(boolean isScrollable) {}
    
    // SETUP FUNCTIONS
    @Override
     public void setupMusicList(ArrayList<String> artPaths, ArrayList<String> nameList, ArrayList<String> bioList){} // NOT NEEDED
    
    @Override
    public void setupLibraryList (ArrayList<String> list)
    {
        btns = new ArrayList<>();
        int yPos = BTN_HEIGHT;
        
        for (String str : options)
        {
            btns.add(new SystemMenuButton(str, bounds.x, bounds.y+yPos, bounds.width, BTN_HEIGHT, true));
            yPos += BTN_HEIGHT;
        }
        
        btns.get(0).setFocused(true);
        focusedButton = btns.get(0);
    }
    
    @Override
    public void unloadMenu()
    {
        focusedButton = null;
        focusedButtonPos = 0;
        
        for (SystemMenuButton btn : btns)
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
        
        if (btns != null)
        {
            for (int x = 0; x < btns.size(); x++)
            {
                if (btns.get(x).getRect().contains(mousePos))
                {
                    SceneManager.getCurrentScene().buttonClicked();
                }
            }  
        }
    }
    
    @Override
    public void focusNextButton() 
    {
        if (focusedButtonPos+1 > btns.size()-1)
        {
            scrollToTop();
            focusButton(0);
        }
        else
        {
            if (btns.get(focusedButtonPos+1).getVisible() == false)
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
            focusButton(btns.size()-1);
        }
        else
        {
            if (btns.get(focusedButtonPos-1).getVisible() == false)
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
        
        for (int x = 0; x < btns.size(); x++)
        {
            if (btns.get(x).getRect().contains(mousePos))
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

        focusedButton = btns.get(button);
        focusedButtonPos = button;
        btns.get(button).setFocused(true);

        if (SceneManager.getCurrentScene() != null)
        {
            SceneManager.getCurrentScene().paintScene();
        }
    }
    
    private void scrollToTop()
    {
        if (currentScrollAmount < 0)
        {
            for (SystemMenuButton btn : btns)
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
            for (int x = btns.size()-1; x >= 0; x--)
            {
                if (btns.get(x).getVisible() == false)
                {
                    btnsInvis++;
                }
                else
                {
                    break;
                }
            }
            
            if (btnsInvis > 0)
            {
                int adjustAmount = btnsInvis*BTN_HEIGHT;

                for (SystemMenuButton btn : btns)
                {
                      btn.updateY(-adjustAmount);
                      toggleButtonVisible(btn);
                }
                currentScrollAmount = adjustAmount*-1;
            }
    }
    
    @Override
    public void scrollList (int direction)
    {
        if (isScrollable)
        {
            //Ensure even number
            int scrollAmount =(int) Math.floor((BTN_HEIGHT)/10)*10;

            if (direction < 0) //UP
            {
                if (!(btns.get(btns.size()-1).getY() < LIST_HEIGHT))
                {
                    for (SystemMenuButton btn : btns)
                    {
                        btn.updateY(-scrollAmount);
                        toggleButtonVisible(btn);
                    }
                    currentScrollAmount -= scrollAmount;
                }
            }
            else //DOWN
            {
                if (!(btns.get(0).getY() >= 0))
                {
                    for (SystemMenuButton btn : btns)
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
    }

    private void toggleButtonVisible(SystemMenuButton btn)
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
        g2d.setColor(SceneManager.getMenuColor());
        g2d.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()));
        g2d.fill(bounds);
        
        g2d.setColor(ColorUtils.brighten(SceneManager.getMenuColor(), 3));
        
        int stringWidth = g2d.getFontMetrics(TextUtils.STANDARD_FONT).stringWidth("System Menu");
        int stringHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight();
        
        g2d.drawString("System Menu", bounds.x+bounds.width/2-stringWidth/2, (bounds.y+BTN_HEIGHT/2)+stringHeight/4);
        
        //Paint separator line 
        Color menuColor = SceneManager.getMenuColor();
        final float[] gradientFractions = {0f, 0.2f, 0.8f, 1f};
        final Color[] backgroundGradient = {new Color(0, 0, 0, 0), ColorUtils.darken(menuColor, 3), 
                                                                   ColorUtils.darken(menuColor, 3), new Color(0, 0, 0, 0)};

        LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                    new Point2D.Double(bounds.x, bounds.y+BTN_HEIGHT),
                                                    new Point2D.Double(bounds.x+bounds.width, bounds.y+BTN_HEIGHT),
                                                    gradientFractions,
                                                    backgroundGradient);

        g2d.setStroke(new BasicStroke(1));
        g2d.setPaint(separatorGrad);
        g2d.drawLine(bounds.x, bounds.y+BTN_HEIGHT, bounds.x+bounds.width, bounds.y+BTN_HEIGHT);
        
        for (SystemMenuButton btn : btns)
        {
            btn.paintSceneComponent(g2d);
        }
    }
}
