import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ConnectToDatabase {
    private static final String path = "C:\\JsonFiles\\";
    public String message;
    Socket socket;




    public String connectoDatabase(String[] queryArray) throws IOException {
        for (int i = 0; i < queryArray.length; i++) {
            System.out.println("\n" + queryArray[i]);
        }

        File f = new File(path + queryArray[2].toLowerCase());
        if (f.exists()) {

            return "connection" + " " + "SUCCESS".toUpperCase() + " " + queryArray[2];

        } else {
            return "databaseDoNotExist" + " " + "fail".toUpperCase();
        }
    }


}
