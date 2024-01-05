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
public class SongLastModifiedComparator implements Comparator<Song>{
    @Override
    public int compare(Song s1, Song s2) {
       return -1 * Long.compare(s1.getLastModified(), s2.getLastModified());
    }
}
