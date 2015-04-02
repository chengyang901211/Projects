package my.map;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class GeoCoder {
	 public static String getAddressByLatLng(String latLng) throws ParserConfigurationException, SAXException, JDOMException{
		  String address = "";
		 BufferedReader in= null;
		  try {
			  
		   URL url = new URL("http://maps.google.com/maps/api/geocode/xml?latlng="+latLng+"&sensor=true");
		   HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		   httpConn.setDoInput(true);   
		   
		   in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));//ע�⣬����Ҫ�����ַ������UTF-8�����������      String line;
		   
		   StringBuilder sb = new StringBuilder();
		   String inline = "";
	        while ((inline = in.readLine()) != null) {
	          sb.append(inline);
	        }
	    
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	        factory.setNamespaceAware(true);
	        DocumentBuilder builder = factory.newDocumentBuilder();
         
	        Document doc = builder.parse(new ByteArrayInputStream(sb.toString().getBytes()));
	       
	        NodeList nList = doc.getElementsByTagName("result");//result
	        Node nNode = nList.item(2); 
	    	
	        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                   Element eElement = (Element) nNode;
	                   address= eElement.getElementsByTagName("formatted_address").item(0).getTextContent();
	    		}

		 } catch (MalformedURLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		   
		  } catch (UnsupportedEncodingException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
	     
		  return address;
		 }
	 
	 
}
