/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.utility_class;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Ying
 */
public class CustomChannelDialog extends JPanel {
    
//    private JPanel dialogPanel = new JPanel();
    private final JLabel titleLab = new JLabel("Add a new feed");        
    private final JLabel addressLab = new JLabel("Address: ");
    private final JLabel categoryLab = new JLabel("Category: ");        
    private final JTextField tfAddress = new JTextField("Enter a website");
    private ArrayList<String> feedCategory = new ArrayList<>(); 
    private JComboBox boxCategory;
    
    public CustomChannelDialog(ArrayList<String> category){
        feedCategory = category;
        tfAddress.addFocusListener(tfAddressListener);
        initPanel();
    }
    
    public JPanel getDialogPanel(){
       return this;
    }
   
    public String getTFAddress(){
      return tfAddress.getText().toString();
    }
    
    public void disableBoxCategory(){
        boxCategory.setEnabled(false);
    }

    public void setBoxCategoryItem(Object name){
        boxCategory.setSelectedItem(name);
    }    

    public Object getSelectedBoxCategoryItem(){
        return boxCategory.getSelectedItem();
    }        
    
    private void initPanel(){
        Object[] tmp = feedCategory.toArray();
        boxCategory = new JComboBox(tmp);
        boxCategory.setEditable(true);
        
        this.setLayout(null);
        
        titleLab.setFont(new Font("Serif", Font.BOLD,16));
        titleLab.setSize(130,14);
        titleLab.setLocation(200-55,20);
        this.add(titleLab);

        addressLab.setFont(new Font("Serif", Font.BOLD,14));
        addressLab.setSize(80,20);
        addressLab.setLocation(50,73);
        this.add(addressLab);

        categoryLab.setFont(new Font("Serif", Font.BOLD,14));
        categoryLab.setSize(80,20);
        categoryLab.setLocation(45,130);
        this.add(categoryLab);        
        
        tfAddress.setSize(220,30);
        tfAddress.setLocation(110,70);
        this.add(tfAddress);

        boxCategory.setSize(220,30);
        boxCategory.setLocation(109, 125);
        this.add(boxCategory);

        this.setPreferredSize(new Dimension(400,180));     
    }
    
    private FocusListener tfAddressListener = new FocusListener(){
        @Override
        public void focusGained(FocusEvent e){
           if(tfAddress.getText().equals("Enter a website")) 
               tfAddress.setText("");
        } 

        @Override
        public void focusLost(FocusEvent fe) {
           if(tfAddress.getText().equals(""))
               tfAddress.setText("Enter a website");            
        }
    };
    
}
