package domain;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

//I guess we need to resubscribe listeners after loading game state. !!!!!!1
//Hashmap.keyset() , Hashmap.values() kullanılabilir.

public class GameSaveLoader {
    
    private File[] savedFiles;

    public static GameSaveLoader instance;

    public static GameSaveLoader getInstance() {
        if (instance == null) {
            instance = new GameSaveLoader();
        }
        return instance;
    }
    
    private GameSaveLoader() {
    }

    public static void saveGame() {
        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        Game game = Game.getInstance();

        File saveDirectory = new File("gameSaves");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs(); // Create the directory if it doesn't exist
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(("gameSaves/gameSave" + date +".dat")));

            oos.writeObject(game);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public void loadGame (int index) {
            File savedFile = savedFiles[index];
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedFile))) {
                Game loadedGame = (Game) ois.readObject();
                System.out.println("Game loaded successfully from " + savedFile.getName());
                Game.initLoadedGame(loadedGame);

                // Use the loaded game object as needed
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load game from " + savedFile.getName());
                e.printStackTrace();
            }

        }

        public int readGameSaves() {
            File gameSavesDir = new File("gameSaves"); // Directory path
            // Check if the directory exists and is a valid directory
            if (gameSavesDir.exists() && gameSavesDir.isDirectory()) {
                // Get all the files in the directory
                savedFiles = gameSavesDir.listFiles();

                // Check if the directory is empty
                if (savedFiles != null) {
                    return savedFiles.length;
                }
            }
            return 0;
        }
}



