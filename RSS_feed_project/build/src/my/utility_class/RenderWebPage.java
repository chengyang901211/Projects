/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.utility_class;

import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javax.swing.JPanel;

/**
 *
 * @author Ying
 */
public class RenderWebPage extends JPanel{
    
  JFXPanel jfxPanel;
  WebView webComponent;


  public RenderWebPage(String address){
     
    jfxPanel = new JFXPanel();
    initSwingComponents();
    loadJavaFXScene(address);
    Platform.setImplicitExit(false);
  }
  private void initSwingComponents(){
    this.setLayout(new BorderLayout()); 
    this.add(jfxPanel, BorderLayout.CENTER);
  }
  private void loadJavaFXScene(final String address){
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        BorderPane borderPane = new BorderPane();
        webComponent = new WebView();
 
        webComponent.getEngine().load(address);
 
        borderPane.setCenter(webComponent);
        Scene scene = new Scene(borderPane);
        jfxPanel.setScene(scene);
        
      }
    });
  } 
}
