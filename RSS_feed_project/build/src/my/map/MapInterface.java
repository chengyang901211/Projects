package my.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.ParserConfigurationException;
import my.main_interface.MainInterface;
import my.utility_class.ButtonTabComponent;
import my.utility_class.CustomFilterDialog;
import my.utility_class.RenderWebPage;

import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;


public class MapInterface extends JPanel{
    private Map my_map;
	
	//components
	private  JLayeredPane layer_panel;
	private RoundedPanel pop_panel;
	private JPanel my_panel;
	private GeoCoder address_finder;
                  private JCheckBoxMenuItem browserCB; 
                  private JTabbedPane tabPane;
                  private ArrayList sourceTitle, sourceLink,sourceDescription, sourcePubDate , sourceTime;    
                  private ArrayList<Integer> lookup;
                  //private int width;
                  //private int height;
        private static int NumFeed = 100;       
        public MapInterface(){
        
                  
                     
                   this.setLayout(new BorderLayout());
                  
                this.setPreferredSize(new Dimension(1141,650)); //screen launch size: new Dimension(570,470)
                   //this.setBounds(10, 10, 570, 470);
                   layer_panel = new JLayeredPane();
                   pop_panel = new RoundedPanel();
	 my_panel = new JPanel(new BorderLayout());
	 address_finder = new GeoCoder();
	 
           
		 
	     my_map = new Map();
                      lookup = new ArrayList<Integer>();

        //     my_panel.setLayout(new BorderLayout());
	     my_panel.setBounds(0, 0, this.getPreferredSize().width, this.getPreferredSize().height);
	     my_panel.add(my_map.get_mapview());
            
	     pop_panel.setBounds(300,150,500,300); //150, 100, 500, 300
	     pop_panel.setBackground(new Color(255,250,205));
	     pop_panel.setVisible(false);

             
                 
        final JButton satellite_street_btn = new JButton(new ImageIcon(MainInterface.class.getResource("/satellite_view.png")));
	satellite_street_btn.setSize(48, 48);
        satellite_street_btn.setActionCommand("Satellite");
	satellite_street_btn.setLocation(1080, 25); // screen launch position: 510, 30

             
             
             

	   ImageIcon close= new ImageIcon(MapInterface.class.getResource("/RoundPaneClose.png"));
	   final JButton close_button = new JButton(close);

	     
	      
	     layer_panel.add(my_panel, JLayeredPane.DEFAULT_LAYER, 0);
	     layer_panel.add(pop_panel, new Integer(1), 0);
	     layer_panel.add(satellite_street_btn, new Integer(2), 0);
	
	     layer_panel.setPreferredSize(new Dimension(1920, 1080));
                       layer_panel.setVisible(true);
	     this.add(layer_panel);
 //	    this.add(my_panel);
	     
	     my_map.get_main_map().addMouseListener(new MouseListener(){
			 
		 @Override
		 public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

		      int i =0;
			
				   
	             //while(i<my_map.get_my_vector().size()){ 
                                while(i<lookup.size()){
			        Point2D gp_pt = my_map.get_main_map().getTileFactory().geoToPixel(my_map.get_my_vector().elementAt(i), my_map.get_main_map().getZoom());
			        //convert to screen
			        Rectangle rect = my_map.get_main_map().getViewportBounds();
			        Point converted_gp_pt = new Point((int)gp_pt.getX()-rect.x, (int)gp_pt.getY()-rect.y);
                                                              if(converted_gp_pt.distance(e.getPoint())<20)  {
			            		pop_panel.setLayout(null);
			            		
                                                                                           JLabel title_label = new JLabel(sourceTitle.get(lookup.get(i)).toString());
			            		title_label.setSize(400,35);
			            		Font newLabelFont=new Font(title_label.getFont().getName(),title_label.getFont().getStyle(),15);

			            		 //Set JLabel font using new created font
			            		title_label.setFont(newLabelFont);
			            		title_label.setLocation(20,25);
			            	                   pop_panel.add(title_label);
       String description = sourceDescription.get(lookup.get(i)).toString();
       int j = description.indexOf("&lt");
       if(j>=0){
           description = description.substring(0, j);
       }       			            
                                                           
       JTextPane descriptionTP = new JTextPane();

       descriptionTP.setContentType( "text/html" );
       descriptionTP.setEditable(false);
       descriptionTP.setBackground(new Color(255,250,205));
       descriptionTP.setSize(450,110);
       descriptionTP.setLocation(20,60);

       HTMLDocument doc = (HTMLDocument)descriptionTP.getDocument();
       HTMLEditorKit editorKit = (HTMLEditorKit)descriptionTP.getEditorKit();
                                    try {
                                        editorKit.insertHTML(doc, doc.getLength(), description, 0, 0, null);
                                    } catch (BadLocationException ex) {
                                        Logger.getLogger(MapInterface.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(MapInterface.class.getName()).log(Level.SEVERE, null, ex);
                                    }

       JScrollPane scrollPane = new JScrollPane(descriptionTP);
       scrollPane.setSize(450,110);
       scrollPane.setLocation(20,60); 
       scrollPane.setBorder(BorderFactory.createEmptyBorder());
       descriptionTP.setCaretPosition(0);    // set the vertical scrollbar to the top        
       pop_panel.add(scrollPane);                                                     
                                                           
                                          
                                                                            JLabel dateLabel = new JLabel(sourcePubDate.get(lookup.get(i)).toString());
                                                                            dateLabel.setSize(450, 20);
                                                                            dateLabel.setLocation(20, 190);
                                                                            dateLabel.setVerticalAlignment(SwingConstants.BOTTOM);
                                                                            pop_panel.add(dateLabel);
                                                                            
                                                                            
                                                                            
                                            
			        JLabel time = new JLabel();
				time.setText(sourceTime.get(lookup.get(i)).toString()+ " ago");
				/*try {
				 address = new JLabel(	address_finder.getAddressByLatLng(my_map.get_my_api().elementAt(i))  );
				} catch (ParserConfigurationException e1) {
									// TODO Auto-generated catch block
				e1.printStackTrace();
				} catch (SAXException e1) {
									// TODO Auto-generated catch block
				e1.printStackTrace();
			                 } catch (JDOMException e1) {
									// TODO Auto-generated catch block
				e1.printStackTrace();
				}*/
								
				time.setSize(400, 60);
				time.setLocation(20, 180);
				time.setVerticalAlignment(SwingConstants.BOTTOM);
								 
                                pop_panel.add(time);
						
			        close_button.setSize(16,17);
                                close_button.setLocation(465, 10);
			            	    pop_panel.add(close_button);
			            	    
			            	   Link my_link = new Link(sourceLink.get(lookup.get(i)).toString());
                                                                            my_link.set_title(sourceTitle.get(lookup.get(i)).toString());
                                                                            my_link.setBrowserCB(browserCB);
			            	    my_link.setStatus(my_map.get_status());
                                                                
			            	    my_link.setURL(sourceLink.get(lookup.get(i)).toString(),my_map.get_status());
                                                                           if(my_map.get_status()==1)
                                                                             {
                                                                               my_link.setTabPane(tabPane);
                                                                             }
			            	    my_link.setText("Click here to read more...");
			            	    my_link.setSize(160,30);
			            	    my_link.setLocation(20, 235);
			            	    my_link.setVerticalAlignment(SwingConstants.BOTTOM);
	                                                          pop_panel.add(my_link);
			            	    
			            	    pop_panel.setVisible(true);
//			            	    switch1.setEnabled(false);
//			            	    switch2.setEnabled(false);
					        	break;
					        } else {
					       
					        }
			
					        i++;
			      }
                                                           
                         
             
	  	    
	  	    
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			 });
             
                                    close_button.addActionListener(new ActionListener()
		  {

			@Override
			 
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				      pop_panel.removeAll();
	        	                                         pop_panel.setVisible(false);
	        	                                      } 
		   }
		   );
             
     satellite_street_btn.addActionListener(new ActionListener()
		  {

			@Override
			 
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
                                if(e.getActionCommand().equals("Satellite")){                                
                                    //Satellite View
  				    my_map.set_color_status(1);
				    my_map.change_style(1);
                                    satellite_street_btn.setActionCommand("Street");
                                    satellite_street_btn.setIcon(new ImageIcon(MainInterface.class.getResource("/street_view.png")));
                                }
                                else{ // Street View
      				   my_map.set_color_status(0);
				   my_map.change_style(0);
                                   satellite_street_btn.setActionCommand("Satellite"); 
                                   satellite_street_btn.setIcon(new ImageIcon(MainInterface.class.getResource("/satellite_view.png")));
                                }
			    } 
		   });



		
    }
    
    public void setNumFeed(int num){
       NumFeed = num;
    }    
    
    public void launch_spots(ArrayList Latitude,ArrayList longtitude)
    {
        
                                                     System.out.println("HEYHEY\n");
         System.out.println("Lat: " +Latitude.get(0).toString());
          System.out.println("Lon: " + longtitude.get(0).toString());   
              //String s11 = Latitude.get(0).toString();
                                                                                  
//              double xx=Double.parseDouble(s11);
              //System.out.print(xx);
        
        
                                                      double x = 0;
			double y = 0;
			
			Set<Waypoint> waypoints = new HashSet<Waypoint>();
		                  
			//String text = "41.881944 -87.627778 50.11 8.68 40.716667 -74";
			//String delims = "[ ]+";
			//String[] tokens = text.split(delims);
		                  waypoints.clear();
			my_map.get_my_vector().clear();                                                //------------
			my_map.get_my_api().clear();
			String tmp="";
			
			int i =0;
                                                      lookup.clear();
                                                      
				//while(i<tokens.length-1)
                     //   if(Double.parseDouble(Latitude.get(0).toString()) instanceof Double)
                      //  {
                            
                       //     System.out.print("HAHAHHAHAHA");
                        //    System.out.print("\n");
                            
                       // }
                        //                                                 while(i<Latitude.size())

                                int num = (Latitude.size() < NumFeed)? Latitude.size(): NumFeed;  
                                while(i<num)
				{
				  //x= Double.parseDouble(tokens[i]);
				  //y= Double.parseDouble(tokens[i+1]);
                                          String date = CustomFilterDialog.getDate();
                                          String time = CustomFilterDialog.getTime();
                                                                             if(Latitude.get(i).toString()!="")
                                                                           {
                                                                               
                                                                               if(date != null){
                                                                                 if(!sourcePubDate.get(i).toString().contains(" " + date + " ")){
                                                                                      i++;
                                                                                     continue;
                                                                                 }              
                                                                               }
                                                                               
                                                                               if(time != null){
                                                                                              int time1 = checkTime(time);
                                                                                              int time2 = checkTime(sourceTime.get(i).toString());
                                                                                              if(time1 < time2){ 
                                                                                                     i++;
                                                                                                     continue;
                                                                                              }                           
                                                                               }
                                                                                  
                                                                                  String s1 = Latitude.get(i).toString();
                                                                                  String s2 = longtitude.get(i).toString();
                                                                                   x=Double.parseDouble(s1);
                                                                                   y=Double.parseDouble(s2);
                                                                                   
                                                                                waypoints.add(new Waypoint(x,y));
                                                                                my_map.get_my_vector().add(new GeoPosition(x, y));           //------------------

                                                                                tmp+= Double.toString(x);
                                                                                tmp+=",%20";
                                                                                tmp+=Double.toString(y);

                                                                                my_map.get_my_api().add(new String(tmp));

                                                                                tmp="";
                                                                                lookup.add(i);
                                                                                i++;
                                                                               
                                                                           }
                                                                             else{
                                                                             
                                                                               i++;
                                                                             }
                                                                     }
				WaypointPainter painter = new WaypointPainter();
				painter.setWaypoints(waypoints);
				 my_map.reset(painter); 
                                                                         
                                                                 System.out.print(lookup);
                                                                my_map.set_lookup(lookup);
                                                                
        
        
        
    }

    public void setBrowserCB(JCheckBoxMenuItem bcb){
        browserCB = bcb; 
    }            
 
    public void setTabPane(JTabbedPane tp){
        tabPane = tp; 
        
    
    }    
    public void set_soureTimeList( ArrayList sourceTime)
    {
        this.sourceTime=sourceTime;
                      
    }
                 public void set_soureTitle( ArrayList sourceTitle)
                  {
                      
                      this.sourceTitle=sourceTitle;
                      my_map.set_sourcetitle(sourceTitle);
                  }
                   public void set_sourceLink( ArrayList sourceLink)
                  {
                      
                      this.sourceLink=sourceLink;
                  }
                    public void set_sourceDescription( ArrayList sourceDescription)
                  {
                      
                      this.sourceDescription=sourceDescription;
                  }
                     public void set_sourcePubDate( ArrayList sourcePubDate)
                  {
                      
                      this.sourcePubDate=sourcePubDate;
                  }
           public void set_width(int x)
           {
               
               System.out.println(x);
           }
           
           public void set_height(int y)
           {
               
               System.out.println(y);
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
