/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.source_code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebRssInfo {
   
    private URL url;
    private String type; // rss 2.0 or atom 1.0

    public WebRssInfo(){

    }
            
    public boolean setRssUrl(String rssAddress){
        if(isHTTP(rssAddress)){ // if it is a valid rss address
           try {
              url = new URL(rssAddress);
           } catch (MalformedURLException ex) {
              Logger.getLogger(WebRssInfo.class.getName()).log(Level.SEVERE, null, ex);
           }
           
           if(isRSSFeed(getURLContent().toString()))
               type = "rss";
           else if(isAtomFeed(getURLContent().toString()))
               type = "atom";
           else
               type = "";
           
           return true;
        }
        return false;
    }
    
    public String getFeedType(){
        return type;
    }
    
    public String getURLContent(){
    
        try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            
            StringBuilder urlContent = new StringBuilder();
            String line;// sourceTitle="", sourcelink="";
            while( (line = in.readLine()) != null){
               urlContent.append(line);
            }          
            in.close();
            return urlContent.toString();
            //System.out.println(sourceTitle.size());
            //System.out.println(sourceLink.size());          
        }catch (IOException ex) {
            System.out.println(ex.getMessage().toString());
        }  
        return null;
    }
    
    private boolean isHTTP(String rssAddress){
       if(rssAddress.length() < 6){
          //System.out.println("less than 6");
          return false;
       }
       else if(!rssAddress.toLowerCase().substring(0, 7).equals("http://")){
         //System.out.println("not http");           
         return false;
       }

       return true;
    }

    private boolean isRSSFeed(String content){
        if(!content.equals("") && content != null){
          
          Pattern pattern = Pattern.compile("<rss.*?version=\"2.0\".*?>");  
          Matcher matcher = pattern.matcher(content); //match the <rss ... version="2.0"> format

          while (matcher.find()) {
             System.out.println(matcher.group());
             return true;
          }
        }
        return false;        
    }

    private boolean isAtomFeed(String content){

        if(!content.equals("") && content != null){
          
          Pattern pattern = Pattern.compile("<feed xmlns=\"http://www.w3.org/2005/Atom\" .*?>");  
          Matcher matcher = pattern.matcher(content); //match the <feed xmlns=\"http://www.w3.org/2005/Atom\" ...> format

          while (matcher.find()) {
             System.out.println(matcher.group());
             return true;
          }
        }
        return false;        
    }    
    
    public String searchChannelName(String urlAddress){ // or Search Feed Name for atom 1.0
        //StringBuilder sb = getUrlContent(urlAddress);     
        if(!setRssUrl(urlAddress)) // not even a valid http format
            return "No..";
        
        String searchChannel = "No..";
        String content = getURLContent();
               
        
        if(type.equals("rss")){
            //find the information before item
            searchChannel = tokenInfoBeforeItem(content, searchChannel, "<channel.*?>(.*?)</channel>", "<item>");
            //find the title name            
            searchChannel = findNameInTitleTag(searchChannel);
        }
        else if(type.equals("atom")){
            //find the information before entry
            searchChannel = tokenInfoBeforeItem(content, searchChannel, "<feed.*?>(.*?)</feed>", "<entry>");
            //find the title name            
            searchChannel = findNameInTitleTag(searchChannel);
        
        }
          
        return searchChannel;
    }      

    private String tokenInfoBeforeItem(String content, String searchChannel, String pat, String itemOrEntry){ // or before entry
            Pattern pattern = Pattern.compile(pat,Pattern.DOTALL); 
            Matcher matcher = pattern.matcher(content);                  
            //match the feed name
            if(matcher.find()){ 
               searchChannel = matcher.group();
               try{
                 int lastPos = searchChannel.indexOf(itemOrEntry);
                 searchChannel = searchChannel.substring(0,lastPos);
               }
               catch(Exception e){
                  searchChannel = "No..";
               }
               System.out.println("Here is the feed name: " + searchChannel);
            }
            else{
              searchChannel = "No..";
            }

            return searchChannel;    
    }
    
    private String findNameInTitleTag(String searchChannel){
          Pattern pattern = Pattern.compile("<title.*?>(.*?)</title>",Pattern.DOTALL); 
          Matcher matcher = pattern.matcher(searchChannel);                  
            
          while(matcher.find()){
            System.out.println(matcher.group().toString());
            searchChannel = matcher.group().toString();
            int firstPos = searchChannel.indexOf(">");
            int lastPos = searchChannel.lastIndexOf("<");
            searchChannel = searchChannel.substring(firstPos+1, lastPos);
          }
          return searchChannel;
    }
    
}
