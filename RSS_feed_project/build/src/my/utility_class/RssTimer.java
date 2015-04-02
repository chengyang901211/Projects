/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.utility_class;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.Timer;

public class RssTimer {
    
    // set the default refresh time to 1 minute refresh time
    private static int refresh_time = 300000; // unit in milliseconds
    private Timer timer = null;
    private JButton refreshBtn;

    public RssTimer(){
    
    }
    
    public RssTimer(JButton btn){    
         refreshBtn = btn;
    }
    
    public void setRefreshTime(int time){
       time = time * 60 * 1000; // convert to millisecond
       refresh_time = time;
       stopTimer();
       setTimer();
       timer.restart();
    }

    public static void setRefreshTime(int time, int load){

           refresh_time = time;
    }    
    
    public void setTimer(){
        timer = new Timer(refresh_time,new ActionListener(){      // Timer 4 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBtn.doClick(); // click the Refresh Button
            }
        });
        startTimer();
    }
    
    public void startTimer(){
      timer.start();
    }
    
    public void stopTimer(){
      timer.stop();
    }
    
    
    public static int getRefreshTime(){
       return refresh_time; 
    }

    
    public Timer getTimer(){
       return timer;
    }
   
}
