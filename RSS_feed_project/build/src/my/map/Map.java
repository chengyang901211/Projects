package my.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.OSMTileFactoryInfo;
import org.jdesktop.swingx.VirtualEarthTileFactoryInfo;
import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;


public class Map {
	
	private  JXMapKit mapView;
	private  JXMapViewer map ;
	private  JXMapViewer map2 ;
	private  GeoCoder address_finder;
	
	private  List<TileFactory> factories ;
	private  TileFactoryInfo sat;
    private  TileFactoryInfo str ;
    
    private Set<Waypoint> waypoints0; 
    private WaypointPainter painter0;
    
  //contaniers
  	private Vector<GeoPosition> my_vector;
  	private  Vector<JLabel> my_labels;
  	private  Vector<JLabel> my_title;
  	private  Vector<JLabel> my_description;
  	private  Vector <JLabel> my_URL;
                  private  Vector<feeds> my_info;
  	private  int status;
  	private  int color_status;
  	private  Vector<String> my_api;

                  private ArrayList sourceTitle;    
                  private ArrayList<Integer> lookup;
                  
	public Map(){
		
		
		// map basic
		 mapView = new JXMapKit();
                                     mapView.setSize(new Dimension(100,100));
		 map = mapView.getMainMap();
		 map2 = mapView.getMiniMap();
	                   address_finder = new GeoCoder();
	     
		 //style
		 factories = new ArrayList<TileFactory>();
		  sat= new VirtualEarthTileFactoryInfo (VirtualEarthTileFactoryInfo.SATELLITE);
	                   str = new OSMTileFactoryInfo();
		 factories.add(new DefaultTileFactory(str));
		 factories.add(new DefaultTileFactory(sat));
	     mapView.setTileFactory(factories.get(0));
	     
	     //location&&zoom 
	     map.setAddressLocation(new GeoPosition(41.9672, -95.7716));
		 map2.setAddressLocation(new GeoPosition(41.9672, -95.7716));
	     map.setZoom(15);
	     map2.setZoom(17);
	     
	     
	    //waypoints
	     waypoints0 = new HashSet<Waypoint>();
		 painter0 = new WaypointPainter();
	     painter0.setWaypoints(waypoints0);
	     map.setOverlayPainter(painter0);
	    map2.setOverlayPainter(painter0);
	     
	     //containers
	     color_status=0;
	     status=1;
	     
	     my_info = new Vector<feeds>();
             my_info.add(new feeds("Chicago", "Chicago is great!", "http://edition.cnn.com/2014/04/05/us/michigan-hate-crime-attack/index.html"));
             my_info.add(new feeds("Europe", "Europe is awsome!", "http://edition.cnn.com/2014/04/05/us/fort-hood-gunman-facebook/index.html"));
             my_info.add(new feeds("New York City", "New York is the most populous city in the United States and the center of the New York metropolitan area, one of the most populous urban agglomerations in the world.[6] The city is referred to as New York City or the City of New York to distinguish it from the State of New York, of which it is a part.", "http://edition.cnn.com/2014/04/05/world/asia/afghanistan-elections/index.html"));

                        my_vector= new Vector<GeoPosition>();
	     my_api=new Vector<String>();
	     my_labels= new Vector<JLabel>(); 
	     
	     my_title= new Vector<JLabel>();
	     my_description = new Vector<JLabel>();
                      my_URL = new Vector<JLabel>();
	
	     for (int i =0; i<my_vector.size();i++){
			  my_labels.add(new JLabel());
		}
	     
	    for(int i=0; i<my_info.size();i++){
			  my_title.add(new JLabel(my_info.elementAt(i).get_title()));
		                    my_description.add(new JLabel(my_info.elementAt(i).get_description()));
			  my_URL.add(new JLabel(my_info.elementAt(i).get_URL()));
			 }
	     
	     // map mouse move
	     map.addMouseMotionListener(new MouseMotionListener() {
	 	    
	 		@Override
	 		public void mouseDragged(MouseEvent e) {
	 			// TODO Auto-generated method stub
	 			
	 		}

	 		@Override
	 		public void mouseMoved(MouseEvent e) {
	 			// TODO Auto-generated method stub
	 		   for (int i =0; i<my_vector.size();i++){
			     my_labels.add(new JLabel());
		                      }         
                                                     
	 			
	 			        //convert to world bitmap
	 	            int i =0;
	 	
	 		      //  while(i<my_vector.size()){ 
	 		       while(i<lookup.size()){ 
	 		        	    Point2D gp_pt = map.getTileFactory().geoToPixel(my_vector.elementAt(i), map.getZoom());
	 				        //convert to screen
	 				        Rectangle rect = map.getViewportBounds();
	 				        Point converted_gp_pt = new Point((int)gp_pt.getX()-rect.x,
	 				                                          (int)gp_pt.getY()-rect.y);
	 				  
	 						        if(converted_gp_pt.distance(e.getPoint())<20)  {
	 						            
	 						            String tmp = "<html>";
//	 						            tmp+=my_info.elementAt(i).get_title();
                                                                    tmp+=sourceTitle.get(lookup.get(i)).toString();
	 						            tmp+="<BR>";
	 						            
	 									/*try {
	 										
	 										tmp +=address_finder.getAddressByLatLng(my_api.elementAt(i));
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

	 						            tmp+="</html>";
	 						            my_labels.elementAt(i).setText(tmp);
	 						            
	 								           if( color_status==1)
	 								           {
	 								        	   my_labels.elementAt(i).setForeground(Color.ORANGE);
	 								        	   
	 								           }
	 								           else{
	 								        	   my_labels.elementAt(i).setForeground(Color.BLACK);
	 								        	   
	 								           }
	 								           
	 						            map.add(my_labels.elementAt(i));
	 						            my_labels.elementAt(i).setLocation(converted_gp_pt);
	 						            my_labels.elementAt(i).setVisible(true);
	 						        } else {
	 						        	
	 						        	my_labels.elementAt(i).setVisible(false);
	 						        }
	 				        
	 				        i++;
	 		           }
	 	     
	 		   }
	 	  });
         
	     
	}
	
	// reset button pushed, reset all waypoints on map. 
	public void reset(WaypointPainter painter){                
		
		    map.setOverlayPainter(painter);
		     map2.setOverlayPainter(painter);
	
		     map.setAddressLocation(new GeoPosition(41.9672, -95.7716));
			 map2.setAddressLocation(new GeoPosition(41.9672, -95.7716));
			 
		     map.setZoom(15);
		     map2.setZoom(17);
		
	}
public void change_style(int x)
{
	   TileFactory factory = factories.get(x);
	   map.setTileFactory(factory);
	   map2.setTileFactory(factory);

	     map.setAddressLocation(new GeoPosition(41.9672, -95.7716));
		 map2.setAddressLocation(new GeoPosition(41.9672, -95.7716));
	     map.setZoom(15);
	     map2.setZoom(17);
	
	
	
	
	
}
	
   public  Vector<GeoPosition> get_my_vector(){
		return my_vector;
	}
	public Vector<JLabel> get_my_labels(){
		return my_labels;
	}
	public Vector<JLabel> get_my_title(){
		return my_title;
	}
	public Vector<JLabel> get_my_description(){
		return my_description;
	}
	public Vector <JLabel> get_my_URL(){
		return my_URL;
	}
//	public Vector<feeds> get_my_info(){
//		return my_info;
//	}
	public int get_status(){
		return status;
	}
	public void set_status(int status){
		
		this.status =status;
	}
	public int get_color_status(){
		return color_status;
	}
	public void set_color_status(int color_status){
		
		this.color_status =color_status;
	}
	public Vector<String> get_my_api(){
		return my_api;
	}
	public JXMapKit get_mapview(){
		return  mapView;
	}
	
	public JXMapViewer get_main_map(){
		
		return map;
	}
        
          
	
public JXMapViewer get_mini_map(){
		
		return map2;
	}

public void set_lookup(ArrayList<Integer> lookup){
             // this.lookup.clear();
              this.lookup=lookup;
    
}

public void set_sourcetitle(ArrayList sourceTitle){
              //this.sourceTitle.clear();
              this.sourceTitle=sourceTitle;
    
}


}
