package sl.app;

// Amit Patel and Hideyo Sakamoto

public class Song {
	private String name;
	private String artist;
	private String album;
	private String year;
	
	public Song() {
		this.name=""; this.artist="";
		this.album=""; this.year="";
	}
	
	public Song(String name, String artist, String album, String year) {
		this.name=name; this.artist=artist; 
		this.album=album; this.year=year;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public void setArtist(String artist) {
		this.artist=artist;
	}
	
	public String getAlbum() {
		return this.album;
	}
	
	public void setAlbum(String album) {
		this.album=album;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public void setYear(String year) {
		this.year=year;
	}
	
	public String toString() {
		return "Song Name: " + this.name + " - Artist: " + this.artist;	
	}
	
}