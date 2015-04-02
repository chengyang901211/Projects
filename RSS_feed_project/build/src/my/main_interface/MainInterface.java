package my.main_interface;


import java.io.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JFileChooser;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import my.map.MapInterface;
import my.source_code.CustomItemList;
import my.source_code.FeedTree;
import my.source_code.MyLocalDatabase;
import my.source_code.RSSReader;
import my.utility_class.CustomFilterDialog;
import my.utility_class.CustomSetTimerDialog;
import my.utility_class.RssTimer;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;


public class MainInterface extends JFrame{
    private JDesktopPane desktop = new JDesktopPane();
    private JPanel paneTop = new JPanel();
    private JPanel paneRight = new JPanel(); 
    private RSSReader reader = new RSSReader();
    private String currentWeb = "";
    private String all_unread = "all";    
    private JScrollPane sListPane  = new JScrollPane(paneRight,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private CustomItemList ciList = new CustomItemList();
    private MapInterface map = new MapInterface();
    private FeedTree myTree = new FeedTree(sListPane, reader, all_unread, ciList,map);
    private JButton btnShowAll;
    private JButton btnShowUnread;
    private JSlider itemSlider;
    private JLabel itemNumLabel;
    private static JButton btnRefreshRss = new JButton(new ImageIcon(MainInterface.class.getResource("/Feed-Refresh-32.png")));;
    private RssTimer rss_timer = new RssTimer(btnRefreshRss);
    private MyLocalDatabase my_data;
    private static int NumItem = 100;
    
    public MainInterface(){

        /*set attribute of the frame*/
        ImageIcon frameIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/frame_icon256.png")));
        this.setTitle("Map RSS Feeder");
        this.setIconImage(frameIcon.getImage());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 600); //set the frame size when it launches
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(400,400)); //set minimumsize of the frame
        this.setLayout(new BorderLayout()); 

        //////////////////////////////////////////////

        my_data = new MyLocalDatabase();
        reader.setLocalDatabase(my_data);
        //my_data.printDataList();
        
        //System.out.println(my_data.searchDataList("Hello, this is New Yotk"));

        //////////////////////////////////////////////
        
        myTree.setIconAndColor();
        //map = new MapInterface();
        initScreenLayout(); 
        initMenuBar();

        //map.set_width(this.getSize().width);
        //map.set_height(this.getSize().height);   
        
        /*Change JOptionPane Background Color*/
        UIManager UI=new UIManager();
        UI.put("OptionPane.background", Color.white);       
        UI.put("Panel.background",Color.WHITE);  

        rss_timer.setTimer();
        
        this.addWindowListener( new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                //System.out.println("exit");
                rss_timer.stopTimer();
            }
        });        
    }
    

    public static JButton getRefreshBtn(){
       return btnRefreshRss;
    }
    
    private void initMenuBar(){
        
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        
        //JMenu 
        ////////// FILE ///////////
        JMenu file = new JMenu("File");
        menuBar.add(file);
                        
        JMenuItem save = new JMenuItem("Save", new ImageIcon(MainInterface.class.getResource("/Feed-Save-24.png")));
        save.setActionCommand("Save");
        save.addActionListener(fileItemListener);
        file.add(save);
        
        JMenuItem load = new JMenuItem("Load", new ImageIcon(MainInterface.class.getResource("/Feed-Load-24.png")));
        load.setActionCommand("Load");

        load.addActionListener(fileItemListener);
        file.add(load);
       
        
        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(fileItemListener);        
        file.add(exit);  

        ////////// RSS View ///////////        
        final JMenu view = new JMenu("View");
        menuBar.add(view);
        
        JCheckBoxMenuItem browserCB = new JCheckBoxMenuItem("Lauch Web From Browser");
        ciList.setBrowserCB(browserCB);
        map.setBrowserCB(browserCB);
        view.add(browserCB);
        
        view.addSeparator();
        
        JPanel leftAlignPane = new JPanel();
        JLabel numItemLab = new JLabel("Set Number of Items:");
        leftAlignPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftAlignPane.add(numItemLab);
        itemNumLabel = new JLabel(Integer.toString(NumItem));
        itemNumLabel.setBorder(new EmptyBorder(0,78,0,0));
        leftAlignPane.add(itemNumLabel);
        
        view.add(leftAlignPane);
        

        itemSlider = new JSlider();
        itemSlider.setMaximum(100);
        itemSlider.setMinimum(10);
        itemSlider.setValue(NumItem);
        itemSlider.addChangeListener( new ChangeListener(){

          public void stateChanged(ChangeEvent ce){
              JSlider source = (JSlider) ce.getSource();
              int value = itemSlider.getValue();
              NumItem = value;
              String str = Integer.toString(value);
              itemNumLabel.setText(str);              
              if(!source.getValueIsAdjusting()){
                 //after slider stop, then set the number of Items and 
                 //refresh the list
                 ciList.setNumFeed(value); 
                 map.setNumFeed(value);
                 btnRefreshRss.doClick();
              }
           }
        });
                

        JPanel panel = new JPanel();
        
        panel.add(itemSlider);
        view.add(panel); 
    }
    
    private void initToolBar(){
    
        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
        
        toolbar.setOpaque(true);
        toolbar.setFloatable(false); // not draggable
        
        
        JButton btnAddRssFolder = new JButton(new ImageIcon(MainInterface.class.getResource("/Feed-Folder-Add-blue-32.png")));
        btnAddRssFolder.setPreferredSize(new Dimension(42,37));   
        btnAddRssFolder.setFocusable(false);
        btnAddRssFolder.setToolTipText("Add Category");
        btnAddRssFolder.setActionCommand("Add Category");
        btnAddRssFolder.addActionListener(toolBarBtnListener);
        toolbar.add(btnAddRssFolder);

        JButton btnAddRssChannel = new JButton(new ImageIcon(MainInterface.class.getResource("/Feed-Add-32.png")));
        btnAddRssChannel.setPreferredSize(new Dimension(42,37));
        btnAddRssChannel.setFocusable(false);
        btnAddRssChannel.setToolTipText("Add Channel/Feed");
        btnAddRssChannel.setActionCommand("Add Channel");
        btnAddRssChannel.addActionListener(toolBarBtnListener);
        toolbar.add(btnAddRssChannel);        
          
        JButton btnDot = new JButton(new ImageIcon(MainInterface.class.getResource("/vertical_four_dots.png")));
        btnDot.setPreferredSize(new Dimension(5,37));
        btnDot.setBorder(new EmptyBorder(0,0,0,0));
        btnDot.setEnabled(false);
        btnDot.setFocusable(false);
        toolbar.add(btnDot);        
        
        
        JButton btnRssLoad = new JButton(new ImageIcon(MainInterface.class.getResource("/Feed-Load-32.png")));
        btnRssLoad.setPreferredSize(new Dimension(42,37));
        btnRssLoad.setFocusable(false);
        btnRssLoad.setToolTipText("Load");
        btnRssLoad.setActionCommand("Load"); 
        btnRssLoad.addActionListener(toolBarBtnListener);
        toolbar.add(btnRssLoad);  
        
        
        JButton btnRssSave = new JButton(new ImageIcon(MainInterface.class.getResource("/Feed-Save-32.png")));
        btnRssSave .setPreferredSize(new Dimension(42,37));
        btnRssSave .setFocusable(false);
        btnRssSave.setToolTipText("Save");
        btnRssSave.setActionCommand("Save"); 
        btnRssSave.addActionListener(toolBarBtnListener);
        toolbar.add(btnRssSave);          

        JButton btnDot2 = new JButton(new ImageIcon(MainInterface.class.getResource("/vertical_four_dots.png")));
        btnDot2.setPreferredSize(new Dimension(5,37));
        btnDot2.setBorder(new EmptyBorder(0,0,0,0));
        btnDot2.setEnabled(false);
        btnDot2.setFocusable(false);        
        toolbar.add(btnDot2);  
        
        btnRefreshRss.setPreferredSize(new Dimension(42,37));
        btnRefreshRss.setFocusable(false);
        btnRefreshRss.setToolTipText("Refresh RSS Item");

        toolbar.add(btnRefreshRss);          
    
        btnRefreshRss.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                currentWeb = reader.getCurrentWeb();
                if(!currentWeb.equals("")){
                   reader.initListData(); 
                   reader.readRSS(currentWeb);
                
                   ciList.resetCustomItemList();
                   ciList.setTitleList(reader.getSourceTitle());
                   ciList.setDescriptionList(reader.getSourceDescription());
                   ciList.setLinkList(reader.getSourceLink());
                   ciList.setDateList(reader.getSourcePubDate());
                   ciList.setTimeList(reader.getTimePassed());                
                   ciList.setCustomItemList();
                
                   sListPane.setViewportView(ciList);
                
                   if(!reader.getSourceLatitude().isEmpty()){
                      map.launch_spots( reader.getSourceLatitude(),reader.getSourceLongitude());
                   }
                }
              //  RssTimer rss_timer = new RssTimer(sListPane,0);
              //  rss_timer.setTimer();             
            }
        });        


        JButton btnTimer = new JButton(new ImageIcon(MainInterface.class.getResource("/Feed-Timer-32.png")));
        btnTimer.setPreferredSize(new Dimension(42,37));
        btnTimer.setFocusable(false);
        btnTimer.setToolTipText("Set Timer");
        btnTimer.setActionCommand("Set Timer");
        btnTimer.addActionListener(toolBarBtnListener);
        toolbar.add(btnTimer);        
  
        JButton btnFilter = new JButton(new ImageIcon(MainInterface.class.getResource("/Feed-Search-32.png")));
        btnFilter.setPreferredSize(new Dimension(42,37));
        btnFilter.setFocusable(false);
        btnFilter.setToolTipText("Filter Item");
        btnFilter.setActionCommand("Filter Item");
        btnFilter.addActionListener(toolBarBtnListener);
        toolbar.add(btnFilter);           
        
        
        toolbar.add( Box.createHorizontalGlue() ); // After this every component will be added to the right  
   
        btnShowAll = new JButton(new ImageIcon(MainInterface.class.getResource("/show_all_btn.png")));
        btnShowAll.setPreferredSize(new Dimension(42,37));
        btnShowAll.setFocusable(false);
        btnShowAll.setEnabled(false);
        btnShowAll.setToolTipText("Show All Item");
        btnShowAll.setActionCommand("ShowAll");        
        btnShowAll.addActionListener(toolBarBtnListener );
        toolbar.add(btnShowAll);           

        btnShowUnread = new JButton(new ImageIcon(MainInterface.class.getResource("/show_unread_btn48.png")));
        btnShowUnread.setPreferredSize(new Dimension(48,37));
        btnShowUnread.setFocusable(false);
        btnShowUnread.setEnabled(true);
        btnShowUnread.setToolTipText("Show Only Unread Item");
        btnShowUnread.setActionCommand("ShowUnread");
        btnShowUnread.addActionListener(toolBarBtnListener );
        toolbar.add(btnShowUnread);            
                   

 /*      timer = new Timer(8000, new ActionListener(){      // Timer 4 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
               sListPane.setViewportView(new JButton("1"));                
            }
        });
        
        btnRefreshRss.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //sListPane.setViewportView(null);
                timer.start();              // start timer
            }
        });
*/        
        paneTop.add(toolbar);            
    }
    
    private void initScreenLayout(){
        //Left Panel
        JPanel paneLeft = new JPanel();
        paneLeft.setPreferredSize(new Dimension(200, 560)); //WIDTH, HEIGHT
        paneLeft.setMinimumSize(new Dimension(50, 560));  
        paneLeft.setLayout(new BorderLayout());

        JTree feedTree = myTree.getFeedTree();
               
        JScrollPane jp = new JScrollPane(feedTree);
        paneLeft.add(jp);
        
        //Right Panel
        paneRight.setPreferredSize(new Dimension(400, 560));
        paneRight.setMinimumSize(new Dimension(50,0));
        paneRight.setLayout(new BorderLayout());
        paneRight.setBackground(new Color(40,40,41));

        JPanel tmp = new JPanel();
        tmp.setBackground(new Color(40,40,41));
        sListPane.setViewportView(tmp);

        ///////// Add Tab ////////////
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); 
        tabPane.setBackground(Color.WHITE);
        ciList.setTabPane(tabPane);
        map.setTabPane(tabPane);
        
        tabPane.add("RSS Item", sListPane);
        tabPane.add("Map RSS", map);
                
        paneRight.add(tabPane);
    
        
        //Split Panel Horizontally
        JSplitPane splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, paneLeft, paneRight);
        splitPaneH.setMinimumSize(new Dimension(0,680));
        //Top Panel        
        paneTop.setMinimumSize(new Dimension(800, 40));
        paneTop.setPreferredSize(new Dimension(800, 40));      
        paneTop.setLayout(new BorderLayout());

        initToolBar();
        
        //Split Panel Vertically        
        JSplitPane splitPaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, paneTop, splitPaneH);
        splitPaneV.setDividerSize(0); // will not see the divid bar

        this.add(splitPaneV);    
    }
    
    private ActionListener fileItemListener = new ActionListener(){
       @Override
       public void actionPerformed(ActionEvent e) {
           try {
               //JOptionPane.showMessageDialog(null, e.getActionCommand());
             JFileChooser chooser =new JFileChooser();//windows for saving and loading file 
             //FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
             //chooser.setFileFilter(filter);                 
                    
             File f;
             switch(e.getActionCommand()){
                  case "Save":
                            // save configuration
                            chooser.showSaveDialog(null);//show saving windows 
                            //JOptionPane.showMessageDialog(null, chooser.getSelectedFile().getPath());
                            
                            f  = chooser.getSelectedFile();
                            if(f == null){
                                return;
                            }        
                            File file_save = new File(f.getPath());//get the path of the file user want to save
                            file_save.createNewFile();

                            
                            SaveFile(file_save);//save a xml format file
                            
                      break;
                  case "Load":
                            chooser.showOpenDialog(null);//show loading windows 
                            //JOptionPane.showMessageDialog(null, chooser.getSelectedFile().getPath());

                            f  = chooser.getSelectedFile();
                            if(f == null){
                                return;
                            }        
                            
                            String xmlPath = f.getPath();//get the path of the file user choosed 
                            SAXReader reader = new SAXReader();//set up a xml file parser 
                            File file_load = new File(xmlPath);//get the file which user choosed 
                            Document document = reader.read(file_load);//parse file
                            LoadFile(document);//load file to JTree
                      break;                   
                  case "Exit":
                      //JOptionPane.showMessageDialog(null, "Exit");
                      setVisible(false);
                      dispose(); // destroy the JFrame Object
                      System.exit(0);
                     break;
                  default:
                     break;            
             }   
           } catch (IOException ex) {
               Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
           } catch (DocumentException ex) {
               Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
           }
       }    
    };    

  
    
    private ActionListener toolBarBtnListener = new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
             try {
                 Object source = e.getSource();
                 JButton btn = (JButton) source;
                 
                 JFileChooser chooser =new JFileChooser();//windows for saving and loading file 
                 //FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
                 //chooser.setFileFilter(filter);                 
                 
                 File f;
                 switch(btn.getActionCommand().toString()){
                     case "Add Channel":                                                  
                         myTree.addNewFeed(true, (DefaultTreeModel)myTree.getFeedTree().getModel());      
                         break;         
                     case "Add Category":
                         myTree.addNewCategory((DefaultTreeModel)myTree.getFeedTree().getModel());
                         break;
                     case "Load":
                         chooser.showOpenDialog(null);//show loading windows 
                         //JOptionPane.showMessageDialog(null, chooser.getSelectedFile().getPath());

                         f  = chooser.getSelectedFile();
                         if(f == null){
                             return;
                         }        
                         
                         String xmlPath = f.getPath();//get the path of the file user choosed 
                         SAXReader reader = new SAXReader();//set up a xml file parser 
                         File file_load = new File(xmlPath);//get the file which user choosed 
                         Document document = reader.read(file_load);//parse file
                         LoadFile(document);//load file to JTree
                         break;
                     case "Save":
                         // save configuration
                         chooser.showSaveDialog(null);//show saving windows 
                         //JOptionPane.showMessageDialog(null, chooser.getSelectedFile().getPath());
                         
                         f  = chooser.getSelectedFile();
                         if(f == null){
                             return;
                         }        
                         File file_save = new File(f.getPath());//get the path of the file user want to save
                         file_save.createNewFile();

                         
                         SaveFile(file_save);//save a xml format file
                         
                         break;                     
                     case "ShowAll":
                         btnShowUnread.setEnabled(true);
                         btnShowAll.setEnabled(false);
                         all_unread = "all";
                         ciList.setAllUnread(all_unread);             
                         ciList.resetCustomItemList();
                         ciList.setReadUnread();
                         sListPane.setViewportView(ciList);
                         break;
                     case "ShowUnread":
                         btnShowUnread.setEnabled(false);
                         btnShowAll.setEnabled(true);
                         all_unread = "unread";                     
                         ciList.setAllUnread(all_unread);             
                         ciList.resetCustomItemList();
                         ciList.setReadUnread();
                         sListPane.setViewportView(ciList);
                         break;
                     case "Set Timer":
                         CustomSetTimerDialog std = new CustomSetTimerDialog();
                         std.setRefreshBtn(btnRefreshRss);
                         std.startDialog();
                         rss_timer.stopTimer();
                         rss_timer.setRefreshTime(std.getNUM());
                         break;
                     case "Filter Item":
                         CustomFilterDialog cfd = new CustomFilterDialog();
                         cfd.init();
                         cfd.startDialog();
                         
                         break;
                 }
     //             JOptionPane.showMessageDialog(null, all_unread,"Hi", JOptionPane.INFORMATION_MESSAGE );
                 //JOptionPane.showMessageDialog(null, btn.getActionCommand().toString(),"Hi", JOptionPane.INFORMATION_MESSAGE );
             } catch (IOException ex) {
                 Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
             } catch (DocumentException ex) {
                 Logger.getLogger(MainInterface.class.getName()).log(Level.SEVERE, null, ex);
             }
            }
    };  

    private void SaveFile(File file) throws FileNotFoundException, IOException
    {
                         FileOutputStream is = new FileOutputStream(file);
                         //OutputStreamWriter osw = new OutputStreamWriter(is);
                         int i,k=0;//for iterater 
                         Document document = DocumentHelper.createDocument(); //create a document which we will save
                         Element Document_root = document.addElement("root");//add a root to document
                         Document_root.addElement("Name").addText("Team Bravo");
                         
                         DefaultMutableTreeNode root = myTree.getTreeRoot();//get feedtree root, will use it to get info
                         HashMap hashmap = myTree.getFeedMap();//get feedmap,will use it to get feed info such as link
                         //read root element info to document 
                         for(i=0;i<root.getChildCount();i++)
                         {
                            Element Document_Category = Document_root.addElement("Category").addAttribute("name", root.getChildAt(i).toString());
                            for (k=0;k<root.getChildAt(i).getChildCount();k++){
                                Document_Category.addElement("title").addAttribute("Link",hashmap.get(root.getChildAt(i).getChildAt(k).toString()).toString()).addText(root.getChildAt(i).getChildAt(k).toString());
                            }
                            k = 0;//reset k=0 for next loop
                         }
                         
                         Document_root.addElement("NumItem").addText(Integer.toString(NumItem));
                         Document_root.addElement("Timer").addText(Integer.toString(RssTimer.getRefreshTime()));
                         ///////////////////////////////////////////////////////
                         //save Document in xml format /////////////////////////
                        try{  
                             FileWriter fileWriter = new FileWriter(file);  
                             OutputFormat xmlFormat = new OutputFormat();  
                            //xmlFormat.setEncoding("GB2312");  
                             XMLWriter xmlWriter = new XMLWriter(fileWriter,xmlFormat);  
                             xmlWriter.write(document);  
                             xmlWriter.close();  
                        }catch(IOException e){  
                            System.out.println("can not find file");  
                         }
                         ///////////////////////////////////////////////////////
     }
     private void LoadFile(Document document) throws FileNotFoundException, IOException
     {
         
            boolean valid = false;
            Element root = document.getRootElement();
            DefaultTreeModel model = (DefaultTreeModel) myTree.getFeedTree().getModel();
            DefaultMutableTreeNode TreeRoot = myTree.getFeedTreeRoot();///get root
            //clear feed tree,feed category, and feed map                  
            
            
            for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();//get elements in root
                if(element.getName().equals("Name") && !valid){
                    if(!element.getText().equals("Team Bravo")){
                       JOptionPane.showMessageDialog(null, "Not Valid File");
                       break;
                    }    
                   model.setRoot(null);
                   myTree.getFeedCategory().clear();
                   myTree.getFeedMap().clear();
                   ////////////////////////////////////////////////////////////////////
                   TreeRoot.removeAllChildren();//clear root
                   
                    
                   valid = true;
                }                
                else if(element.getName().equals("NumItem")){
                    NumItem = Integer.parseInt(element.getText().toString());
                    itemSlider.setValue(NumItem);
                    itemNumLabel.setText(Integer.toString(NumItem));  
                    //System.out.println(element.getText());
                }
                else if(element.getName().equals("Timer")){
                    RssTimer.setRefreshTime(Integer.parseInt(element.getText().toString()),1);
                    int num = Integer.parseInt(element.getText().toString());
                    CustomSetTimerDialog.setNum(num/1000/60);
                    //System.out.println(element.getText());
                }
                else{
                   DefaultMutableTreeNode temp = new DefaultMutableTreeNode(element.attributeValue("name"));//create a category node with a name which is a value of name
                   TreeRoot.add(temp);//add node to root
                   //JOptionPane.showMessageDialog(null, element.attributeValue("name"));
                   myTree.getFeedCategory().add(element.attributeValue("name"));///add category to feedcategory list
                   for ( Iterator k = element.elementIterator(); k.hasNext(); ) {
                    Element element_title = (Element) k.next();//get the title elements
                    //JOptionPane.showMessageDialog(null, element_title.attributeValue("Link"));
                    //JOptionPane.showMessageDialog(null, element_title.getText());
                    temp.add(new DefaultMutableTreeNode(element_title.getText()));//add title node to category
                    myTree.getFeedMap().put(element_title.getText(), element_title.attributeValue("Link"));// put title and link to feedmap for searching
                   }
                 
                   model.setRoot(TreeRoot);//reset the root of feedtree
                }
             }
             ciList.removeAll();
             reader.setCurrentWeb("");
             sListPane.setViewportView(ciList);
    }    
}
