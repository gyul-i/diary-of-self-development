package com.example.kirillova.diary.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kirillova.diary.Database.SqliteDatabase;
import com.example.kirillova.diary.R;

import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    EditText subjectEt,descriptionEt;
    Button cancelBt,updateBt;
    SqliteDatabase dbUpdate;
    Spinner spinnerMood,spinnerWork;
    String spinnerMood_text,spinnerWork_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);

        //передача контекста апдейт активити в бд
        dbUpdate = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = dbUpdate.getWritableDatabase();
        //почти аналогичная часть кода как в AddDataActivity, только поменялись немного названия элементов
        subjectEt = findViewById(R.id.subjectEditTextIdUpdate);
        descriptionEt = findViewById(R.id.descriptionEditTextIdUpdate);

        cancelBt = findViewById(R.id.cacelButtonIdUpdate);
        updateBt = findViewById(R.id.saveButtonIdUpdate);

        spinnerMood = (Spinner) findViewById(R.id.mood_spinner_update);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mood_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMood.setAdapter(adapter);
        spinnerMood_text = (String) spinnerMood.getSelectedItem();

        spinnerWork = (Spinner) findViewById(R.id.work_spinner_update);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.work_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWork.setAdapter(adapter2);
        spinnerWork_text = (String) spinnerWork.getSelectedItem();

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        //данные из бд сохраняем в переменные строкового типа
        String sub = intent.getStringExtra("subject");
        String des = intent.getStringExtra("description");

        final String id = intent.getStringExtra("listId");

        //добавляем в элементы
        subjectEt.setText(sub);
        descriptionEt.setText(des);




        //возвращает в основное окно
        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //для обновления базы данных если пользователь нажмет на кнопку для обновления
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String d = (String) DateFormat.format("dd/MM/yyyy",date);

                if(dbUpdate.update(subjectEt.getText().toString(),descriptionEt.getText().toString(),d,id)==true){
                    Toast.makeText(getApplicationContext(),"Обновлено",Toast.LENGTH_SHORT).show();
                    backToMain();
                }
            }
     });
    }

    //возвращает в основное окно после обновления данных
    public void backToMain()
    {
        Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
