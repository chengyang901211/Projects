/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.map;

/**
 *
 * @author Caleb
 */
public class feeds {
    	private String title;
	private String description;
	private String URL;
	
	
	public feeds(String title, String description, String URL){
		this.title = title;
		this.description=description;
	    this.URL = URL;
	}
	public String get_title(){
		    
		return title;
	}
	public String get_description(){
	    
		return description;
	}
	public String get_URL(){
	    
		return URL;
	}
}
