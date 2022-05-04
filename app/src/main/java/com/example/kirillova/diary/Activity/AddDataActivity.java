package com.example.kirillova.diary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AddDataActivity extends AppCompatActivity {
    EditText subjectEt,descriptionEt;
    Button cancelBt,saveBt;
    SqliteDatabase mydb;
    Spinner spinnerMood,spinnerWork;
    String spinnerMood_text,spinnerWork_text;
    AddGoal users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        mydb = new SqliteDatabase(this);

        subjectEt = findViewById(R.id.subjectEditTextId);
        descriptionEt = findViewById(R.id.descriptionEditTextId);

        cancelBt = findViewById(R.id.cacelButtonId);
        saveBt = findViewById(R.id.saveButtonId);

        users = new AddGoal();

        spinnerMood = (Spinner) findViewById(R.id.mood_spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mood_array, android.R.layout.simple_spinner_item);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinnerMood.setAdapter(adapter);
        spinnerMood_text = (String) spinnerMood.getSelectedItem();

        // аналогично
        spinnerWork = (Spinner) findViewById(R.id.work_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.work_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWork.setAdapter(adapter2);
        spinnerWork_text = (String) spinnerWork.getSelectedItem();



        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData(); //запускает выполнение insertData
                backToMain(); //и после этого осуществляет возвращение в главное окно
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        }); //при нажатие на кнопку назад возвращает в основное окно

    }

    //Добавляем новые данные
    public void insertData(){
        long l = -1; // для проверки введен заголовок или нет

        Date date = new Date();// указывает дату написания
        String d = (String) DateFormat.format("dd/MM/yyyy",date); // указывает формат даты

        if(subjectEt.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"Вы не добавили заголовок",Toast.LENGTH_SHORT).show();
        }
        else{
            l = mydb.insertData(subjectEt.getText().toString(),
                    descriptionEt.getText().toString(),d); //если все проходит успешно, то вызывается insertData и
            //добавляются новые данные
        }
        // в соответствии с результатом появляется тост уведомление внизу экрана
        if(l>=0){
            Toast.makeText(getApplicationContext(),"Готово",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }
    public void backToMain() //возвращаемся на основной экран
    {
        Intent intent = new Intent(AddDataActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
