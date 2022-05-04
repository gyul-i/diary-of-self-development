package com.example.kirillova.diary.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kirillova.diary.Database.SqliteDatabase;
import com.example.kirillova.diary.NeedyClass.Information;
import com.example.kirillova.diary.NeedyClass.InformationAdapter;
import com.example.kirillova.diary.R;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    SqliteDatabase db;
    ArrayList<Information> arrayList;
    ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Integer> unDeleteSelect = new ArrayList<Integer>();

    ArrayAdapter arrayAdapter;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();

        listView = findViewById(R.id.ListviewId);

        arrayList=new ArrayList<Information>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

       // При нажатие на кнопку в правом нижнем углу переходит на новый лист
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,AddDataActivity.class);
               startActivity(intent);
            }
        });

        view();//вызываем метод view

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override // при нажатие на элемент списка
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                intent.putExtra("subject",arrayList.get(i).getSubject());
                intent.putExtra("description",arrayList.get(i).getDescription());
                intent.putExtra("listId",arrayList.get(i).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // показывает какие элементы могут быть выбраны в меню и перенаправляет на about(),add()
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutIdMainActivity:
                about();
                return true;
            case R.id.addGoal:
                add();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void view() {
        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            Information information = new Information(cursor.getString(0),cursor.getString(1),
                    cursor.getString(2),cursor.getString(3));
            arrayList.add(information);
        }
        Collections.reverse(arrayList);//ресервим arrayList для правильного отображения данных

        arrayAdapter = new InformationAdapter(this, arrayList);// передача контекста и arraylist в arrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//устанавливаем choice mode
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {//метод для выбора нескольких элементов

            // проверка состояния элемента на клик
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                String id = arrayList.get(i).getId();//для получения айди
                //если нажать два раза то цвет элемента будет белым
                if(selectList.contains(id) && count>0){
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                    selectList.remove(id); //убирает из массива selectList выбранный ранее элемент
                    count--; // уменьшается кол-во выбранных элементов
                }
                //иначе серым
                else{
                    selectList.add(arrayList.get(i).getId());
                    listView.getChildAt(i).setBackgroundColor(Color.GRAY);
                    unDeleteSelect.add(i);//сохраняет позицию элемента в списке для удаления
                    count++;
                }
                actionMode.setTitle("Выбраны элементы ("+count+")"); //count показывает сколько элементов выбрано
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();// для связи между меню с основным меню
                inflater.inflate(R.menu.selector_layout,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            //метод для удаления элементов
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.deleteContextMenuId){
                    for(String i : selectList){
                        db.delete(i); //удаляем элемент
                        arrayAdapter.remove(i);
                        Toast.makeText(getApplicationContext(),"Удалены выбранные элементы ("+count+")",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                    arrayAdapter.notifyDataSetChanged();
                    actionMode.finish();
                    count = 0; // сбрасывает до нуля
                }
                return true;
            }

            //метод чтобы выключить actionMode
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                for(int i: unDeleteSelect){
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);//все выбранные элементы которые были серые
                }                                                     // снова станут белыми
                count = 0;//сбрасывает count до нуля
                unDeleteSelect.clear();
                selectList.clear();
            }
        });
    }
    public void about(){
        Intent intent = new Intent(this,InstructionActivity.class); // выполняет InstructionActivity
        startActivity(intent);
    }
    public void add(){
        Intent intent = new Intent(this,AddGoal.class); // выполняет AddGoal
        startActivity(intent);
    }
}
