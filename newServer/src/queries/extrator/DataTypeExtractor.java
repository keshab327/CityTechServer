package queries.extrator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DataTypeExtractor {

    private static Socket clientSocket = null;
    JSONArray defiation = new JSONArray();
    List<String[]> values = null;
    String[] tableFieldName = null;
    List<Integer> datatypeSymbols=new ArrayList<>();
    //int =1
    //varchar =2
    //float=3


    public List<Integer> dataTypeExtractor(JSONArray defiation, String[] tableFieldName) {

        this.defiation = defiation;
        this.tableFieldName = tableFieldName;


for(int i=0;i<tableFieldName.length;i++){
    int j=0;
    for( Object o:defiation) {
        JSONObject jsonObject = (JSONObject) o;
        JSONObject fieldType = (JSONObject) jsonObject.get("field" + String.valueOf(j));
        String definationOnly = (String) fieldType.get(tableFieldName[i]);
        if(definationOnly!=null) {
            char[] fieldSplit = definationOnly.toCharArray();
            decode(fieldSplit);
        }
        j++;
    }

}
System.out.println("\n datatype symbols"+datatypeSymbols.toString());
        return datatypeSymbols;
    }

    private String decode(char[] field) {
        if (field[0] == 'i' && field[1] == 'n' && field[2] == 't')
            datatypeSymbols.add(1);
        else if (field[0] == 'v' && field[1] == 'a' && field[2] == 'r' && field[3] == 'c' && field[4] == 'h' && field[5] == 'a' && field[6] == 'r') {
            
                datatypeSymbols.add(2);
            
        } else if (field[0] == 'f' && field[1] == 'l' && field[2] == 'o' && field[3] == 'a' && field[4] == 't') {
            datatypeSymbols.add(3);
        }
        return null;

    }
}
