import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        File src = new File("C://Games/src");
        File res = new File("C://Games/res");
        File savegames = new File("C://Games/savegames");
        File temp = new File("C://Games/temp");
        File main = new File("C://Games/src/main");
        File test = new File("C://Games/src/test");
        File mainFile = new File("C://Games/src/main//Main.java");
        File utilsFile = new File("C://Games/src/main//Utils.java");
        File drawables = new File("C://Games/res/drawables");
        File vectors = new File("C://Games/res/vectors");
        File icons = new File("C://Games/res/icons");

        StringBuilder result = new StringBuilder();
        result.append("Каталог src создан: " + src.mkdir());
        result.append("\nКаталог res создан: " + res.mkdir());
        result.append("\nКаталог savegames создан: " + savegames.mkdir());
        result.append("\nКаталог temp создан: " + temp.mkdir());
        result.append("\nКаталог main создан: " + main.mkdir());
        result.append("\nКаталог test создан: " + test.mkdir());

        try {
            result.append("\nФайл Main.java создан: " + mainFile.createNewFile());
        } catch (IOException e) {
            result.append("\nФайл Main.java: " + e.getMessage());
        }
        try {
            result.append("\nФайл Utils.java создан: " + utilsFile.createNewFile());
        } catch (IOException e) {
            result.append("\nФайл Utils.java: " + e.getMessage());
        }

        result.append("\nКаталог drawables создан: " + drawables.mkdir());
        result.append("\nКаталог vectors создан: " + vectors.mkdir());
        result.append("\nКаталог icons создан: " + icons.mkdir());

        try (FileWriter writer = new FileWriter("C://Games/temp//temp.txt")) {
            writer.write(String.valueOf(result));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameProgress gameProgress1 = new GameProgress(100, 12, 30, 12);
        GameProgress gameProgress2 = new GameProgress(70, 31, 45, 37);
        GameProgress gameProgress3 = new GameProgress(48, 77, 55, 54);
        saveGame("C://Games/savegames/save1.data", gameProgress1);
        saveGame("C://Games/savegames/save2.data", gameProgress2);
        saveGame("C://Games/savegames/save3.data", gameProgress3);
        List<String> pathFiles = new ArrayList<>();
        pathFiles.add("C://Games/savegames/save1.data");
        pathFiles.add("C://Games/savegames/save2.data");
        pathFiles.add("C://Games/savegames/save3.data");
        zipFiles("C://Games/savegames/saveGames.zip", pathFiles);
        openZip("C://Games/savegames/saveGames.zip", "C://Games/savegames/");
        GameProgress gameProgress = openProgress("C://Games/savegames/save1.data");
        System.out.println(gameProgress);

    }

    static public void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gameProgress);
            oos.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static public void zipFiles(String path, List<String> pathFiles) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String file : pathFiles) {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(new File(file).getName());
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
                File fileDelete = new File(file);
                fileDelete.delete();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static public void openZip(String pathZip, String path) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(path + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static public GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gameProgress;
    }
}