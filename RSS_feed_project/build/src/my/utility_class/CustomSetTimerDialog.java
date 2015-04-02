/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.utility_class;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Ying
 */
public class CustomSetTimerDialog extends JPanel {
    
    private JSpinner time_spinner;
    private JLabel reset_time_label = new JLabel("Set Time:");
    private JLabel minute_label = new JLabel("minutes");
    private Color backgroundColor = new Color(40,40,41);
    private Color purpleColor = new Color(101,34,120);  
    private JOptionPane pane;
    private static int NUM = 5;    
    private static SpinnerNumberModel sModel = new SpinnerNumberModel(5, 5, 180, 1); // start, min, max, increment
    private static JButton rBtn;
    
    public CustomSetTimerDialog(){
        init();
    }
        
    public void init(){
    
      this.setPreferredSize(new Dimension(255,65));
       this.setBackground(backgroundColor);
       this.setLayout(null);
       
       JPanel top = new JPanel();
       top.setSize(255,30);
       top.setLayout(null);

       top.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(101,34,120)));
       top.setBackground(Color.WHITE);
       
       reset_time_label.setSize(new Dimension(150,25));
       reset_time_label.setLocation(10, 3);
       reset_time_label.setFont(new Font("Serif", Font.BOLD,14));
       reset_time_label.setForeground(purpleColor);
       top.add(reset_time_label);
       
       
       
       sModel.setValue(NUM);
       time_spinner = new JSpinner(sModel);
       
       JSpinner.DefaultEditor editor = ( JSpinner.DefaultEditor ) time_spinner.getEditor();
       //editor.getTextField().setEditable(false);
       editor.getTextField().setBackground(new Color(250,250,250));
       time_spinner.setSize(new Dimension(80,25));
       time_spinner.setLocation(85,3);
       top.add(time_spinner);

       
       minute_label.setSize(new Dimension(100,25));
       minute_label.setLocation(185, 3);
       minute_label.setFont(new Font("Serif", Font.BOLD,14));
       minute_label.setForeground(purpleColor);

       top.add(minute_label);  
    
       this.add(top);
       
       JPanel down = new JPanel();
       down.setBackground(backgroundColor);
       down.setSize(260,50); 
       down.setLocation(0,35);
       down.setLayout(null);
       
       JButton setTimeBtn = new JButton("Set Time");
       setTimeBtn.setSize(85,30);
       setTimeBtn.setLocation(93,0);
       setTimeBtn.setBackground(backgroundColor);
       setTimeBtn.setForeground(Color.WHITE);
       setTimeBtn.setActionCommand("Set Time");
       setTimeBtn.addActionListener(dialogBtnListener);
       down.add(setTimeBtn);
 
       JButton cancelBtn = new JButton("Cancel");
       cancelBtn.setSize(75,30);
       cancelBtn.setLocation(180,0);
       cancelBtn.setBackground(backgroundColor);
       cancelBtn.setForeground(Color.WHITE);
       cancelBtn.setActionCommand("Cancel");
       cancelBtn.addActionListener(dialogBtnListener);
       down.add(cancelBtn);       

       
       JLabel timeLab = new JLabel("Limit (5 ~ 180)");
       timeLab.setForeground(Color.LIGHT_GRAY);
       timeLab.setFont(new Font("Courier", Font.BOLD,9));
       timeLab.setSize(75,30);
       timeLab.setLocation(5,-10);
       down.add(timeLab);   
       
       this.add(down);     
    }
    
    public void setRefreshBtn( JButton btn){
        rBtn = btn;
    }
    
    public static int getNUM(){
       return NUM;
    }

    public static void setNum(int num){
       NUM = num;
    }    
    
    public void startDialog(){
        try {
            UIManager UI=new UIManager();
            
             UI.put("OptionPane.background", backgroundColor);       
             UI.put("Panel.background",backgroundColor);  

             UI.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
             UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

             Object[] options1 = { };
                
             pane = new JOptionPane();        
             pane.showOptionDialog(null, this , "Set Refresh Time",JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);

             UI.put("OptionPane.background", Color.white);       
             UI.put("Panel.background",Color.WHITE);  
             
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomSetTimerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CustomSetTimerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CustomSetTimerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CustomSetTimerDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private ActionListener dialogBtnListener = new ActionListener(){
       @Override
       public void actionPerformed(ActionEvent e) {
            //JOptionPane.showMessageDialog(null, e.getActionCommand());

          switch(e.getActionCommand()){
               case "Set Time":
                   //System.out.println(time_spinner.getValue().toString());
                   Integer i =new Integer(time_spinner.getValue().toString());
                   NUM = i.intValue();
                   System.out.println(time_spinner.getValue());
                   rBtn.doClick();
                   pane.getRootFrame().dispose(); 
                               
                   break;
               case "Cancel":
                   pane.getRootFrame().dispose(); 
                   break;                   
               default:
                  break;            
          }   
       }    
    };     
}
