package sl.app;

import java.util.Comparator;

// Amit Patel and Hideyo Sakamoto

public class SongSorter implements Comparator<Song>{

	@Override
	public int compare(Song first, Song second) {
		if (first.getName().compareToIgnoreCase(second.getName()) != 0)
		{
			return first.getName().compareToIgnoreCase(second.getName());
		}
		
		return first.getArtist().compareToIgnoreCase(second.getArtist());
	}

}
