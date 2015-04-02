package my.source_code;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import my.utility_class.TimeConversion;


public class RSSReader {
    
    private ArrayList<String> feedItem; // or feed entry for atom 1.0
    private ArrayList sourceTitle;
    private ArrayList sourceDescription;
    private ArrayList sourceLink;
    private ArrayList sourcePubDate;
    private ArrayList sourceLongitude;
    private ArrayList sourceLatitude;
    private ArrayList sourceTimePassed;
    private String currentWeb = "";   // record the current Web for refresh button
    private WebRssInfo wri;
    
    private final String rssTag[] = new String[] {"<title.*?>(.*?)</title>","<link.*?>(.*?)</link>","<description.*?>(.*?)</description>","<pubDate.*?>(.*?)</pubDate>", "<geo:lat>(.*?)</geo:lat>","<geo:long>(.*?)</geo:long>" };
    private final String atomTag[] = new String[] {"<title.*?>(.*?)</title>", "<link .*?/>","<content.*?>(.*?)</content>","<published.*?>(.*?)</published>", "<georss:point>(.*?)</georss:point>"};

    private TimeConversion tc = new TimeConversion();
    private Date current_date;
    private int i;
    private MyLocalDatabase my_data;
    
    
    public RSSReader(){
        initListData();
    }
    
    public ArrayList getSourceTitle(){
       return sourceTitle;
    }

    public ArrayList getSourceDescription(){
       return sourceDescription;
    }
    
    public ArrayList getSourceLink(){
       return sourceLink;
    }

    public ArrayList getSourcePubDate(){
       setTimePassed();
       return sourcePubDate;
    }

    public ArrayList getSourceLongitude(){
       return sourceLongitude;
    }

    public ArrayList getSourceLatitude(){
       return sourceLatitude;
    }    
    
    public String getCurrentWeb(){
       return currentWeb;
    }
    
    public void setCurrentWeb(String s){
       currentWeb = s;
    }
    
    public ArrayList getTimePassed(){    
        return sourceTimePassed;
    }

    public ArrayList get_sourceLatitude(){
        return sourceLatitude;       
    }
    
    public ArrayList get_sourceLongitude(){    
        return sourceLongitude;        
    }
    
    public void setLocalDatabase(MyLocalDatabase d){
        my_data = d;    
    }
    
    public void initListData(){
       feedItem = new ArrayList();
       sourceTitle = new ArrayList();
       sourceDescription = new ArrayList();       
       sourceLink = new ArrayList();
       sourcePubDate = new ArrayList();
       sourceLongitude = new ArrayList();    
       sourceLatitude = new ArrayList();         
       sourceTimePassed = new ArrayList();
    }
    
    public boolean readRSS(String urlAddress){      
            wri = new WebRssInfo();

            if(!wri.setRssUrl(urlAddress)) // set rss url failed
                return false;
            currentWeb = urlAddress;    
            if(wri.getFeedType().equals("rss")){
               findRegexPatternLoop(wri.getURLContent().toString(), "<item.*?>(.*?)</item>", feedItem);
               scanFeedItem(rssTag);
            }
            else if (wri.getFeedType().equals("atom")){
               findRegexPatternLoop(wri.getURLContent().toString(), "<entry.*?>(.*?)</entry>", feedItem);
               scanFeedItem(atomTag); 
            }
            else
               return false; 
          
            return true;
    }

    private void scanFeedItem(String tag[]){
         current_date = tc.getCurrentTime();
         i = 0;
         for(String s : feedItem){
             //System.out.println(s); 
              findRegexPattern(s,tag[3], sourcePubDate, true);  
              findRegexPattern(s,tag[0], sourceTitle, false);
              findRegexPattern(s,tag[1], sourceLink, false);
              findRegexPattern(s,tag[2], sourceDescription, false);
              
              if(wri.getFeedType().equals("rss")){
                 //rss longtitude latitude
                 findRegexPattern(s,tag[4], sourceLatitude, false);
                 findRegexPattern(s,tag[5], sourceLongitude, false);
              }
              else if (wri.getFeedType().equals("atom")){
                 //ArrayList tempList = new ArrayList();
                 //findRegexPattern(s,tag[4],tempList, false);
                 sourceLatitude.add("");
                 sourceLongitude.add("");
                 //////////////////////// SEPERATE INTO LONGITUDE AND LATITUDUE 
                 //ex ... <georss:point>45.256 -71.92</georss:point>
              } 
              
             if(sourceLatitude.get(i).toString().equals("") && 
                   sourceLongitude.get(i).toString().equals("")){                  
                   List<String> l =  my_data.searchDataList(sourceTitle.get(i).toString());
                   if(l != null){
                       //System.out.println(sourceTitle.get(i).toString()+ ":"+l.get(0).toString());
                       sourceLatitude.set(i, l.get(1).toString());
                       sourceLongitude.set(i, l.get(2).toString());
                   }                    
              }              
          }      
    }
    
    private void findRegexPattern(String content, String pat, ArrayList list, boolean isPubDate){
    
            Pattern pattern = Pattern.compile(pat,Pattern.DOTALL); 
            Matcher matcher = pattern.matcher(content);
            
            if(matcher.find()) {
                
                if(wri.getFeedType().equals("atom") && pat.equals("<link .*?/>")){ // special case for atom 1.0 link
                    Pattern link_pat = Pattern.compile("herf=\".*?\"", Pattern.DOTALL);
                    Matcher link_match = link_pat.matcher(matcher.group().toString());
                    if(link_match.find()){
                        String link_name = link_match.group().toString();
                        int firstPos = link_name.indexOf("\"");
                        int lastPos = link_name.lastIndexOf("\"");
                        link_name = link_name.substring(firstPos+1,lastPos);
                        list.add(i,link_name);
                    }
                    else{
                        list.add(i,"");
                    }
                 } 
                 else if(wri.getFeedType().equals("atom") && pat.equals("<content.*?>(.*?)</content>")){
                      list.add(i,"");
                 }
                 else{ // rss
                   String find = findSource(matcher.group().replaceAll("\\s"," "));
                   if(isPubDate){
                      i = searchInsertIndex(find);       
                      //System.out.println(i);
                   }  
                  
                   list.add(i,find);
                 }
            }
            else{
                list.add(i,""); // if not match then add empty string
            }
    }
    
    private int searchInsertIndex(String find){
          long diff = tc.calculateDifference(tc.parseRFC(find), current_date);

          for(i = 0; i < sourcePubDate.size(); i++){
               long diff1 = tc.calculateDifference(tc.parseRFC(sourcePubDate.get(i).toString()), 
                                                   current_date);
               if(diff < diff1 ){
                  break;
               }
          }
          return i;
    }
    
    private void findRegexPatternLoop(String content, String pat, ArrayList list){
    
            Pattern pattern = Pattern.compile(pat,Pattern.DOTALL);
 
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                // System.out.print("Start index: " + matcher.start());
                // System.out.println(" End index: " + matcher.end() + " ");
                // System.out.println(matcher.group().replaceAll("\\s",""));
                list.add(matcher.group().replaceAll("\\s"," "));
            }
    }    
    
    private String findSource(String line){
        
        try{        
            
           int firstPos = line.indexOf(">");
           int lastPos = line.lastIndexOf("<");
           line = line.substring(firstPos+1,lastPos);          
        }
        catch(Exception e){
          line = "";
        }
   
        return line;
    }
   
    private void setTimePassed(){
        current_date = tc.getCurrentTime();
        for(Object obj : sourcePubDate){
            if(obj.toString().equals("")){
                sourceTimePassed.add("");
                continue;
            }
            Date rfc = tc.parseRFC(obj.toString());
            sourceTimePassed.add(tc.getTimePassed(rfc, current_date));            
        }   
    }
    
}
