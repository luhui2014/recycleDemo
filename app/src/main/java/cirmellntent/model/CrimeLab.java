package cirmellntent.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import cirmellntent.util.CriminalIntentJSONSerializer;

/**
 * Created by Administrator on 2015/7/2.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;

    private static final String FILENAME = "crime.json";
    private CriminalIntentJSONSerializer mSerializer;

    private CrimeLab(Context context){
        this.mAppContext = context;

//        for(int i=0;i<10;i++){
//            Crime crime = new Crime.Builder().title("Crime # "+i).solvedState(i%2==0).build();
//            mCrimes.add(crime);
//
//        }
        mSerializer = new CriminalIntentJSONSerializer(mAppContext,FILENAME);

        try {
            mCrimes = mSerializer.loadCrimes();
            Log.d("AAAA", "CrimeLab ArrayList" + mCrimes.size());
        } catch (Exception e) {
            mCrimes = new ArrayList<>();
            Log.d("AAAA", "CrimeLab ArrayList Exception" + mCrimes.size()+",e="+e);
        }

    }

    //构造单例获取唯一的实例对象
    public static CrimeLab getCrimeLab(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }

        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes(){
        Log.d("AAAA", "CrimeLab getCrimes=" + mCrimes.size());
        return mCrimes;
    }

    public Crime getCrime(UUID crimeId){
        for(Crime crime : mCrimes){
            if(crime.getId().equals(crimeId)){
                return crime;
            }
        }
        return null;
    }

    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime){
        mCrimes.remove(crime);
    }

    /**
     * 将文件保存到本地的方法
     * @return true 保存成功，false 保存失败
     */
    public boolean saveCrimes(){
        try {
            mSerializer.saveCrime2Local(mCrimes);
            return true;
        } catch (Exception e) {
            //保存到本地文件失败
            Log.d("AAAA","save failed");
        }

        return false;
    }

}
