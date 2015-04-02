package my.utility_class;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;


public class TitlePanel extends JPanel {
    
    private final ImageIcon collapseBlueIcon = new ImageIcon(TitlePanel.class.getResource("/collapse_blue.png"));
    private final ImageIcon collapseGrayIcon = new ImageIcon(TitlePanel.class.getResource("/collapse_gray.png"));    
    private final ImageIcon expandGrayIcon = new ImageIcon(TitlePanel.class.getResource("/expand_gray.png"));
    private boolean isExpand = true;
    private JLabel iconLabel = new JLabel();
    private JLabel titleLabel;
    private JButton linkBtn;
    private ItemForm iform;
    private String my_date;
    private JCheckBox read_unread_cbox;
    private JCheckBoxMenuItem browserCB; 
    private JTabbedPane tabPane;
    private final Color color_black = new Color(40,40,41);    
    private final Color color_blue = new Color(44,134,245);
    private final Color color_gray = new Color(62,64,69);
    private final Color color_lightGray = new Color(112,112,112);
    private final Color color_lightBlue = new Color(153,204,222);
    private final Color color_white = Color.WHITE;
    private Color backgroundColor = color_black;
    private Color cbColor = color_white; // check box (read or unread) words color
    private Color dateColor = color_lightBlue; // check box (read or unread) words color
    
    public TitlePanel(){
    
    
    }
    
    public TitlePanel(final String title, final String link, String date, String time, ItemForm form){
       iform = form;
       my_date = date;
       this.setLayout(new BorderLayout());
       this.setPreferredSize(new Dimension(550,30));//WIDTH, HEIGHT
       //set background color
       this.setBackground(backgroundColor); 
       this.addMouseListener(titleListener);
       //set the border color
       this.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color_gray),BorderFactory.createEmptyBorder(0,0,0,0)));
  
       JPanel leftPane = new JPanel();
       leftPane.setBackground(backgroundColor);
       
       iconLabel.setIcon(collapseBlueIcon);
       iform.setVisible(false);
       iconLabel.setSize(24,24);
       iconLabel.setLocation(5,2); 
       iconLabel.addMouseListener(titleListener);
       leftPane.add(iconLabel);
       isExpand = false;
       
       
       titleLabel = new JLabel("<html><body><nobr>"+title+"</nobr></body></html>");
       //set the text color
       titleLabel.setForeground(color_blue); // color blue
       titleLabel.setFont(new Font("Aharoni Bold", Font.PLAIN, 18));
       //titleLabel.setBorder(BorderFactory.createEmptyBorder(0,40,0,0)); //top,left,down,right
       titleLabel.addMouseListener(titleListener);
       titleLabel.setToolTipText("<html><body><nobr>"+title+"</nobr></body></html>");
       leftPane.add(titleLabel);

       JLabel timeLabel = new JLabel(time+ " ago");
       //set the text color
       timeLabel.setForeground(dateColor);
       timeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
       leftPane.add(timeLabel);      
       
       
       
       JPanel rightPane = new JPanel();
       rightPane.setBackground(backgroundColor);
       JLabel dateLabel = new JLabel(date);
       //set the text color
       dateLabel.setForeground(dateColor);
       dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));
       dateLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
       rightPane.add(dateLabel);      
       
       
       linkBtn = new JButton(new ImageIcon(TitlePanel.class.getResource("/web_rss_orange.png")));
       linkBtn.setBackground(backgroundColor);
       linkBtn.setFocusable(false);
       linkBtn.setToolTipText(link);
       linkBtn.setBorder(BorderFactory.createEmptyBorder());
       linkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  //JOptionPane.showMessageDialog(null, link, "Link", JOptionPane.PLAIN_MESSAGE);
                  linkBtn.setIcon(new ImageIcon(TitlePanel.class.getResource("/web_rss_purple.png")));
                  read_unread_cbox.setSelected(true);
                  
                  try {

                      if(browserCB.isSelected()){         
                        if(Desktop.isDesktopSupported()){  
                            //get the corresponding url
                            URL url = new URL(link);
                            Desktop desktop = Desktop.getDesktop();  //get the user desktop information
                            desktop.isSupported(Desktop.Action.BROWSE); 
                            desktop.browse(url.toURI()); // launch the url using the browser
                        }
                        else{
                           JOptionPane.showMessageDialog(null, "Desktop Permission Denied to get the Browser", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                      }  
                      else{                               
                          RenderWebPage rwp = new RenderWebPage(link);
                          
                          tabPane.addTab(title.toString(), rwp);
                          tabPane.setTabComponentAt(tabPane.getTabCount()-1,new ButtonTabComponent(tabPane)); // add the close button
                          tabPane.setSelectedIndex(tabPane.getTabCount()-1);
                      }
                  } catch (URISyntaxException ex) {
                      System.out.println("Error: "+ex.getMessage());
                  } catch (IOException ex) {
                      System.out.println("Error: "+ex.getMessage());                        
                  } 
                  
            }
        });
             
       rightPane.add(linkBtn);  
       
       read_unread_cbox = new JCheckBox("Unread");
       read_unread_cbox.setSelected(false);
       read_unread_cbox.setBackground(backgroundColor);
       read_unread_cbox.setForeground(cbColor);
       read_unread_cbox.setFocusable(false);
       read_unread_cbox.addItemListener(checkBoxListener);
       //read_unread_cbox.addMouseListener(titleListener);
       rightPane.add(read_unread_cbox);
       
       add(rightPane,BorderLayout.EAST);
       add(leftPane, BorderLayout.WEST);
    }
    
    public String getDate(){
         return my_date;
    }
    
    public String getUnreadOrRead(){
        return read_unread_cbox.getText().toString();
    }
    
    public JPanel getItemForm(){
        return iform; 
    }
    
    public void setBrowserCB(JCheckBoxMenuItem bcb){
        browserCB = bcb; 
    } 

    public void setTabPane(JTabbedPane tp){
        tabPane = tp; 
    }          
    
    public void setIsExpand(boolean isexpand){
       isExpand = isexpand;
    }
    
    public void resetReadCollapseIconLabel(){
       iconLabel.setIcon(collapseGrayIcon); 
    }
    
    private ItemListener checkBoxListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent ie) {
            JCheckBox cb = (JCheckBox) ie.getItem();
            if(cb.getText().toString().equals("Unread")){                
              cb.setText("Read");
              cb.setSelected(true);  
              iconLabel.setIcon(collapseGrayIcon);                 
                  
              titleLabel.setForeground(color_lightGray);             
            }
            else{
              linkBtn.setIcon(new ImageIcon(TitlePanel.class.getResource("/web_rss_orange.png")));
              cb.setText("Unread");              
              cb.setSelected(false);                              
              iconLabel.setIcon(collapseBlueIcon);                 
              titleLabel.setForeground(color_blue);            
            }
        }
        
    };
    
    private MouseAdapter titleListener= new MouseAdapter(){        
          @Override
          public void mouseReleased(MouseEvent e) {  
             
             if(e.getClickCount() ==1){
                
                 if(isExpand == false && read_unread_cbox.getText().equals("Unread")){
                    read_unread_cbox.setSelected(true);                     

                 }                 
                 
                 if(isExpand == true){
                   if(read_unread_cbox.getText().equals("Unread"))
                      iconLabel.setIcon(collapseBlueIcon);   
                   else
                      iconLabel.setIcon(collapseGrayIcon);   
                   iform.setVisible(false);                            
                 }
                 else if(isExpand == false){ //collapse
                   iconLabel.setIcon(expandGrayIcon);                     
                   iform.setVisible(true);
                   titleLabel.setForeground(color_lightGray);                     
                 }
                              
                 isExpand = !isExpand;
             }
          }
    };    
}
