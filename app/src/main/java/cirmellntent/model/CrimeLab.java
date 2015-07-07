package cirmellntent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2015/7/2.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;

    private CrimeLab(Context context){
        this.mAppContext = context;

        mCrimes = new ArrayList<>();
        for(int i=0;i<100;i++){
            Crime crime = new Crime.Builder().title("Crime # "+i).solvedState(i%2==0).build();
            mCrimes.add(crime);

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

}
