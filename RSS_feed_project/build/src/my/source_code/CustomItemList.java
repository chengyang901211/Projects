package my.source_code;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import my.utility_class.CustomFilterDialog;
import my.utility_class.ItemForm;
import my.utility_class.TitlePanel;


public class CustomItemList extends JPanel{
    
    private String all_unread;
    private TitlePanel[] titlePane = new TitlePanel[200];
    private JCheckBoxMenuItem browserCB; 
    private JTabbedPane tabPane;
    private ArrayList titleList = new ArrayList();
    private ArrayList descriptionList, linkList, dateList, timeList;
    private String allOrUnread;
    private static int NumFeed = 100;
            
    public CustomItemList(){
      
       this.setLayout(new BorderLayout());
       this.setBackground(new Color(40,40,41));
       initTitlePane();
    }
    
    public void setNumFeed(int num){
       NumFeed = num;
    }
    
    public void initTitlePane(){
       for(int i =0; i < 200; i++)
           titlePane[i] = new TitlePanel(); 
    }
    
    public void cleanCustomItemList(){
       this.removeAll();
       initTitlePane();
    }

    public void resetCustomItemList(){
      if(titleList.isEmpty()){
         return;
      }

  
      this.removeAll();
    } 
      
    public void setReadUnread(){
        if(all_unread.equals("all")){        
            setAll();
        }
        else{
           setUnreadOnly();
        }    
    }
    
    
    public void setBrowserCB(JCheckBoxMenuItem bcb){
        browserCB = bcb; 
    }         
    public void setTabPane(JTabbedPane tp){
        tabPane = tp; 
    }      
    
    private void setAll(){   
        
      int num = (titleList.size() < NumFeed)? titleList.size(): NumFeed;
        
      JPanel alignmentPane = new JPanel(new GridBagLayout());
        
      for(int i=0; i< num ;i++){
          titlePane[i].setVisible(true);
          if(titlePane[i].getUnreadOrRead().equals("Read")){
              titlePane[i].resetReadCollapseIconLabel();
          }
          /*Align Vertically to the Top*/
          GridBagConstraints gbc = new GridBagConstraints();
          gbc.anchor = GridBagConstraints.NORTH;
          gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.weightx = 1.0;  //fit the horizontal content
          gbc.weighty = 0.0;
          gbc.gridwidth = GridBagConstraints.REMAINDER; 
      
    
          titlePane[i].setIsExpand(false);
          alignmentPane.add(titlePane[i],gbc);
          ItemForm iform  = (ItemForm) titlePane[i].getItemForm();
          iform.setVisible(false);          
          alignmentPane.add(iform,gbc);
      } 
     // for(String str: description ){ }
      this.add(alignmentPane, BorderLayout.NORTH);               
    }

    private void setUnreadOnly(){   
        
      int num = (titleList.size() < NumFeed)? titleList.size(): NumFeed;  
      JPanel alignmentPane = new JPanel(new GridBagLayout());
            
      for(int i=0; i< num;i++){
    
          if(titlePane[i].getUnreadOrRead().equals("Read")){
              titlePane[i].setVisible(false);
              titlePane[i].resetReadCollapseIconLabel();
          }
          /*Align Vertically to the Top*/
          GridBagConstraints gbc = new GridBagConstraints();
          gbc.anchor = GridBagConstraints.NORTH;
          gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.weightx = 1.0;  //fit the horizontal content
          gbc.weighty = 0.0;
          gbc.gridwidth = GridBagConstraints.REMAINDER; 
      
    
          titlePane[i].setIsExpand(false);
          
          alignmentPane.add(titlePane[i],gbc);
          ItemForm iform  = (ItemForm) titlePane[i].getItemForm();
          iform.setVisible(false);
          alignmentPane.add(iform,gbc);          
      } 
     // for(String str: description ){ }
      this.add(alignmentPane, BorderLayout.NORTH);               
    }
    
    
    public void setAllUnread(String allOrUnread){
       all_unread = allOrUnread;
    }
    

    
    public void setTitleList(ArrayList list){
       titleList = list;
    }

    public void setDescriptionList(ArrayList list){
       descriptionList = list;
    }
    
    public void setLinkList(ArrayList list){
       linkList = list;   
    }

    public void setDateList(ArrayList list){
       dateList = list;
    }

    public void setTimeList(ArrayList list){
       timeList = list;
    }

    public void setAllOrUnread(String q){
        allOrUnread = q;    
    }
    
    public void setCustomItemList() {
     
      int num = (titleList.size() < NumFeed)? titleList.size(): NumFeed;
      
      all_unread = allOrUnread;
      
      String date = CustomFilterDialog.getDate();
      String time = CustomFilterDialog.getTime();
      
      JPanel alignmentPane = new JPanel(new GridBagLayout());
      for(int i=0; i< num ;i++){
          if(date != null){
             if(!dateList.get(i).toString().contains(" " + date + " ")){
                continue;
             }              
          }
          if(time != null){
              int time1 = checkTime(time);
              int time2 = checkTime(timeList.get(i).toString());
              if(time1 < time2){              
                 continue;
              }                           
          }
                   
          ItemForm iform = new ItemForm(descriptionList.get(i).toString());      
          TitlePanel titlePane1 = new TitlePanel(titleList.get(i).toString(),
                                                 linkList.get(i).toString(), 
                                                 dateList.get(i).toString(),
                                                 timeList.get(i).toString(),
                                                 iform);
          
          titlePane1.setBrowserCB(browserCB);
          titlePane1.setTabPane(tabPane);
          titlePane[i] = titlePane1;
    
          /*Align Vertically to the Top*/
          GridBagConstraints gbc = new GridBagConstraints();
          gbc.anchor = GridBagConstraints.NORTH;
          gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.weightx = 1.0;  //fit the horizontal content
          gbc.weighty = 0.0;
          gbc.gridwidth = GridBagConstraints.REMAINDER; 
      
    
          alignmentPane.add(titlePane1,gbc);
          alignmentPane.add(iform,gbc);
      } 
     // for(String str: description ){ }
      this.add(alignmentPane, BorderLayout.NORTH);      
    }   
    
    public int checkTime(String time){
      int timeInSecond = 0;
      if(time != null){
         if(time.contains("days")){        
            String []temp = time.split(" days");
            timeInSecond = (Integer.parseInt(temp[0])*24*60);      
         }
         else if(time.contains("hours")){
            String []temp = time.split(" hours");
            timeInSecond = Integer.parseInt(temp[0]) * 60;
         }
         else if(time.contains("minutes")){ // minute
            String []temp = time.split(" minutes");
            System.out.println(temp[0]);
            timeInSecond = Integer.parseInt(temp[0]);      
         }
         else if(time.contains("seconds")){
            timeInSecond = 0;
         }
         else{ // weeks ... 
            timeInSecond = 50000;
         }
       }  
       return timeInSecond;
    }
    
}
