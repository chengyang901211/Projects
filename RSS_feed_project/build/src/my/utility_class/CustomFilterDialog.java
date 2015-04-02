package my.utility_class;

import com.michaelbaranov.microba.calendar.DatePicker;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import my.main_interface.MainInterface;

public class CustomFilterDialog extends JPanel{

    private CardLayout c1 = new CardLayout(); 
    private JPanel myPane = this;
    private DatePicker datepicker;
    private JButton time_btn;
    private JOptionPane pane;
    private static String date = null;
    private static String time = null;
    private JLabel hLab;
    private JLabel mLab;
    
    public CustomFilterDialog(){

    }
        
    public void init(){
        this.setLayout(c1);
                
        JPanel outPane = new JPanel(null);
        outPane.setPreferredSize(new Dimension(200,90)); //size control in JOptionPane
        outPane.setBackground(Color.WHITE);
     
        JRadioButton dateRBtn = new JRadioButton("Filter with Date");
        dateRBtn.setActionCommand("Date");
        dateRBtn.setSize(150,20);
        dateRBtn.setLocation(60,20);
        dateRBtn.addActionListener(radioBtnListener);
        dateRBtn.setBackground(Color.WHITE);
            
        JRadioButton timeRBtn = new JRadioButton("Filter with Time");
        timeRBtn.setActionCommand("Time");
        timeRBtn.setSize(150,20);
        timeRBtn.setLocation(60,45);
        timeRBtn.addActionListener(radioBtnListener);
        timeRBtn.setBackground(Color.WHITE);
            
        ButtonGroup bG = new ButtonGroup();
        bG.add(dateRBtn);
        bG.add(timeRBtn);
             
        outPane.add(dateRBtn);
        outPane.add(timeRBtn);
          
        this.add(outPane, "1");
  
        JPanel datePane = new JPanel();
        datePane.setLayout(null);
        datePane.setBackground(Color.WHITE);
        DateFormat dateformat = new SimpleDateFormat("yyy/MM/dd");   
        datepicker = new DatePicker(new Date(), dateformat, Locale.ENGLISH);
        datepicker.setSize(100, 25);
        datepicker.setLocation(80,10);
        //datepicker.showButtonOnly(true); 
        datePane.add(datepicker);
        
        JButton btn = new JButton("OK");
        btn.setBackground(Color.WHITE);
        btn.setActionCommand("Date OK");
        btn.addActionListener(btnListener);
        btn.setSize(55,20);
        btn.setFocusable(false);
        btn.setLocation(185,70);
        datePane.add(btn);
        
            
        JPanel timePane = new JPanel();
        timePane.setLayout(new BorderLayout());             
        CustomFilterDialog.MyTimePanel myTimePane = new CustomFilterDialog.MyTimePanel();
        myTimePane.setBackground(Color.WHITE);
        timePane.add(myTimePane);

        this.add(datePane, "2");
        this.add(timePane,"3");         
        
 
        c1.show(this, "1");
              
    }

    public void startDialog(){
        Object[] options1 = {};
                
        pane = new JOptionPane();        
        pane.showOptionDialog(null, this , "Filter Date/Time",JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);

    }
    
    public static String getDate(){
       return date;
    }

    public static String getTime(){
       return time;
    }
        
    private ActionListener btnListener = new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("HELLO");
            if(pane != null){
               switch(e.getActionCommand()){
                 
                   case "Date OK":
                       Date d = datepicker.getDate();
                       if(d != null){
                         String mydate = d.toString();
                         date = mydate.substring(8,10);
                         date += " " + mydate.substring(4,7);
                       //System.out.println(date);
                       }
                       else
                         date = null;
                       MainInterface.getRefreshBtn().doClick();                       
                       break;
                   case "Time OK":
                       String hour = hLab.getText().toString();
                       String minute = mLab.getText().toString();
                       int hInt = Integer.parseInt(hour);
                       int mInt = Integer.parseInt(minute);
                       if(hInt != 0){
                          if(hInt == 23){
                             if(mInt == 59)
                                 time = "1 days";
                             else
                                 time = "23 hours";
                          }
                          else{
                             time = Integer.toString(hInt)+ " hours";
                          }
                       }
                       else if (mInt > 1 ){
                          time = Integer.toString(mInt) + " minutes";
                       }
                       else{
                          time = null;
                       }
                       //System.out.println(time);
                       MainInterface.getRefreshBtn().doClick();
                       
                       break;                 
               }
               JOptionPane.getRootFrame().dispose();
            }        
        }
    };
    
    private ActionListener  radioBtnListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Date":
                    c1.show(myPane, "2");
                    break;
                case "Time":
                    c1.show(myPane, "3");
                    break;            
            }        
        }
    };
    
    
    private class MyTimePanel extends JPanel {
        public MyTimePanel(){
           this.setLayout(null);
           this.setBackground(Color.WHITE);
           
           JLabel timeLab = new JLabel("Time ");
           timeLab.setSize(new Dimension(50,20));
           timeLab.setLocation(10, 10);
           this.add(timeLab);

           hLab = new JLabel("00");
           hLab.setSize(new Dimension(50,20));
           hLab.setLocation(120, 10);
           this.add(hLab);
           
           JLabel dotLab = new JLabel(":");
           dotLab.setSize(new Dimension(10,20));
           dotLab.setLocation(140, 9);
           this.add(dotLab);

           
           mLab = new JLabel("00");
           mLab.setSize(new Dimension(50,20));
           mLab.setLocation(150, 10);
           this.add(mLab);
        
           //Display Hour Slide        
           JLabel hourLab = new JLabel("Hour");
           hourLab.setSize(new Dimension(50,20));
           hourLab.setLocation(10, 30);
           this.add(hourLab);
           
        
           final JSlider hourSlider = new JSlider(); 
           hourSlider.setMinimum(0);
           hourSlider.setMaximum(23);
           hourSlider.setValue(0);
           hourSlider.setSize(150,20);
           hourSlider.setLocation(65,30);
           hourSlider.setBackground(Color.WHITE);
        
           hourSlider.addChangeListener( new ChangeListener(){

           public void stateChanged(ChangeEvent ce){
              JSlider source = (JSlider) ce.getSource();
              int value = hourSlider.getValue();
              
              hLab.setText(String.format("%02d", value));            }
           });  
           
           this.add(hourSlider);
      
       
           //Display Minute Slider
           JLabel minLab = new JLabel("Minute");
           minLab.setSize(new Dimension(50,20));
           minLab.setLocation(10, 50);
           this.add(minLab);
        
           final JSlider minSlider = new JSlider(); 
           minSlider.setMinimum(0);
           minSlider.setMaximum(59);
           minSlider.setValue(1);
           minSlider.setSize(150,20);
           minSlider.setLocation(65,50);
           minSlider.setBackground(Color.WHITE);
           
           minSlider.addChangeListener( new ChangeListener(){

           public void stateChanged(ChangeEvent ce){
 
              JSlider source = (JSlider) ce.getSource();
              int value = minSlider.getValue();
              mLab.setText(String.format("%02d", value));              
             }
           });     
           this.add(minSlider);
           
           time_btn = new JButton("OK");
           time_btn.setBackground(Color.WHITE);
           time_btn.setActionCommand("Time OK");
           time_btn.addActionListener(btnListener);
           time_btn.setSize(55,20);
           time_btn.setFocusable(false);
           time_btn.setLocation(185,70);
           this.add(time_btn);
           
        }    
    }

}
