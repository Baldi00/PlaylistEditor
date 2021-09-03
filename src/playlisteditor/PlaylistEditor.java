package playlisteditor;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PlaylistEditor {
    
    public static String playListName = "";
    private static ArrayList<Song> songs = new ArrayList();
    private static JFrame window = null;
    private static Semaphore semaphore = new Semaphore(0);
    
    public static void main(String[] args) throws Exception{
        
        setLookAndFeel();
        
        //Get Playlist Name
        createInsertPlaylistNameWindow();
        semaphore.acquire();
        
        File cacheFolder = new File("cache");
        if(!cacheFolder.exists())
            cacheFolder.mkdir();

        File cacheAlbumArtFolder = new File("cache\\albumArts");
        if(!cacheAlbumArtFolder.exists())
            cacheAlbumArtFolder.mkdir();

        File cacheSongTagsFolder = new File("cache\\songTags");
        if(!cacheSongTagsFolder.exists())
            cacheSongTagsFolder.mkdir();
        
        
        //Read Playlist
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document doc = parser.parse(new File("E:\\Musica\\Playlists\\"+playListName+".zpl"));
        NodeList playlistSongsElements = doc.getElementsByTagName("media");
        ArrayList<String> playlistSongsFileNames = new ArrayList();
        
        for(int i = 0; i < playlistSongsElements.getLength(); i++){
            Element e = (Element)playlistSongsElements.item(i);
            String src = e.getAttribute("src");
            playlistSongsFileNames.add(src);
        }
        
        //Read Songs
        File [] songFiles = listFilesRecursive(new File("E:\\Musica\\Andrea"));
        File [] cacheSongTagsFiles = (new File("cache\\songTags")).listFiles();
        ArrayList<String> cacheSongTagsFileNames = new ArrayList();
        
        for(int i=0; i<cacheSongTagsFiles.length; i++){
            cacheSongTagsFileNames.add(cacheSongTagsFiles[i].getName());
        }
        
        for(int i=0; i<songFiles.length; i++){
            if(songFiles[i].getName().endsWith(".mp3") || songFiles[i].getName().endsWith(".MP3")){
                boolean selected = false;
                if(playlistSongsFileNames.contains(songFiles[i].getAbsolutePath())){
                    selected = true;
                }
                
                if(cacheSongTagsFileNames.contains(songFiles[i].getName())){
                    BufferedReader br = new BufferedReader(new FileReader(new File("cache\\songTags\\"+songFiles[i].getName())));
                    String title = br.readLine();
                    String artist = br.readLine();
                    String album = br.readLine();
                    br.close();
                    songs.add(new Song(songFiles[i].getAbsolutePath(),songFiles[i].getName(),title,artist,album,selected));
                }else{
                    songs.add(new Song(songFiles[i].getAbsolutePath(),songFiles[i].getName(),selected));
                }
            }
        }
        
        
        //GRAPHICS
        createWindow();
    }
    
    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}
    }
    
    public static BufferedImage setImageOpacity(final ImageIcon image, float opacity) {
        final BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        graphics2D.drawImage(image.getImage(), 0, 0, 100, 100, null);
        graphics2D.dispose();
        return bufferedImage;
    }
    
    private static File[] listFilesRecursive(File directory) {
        ArrayList<File> files = new ArrayList();
        File [] filesInDir = directory.listFiles();
        for(File f : filesInDir){
            if(f.isFile()){
                files.add(f);
            }else if (f.isDirectory()){
                File [] fromRecursion = listFilesRecursive(f);
                for(File f2 : fromRecursion){
                    files.add(f2);
                }
            }
        }
        
        File [] toReturn = new File[files.size()]; 
  
        for (int i = 0; i < files.size(); i++){
            toReturn[i] = files.get(i); 
        }
        
        return toReturn;
    }
    
    public static void sortByTitle(){
        songs.sort(new SongTitleComparator());
    }
    
    public static void sortByArtist(){
        songs.sort(new SongTitleComparator());
        songs.sort(new SongArtistComparator());
    }
    
    public static void sortBySelected(){
        songs.sort(new SongTitleComparator());
        songs.sort(new SongSelectedComparator());
    }
    
    public static void createWindow(){
        window = new JFrame("Edit Music Playlist");
        
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItemSave, menuItemSortTitle, menuItemSortArtist, menuItemSortSelected;
        menuBar = new JMenuBar();

        menu = new JMenu("Azioni");
        menuBar.add(menu);
        
        menuItemSave = new JMenuItem("Salva");
        menuItemSave.setName("Save");
        menuItemSave.addActionListener(new MyMenuActionListener(songs));
        menuItemSortTitle = new JMenuItem("Ordina per Titolo");
        menuItemSortTitle.setName("SortTitle");
        menuItemSortTitle.addActionListener(new MyMenuActionListener(songs));
        menuItemSortArtist = new JMenuItem("Ordina per Artista");
        menuItemSortArtist.setName("SortArtist");
        menuItemSortArtist.addActionListener(new MyMenuActionListener(songs));
        menuItemSortSelected = new JMenuItem("Ordina per Selezionati");
        menuItemSortSelected.setName("SortSelected");
        menuItemSortSelected.addActionListener(new MyMenuActionListener(songs));
        
        menu.add(menuItemSave);
        menu.add(menuItemSortTitle);
        menu.add(menuItemSortArtist);
        menu.add(menuItemSortSelected);
        
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(0,15));
        containerPanel.setPreferredSize(new Dimension(1900, (songs.size()/15+1)*135));
        ArrayList<JPanel> panels = new ArrayList();
        
        for(Song s : songs){
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            
            ImageIcon i = null;
            try {
                i = new ImageIcon(ImageIO.read(new File("cache\\albumArts\\"+s.getAlbum()+".jpg")));
            } catch (IOException ex) {
                Logger.getLogger(PlaylistEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            JLabel cover = new JLabel();
            
            if(s.isSelected()){
                cover.setName("cover");
            }else{
                i.setImage(setImageOpacity(i, 0.05f));
                cover.setName("cover_op");
            }
            
            cover.setIcon(i);
            
            JLabel title = new JLabel(s.getTitle());
            title.setName("title");
            JLabel artist = new JLabel(s.getArtist());
            artist.setName("artist");
            title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 14));
            artist.setFont(new Font(artist.getFont().getName(), Font.PLAIN, 11));
            p.setName(s.getFilePath());
            p.add(cover);
            p.add(title);
            p.add(artist);
            p.addMouseListener(new MyMouseListener(songs));
            containerPanel.add(p);
        }
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(containerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        window.setJMenuBar(menuBar);
        window.add(scrollPane);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    
    public static void createInsertPlaylistNameWindow(){
        JFrame window = new JFrame("Insert Playlist Name");
        
        JTextField textField = new JTextField();
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar() == '\n'){
                    playListName = textField.getText();
                    semaphore.release();
                    window.dispose();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        
        window.add(textField);
        window.setSize(new Dimension(300, 60));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public static void recreateWindow() {
        window.dispose();
        createWindow();
    }
}
