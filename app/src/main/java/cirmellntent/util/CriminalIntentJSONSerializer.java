package cirmellntent.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import cirmellntent.model.Crime;

/**
 * Created by Administrator on 2015/7/7.
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer(Context context,String fileName){
        mContext = context;
        mFileName = fileName;
    }

    /**
     *
     * @return 载入本地文件里面的json,json 中无数据则返回空list
     * @throws IOException
     * @throws JSONException
     */
    public ArrayList<Crime> loadCrimes() throws IOException,JSONException{
        ArrayList<Crime> crimes = new ArrayList<>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while((line = reader.readLine())!= null){
                jsonString.append(line);
            }

            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            Log.d("AAAA","loadCrimes crime size="+array.length());

            for(int i=0;i<array.length();i++){
                crimes.add(new Crime(array.getJSONObject(i)));
            }
            Log.d("AAAA","loadCrimes crime ="+crimes.size());
        } catch (FileNotFoundException e) {
            //ignore this one,it happens when starting fresh
        }finally {
            if(reader!=null){
                reader.close();
            }
        }


        return crimes;
    }

    public void saveCrime2Local(ArrayList<Crime> crimes) throws IOException,JSONException{
        Log.d("AAAA","crime 保存过来的数据是="+crimes.size());
        JSONArray array = new JSONArray();
        for(Crime crime : crimes){
            array.put(crime.toJSON());
        }

        //write the file to disk
        Writer writer = null;
        try {
            OutputStream outputStream = mContext.openFileOutput(mFileName,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            writer.write(array.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }


}
