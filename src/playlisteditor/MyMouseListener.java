/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlisteditor;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Andrea
 */
public class MyMouseListener implements MouseListener{
    
    private ArrayList<Song> songs;
    
    public MyMouseListener(ArrayList<Song> songs){
        this.songs = songs;
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        if(me.getSource() instanceof JPanel){
            JPanel p = (JPanel)me.getSource();
            String filePath = p.getName();
            Song s = null;
            for(Song ss : songs){
                if(ss.getFilePath().equals(filePath)){
                    s = ss;
                    break;
                }
            }
            
            Component[] comps = p.getComponents();
            for(Component c : comps){
                if (c instanceof JLabel){
                    JLabel l = (JLabel)c;
                    
                    ImageIcon i = null;
            
                    try {
                        i = new ImageIcon(ImageIO.read(new File("cache\\albumArts\\"+s.getAlbum()+".jpg")));
                    } catch (IOException ex) {
                        Logger.getLogger(PlaylistEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(l.getName().equals("cover_op")){
                        i.setImage(PlaylistEditor.setImageOpacity(i, 1f));
                        l.setIcon(i);
                        l.setName("cover");
                        s.setSelected(true);
                    }else if(l.getName().equals("cover")){
                        i.setImage(PlaylistEditor.setImageOpacity(i, 0.05f));
                        l.setIcon(i);
                        l.setName("cover_op");
                        s.setSelected(false);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }
    
}
