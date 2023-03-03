package queries.response;

import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ResponseForSelect {
  private   JSONArray  values;
   private String message;
   private  boolean error;

    public boolean isError() {
        return this.error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    List<Integer> extractedDatatypes=new ArrayList<>();

    public List<Integer> getExtractedDatatypes() {
        return this.extractedDatatypes;
    }

    public void setExtractedDatatypes(List<Integer> extractedDatatypes) {
        this.extractedDatatypes = extractedDatatypes;
    }

    public JSONArray getValues() {
        return this.values;
    }

    public void setValues(JSONArray values) {
        this.values = values;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
