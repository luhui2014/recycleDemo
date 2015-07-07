package cirmellntent.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 尝试使用builder的写法
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    private String mDate;
    private boolean mSolved;

    public Crime(){
        this.mId = UUID.randomUUID();
        this.mDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
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
}
