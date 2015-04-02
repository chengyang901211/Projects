/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.utility_class;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ying
 */
public class TimeConversion {
    // Convert the RFC-822 Format to the Current Location Time format
    // ex RFC-822: Sat, 26 Apr 2014 02:35:12 PDT
    private final DateFormat RFCformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
    private final DateFormat dateformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");    
    private Date date;
    
    public TimeConversion(){
                      
    }

    public long calculateDifference(Date rfc, Date d){
        return d.getTime() - rfc.getTime();
    }
    
    public String getTimePassed(Date rfc, Date d){
                /*calculate two date difference in milliseconds*/        
        //System.out.println(differenceInMillis);
        long differenceInMillis = calculateDifference(rfc, d);
        

        
        int seconds = (int) (differenceInMillis / 1000) % 60 ;
        int minutes = (int) ((differenceInMillis / (1000*60)) % 60);
        int hours   = (int) ((differenceInMillis / (1000*60*60)) % 24);
        int days    = (int) ((differenceInMillis / (1000*60*60*24)) % 7);
        int weeks   = (int) (differenceInMillis  / (1000*60*60*24*7));
 
 
        //System.out.println(hours+":"+minutes+":"+seconds);        
       
        if(weeks > 0){
            return (new Integer(weeks)).toString() + " weeks";                          
        }        
        if(days > 0){
            return (new Integer(days)).toString() + " days";                  
        }        
        else if(hours == 0 && minutes == 0){
            return (new Integer(seconds)).toString() + " seconds";       
        }
        else if(hours == 0){
            return (new Integer(minutes)).toString() + " minutes";       
        }
        else{
            return (new Integer(hours)).toString() + " hours";               
        }       
    }
    
    public Date parseRFC(String rfc){
        /*parse RSS PubDate*/
    
        try {
            date = RFCformat.parse(rfc);
            //System.out.println(CLTformat.format(date));
        } catch (ParseException ex) {
            Logger.getLogger(TimeConversion.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        return date;    
    }
    
    public Date getCurrentTime(){    
        /*get Current Time*/        
        date = new Date();
        return date;                
    }
}
