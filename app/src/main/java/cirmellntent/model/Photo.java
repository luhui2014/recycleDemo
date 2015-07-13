package cirmellntent.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *  相机的实体类 2015/7/13.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";

    private String mFileName;

    public Photo(String fileName){
        mFileName = fileName;
    }

    public Photo(JSONObject jsonObject) throws JSONException{
        mFileName = jsonObject.optString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_FILENAME,mFileName);

        return jsonObject;
    }

    public String getFileName() {
        return mFileName;
    }
}
