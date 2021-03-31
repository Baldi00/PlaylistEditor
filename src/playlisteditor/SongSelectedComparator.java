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
public class SongSelectedComparator implements Comparator<Song>{
    @Override
    public int compare(Song s1, Song s2) {
        if((s1.isSelected() && s2.isSelected()) || (!s1.isSelected() && !s2.isSelected()))
            return 0;
        if(s1.isSelected() && !s2.isSelected())
            return -1;
        return 1;
    }
}
