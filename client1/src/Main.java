

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    static    final int id = 1;

    static BufferedReader bufferedReader1 = null;
    static PrintWriter printWriter = null;//send to server

    static BufferedReader bufferedReader2 = null;
    static  String databaseName;

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 9090);

        printWriter = new PrintWriter(socket.getOutputStream(), true);
        bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
        bufferedReader2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String databaseReturns = databaseOption(printWriter, bufferedReader1, bufferedReader2);
        if(databaseReturns.equals("EXIT")){
            System.exit(0);

        } else if (databaseReturns.equalsIgnoreCase("databaseconnected")) {
            System.out.println("\n Connected To Database: "+databaseName);
        }



        while (true) {
            System.out.println("\n Enter 1 to access database menu OR\nEnter query: ");
            String query = bufferedReader1.readLine();
            if (query.equalsIgnoreCase("1"))
                databaseOption(printWriter, bufferedReader1, bufferedReader2);
            String[] queryBreakdown = query.split(" ");
            if (queryBreakdown[0].equalsIgnoreCase("select")) {
                printWriter.println(databaseName + " " + query);
                String response = bufferedReader2.readLine();
                if(response!=null){
                    String[] responseBreak=response.split(":");
                    System.out.print("response from server :\n");
                    for(int i=0;i<responseBreak.length;i++){
                        System.out.print("\n"+responseBreak[i]);
                    }}else {
                    System.out.print("response from server :"+response);
                }

            } else {
                printWriter.println(databaseName + " " + query);
                String response = bufferedReader2.readLine();
                System.out.println("response from server :" + response);
            }
        }


    }

    private static String databaseOption(PrintWriter printWriter, BufferedReader bufferedReader1, BufferedReader bufferedReader2) throws IOException {

        Scanner scanner = new Scanner(System.in);
        while (true) {

            int choicetoExit ;
            System.out.println("\nPlease insert 1 select the database : " + "\n" + "Please insert 2 to create database : " + "\n Press 3 to exit  : ");
            int choice = scanner.nextInt();

            if (choice == 2) {


                System.out.println("\nEnter query: ");
                String value = bufferedReader1.readLine();
                printWriter.println(databaseName + " " + value);
                String response = bufferedReader2.readLine();

                if(response!=null){
                    String[] responseArray=response.split(" ");
                    if(responseArray[1].equalsIgnoreCase("success")){
                        System.out.println("response from server :" + response);
                        databaseName=responseArray[2];
                        return "DATABASECONNECTED";

                    }
                }else{
                    System.out.println("Problem Creating  Database" );
                }


                return "databasecreation".toUpperCase();
            }
            else if (choice == 1) {
                System.out.println("\n Enter database you want to connect: ");
                String databaseNameInput = bufferedReader1.readLine();
                //send database selection to databse
                printWriter.println("NULL"+" "+"DATABASESELECTION"+" "+databaseNameInput);

                //response from database
                String responseFromServer = bufferedReader2.readLine();
                if(responseFromServer!=null){
                    String[] responseArray = responseFromServer.split(" ");
                    System.out.println("\n response from server: " + responseFromServer);

                    if (responseArray[1].equalsIgnoreCase("FAIL")) {
                        System.out.println("\n Press 1 to continue : " + "\n" + "Press 2 to exit");
                        choicetoExit = scanner.nextInt();
                        if (choicetoExit == 2) {
                            return "exit".toUpperCase();
                        } else if (choicetoExit == 1) {
                            continue;
                        } else {
                            System.out.println("\n please Enter valid input");
                        }

                    } else if (responseArray[1].equalsIgnoreCase("SUCCESS")) {
                        System.out.println("\n connected to database "+responseArray[2]);
                        databaseName=responseArray[2];
                        return "DATABASECONNECTED";
                    }


                } else if (choice == 3) {

                    return "exit".toUpperCase();

                } else {
                    System.out.println("\n Please insert valid input");

                }

            }
        }
    }
}