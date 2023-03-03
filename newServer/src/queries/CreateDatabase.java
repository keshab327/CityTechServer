package queries;

import java.io.File;
import java.net.Socket;

public class CreateDatabase {
    private static final String path = "C:\\JsonFiles\\";
    public String message;



    public boolean createDatabase(String[] queryArray) {

        String newPath = path + queryArray[3];
        File f = new File(newPath);
        if (f.exists())
            return false;
        else {

            f.mkdirs();
            return true;
        }
    }

}
