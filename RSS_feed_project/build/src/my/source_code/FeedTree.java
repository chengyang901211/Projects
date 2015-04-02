package my.source_code;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import my.utility_class.CustomChannelDialog;
import my.map.MapInterface;

/**
 *
 * @author Ying
 */
public class FeedTree{

   //private RSSReader reader = new RSSReader();  
    private ArrayList sourceTitle, sourceLink,sourceDescription, sourcePubDate, sourceTimePassed;    
    private JScrollPane sListPane; 
    private JTree feedTree;
    private DefaultMutableTreeNode root; 
    private DefaultMutableTreeNode nodeSelect;  
    private TreePath pathSelect;
    private HashMap feedMap = new HashMap();
    private ArrayList<String> feedCategory = new ArrayList<>(); 
    private RSSReader reader;
    private String all_unread;
    private CustomItemList ciList;
    private final Color black_color = new Color(40,40,41);
    private MapInterface map;
    
    public FeedTree(JScrollPane spane, RSSReader read, String allOrUnread, CustomItemList cil, MapInterface map){
        
      sListPane = spane;
      reader = read; 
      all_unread = allOrUnread;
      ciList = cil;
      
      root = new DefaultMutableTreeNode("root");
     
      /*Add Uncategorized Channel file*/
      DefaultMutableTreeNode uncatFile = new DefaultMutableTreeNode("Uncategorized Channel");
      root.add(uncatFile);
      feedCategory.add("Uncategorized Channel");
      
      /*Add Starred Items file*/      
      DefaultMutableTreeNode starItem = new DefaultMutableTreeNode("Starred Channel");
      root.add(starItem);
      feedCategory.add("Starred Channel");      

      /*Add Some RSS Feed*/      
      uncatFile.add(new DefaultMutableTreeNode("CNN"));
      feedMap.put("CNN", "http://rss.cnn.com/rss/edition.rss");
      uncatFile.add(new DefaultMutableTreeNode("THE OFFICIAL SITE OF THE MIAMI HEAT"));
      feedMap.put("THE OFFICIAL SITE OF THE MIAMI HEAT", "http://www.nba.com/heat/rss.xml");      
      //uncatFile.add(new DefaultMutableTreeNode("NYT > Home Page"));
      //feedMap.put("NYT > Home Page", "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml");                
      uncatFile.add(new DefaultMutableTreeNode("GEO TEST"));
      feedMap.put("GEO TEST", "http://api.geonames.org/rssToGeoRSS?feedUrl=http://feeds.reuters.com/reuters/worldNews&username=demo&style=full");
      //feedMap.put("GEO TEST", "http://api.geonames.org/findNearbyWikipediaRSS?lat=47&lng=9&username=demo&style=full");
      //uncatFile.add(new DefaultMutableTreeNode("GEO TEST2"));
      //feedMap.put("GEO TEST2", "http://api.geonames.org/findNearbyWikipediaRSS?lat=47&lng=9&username=demo&style=full");
      feedTree = new JTree(root);      
      feedTree.setBackground(black_color);         
      feedTree.setRootVisible(false);
      feedTree.setShowsRootHandles(true);
      feedTree.setSize(200,100);
      feedTree.addMouseListener(feedTreeListener);
      this.map=map;
    }
    
    public ArrayList<String> getFeedCategory(){
       return feedCategory;
    } 
    
    public HashMap getFeedMap(){
       return feedMap;
    }

    
    public JTree getFeedTree(){
      return feedTree;
    }
    public DefaultMutableTreeNode getFeedTreeRoot(){
      return root;
    }
    public ArrayList<String> getfeedCategory(){
      return feedCategory;
    }
    public DefaultMutableTreeNode getTreeRoot(){
      return root;
    }
    
    public void setIconAndColor(){
      final ImageIcon rssFolderIcon = new ImageIcon(FeedTree.class.getResource("/Feed-Folder-blue-16.png"));      
      final ImageIcon rssIcon = new ImageIcon(FeedTree.class.getResource("/rss-icon.png"));
      DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer(){
          @Override
          public Component getTreeCellRendererComponent(JTree tree,
                  Object value, boolean selected, boolean expanded,
                  boolean leaf, int row, boolean hasFocus) {
          super.getTreeCellRendererComponent(tree, value, selected,expanded, leaf, row, hasFocus);
            setBackgroundNonSelectionColor(black_color);
//            setBackgroundSelectionColor(black_color);
            setTextNonSelectionColor(Color.WHITE);
//            setTextSelectionColor(Color.WHITE);
            
           setLeafIcon(rssIcon);   
           
           for( String s : feedCategory){              
             if(value.toString().equals(s)){
                //System.out.println(value.toString()); 
                setIcon(rssFolderIcon);           
             }
           }           

          return this;
        }
      
      };      
      feedTree.setCellRenderer(renderer);
    }    
    
    private MouseAdapter feedTreeListener = new MouseAdapter(){
      
        @Override
        public void mousePressed(MouseEvent e){ 
                pathSelect = feedTree.getPathForLocation(e.getX(), e.getY());
                if(pathSelect == null && SwingUtilities.isRightMouseButton(e) ){ // add a new category
                     JPopupMenu menu = new JPopupMenu();
                     JMenuItem item = new JMenuItem("Add a new category");
                     item.addActionListener(popupMenuListener);                         
                     menu.add(item);        
                         
                     menu.show(feedTree,e.getX(),e.getY());  
                     return;
                }
                else if(pathSelect == null){
                   return;
                }
                
                feedTree.setSelectionPath(pathSelect);                
                
                if(SwingUtilities.isRightMouseButton(e)){
                    //JOptionPane.showMessageDialog(null, "Right Button"); 
                     Rectangle pathBounds = feedTree.getUI ().getPathBounds (feedTree, pathSelect);
                     if(pathBounds != null){ // add popup menu item
                         nodeSelect = (DefaultMutableTreeNode) pathSelect.getLastPathComponent();
                         JPopupMenu menu = new JPopupMenu();
                         
                         if(pathSelect.getPathCount()==2){ // the folder under the root node
                            JMenuItem item = new JMenuItem("Add a new feed");
                            item.addActionListener(popupMenuListener);
                            menu.add(item);                           
                         }
                         else{
//                          if(pathSelect != null && pathBounds.contains(e.getX(),e.getY())){
                            JMenuItem item = new JMenuItem("Delete a feed");
                            item.addActionListener(popupMenuListener);                         
                            menu.add(item);
                         }
                         JMenuItem item = new JMenuItem("Rename");
                         item.addActionListener(popupMenuListener);                         
                         menu.add(item);
                     
                         menu.show(feedTree,pathBounds.x+40,pathBounds.y + pathBounds.height); 
                     }
                }
                else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){ 
                      nodeSelect = (DefaultMutableTreeNode) pathSelect.getLastPathComponent();
                      if(pathSelect.getPathCount() > 2){
                            reader.initListData();
                            System.out.println("path: "+ pathSelect.getLastPathComponent().toString());
                            if(reader.readRSS(feedMap.get(pathSelect.getLastPathComponent().toString()).toString())){
                                 sourceTitle = reader.getSourceTitle();
                                 sourceDescription = reader.getSourceDescription();
                                 sourceLink = reader.getSourceLink();
                                 sourcePubDate = reader.getSourcePubDate();
                                 sourceTimePassed = reader.getTimePassed();
                                 ciList.cleanCustomItemList();
                                 
                                 ciList.setTitleList(sourceTitle);
                                 ciList.setDescriptionList(sourceDescription);
                                 ciList.setLinkList(sourceLink);
                                 ciList.setDateList(sourcePubDate);
                                 ciList.setTimeList(sourceTimePassed);
                                 ciList.setAllOrUnread(all_unread);
                                 
                                 ciList.setCustomItemList();
                                 sListPane.setViewportView(ciList);
                                 
                                 /*add to map interface*/
                                 map.set_soureTitle(sourceTitle);
                                 map.set_sourceDescription(sourceDescription);
                                 map.set_sourceLink(sourceLink);
                                 map.set_sourcePubDate(sourcePubDate);
                                 map.set_soureTimeList(sourceTimePassed);
                                 // System.out.println("Lat: " + reader.getSourceLatitude());
                                 // System.out.println("Lon: " + reader.getSourceLongitude());    
                                 map.launch_spots( reader.getSourceLatitude(),reader.getSourceLongitude());
                                 //map.launch_spots( latitude,longtitude);
                            }
                            else{
                                 JOptionPane.showMessageDialog(null, "Failed to get RSS Info","Error", JOptionPane.ERROR_MESSAGE );                          
                            }
                      }
                    //     JOptionPane.showMessageDialog(null, feedMap.get(pathSelect.getLastPathComponent().toString()));                
                }  
              //  System.out.print(reader.getSourceLatitude().get(0).toString());       
        }
    };

 
    private ActionListener popupMenuListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
           DefaultTreeModel model = (DefaultTreeModel) feedTree.getModel();
           //JOptionPane.showMessageDialog(null, e.getActionCommand());
            switch (e.getActionCommand()) {
                case "Delete a feed":
                     //System.out.println(nodeSelect);
                     model.removeNodeFromParent(nodeSelect);
                    break;
                case "Add a new feed":
                    //System.out.println(nodeSelect);
                    addNewFeed(false, model);

                    break;
                case "Add a new category":
                    addNewCategory(model);
                    break;
                case "Rename":        
                    //JOptionPane.showMessageDialog(null, nodeSelect);
                    if(!nodeSelect.toString().equals("Uncategorized Channel") &&
                       !nodeSelect.toString().equals("Starred Items")){
                       
                       String value = (String) feedMap.get(nodeSelect.toString());
                       Object obj = feedMap.remove(nodeSelect.toString());
                       String newName = JOptionPane.showInputDialog ( null, "Enter a new name", "Rename", JOptionPane.PLAIN_MESSAGE); 
                       if(!newName.equals("")){
                           feedMap.put(newName,value);
//                         nodeSelect.setUserObject(newName);
//                         feedTree.expandPath(new TreePath(model.getPathToRoot(nodeSelect.getParent())));   // refresh the tree without changing the current status                       }
                           model.valueForPathChanged(pathSelect, newName);
                       }
                    }
                    else{
                        //cannot rename the default category
                    }
                    break;
                default:
                    break;
            }
        }    
    };    
    public void addNewFeed(boolean fromBtn, DefaultTreeModel model){

           CustomChannelDialog cfd = new CustomChannelDialog(feedCategory);
           if(fromBtn == false){ // from the popup menu
              cfd.disableBoxCategory();
              cfd.setBoxCategoryItem(nodeSelect);
           } 
           Object[] options1 = { "Add the feed", "Cancel"};
           int result = JOptionPane.showOptionDialog(null, cfd.getDialogPanel(), "Add a New Feed",JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);        
                    
           if(result == 0 && !cfd.getTFAddress().toString().equals("Enter a website") && !cfd.getTFAddress().toString().equals("") ){
                    
                WebRssInfo wri = new WebRssInfo();
                String new_feed_name = "<html>" + wri.searchChannelName(cfd.getTFAddress()) + "</html>";                        
                                
                if(!new_feed_name.equals("<html>"+"No.."+"</html>")){  
                     if(fromBtn){
                         TreePath pathSelect = feedTree.getNextMatch(cfd.getSelectedBoxCategoryItem().toString(), 1, Position.Bias.Forward);
                         nodeSelect = (DefaultMutableTreeNode) pathSelect.getLastPathComponent();                     
                     }
                    
                     feedMap.put(new_feed_name,cfd.getTFAddress().toString()); 
                      
                     DefaultMutableTreeNode newRssFeed = new DefaultMutableTreeNode(new_feed_name);
                     model.insertNodeInto(newRssFeed, nodeSelect, nodeSelect.getChildCount());
                     feedTree.expandPath(new TreePath(model.getPathToRoot(newRssFeed.getParent())));                
                }
                else{
                     JOptionPane.showMessageDialog(null, "Invalid RSS Feed","Warning", JOptionPane.WARNING_MESSAGE );
                }
           }
           else if(result != 1 && result != -1){ // not cancel or close btn. Then, .. failed -- Print the warning error message
                JOptionPane.showMessageDialog(null, "Faild to Add the Feed","Warning", JOptionPane.WARNING_MESSAGE );
           }         
    }
    
    public void addNewCategory(DefaultTreeModel model){
                       
        String newCName = JOptionPane.showInputDialog ( null, "Enter a new category name", "Add a New Category", JOptionPane.PLAIN_MESSAGE); 

        if(newCName != null && !newCName.equals("")){                   
            
           boolean flag = newCName.matches(".*[a-zA-Z0-9].*"); // check if user enter at least one letter or number
           if(!flag){ // name does not match at least one character requirement
              return;
           } 
           if(feedCategory.contains(newCName)){
              JOptionPane.showMessageDialog(null, "Dupilcate Category Name","Warning", JOptionPane.WARNING_MESSAGE );                           
              return;
           }  
                           
           DefaultMutableTreeNode new_cat = new DefaultMutableTreeNode(newCName);
           model.insertNodeInto(new_cat, root,root.getChildCount());                       
           feedCategory.add(newCName);  
           feedTree.fireTreeExpanded(new TreePath(root.getPath()));   // refresh the tree without changing the current status
        }        
    }
    
}
