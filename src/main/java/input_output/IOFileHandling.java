package input_output;

import model.User;

import javax.swing.*;
import java.io.*;
import java.util.Set;

public class IOFileHandling {

    private final static String USERS_SER = "IOFiles/users.ser";

    public static void saveUsersSet(Set<User> usersSet) {
        try{
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(USERS_SER));
            os.writeObject(usersSet);
            os.close();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Error when saving data base file", "ACHTUNG!",
                    JOptionPane.DEFAULT_OPTION);
        }
    }

    public static Set<User> loadUsersSet() {
        Set<User> usersSet = null;
        ObjectInputStream is;
        try {
            is = new ObjectInputStream(new FileInputStream(USERS_SER));
            usersSet = (Set<User>) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showConfirmDialog(null, "Error when reading data base file", "ACHTUNG!",
                    JOptionPane.DEFAULT_OPTION);
        }
        return usersSet;
    }

    public static String readFromFile(String fileName) {
        String interLine, finalLine = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((interLine = reader.readLine()) != null) {
                finalLine = finalLine + " " + interLine;
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "File does not exist", "ACHTUNG!",
                    JOptionPane.DEFAULT_OPTION);
        }
        return finalLine;
    }

    public static void writeToFile(String outputLine, String fileName){
        try {
            if (fileName != null) {
                FileWriter writer = new FileWriter(fileName);
                writer.write(outputLine);
                writer.close();
            }
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Error when writing text to a file", "ACHTUNG!",
                    JOptionPane.DEFAULT_OPTION);
        }
    }
}
