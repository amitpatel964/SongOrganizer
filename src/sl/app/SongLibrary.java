package sl.app;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Amit Patel and Hideyo Sakamoto

public class SongLibrary{
	List<Song> library;
	ArrayList<Song> libraryToCall;
	
	public SongLibrary() {
		library = new ArrayList<Song>();
		libraryToCall = new ArrayList<Song>();
	}
	
	public void add(Song song) {
		library.add(song);
		sortLibrary();
	}
	
	public void sortLibrary()
	{
		Collections.sort(library, new SongSorter());
		
		libraryToCall.clear();
		for(int i = 0; i < library.size(); i++)
		{
			libraryToCall.add(library.get(i));
		}
	}
	
	public void replace(Song song1, Song song2) {
		for(int i =0; i<library.size(); i++) {
			if(library.get(i).getName().equals(song1.getName()) &&
					library.get(i).getArtist().equals(song1.getArtist())) {
				
				library.set(i, song2);
			}
		}
	}
	
	public boolean isDuplicate(Song song) {
		for(Song s: library) {
			if( (s.getName().equalsIgnoreCase(song.getName()))  && (s.getArtist().equalsIgnoreCase(song.getArtist())) ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDuplicateEdit(Song song, int index) {
		int counter = 0;
		
		for(Song s: library) {
			if (counter == index)
			{
				continue;
			}
			if( (s.getName().equalsIgnoreCase(song.getName()))  && (s.getArtist().equalsIgnoreCase(song.getArtist())) ) {
				return true;
			}
			counter++;
		}
		return false;
	}
	
	public void deleteSong(Song song) {
		if(isDuplicate(song)) {
			library.remove(song);
			libraryToCall.remove(song);
		}
	}
	
	public ArrayList<Song> getLibrary()
	{
		return this.libraryToCall;
	}
	
}
