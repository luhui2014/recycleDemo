package com.cnpaypal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.cnpaypal.home.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/6/18.
 *
 */
public class CustomExpiredActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_expired_layout);

//        sortByComparable();
//        sortByComparator();

        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<String>(),
                new HashMap<String,String>().keySet()
        };

        for(Collection<?> collection :collections){
            Log.d("AAAA","打印结果="+classify(collection));
        }

//        Integer

    }

    private void sortByComparable(){
        //Comparator 和 Comparable 的用法的比较
        List<Person> persons = new ArrayList<>();
        persons.add(new Person(20,"aAa"));
        persons.add(new Person(22,"aBa"));
        persons.add(new Person(21,"BAa"));
        persons.add(new Person(23,"ccc"));
        persons.add(new Person(22,"路"));
        persons.add(new Person(22,"jack"));

        //别忘记调用这句，来排序
        Collections.sort(persons);
        for(Person person : persons){
            Log.d("AAAA","sort="+person.toString());
        }
    }

    private void sortByComparator(){
        List<Person2> persons = new ArrayList<>();
        persons.add(new Person2(20,"aAa"));
        persons.add(new Person2(22,"王啛啛喳喳出错"));
        persons.add(new Person2(21,"BAa"));
        persons.add(new Person2(23,"ccc"));
        persons.add(new Person2(22,"路"));
        persons.add(new Person2(22,"jack"));

        Comparator<Person2> comparator = new Comparator<Person2>() {
            @Override
            public int compare(Person2 lhs, Person2 rhs) {
                int ageDiff = lhs.getAge()-rhs.getAge();
                if(ageDiff!=0){
                    return ageDiff;
                }

                // 汉字拼音排序  取得比较对象的汉字编码，并将其转换成字符串
                try {
                    String s1 = new String(lhs.getName().getBytes("GB2312"), "ISO-8859-1");
                    String s2 = new String(rhs.getName().getBytes("GB2312"), "ISO-8859-1");
                    // 运用String类的 compareTo（）方法对两对象进行比较
                    return s1.compareTo(s2);
                } catch (UnsupportedEncodingException e) {
                    Log.e("AAAA","getBytes UnsupportedEncodingException ",e);
                }
                return 0;
            }
        };

        //别忘记调用这句，来排序
        Collections.sort(persons,comparator);
        for(Person2 person : persons){
            Log.d("AAAA","sort="+person.toString());
        }
    }

    //枚举类实现的单利
//    private enum expired{
//        INSTANCE;
//    }

    //首先是comparable 类在设计的时候考虑到了需要排序，例如先age排序后名称排序。自然顺序
    public class Person implements Comparable<Person>{
        private int mAge;
        private String mName;

        public Person(int age,String name){
            this.mAge = age;
            this.mName = name;
        }

        public int getAge() {
            return mAge;
        }

        public String getName() {
            return mName;
        }

        @Override
        public String toString() {
            return "person sort age="+this.mAge+" ,name="+this.mName;
        }

        @Override
        public int compareTo(Person person) {
            if(this.mAge>person.getAge()){
                return 1;
            }

            if(this.mAge < person.getAge()){
                return -1;
            }

            // 年龄相同后按name排序
            if (this.mName.compareTo(person.getName()) < 0) {
                return -1;
            }

            if (this.mName.compareTo(person.getName()) > 0) {
                return 1;
            }
            return 0;
        }
    }

    public class Person2{
        private int mAge;
        private String mName;

        public Person2(int age,String name){
            this.mAge = age;
            this.mName = name;
        }

        public int getAge() {
            return mAge;
        }

        public String getName() {
            return mName;
        }

        @Override
        public String toString() {
            return "person22 sort age="+this.mAge+" ,name="+this.mName;
        }

    }


    public static String classify(Set<?> s){
        return "set";
    }

    public static String classify(List<?> list){
        return "list";
    }

    public static String classify(Collection<?> c){
//        return "unKnown Collection";
        return c instanceof Set ? "set"
                : c instanceof List ? "list"
                : "UnKnow Collection";
    }
}
