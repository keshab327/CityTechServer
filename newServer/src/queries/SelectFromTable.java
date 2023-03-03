package queries;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import queries.response.ResponseForSelect;
import queries.extrator.DataTypeExtractor;

import java.util.List;

public class SelectFromTable extends InsertIntoTable {
    private static final String path = "C:\\JsonFiles\\";
    public String message;
    Integer[] dataTypeSymbol;


    public String selectFromTable(String[] queryArray, String query) {


        if (query.toUpperCase().contains("WHERE")) {
            if (queryArray[3].equalsIgnoreCase("from") && queryArray[5].equalsIgnoreCase("where")) {
                System.out.println("\n where present");

            } else {
                return "Invalid query";
            }

        } else {
            if (queryArray[3].equalsIgnoreCase("from")) {
                System.out.println("\n where not present");
                message = getValuesForSelectNotWhere(queryArray, query);
            } else {
                return "invalid query";
            }

        }

        return message;
    }

    public String getValuesForSelectNotWhere(String[] queryArray, String query) {

        String[] selectedFields = queryArray[2].split(",");
//        String tableName = queryArray[4];
        message = "";
        JSONParser jsonParser = new JSONParser();
        JSONObject valuesJsonObj = new JSONObject();
        JSONArray valueJsonArray = new JSONArray();
//check for fields in table
        ResponseForSelect responseForSelect = checkTableAndQueryFields(queryArray, query);

        if (!responseForSelect.isError()) {
            valueJsonArray = responseForSelect.getValues();
            for (int i = 0; i < selectedFields.length; i++) {
                message = message + "\t\t\t\t" + selectedFields[i];
            }

            for (Object o : valueJsonArray) {
                message = message + ":";
                valuesJsonObj = (JSONObject) o;
                for (int i = 0; i < selectedFields.length; i++) {
                    if (dataTypeSymbol[i] == 1) {
                        Long value = (Long) valuesJsonObj.get(selectedFields[i]);
                        message = message + "\t\t\t\t" + String.valueOf(value);
                    } else if (dataTypeSymbol[i] == 2) {
                        String value = (String) valuesJsonObj.get(selectedFields[i]);
                        message = message + "\t\t\t\t" + value;
                    } else if (dataTypeSymbol[i] == 3) {
                        Double value = (Double) valuesJsonObj.get(selectedFields[i]);
                        message = message + "\t\t\t\t" + String.valueOf(value);
                    }
                }
            }

        } else {
            message = responseForSelect.getMessage();
        }
        return message;
    }


    private ResponseForSelect checkTableAndQueryFields(String[] queryArray, String query) {
        String[] selectedFields = queryArray[2].split(",");
        ResponseForSelect responseForSelect = new ResponseForSelect();
        JSONParser jsonParser = new JSONParser();

        String returnMsg = readDefinationAndValues(queryArray, queryArray[4]);

        if (returnMsg != null) {
            responseForSelect.setError(true);
            responseForSelect.setMessage(returnMsg);
            return responseForSelect;
        }


        for (int i = 0; i < selectedFields.length; i++) {
            int j=0;
            boolean fieldFound=false;
            for (Object field : super.definationsJsonArray) {
                JSONObject fieldInJson = (JSONObject) field;
                JSONObject actualField = (JSONObject) fieldInJson.get("field" + String.valueOf(j));
                try {
                    String singleField = (String) actualField.get(selectedFields[i].toLowerCase());
                    if (singleField != null) {
                        fieldFound=true;
                        break;
                    }
                    //           System.out.println("\t" + singleField);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                j++;
            }
            if(fieldFound==false){
                responseForSelect.setMessage("Fields in query didnot matched");
                responseForSelect.setError(true);
                return responseForSelect;
            }
        }


        DataTypeExtractor dataTypeExtractor = new DataTypeExtractor();
        List<Integer> fieldsRepresentatinSymbol = dataTypeExtractor.dataTypeExtractor(super.definationsJsonArray, selectedFields);
        dataTypeSymbol = fieldsRepresentatinSymbol.toArray(new Integer[fieldsRepresentatinSymbol.size()]);
        responseForSelect.setValues(valueJsonArray);
        responseForSelect.setMessage("successfully matched fields");//no need for message this one
        return responseForSelect;
    }


}
