package queries;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTable {


    private static final String path = "C:\\JsonFiles\\";

    public String createTable(String[] queryArray, String query) throws IOException {

        String[] forTableName = queryArray[3].split("\\(");
        String tableName = forTableName[0];

        System.out.println("\ntableName :" + tableName);

//for the definatin of table
        Pattern regex = Pattern.compile("\\((.*?)\\)");
        Matcher regexMatcher = regex.matcher(query);
        List<String> matchList = new ArrayList<>();

        while (regexMatcher.find()) {//Finds Matching Pattern in String
            matchList.add(regexMatcher.group(1));//Fetching Group from String
        }


        JSONArray tableDefinationArray = new JSONArray();

        for (String fieldType : matchList) {
            System.out.println("\n" + fieldType);
            String[] onlyKeyValueOf_AGroup = fieldType.split(",");
            for (int i = 0; i < onlyKeyValueOf_AGroup.length; i++) {

                String[] fieldTypePairs = onlyKeyValueOf_AGroup[i].split(" ");
                JSONObject definationTemp = new JSONObject();
                String field = fieldTypePairs[0];

                String dataType = fieldTypePairs[1];
                definationTemp.put(field.toLowerCase(), dataType.toLowerCase());
                JSONObject jsonObjectWithKeyNameField = new JSONObject();

                jsonObjectWithKeyNameField.put("field" + String.valueOf(i), definationTemp);
                tableDefinationArray.add(jsonObjectWithKeyNameField);

            }
        }
        // defination
        JSONObject definationObjectWithKey = new JSONObject();
        definationObjectWithKey.put("defination", tableDefinationArray);

        //values array in a object
        JSONObject valueObject = new JSONObject();
        JSONArray values = new JSONArray();
        valueObject.put("values", values);

//defiantion and value two pairs
        JSONArray definataionValue = new JSONArray();
        definataionValue.add(definationObjectWithKey);
        definataionValue.add(valueObject);

        String newPath = path + "\\" + queryArray[0].toLowerCase() + "\\" + tableName + ".json";
        try (FileWriter fileWriter = new FileWriter(newPath)) {
            fileWriter.write(definataionValue.toString());
            fileWriter.flush();
            return "Table Creation Success "+tableName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Problem Creating Table "+tableName;
        }
    }



}
