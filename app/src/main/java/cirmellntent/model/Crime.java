package cirmellntent.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 尝试使用builder的写法
 */
public class Crime {
    //将数据保存到本地的时候，封装到json中
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";

    private UUID mId;
    private String mTitle;
    private String mDate;
    private boolean mSolved;
    private Photo mPhoto;

    public Crime(){
        this.mId = UUID.randomUUID();
        this.mDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
    }

    /**
     * 获取json的数据
     * @param jsonObject
     * @throws JSONException
     */
    public Crime(JSONObject jsonObject) throws JSONException{
        Log.d("AAAA", "Crime jsonObject=" + jsonObject.toString());
        mId = UUID.fromString(jsonObject.getString(JSON_ID));
        if(jsonObject.has(JSON_TITLE)){
            mTitle = jsonObject.getString(JSON_TITLE);
        }
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
        mDate = jsonObject.optString(JSON_DATE);

        if(jsonObject.has(JSON_PHOTO)){
            mPhoto = new Photo(jsonObject.getJSONObject(JSON_PHOTO));
        }
    }

        //build 的写法，将构造私有
        public static class Builder {
        private UUID mId;
        private String mTitle;
        private String mDate;
        private boolean mSolved;

        public Builder(){
            this.mId = UUID.randomUUID();
            this.mDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
        }

        public Builder title(String title){
            this.mTitle = title;
            return this;
        }

        public Builder solvedState(boolean state){
            this.mSolved = state;
            return this;
        }

        public Crime build(){
            return new Crime(this);
        }
    }

    private Crime(Builder builder){
        mId = builder.mId;
        mTitle = builder.mTitle;
        mDate = builder.mDate;
        mSolved = builder.mSolved;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }


    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID,mId.toString());
        json.put(JSON_TITLE,mTitle);
        json.put(JSON_SOLVED,mSolved);
        json.put(JSON_DATE,mDate);

        if(mPhoto != null){
            json.put(JSON_PHOTO,mPhoto.toJSON());
        }
        return json;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }
}
