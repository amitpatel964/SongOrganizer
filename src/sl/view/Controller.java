package sl.view;
import java.net.URL;

import java.util.ResourceBundle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import sl.app.Song;
import sl.app.SongLibrary;

// Amit Patel and Hideyo Sakamoto

public class Controller {

	SongLibrary library = new SongLibrary();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Song> songList = new ListView<>();

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField songText;

    @FXML
    private TextField albumText;

    @FXML
    private TextField artistText;

    @FXML
    private TextField yearText;
    
    @FXML
    private TextField conform;
    
    @FXML
    private AnchorPane myAnchorPane;
    
 // Button clicked
    @FXML
    void addEditDeleteProgram(ActionEvent event) {
    	
    	Stage stage = (Stage) myAnchorPane.getScene().getWindow();
    	Alert.AlertType type = Alert.AlertType.CONFIRMATION;
    	Alert alert = new Alert(type, "");
    	
    	// check if add is clicked
    	if(event.getSource() == addButton) {
    		
    		alert.initModality(Modality.APPLICATION_MODAL);
    		alert.initOwner(stage);
    		alert.getDialogPane().setContentText("Are you sure you want to add?");
    		alert.getDialogPane().setHeaderText("Confirmation");
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		//confirmation yes
    		if(result.get() == ButtonType.OK)
    		{
    			// getting text from text fields
        		String name = songText.getText().trim();
        		String artist = artistText.getText().trim();
        		String album = albumText.getText().trim();
        		String year = yearText.getText().trim();
        		
        		if (name.contains("|") || artist.contains("|") || album.contains("|"))
        		{
            		conform.setText("Invalid entry. No | is allowed");
            		return;
        		}
        		
        		// At least name and artist should be entered
        		if(name.isEmpty() || artist.isEmpty()) {
        			songText.setText("**Required field**");
        			artistText.setText("**Required field**");
        			return;
        		}
        		
        		// If album or year fields are empty, they are set to an empty string
        		if (album.equals(null))
        		{
        			album = "";
        		}
        		
        		if (year.equals(null) || year.equals(""))
        		{
        			year = "";
        		}
        		else
        		{
        			// Year should only consist of digits and be greater than zero.
        			try {
        				int yearCheck = Integer.parseInt(year);
        				
        				if (yearCheck <= 0)
        				{
        					conform.setText("Invalid year: Year should be greater than zero");
        					return;
        				}
        			}
        			catch(NumberFormatException e)
        			{
        				conform.setText("Invalid year: Year should only have digits");
        				return;
        			}
        		}
        		
        		// Make a song with given texts
            	Song songToAdd = new Song(name, artist, album, year);
        		
            	if(library.isDuplicate(songToAdd)) {
            		
            		conform.setText("Duplicate song & artist");
            	}else {
            		
            		library.add(songToAdd);
            		
                	// adding to listview
                	songList.getItems().clear();
                	ArrayList<Song> currentSongs = library.getLibrary();
                		
                	for(int i = 0; i < currentSongs.size(); i++)
                	{
                		songList.getItems().add(currentSongs.get(i));
                	}
                	
                	//Save current list of songs to text file
                	saveFile(library);
                	
                	//Highlight added song
                	for(int i = 0; i < currentSongs.size(); i++)
                	{
                		if (songToAdd.getName().equals(currentSongs.get(i).getName()) 
                	    	&& songToAdd.getArtist().equals(currentSongs.get(i).getArtist()))
                	    {
                	    	songList.getSelectionModel().select(i);
                	    	break;
                	    }
                	}
                	    
                	conform.setText("Name: " + songToAdd.getName() + " - Artist: " + songToAdd.getArtist() +
        						" - Album: " + songToAdd.getAlbum() + " - Year: " + songToAdd.getYear());
                	
            		songText.setText(songToAdd.getName());
            		artistText.setText(songToAdd.getArtist());
            		albumText.setText(songToAdd.getAlbum());
            		yearText.setText(songToAdd.getYear());
            	}
    		}
    		// Confirmation no
    		if(result.get() == ButtonType.CANCEL)
    		{
    			//Do nothing
    		}
    		
    		// when edit is clicked
    	}else if(event.getSource() == editButton) {
    		alert.initModality(Modality.APPLICATION_MODAL);
    		alert.initOwner(stage);
    		alert.getDialogPane().setContentText("Are you sure you want to edit?");
    		alert.getDialogPane().setHeaderText("Confirmation");
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		
    		//confirmation yes
    		if(result.get() == ButtonType.OK)
    		{
    			// Song from listView
    			Song song1 = songList.getSelectionModel().getSelectedItem();
    			ArrayList<Song> currentSongs = library.getLibrary();
    			
    			// If there are no songs in the playlist, display message in the detail window and stop the process
    			if(currentSongs.size() == 0)
    			{
    				conform.setText("No songs currently in playlist");
    				return;
    			}
    			
    			// Index is utilized to see if the user's inputs matches any songs other than the one they are editing.
    			// Without this, the program will always say there is a duplicate song since it would compare the song against itself.
    			int index = 0;
        	    for(int i = 0; i < currentSongs.size(); i++)
        	    {
        	    	if (song1.getName().equals(currentSongs.get(i).getName()) 
        	    			&& song1.getArtist().equals(currentSongs.get(i).getArtist()))
        	    	{
        	    		index = i;
        	    		break;
        	    	}
        	    }
    			
    			// getting text from text fields
        		String name = songText.getText().trim();
        		String artist = artistText.getText().trim();
        		String album = albumText.getText().trim();
        		String year = yearText.getText().trim();
        		
        		// There should be no vertical bars in name, artist or album.
        		if (name.contains("|") || artist.contains("|") || album.contains("|"))
        		{
            		conform.setText("Invalid entry. No | is allowed");
            		return;
        		}
        		
        		// At least name and artist should be entered
        		if(name.isEmpty() || artist.isEmpty()) {
        			songText.setText("**Required field**");
        			artistText.setText("**Required field**");
        			return;
        		}
        		
        		// If album or year fields are empty, they are set to an empty string rather than null.
        		if (album.equals(null))
        		{
        			album = "";
        		}
        		
        		if (year.equals(null) || year.equals(""))
        		{
        			year = "";
        		}
        		else
        		{
        			// Year should only have digits and is greater than zero.
        			try {
        				int yearCheck = Integer.parseInt(year);
        				
        				if (yearCheck <= 0)
        				{
        					conform.setText("Invalid year: Year should be greater than zero");
        					return;
        				}
        			}
        			catch(NumberFormatException e)
        			{
        				conform.setText("Invalid year: Year should only have digits");
        				return;
        			}
        		}
        		
        		Song song2 = new Song(name, artist, album, year);
        		
        		if(library.isDuplicateEdit(song2, index)) {
        			conform.setText("Duplicate song & artist");
        		}else {
        			// delete song1 and add song 2 in the same index
        			library.replace(song1, song2);
            		// adding to listview and sorting library
            		library.sortLibrary();
            		
            		songList.getItems().clear();
            		
            		currentSongs = library.getLibrary();
            		
            	    for(int i = 0; i < currentSongs.size(); i++)
            	    {
            			songList.getItems().add(currentSongs.get(i));
            	    }
            	    
            	    // Save current list of songs to the text file
            	    saveFile(library);
            	    
            	    // Highlight the song
            	    for(int i = 0; i < currentSongs.size(); i++)
            	    {
            	    	if (song2.getName().equals(currentSongs.get(i).getName()) 
            	    			&& song2.getArtist().equals(currentSongs.get(i).getArtist()))
            	    	{
            	    		songList.getSelectionModel().select(i);
            	    		break;
            	    	}
            	    }
            	    
            	    // Put song details into the detail window
            		conform.setText("Name: " + song2.getName() + " Artist: " + song2.getArtist() +
    						" - Album: " + song2.getAlbum() + " - Year: " + song2.getYear());
            		
            		// Put song info into the input fields
            		songText.setText(song2.getName());
            		artistText.setText(song2.getArtist());
            		albumText.setText(song2.getAlbum());
            		yearText.setText(song2.getYear());
        		}
    		}
    		// Confirmation no
    		if(result.get() == ButtonType.CANCEL)
    		{
    			// Do nothing
    		}
    		
    		// when delete is clicked
    	}else if(event.getSource() == deleteButton) {
    		alert.initModality(Modality.APPLICATION_MODAL);
    		alert.initOwner(stage);
    		alert.getDialogPane().setContentText("Are you sure you want to delete?");
    		alert.getDialogPane().setHeaderText("Confirmation");
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		// Ok is clicked
    		if(result.get() == ButtonType.OK)
    		{
    			ArrayList<Song> currentSongsInLibrary = library.getLibrary();
    			
    			// If there are no songs in the playlist, display message in the detail window and stop the process
    			if(currentSongsInLibrary.size() == 0)
    			{
    				conform.setText("No songs currently in playlist");
    				return;
    			}
    			
    			Song songToDelete = songList.getSelectionModel().getSelectedItem();
    			
    			int selectedIdx = songList.getSelectionModel().getSelectedIndex();
        		songList.getItems().remove(selectedIdx);
        		
        	    Song songToHighlight = null;
        	    
        	    // Find which song to highlight after deleting the selected song.
        	    // If there is none, nothing will be highlighted and the detail window will be empty.
        	    for(int i = 0; i < currentSongsInLibrary.size(); i++)
        	    {
        	    	if (songToDelete.getName().equals(currentSongsInLibrary.get(i).getName()) 
        	    			&& songToDelete.getArtist().equals(currentSongsInLibrary.get(i).getArtist()))
        	    	{
        	    		if (currentSongsInLibrary.size() == 1)
        	    		{
        	    			break;
        	    		}
        	    		if (i == 0)
        	    		{
            	    		songList.getSelectionModel().select(0);
            	    		songToHighlight = currentSongsInLibrary.get(1);
            	    		break;
        	    		}
        	    		songList.getSelectionModel().select(i-1);
        	    		songToHighlight = currentSongsInLibrary.get(i-1);
        	    		break;
        	    	}
        	    }
        	    
        	    if (currentSongsInLibrary.size() > 1)
        	    {
            		conform.setText("Name: " + songToHighlight.getName() + " - Artist: " + songToHighlight.getArtist() +
        					" - Album: " + songToHighlight.getAlbum() + " - Year: " + songToHighlight.getYear());
            		
            		songText.setText(songToHighlight.getName());
            		artistText.setText(songToHighlight.getArtist());
            		albumText.setText(songToHighlight.getAlbum());
            		yearText.setText(songToHighlight.getYear());
            		
        	    }
        	    else
        	    {
        	    	// If there are no songs left after deletion, all input fields and the detail window are set to be empty
        	    	conform.setText("");
            		songText.setText("");
            		artistText.setText("");
            		albumText.setText("");
            		yearText.setText("");
        	    }
        		
        		library.deleteSong(songToDelete);
        		
        		// Save current list of songs to the text file
        		saveFile(library);
    		}
    		// Confirmation no
    		if(result.get() == ButtonType.CANCEL)
    		{
    			// Do nothing
    		}
    	}
    }

    @FXML
    void songListProgram(MouseEvent event) {
    	
    	// Highlight selected song and put song details into input fields and details window
    	if(songList.getSelectionModel().getSelectedIndex()>=0) {
    		Song song = (Song) songList.getSelectionModel().getSelectedItem();
    		
    		
    		songText.setText(song.getName());
    		artistText.setText(song.getArtist());
    		albumText.setText(song.getAlbum());
    		yearText.setText(song.getYear());
    		
    		conform.setText("Name: " + song.getName() + " - Artist: " + song.getArtist() +
    						" - Album: " + song.getAlbum() + " - Year: " + song.getYear());
    	}
    }
    
    private void saveFile(SongLibrary library)
    {
    	//After a song is added or deleted, the text tile will be saved to keep a record of the changes.
    	
    	PrintWriter resetFile = null;
    	
    	try {
			resetFile = new PrintWriter(System.getProperty("user.dir") + "/save.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	resetFile.close();
    	
    	BufferedWriter allSongs = null;
    	
        try {
			allSongs = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/save.txt", true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ArrayList<Song> currentSongs = library.getLibrary();
        
        // There are four ways to write a song into the text file depending on which fields it has
        // The four ways are: Name, Artist, Album, Year - Name, Artist, Album - Name, Arist, Year - Name, Artist
        // The categories are separate by a string and the string used depends on which categories the song has
        for(int i = 0; i < currentSongs.size(); i++)
        {
        	Song currentSongInArrayList = currentSongs.get(i);
        	
            String lineToAddToFile = null;
            
    		if (!currentSongInArrayList.getAlbum().equals("") && !currentSongInArrayList.getYear().equals(""))
    		{
    			lineToAddToFile = currentSongInArrayList.getName()+"-uallo-uallo-"+currentSongInArrayList.getArtist()
    					+"-uallo-uallo-"+currentSongInArrayList.getAlbum()+"-uallo-uallo-"+currentSongInArrayList.getYear();
    		}
    		else if (!currentSongInArrayList.getAlbum().equals(""))
    		{
    			lineToAddToFile = currentSongInArrayList.getName()+"-ualbumo-ualbumo-"+currentSongInArrayList.getArtist()
				+"-ualbumo-ualbumo-"+currentSongInArrayList.getAlbum();
    		}
    		else if (!currentSongInArrayList.getYear().equals(""))
    		{
    			lineToAddToFile = currentSongInArrayList.getName()+"-uyearo-uyearo-"+currentSongInArrayList.getArtist()
				+"-uyearo-uyearo-"+currentSongInArrayList.getYear();
    		}
    		else
    		{
    			lineToAddToFile = currentSongInArrayList.getName()+"-uo-uo-"+currentSongInArrayList.getArtist();
    		}
            
            try {
            	allSongs.newLine();
    			allSongs.append(lineToAddToFile);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
        
        try {
			allSongs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void initialize(){
        assert songList != null : "fx:id=\"songList\" was not injected: check your FXML file 'fx.fxml'.";
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'fx.fxml'.";
        assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'fx.fxml'.";
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'fx.fxml'.";
        assert songText != null : "fx:id=\"songText\" was not injected: check your FXML file 'fx.fxml'.";
        assert albumText != null : "fx:id=\"albumText\" was not injected: check your FXML file 'fx.fxml'.";
        assert artistText != null : "fx:id=\"artistText\" was not injected: check your FXML file 'fx.fxml'.";
        assert yearText != null : "fx:id=\"yearText\" was not injected: check your FXML file 'fx.fxml'.";
        
        File saveFile = new File(System.getProperty("user.dir") + "/save.txt");
        Scanner scanner = null;
		try {
			// If the text file is deleted, a new one will be created.
			// If there is one already, the program will just use that one.
			saveFile.createNewFile();
			scanner = new Scanner(saveFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		// Begin reading the songs into the library. The line is split depending on which string separates the categories
		// This allows the program to determine which fields are present for the song
        while (scanner.hasNextLine())
        {
        	String nextLine = scanner.nextLine();
        	String[] songInfo = new String[4];
        	Song currentSong = null;
        	
        	if (nextLine.equals(""))
        	{
        		continue;
        	}
        	
        	if (nextLine.contains("-uallo-uallo-"))
        	{
        		String[] songInfoForThisSong = nextLine.split("-uallo-uallo-");
        		
        		songInfo[0] = songInfoForThisSong[0];
        		songInfo[1] = songInfoForThisSong[1];
        		songInfo[2] = songInfoForThisSong[2];
        		songInfo[3] = songInfoForThisSong[3];
        	}
        	else if (nextLine.contains("-ualbumo-ualbumo-"))
        	{
        		String[] songInfoForThisSong = nextLine.split("-ualbumo-ualbumo-");
        		
        		songInfo[0] = songInfoForThisSong[0];
        		songInfo[1] = songInfoForThisSong[1];
        		songInfo[2] = songInfoForThisSong[2];
        		songInfo[3] = "";
        	}
        	else if (nextLine.contains("-uyearo-uyearo-"))
        	{
        		String[] songInfoForThisSong = nextLine.split("-uyearo-uyearo-");
        		
        		songInfo[0] = songInfoForThisSong[0];
        		songInfo[1] = songInfoForThisSong[1];
        		songInfo[2] = "";
        		songInfo[3] = songInfoForThisSong[2];
        	}
        	else if (nextLine.contains("-uo-uo-"))
        	{
        		String[] songInfoForThisSong = nextLine.split("-uo-uo-");
        		
        		songInfo[0] = songInfoForThisSong[0];
        		songInfo[1] = songInfoForThisSong[1];
        		songInfo[2] = "";
        		songInfo[3] = "";
        	}
        	else
        	{
        		continue;
        	}
        	
        	currentSong = new Song(songInfo[0], songInfo[1], songInfo[2], songInfo[3]);
        	
        	library.add(currentSong);
        }
        
        // After all of the songs are added, put them into the listview and highlight the first one
        // If there are no songs, nothing will be highlighted
        
        ArrayList<Song> songsAdded = library.getLibrary();
        
        for(int i = 0; i < songsAdded.size(); i++)
        {
    		songList.getItems().add(songsAdded.get(i));
        }
		
		if (songsAdded.size() > 0)
		{
	        songList.getSelectionModel().select(0);
	        
			Song song = (Song) songList.getSelectionModel().getSelectedItem();
			
			conform.setText("Name: " + song.getName() + " - Artist: " + song.getArtist() +
					" - Album: " + song.getAlbum() + " - Year: " + song.getYear());
			
    		songText.setText(song.getName());
    		artistText.setText(song.getArtist());
    		albumText.setText(song.getAlbum());
    		yearText.setText(song.getYear());
		}
		
		
		
    }
}