/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlisteditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Andrea
 */
public class MyMenuActionListener implements ActionListener{
    
    private ArrayList<Song> songs;
    private static final String MOBILE_SONGS_PATH = "/storage/DC24-3A5F/Musica/";
    private static final String PC_SONGS_PATH = "E:\\Musica\\Andrea\\";
    
    public MyMenuActionListener(ArrayList<Song> songs){
        this.songs = songs;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() instanceof JMenuItem){
            JMenuItem mi = (JMenuItem)ae.getSource();
            if(mi.getName().equals("Save")){
                try {
                    
                    //CREATE XML DOCUMENT
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder parser = factory.newDocumentBuilder();
                    Document doc = parser.newDocument();
                    Element seq = doc.createElement("seq");
                    
                    BufferedWriter destPhone = new BufferedWriter(new FileWriter("C:\\Users\\Andrea\\Desktop\\"+PlaylistEditor.PLAYLIST_NAME+".m3u"));
                    
                    //SELECT SONGS
                    for(Song s : songs){
                        if(s.isSelected()){
                            Element media = doc.createElement("media");
                            media.setAttribute("src",s.getFilePath());
                            seq.appendChild(media);
                            
                            destPhone.write(s.getFilePath().replace(PC_SONGS_PATH, MOBILE_SONGS_PATH).replace("\\", "/"));
                            destPhone.newLine();
                        }
                    }
                    
                    destPhone.close();

                    //WRITE XML
                    Element root = doc.createElement("smil");
                    Element head = doc.createElement("head");
                    Element title = doc.createElement("title");
                    title.setTextContent(PlaylistEditor.PLAYLIST_NAME);
                    Element body = doc.createElement("body");

                    head.appendChild(title);
                    body.appendChild(seq);
                    root.appendChild(head);
                    root.appendChild(body);
                    doc.appendChild(root);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource domSource = new DOMSource(doc);
                    StreamResult streamResult = new StreamResult(new File("C:\\Users\\Andrea\\Desktop\\"+PlaylistEditor.PLAYLIST_NAME+".zpl"));

                    transformer.transform(domSource, streamResult);
                } catch (IOException ex) {
                    Logger.getLogger(MyMenuActionListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(MyMenuActionListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerConfigurationException ex) {
                    Logger.getLogger(MyMenuActionListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(MyMenuActionListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(mi.getName().equals("SortTitle")){
                PlaylistEditor.sortByTitle();
                PlaylistEditor.recreateWindow();
            } else if(mi.getName().equals("SortArtist")){
                PlaylistEditor.sortByArtist();
                PlaylistEditor.recreateWindow();
            } else if(mi.getName().equals("SortSelected")){
                PlaylistEditor.sortBySelected();
                PlaylistEditor.recreateWindow();
            }
        }
    }
    
}
