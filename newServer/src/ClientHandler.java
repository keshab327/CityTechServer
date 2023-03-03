

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements  Runnable{
    private final Socket clientSocket;

    PrintWriter out ;
    BufferedReader in ;
    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        out = new  PrintWriter( clientSocket.getOutputStream(),true);

        in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
    }


    @Override
    public void run() {
        try {
            while (true) {
                String value = in.readLine();
                QueryHandler queryHandler=new QueryHandler(clientSocket);
                String  message=queryHandler.checkQuery(value);

                out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
