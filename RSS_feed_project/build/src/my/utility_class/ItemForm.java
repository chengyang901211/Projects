package my.utility_class;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


public class ItemForm extends JPanel {
    
   private Color color_black = new Color(40,40,41);
   private Color color_white = Color.WHITE;
   private Color backgroundColor = color_black;
   
   public ItemForm(String description){
       try {
           this.setBackground(backgroundColor); // set black color
           this.setLayout(new BorderLayout());
           this.setPreferredSize(new Dimension(550,100));
           //set the ItemForm top and bottom line color 
           this.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, new Color(101,34,120)),BorderFactory.createEmptyBorder(0,35,0,30)));
               
           int i = description.indexOf("&lt");
           if(i>=0){
                description = description.substring(0, i);
            }
           
           JTextPane descriptionTP = new JTextPane();
 
           descriptionTP.setContentType( "text/html" );
           descriptionTP.setEditable(false);
           descriptionTP.setBackground(backgroundColor);
                  
           HTMLDocument doc = (HTMLDocument)descriptionTP.getDocument();
           HTMLEditorKit editorKit = (HTMLEditorKit)descriptionTP.getEditorKit();
           description = "<font size=\"3\" face=\"verdana\" color=\"white\"> " + description + "</font>"; // make the description color to white
           editorKit.insertHTML(doc, doc.getLength(), description, 0, 0, null);

           JScrollPane scrollPane = new JScrollPane(descriptionTP);
           
           descriptionTP.setCaretPosition(0);    // set the vertical scrollbar to the top       

           
           scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
           scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
           scrollPane.setBorder(BorderFactory.createEmptyBorder());
           this.add(scrollPane);           
       } catch (BadLocationException ex) {
           Logger.getLogger(ItemForm.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
           Logger.getLogger(ItemForm.class.getName()).log(Level.SEVERE, null, ex);
       }


    } 
}
