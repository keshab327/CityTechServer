package queries;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import queries.extrator.DataTypeExtractor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertIntoTable {

    private static final String path = "C:\\JsonFiles\\";
    private String message;
    Integer[] dataTypeSymbol;
    protected JSONParser jsonParser = new JSONParser();
    protected JSONObject defination = new JSONObject();
    protected JSONObject valuesJsonObj = new JSONObject();
    protected JSONArray jsonArrayToSave = new JSONArray();
    protected JSONArray valueJsonArray = new JSONArray();
    protected JSONArray definationsJsonArray = new JSONArray();


    public String insertIntoTablePhase1(String[] queryArray, String query) throws IOException {
        String[] tableFieldsName = null;
        List<String[]> valuesList = new ArrayList<>();


        List<String> paranthesisFields = new ArrayList<>();
        String[] forTaleName = queryArray[3].split("\\(");
        String tableName = forTaleName[0];

        Pattern regex = Pattern.compile("\\((.*?)\\)");
        Matcher regexMatcher = regex.matcher(query);
        int j = 0;
        while (regexMatcher.find()) {//Finds Matching Pattern in String

            if (j == 0) {
                paranthesisFields.add(regexMatcher.group(1));//Fetching Group from String
                String defination = regexMatcher.group(1);
                tableFieldsName = defination.split(",");

            } else {
                String[] fieldValueArray = regexMatcher.group(1).split(",");
                valuesList.add(fieldValueArray);


            }
            j++;
        }


//check defination and query field defination

        String returnFromCheckDuplicateFielda = checkForDuplicateFields(tableFieldsName);
        if(returnFromCheckDuplicateFielda!=null){
            return returnFromCheckDuplicateFielda;
        }

        String newPath = path + queryArray[0] + "\\" + tableName.toLowerCase() + ".json";
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(newPath)) {
            Object object = jsonParser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) object;

            int counter = 0;
            for (Object ob : jsonArray) {
                if (counter == 1) {
                    break;
                }
                JSONObject defination = (JSONObject) ob;
                JSONArray fields = (JSONArray) defination.get("defination");
                int i = 0;
                for (Object field : fields) {
                    JSONObject fieldInJson = (JSONObject) field;
                    JSONObject actualField = (JSONObject) fieldInJson.get("field" + String.valueOf(i));

                    try {
                        String singleField = (String) actualField.get(tableFieldsName[i].toLowerCase());
                        if (singleField == null) {
                            return "Fields in query " + tableFieldsName[i] + " and table didnot matched";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "problems in field in query";
                    }
                    i++;
                }
                counter++;
            }
        } catch (IOException e) {

            e.printStackTrace();
            return "Table Not Found";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return InsertIntoTablePhase2(tableFieldsName, valuesList, queryArray, tableName);

    }


    private String InsertIntoTablePhase2(String[] tableFieldsName, List<String[]> valuesList, String[] queryArray, String tableName) throws IOException {


        readDefinationAndValues(queryArray, tableName);

        //Extract datatype
        DataTypeExtractor checkDatatypeInputWIthTableFields = new DataTypeExtractor();
        List<Integer> dataTypeSymbolFromValidation = checkDatatypeInputWIthTableFields.dataTypeExtractor(definationsJsonArray, tableFieldsName);
        dataTypeSymbol = dataTypeSymbolFromValidation.toArray(new Integer[dataTypeSymbolFromValidation.size()]);


        for (String[] valuesArray : valuesList) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < tableFieldsName.length; i++) {

                Pattern pattern = Pattern.compile("\"([^\"]*)\"");
                Matcher regexMatcher = pattern.matcher(valuesArray[i]);
                while (regexMatcher.find()) {//Finds Matching Pattern in String
                    valuesArray[i] = regexMatcher.group(1);//Fetching Group from String

                }
                if (dataTypeSymbol[i] == 1) {
                    try {
                        int val = Integer.parseInt(valuesArray[i]);
                        jsonObject.put(tableFieldsName[i], val);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return "query is invalid " + valuesArray[i] + "\t" + "didnot match the defination";
                    }
                } else if (dataTypeSymbol[i] == 2) {
                    try {
                        jsonObject.put(tableFieldsName[i], valuesArray[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "query is invalid" + tableFieldsName[i] + "\t" + "didnot match the defination";
                    }
                } else if (dataTypeSymbol[i] == 3) {
                    try {
                        float val = Float.valueOf(valuesArray[i]);
                        jsonObject.put(tableFieldsName[i], val);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return "query is invalid " + valuesArray[i] + "\t" + "didnot match the defination";
                    }
                }


            }
            valueJsonArray.add(jsonObject);
        }

        return (ActualWriteToDatabase(queryArray, tableName));


    }


    public String readDefinationAndValues(String[] queryArray, String tableName) {

        try (FileReader fileReader = new FileReader(path + queryArray[0] + "\\" + tableName + ".json")) {
            Object object = jsonParser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) object;
            defination = (JSONObject) jsonArray.get(0);
            valuesJsonObj = (JSONObject) jsonArray.get(1);
            valueJsonArray = (JSONArray) valuesJsonObj.get("values");
            definationsJsonArray = (JSONArray) defination.get("defination");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Table Not Found";
        } catch (IOException e) {
            e.printStackTrace();
            return "Table Not Found";
        } catch (ParseException e) {
            e.printStackTrace();
            return "Problem During Fetching";
        }

        return null;
    }


    private String ActualWriteToDatabase(String[] queryArray, String tableName) {
        try (FileWriter fileWriter = new FileWriter(path + queryArray[0] + "\\" + tableName + ".json")) {
            JSONObject jsonObjectval = new JSONObject();
            jsonObjectval.put("values", valueJsonArray);
            jsonArrayToSave.add(defination);
            jsonArrayToSave.add(jsonObjectval);
            fileWriter.write(jsonArrayToSave.toJSONString());
            fileWriter.flush();
            message = "successfully inserted to table " + tableName;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error during insertion to " + tableName;
        }


        return message;
    }

    public String checkForDuplicateFields(String[] tableFieldsInQuery) {

        for (int i = 0; i < tableFieldsInQuery.length; i++) {
            int matchedTimes = 0;
            for (int j = 0; j < tableFieldsInQuery.length; j++) {
                if (tableFieldsInQuery[i].equalsIgnoreCase(tableFieldsInQuery[j])) {
                    matchedTimes++;
                    if(matchedTimes==2){
                    return tableFieldsInQuery[i] + " appeared multiple times";
                }}
            }
        }

        return null;
    }
}
