/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playlisteditor;

import java.util.Comparator;

/**
 *
 * @author Andrea
 */
public class SongArtistComparator implements Comparator<Song>{
    @Override
    public int compare(Song s1, Song s2) {
       return s1.getArtist().compareTo(s2.getArtist());
    }
}
