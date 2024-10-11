import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlayer 
{
    private static List<File> songs = new LinkedList<>();
    private static int currentSongIndex = 0;
    private static Clip clip;
    private static long pausePosition;
    private static List<File> playlist = new ArrayList<>();
    public static void main(String[] args) 
    {
        File musicDir = new File("C:\\Users\\Archana\\OneDrive\\Desktop\\Music\\MusicSong"); 
        for (File file : musicDir.listFiles()) 
        {
            if (file.getName().endsWith(".wav")) 
            {
                songs.add(file);
            }
        }

        Scanner scanner = new Scanner(System.in);
        int choice;

        do 
        {
            System.out.println("1. Show All Songs");
            System.out.println("2. Search Songs");
            System.out.println("3. Search Artist's Songs");
            System.out.println("4. Show my playlist");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) 
            {
                case 1:
                {
                    selectSong(scanner);
                    break;
                }
                case 2:
                {
                    System.out.println("Enter the name of the song you want to search for: ");
                    String songName = scanner.next();
                    SongSearch(songName);
                    break;
                }
                case 3:
                {
                    System.out.println("Enter the name of the artist you want to search for: ");
                    String artistName = scanner.next();
                    searchArtistsSongs(artistName);
                    break;
                }
                case 4:
                {
                    showPlaylist();
                    break;
                }
                case 5:
                {
                    stopMusic();
                    System.out.println("Exiting music player.");
                    break;
                }
                default:
                {
                    System.out.println("Invalid choice! Please choose a valid option.");
                    break;
                }
            }
        } while (choice != 5);
    }

    public static void selectSong(Scanner scanner) 
    {
        System.out.println("Select a song to play:");

        for (int i = 0; i < songs.size(); i++) 
        {
            System.out.println((i + 1) + ". " + songs.get(i).getName());
        }

        int choice;

        do 
        {
            System.out.print("Enter the number of the song you want to play (or 0 to cancel): ");
            choice = scanner.nextInt();
    
            if (choice >= 1 && choice <= songs.size()) 
            {
                currentSongIndex = choice - 1;
                playMusic();
                break;
            } 
            else if (choice == 0) 
            {
                System.out.println("Selection canceled.");
                break;
            } 
            else 
            {
                System.out.println("Invalid choice! Please enter a valid number or 0 to cancel.");
            }

        } while (true);
    }
    

    public static void SongSearch(String keyword) 
    {
        File musicDir = new File("C:\\Users\\Archana\\OneDrive\\Desktop\\Music\\MusicSong");
        List<File> matchingFiles = new ArrayList<>();

        if (musicDir.isDirectory()) 
        {
            String keywordLower = keyword.toLowerCase();
            for (File file : musicDir.listFiles()) 
            {
                String fileNameWithoutExtension = file.getName().toLowerCase().replace(".wav", "");

                // Check if either the artist or song name contains the keyword
                if (fileNameWithoutExtension.contains(keywordLower)) 
                {
                    matchingFiles.add(file);
                }
            }
        }

        if (!matchingFiles.isEmpty()) 
        {
            System.out.println("Found " + matchingFiles.size() + " matching songs:");

            // Display only matching songs
            for (int i = 0; i < matchingFiles.size(); i++) 
            {
                System.out.println((i + 1) + ". " + matchingFiles.get(i).getName());
            }

            int choice;
            do 
            {
                System.out.print("Enter the number of the song you want to play (or 0 to cancel): ");
                Scanner scanner1=new Scanner(System.in);
                choice = scanner1.nextInt();

                if (choice >= 1 && choice <= matchingFiles.size()) 
                {
                    currentSongIndex = songs.indexOf(matchingFiles.get(choice - 1));
                    playMusic(); // Play the selected song
                    break;
                } 
                else if (choice == 0) 
                {
                    System.out.println("Search canceled.");
                    return; // Exit the SongSearch method
                } 
                else 
                {
                    System.out.println("Invalid choice! Please enter a valid number or 0 to cancel.");
                }

            } while (true);
        }
        else 
        {
            System.out.println("No matching songs found for keyword: " + keyword);
        }
    }

    public static void searchArtistsSongs(String artistName) 
    {
      
        File musicDir = new File("C:\\Users\\Archana\\OneDrive\\Desktop\\Music\\MusicSong");
        List<File> matchingFiles = new ArrayList<>();
    
        if (musicDir.isDirectory()) 
        {
            String artistNameLower = artistName.toLowerCase();

            for (File file : musicDir.listFiles()) 
            {
                String fileNameWithoutExtension = file.getName().toLowerCase().replace(".wav", "");
                String[] fileNameParts = fileNameWithoutExtension.split(" - ");
    
                // Check if the artist name contains the keyword
                if (fileNameParts[0].contains(artistNameLower)) 
                {
                    matchingFiles.add(file);
                }
            }
        }
    
        if (!matchingFiles.isEmpty()) 
        {
            System.out.println("Found " + matchingFiles.size() + " matching song(s) for artist " + artistName + ":");
    
            // Display only matching songs
            for (int i = 0; i < matchingFiles.size(); i++) 
            {
                System.out.println((i + 1) + ". " + matchingFiles.get(i).getName());
            }
    
            int choice;

            do 
            {
                System.out.print("Enter the number of the song you want to play (or 0 to cancel): ");
                Scanner scanner1=new Scanner(System.in);
                choice = scanner1.nextInt();
    
                if (choice >= 1 && choice <= matchingFiles.size()) 
                {
                    currentSongIndex = songs.indexOf(matchingFiles.get(choice - 1));
                    playMusic(); // Play the selected song
                    break;
                } 
                else if (choice == 0) 
                {
                    System.out.println("Search canceled.");
                    return; // Exit the searchArtistsSongs method
                } 
                else 
                {
                    System.out.println("Invalid choice! Please enter a valid number or 0 to cancel.");
                }

            } while (true);

        } 
        else 
        {
            System.out.println("No songs found for artist: " + artistName);
        }
    }
    
    public static void playMusic() 
    {
       
    try 
    {
        javax.sound.sampled.AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(songs.get(currentSongIndex));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

        Scanner scanner=new Scanner(System.in);
        int choice;
        
        while (true) 
        {
            System.out.println("1. Pause");
            System.out.println("2. Resume");   
            System.out.println("3. Add to my playlist");
            System.out.println("4. Next Song");
            System.out.println("5. Previous Song");      
            System.out.println("6. Stop and Return to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) 
            {
                case 1:
                {
                    if (clip.isRunning()) 
                    {
                        pausePosition = clip.getMicrosecondPosition();
                        clip.stop();
                    }
                    break;
                }
                case 2:
                {
                    if (!clip.isRunning()) 
                    {
                        clip.setMicrosecondPosition(pausePosition);
                        clip.start();
                    }
                    break;
                }
                case 3:
                {
                    addToPlaylist(currentSongIndex);
                    break;
                }
              
                case 4:
                {
                    // Stop the current song and play the next song
                    if (clip != null && clip.isOpen()) 
                    {
                        clip.close();
                    }
                    currentSongIndex = (currentSongIndex + 1) % songs.size();
                    audioInputStream = AudioSystem.getAudioInputStream(songs.get(currentSongIndex));
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                }
                case 5:
                {
                    // Stop the current song and play the previous song
                    if (clip != null && clip.isOpen()) 
                    {
                        clip.close();
                    }
                    currentSongIndex = (currentSongIndex - 1 + songs.size()) % songs.size();
                    audioInputStream = AudioSystem.getAudioInputStream(songs.get(currentSongIndex));
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    break;
                }
                case 6:
                {
                    clip.stop();
                    return; // Return to the main menu
                }
                default:
                {
                    System.out.println("Invalid choice! Please choose a valid option.");
                }
            }
        } 

    } 
    catch (Exception e) 
    {
        e.printStackTrace();
    }


    }
    
    public static void addToPlaylist(int songIndex) 
    {
        if (!playlist.contains(songs.get(songIndex))) 
        {
            playlist.add(songs.get(songIndex));
            System.out.println("Song added to playlist.");
        } 
        else 
        {
            System.out.println("Song is already in the playlist.");
        }
    }

    public static void showPlaylist() 
    {
        if (!playlist.isEmpty()) 
        {
            System.out.println("Your playlist:");
            for (int i = 0; i < playlist.size(); i++) 
            {
                System.out.println((i + 1) + ". " + playlist.get(i).getName());
            }
    
            int choice;

            do 
            {
                System.out.println("Enter the number of the song you want to remove (or 0 to return to main menu): ");
                Scanner scanner=new Scanner(System.in);
                choice = scanner.nextInt();
    
                if (choice >= 1 && choice <= playlist.size()) 
                {
                    System.out.println("Removed " + playlist.get(choice - 1).getName() + " from the playlist.");
                    playlist.remove(choice - 1);
                    break;
                } 
                else if (choice == 0) 
                {
                    System.out.println("Returning to main menu.");
                    break;
                } 
                else 
                {
                    System.out.println("Invalid choice! Please enter a valid number or 0 to return to main menu.");
                }
            } while (true);

        } 
        else 
        {
            System.out.println("Your playlist is empty.");
        }
    }
    
    public static void stopMusic() 
    {
        if (clip != null && clip.isRunning()) 
        {
            clip.stop();
        }
    }
}