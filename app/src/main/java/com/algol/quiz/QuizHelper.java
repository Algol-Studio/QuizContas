package com.algol.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class QuizHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "mathsone";
    // tasks table name
    private static final String TABLE_QUEST = "quest";
    // tasks Table Columns names
    private static final String KEY_ID = "qid";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer"; // correct option
    private static final String KEY_OPTA = "opta"; // option a
    private static final String KEY_OPTB = "optb"; // option b
    private static final String KEY_OPTC = "optc"; // option c
    private static final String KEY_OPTD = "optd"; // option d
    private SQLiteDatabase dbase;
    QuizHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase = db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUES
                + " TEXT, " + KEY_ANSWER + " TEXT, " + KEY_OPTA + " TEXT, "
                + KEY_OPTB + " TEXT, " + KEY_OPTC + " TEXT, " + KEY_OPTD + " TEXT)";
        db.execSQL(sql);
        addQuestion();
        //db.close();
    }
    private void addQuestion() {
        for (int i=0;i<20;i++){
            Random rand = new Random();
            int a = rand.nextInt(9)+1;
            int b = rand.nextInt(9)+1;
            int c = rand.nextInt(4);
            Question q;
            switch (c){
                case 0: q = sum(a,b);
                break;
                case 1: q = subtraction(a,b);
                break;
                case 2: q = multiplication(a,b);
                break;
                default: q = division(a,b);
                break;
            }
            this.addQuestion(q);
        }
    }

    private Question sum(int a, int b){
        String question = Integer.toString(a)+"+"+Integer.toString(b)+" = ?";
        List<Integer> answer = randInt(a+b);
        return new Question(question,Integer.toString(a+b+answer.get(0)),Integer.toString(a+b+answer.get(1)),Integer.toString(a+b+answer.get(2)),Integer.toString(a+b+answer.get(3)),Integer.toString(a+b));
    }

    private Question subtraction(int a, int b){
        String question = Integer.toString(a)+"-"+Integer.toString(b)+" = ?";
        List<Integer> answer = randInt(a-b);
        return new Question(question,Integer.toString(a-b+answer.get(0)),Integer.toString(a-b+answer.get(1)),Integer.toString(a-b+answer.get(2)),Integer.toString(a-b+answer.get(3)),Integer.toString(a-b));
    }

    private Question multiplication(int a, int b){
        String question = Integer.toString(a)+"x"+Integer.toString(b)+" = ?";
        List<Integer> answer = randInt(a*b);
        return new Question(question,Integer.toString(a*b+answer.get(0)),Integer.toString(a*b+answer.get(1)),Integer.toString(a*b+answer.get(2)),Integer.toString(a*b+answer.get(3)),Integer.toString(a*b));
    }

    private Question division(int a, int b){
        String question = Integer.toString(a*b)+"/"+Integer.toString(a)+" = ?";
        List<Integer> answer = randInt(b);
        return new Question(question,Integer.toString(b+answer.get(0)),Integer.toString(b+answer.get(1)),Integer.toString(b+answer.get(2)),Integer.toString(b+answer.get(3)),Integer.toString(b));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }
    // Adding new question
    private void addQuestion(Question quest) {
        // SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUES, quest.getQUESTION());
        values.put(KEY_ANSWER, quest.getANSWER());
        values.put(KEY_OPTA, quest.getOPTA());
        values.put(KEY_OPTB, quest.getOPTB());
        values.put(KEY_OPTC, quest.getOPTC());
        values.put(KEY_OPTD, quest.getOPTD());
        // Inserting Row
        dbase.insert(TABLE_QUEST, null, values);
    }
    List<Question> getAllQuestions() {
        List<Question> quesList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        //this.close();
        dbase = this.getReadableDatabase();
        this.onUpgrade(dbase,0,1);
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Question quest = new Question();
                quest.setID(cursor.getInt(0));
                quest.setQUESTION(cursor.getString(1));
                quest.setANSWER(cursor.getString(2));
                quest.setOPTA(cursor.getString(3));
                quest.setOPTB(cursor.getString(4));
                quest.setOPTC(cursor.getString(5));
                quest.setOPTD(cursor.getString(6));
                quesList.add(quest);
            } while (cursor.moveToNext());
        }
        // return quest list
        return quesList;
    }

    private List<Integer> randInt(int Answer){
        List<Integer> answerList = new ArrayList<>();
        answerList.add(0);
        Integer variance = Math.max(5,Answer/10);
        while (answerList.size()<4){
            Integer rand = -1*variance+(int)(Math.random()*2*(variance+0.5));
            if(answerList.indexOf(rand)==-1){
                answerList.add(rand);
            }
        }
        Collections.shuffle(answerList);
        return answerList;
    }
}