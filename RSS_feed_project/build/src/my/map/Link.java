/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.map;

/**
 *
 * @author Caleb
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import javax.swing.JCheckBoxMenuItem;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import my.utility_class.ButtonTabComponent;
import my.utility_class.RenderWebPage;

public class Link extends javax.swing.JLabel implements java.awt.event.MouseListener
{
    private String url;
    private int status;
     private JTabbedPane tabPane;
     private String title;
      private JCheckBoxMenuItem browserCB; 
      
    public Link ()
    {
        super ();
        init ("");
    }
    
    public Link (javax.swing.Icon image)
    {
        super (image);
        init ("");
    }
    
    public Link (javax.swing.Icon image, int horizontalAlignment)
    {
        super (image, horizontalAlignment);
        init ("");
    }
    
    public Link (String text)
    {
        super (text);
        init (text);
    }
    
    public Link (String text, javax.swing.Icon icon, int horizontalAlignment)
    {
        super (text, icon, horizontalAlignment);
        init (text);
    }
    
    public void setStatus(int status){
    	
    	this.status=status;
    	
    	}
    public void setURL (String url,int status)
    {
        this.url = url;
        this.status=status;
     
        /*if(status==0)
        {
        this.setToolTipText ("Open " + url + " in your browser");
        }
        else{
         this.setToolTipText ("Open " + url + " in RSS reader");
        	
        }*/
    }
    
    private void init (String url)
    {
        setURL (url,status);
        this.addMouseListener (this);
        this.setForeground (Color.BLUE);
    }
    
    public Link (String text, int horizontalAlignment)
    {
        super (text, horizontalAlignment);
        init (text);
    }
    
    @Override
    public void mouseClicked (MouseEvent arg0)
    {
  
        
    
                 if( browserCB.isSelected()){
                          
                         browse();
                      
                     }  
                     else{                               
                          
                         browse_inside();
                          
                      } 
      
    }
    @Override
    public void mouseEntered (MouseEvent arg0)
    {
        setCursor (new Cursor (Cursor.HAND_CURSOR));
           if(browserCB.isSelected()){
               
                //  if(status==0)
        
        this.setToolTipText ("Open " + url + " in your browser");
        }
        else{
         this.setToolTipText ("Open " + url + " in RSS reader");
        	
        
           }
    }
    @Override
    public void mouseExited (MouseEvent arg0)
    {
        setCursor (new Cursor (Cursor.DEFAULT_CURSOR));
    }
    @Override
    public void mousePressed (MouseEvent arg0) {}
    @Override
    public void mouseReleased (MouseEvent arg0) {}
    
    private void browse ()
    {
        if (java.awt.Desktop.isDesktopSupported ())
        {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop ();
            if (desktop.isSupported (java.awt.Desktop.Action.BROWSE))
            {
                try
                {
                    desktop.browse (new java.net.URI (url));
                    return;
                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace ();
                }
                catch (java.net.URISyntaxException e)
                {
                    e.printStackTrace ();
                }
            }
        }
        
        
     
    }
    private  void browse_inside (){
    	
    	
                         RenderWebPage rwp = new RenderWebPage(url);
                         
                  
                          tabPane.addTab(title, rwp);
                          tabPane.setTabComponentAt(tabPane.getTabCount()-1,new ButtonTabComponent(tabPane)); // add the close button
                          tabPane.setSelectedIndex(tabPane.getTabCount()-1);	
        
   
    	
        }

    public void set_title(String title)
    {
        
        this.title=title;
        
    }
      public void setTabPane(JTabbedPane tp){
        tabPane = tp; 
    }         
    public void setBrowserCB( JCheckBoxMenuItem browserCB )
    {
        
        this.browserCB=browserCB;
        
    }
}