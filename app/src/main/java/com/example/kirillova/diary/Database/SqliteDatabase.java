package com.example.kirillova.diary.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDatabase extends SQLiteOpenHelper {
    public static final String databaseName = "user.db";
    public static final String tableName = "userTable";
    public static final String col_1 = "id";
    public static final String col_2 = "subject";
    public static final String col_3 = "description";
    public static final String col_4 = "dateTime";


    //конструкция для создания базы данных
    public SqliteDatabase(Context context) {
        super(context, databaseName, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    //создаем таблицу
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + tableName +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject TEXT,description TEXT,dateTime Date)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + databaseName);//если уже существует таблица,
        onCreate(sqLiteDatabase); //то удаляем ее и создаем новую
    }

    //Добавляем данные в таблицу
    public long insertData(String subject, String description, String dateTime) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();//для доступа к данным в базе данных
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,subject);
        contentValues.put(col_3, description);
        contentValues.put(col_4, dateTime);

        long id = sqLiteDatabase.insert(tableName, null, contentValues);
        return id;
    }
    public Cursor display(){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();//используем для доступа к данным
        Cursor cursor = sqliteDatabase.rawQuery("select * from "+tableName, null);
        return cursor;
    }
    //для обновления данных в БД
    public boolean update(String subject,String description,String dateTime,String id){
        try{
            SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(col_1,id);
            contentValues.put(col_2,subject);
            contentValues.put(col_3, description);
            contentValues.put(col_4, dateTime);

            sqliteDatabase.update(tableName,contentValues,col_1+" =?", new String[]{id});
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    //для удаления данных из БД
    public boolean delete(String id){
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        sqliteDatabase.delete(tableName,col_1+" = ?",new String[]{id});
        return  true;
    }
}
