

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ExecutorService pool= Executors.newFixedThreadPool(10);



    public static void main(String[] args)throws IOException {
       ServerSocket serverSocket=new ServerSocket(9090);

       while (true) {

            Socket socket = serverSocket.accept();
            ClientHandler client = new ClientHandler(socket);

            // This thread will handle the client
            // separately
            pool.execute(client);
            System.out.println("\n\n connected");
        }

    }

}


