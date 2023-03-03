

import queries.*;
import java.io.*;
import java.net.Socket;



public class QueryHandler {
    private static final String path = "C:\\JsonFiles\\";
    public String message;
    Socket socket;

    public QueryHandler(Socket socket){
        this.socket=socket;
    }


    public String checkQuery(String query) throws IOException {
        String[] queryArray = query.split(" ");

        if (queryArray[1].equalsIgnoreCase("DATABASESELECTION")) {
           ConnectToDatabase connectToDatabase=new ConnectToDatabase();
            message = connectToDatabase.connectoDatabase(queryArray);


        } else if (queryArray[1].equalsIgnoreCase("CREATE") && queryArray[2].equalsIgnoreCase("DATABASE")) {
           CreateDatabase createDatabase= new CreateDatabase();
           boolean check = createDatabase.createDatabase(queryArray);
            if (check == true) {
                message = "databaseCreation" + " " + "SUCCESS" + " " + queryArray[3].toLowerCase();
            } else {
                message = "problemCreating" + " " + "FAIL" + " " + queryArray[3].toLowerCase();
            }


        } else if (queryArray[1].equalsIgnoreCase("CREATE") && queryArray[2].equalsIgnoreCase("TABLE")) {
            System.out.println("\n database during creating table :" + queryArray[0]);
            CreateTable createTable=new CreateTable();
            message= createTable.createTable(queryArray, query);

        } else if (queryArray[1].equalsIgnoreCase("INSERT")) {
            InsertIntoTable insertIntoTable=new InsertIntoTable();
            message = insertIntoTable.insertIntoTablePhase1(queryArray, query);
        } else if (queryArray[1].equalsIgnoreCase("SELECT")) {
            SelectFromTable selectFromTable=new SelectFromTable();
            message = selectFromTable.selectFromTable(queryArray, query);
        }else{
            message="Problem in query";
        }


        return message;
    }
}
