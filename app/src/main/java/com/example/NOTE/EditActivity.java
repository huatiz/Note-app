package com.example.NOTE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private DBService myDB;
    private changeDayTime DT;
    private TextView dayTextView, timeTextView;
    private EditText contentEditText;
    private Button saveBtn, cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        myDB = new DBService(this);
        findView();
        setFont();
        showCursor();
        setTime();
        setListener();
    }

    private void findView() {
        contentEditText = (EditText) findViewById(R.id.et_content);
        dayTextView = (TextView) findViewById(R.id.edit_day);
        timeTextView = (TextView) findViewById(R.id.edit_time);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        saveBtn = (Button) findViewById(R.id.btn_save);
    }

    private void showCursor() {
        // show cursor & force open keyboard
        contentEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private String getTime(String type) {
        SimpleDateFormat day = new SimpleDateFormat(type);
        String str = day.format(new Date());
        return str;
    }

    private void setTime() {
        if(dayTextView.getText().length() == 0)
            dayTextView.setText(getTime("yyyy-MM-dd"));
        if(timeTextView.getText().length() == 0)
            timeTextView.setText(getTime("HH:mm"));

        DT = new changeDayTime();
        DT.getDateTime();
    }

    private void setListener() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrNot();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getText
                String content = contentEditText.getText().toString();
                String day = dayTextView.getText().toString();
                String time = timeTextView.getText().toString();

                if("".equals(content)) {
                    Toast.makeText(EditActivity.this,"內容不能為空", Toast.LENGTH_LONG).show();
                    return;
                }

                // write data to DB
                SQLiteDatabase db = myDB.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(DBService.CONTENT, content);
                values.put(DBService.DAY, day);
                values.put(DBService.TIME, time);
                db.insert(DBService.TABLE,null,values);
                db.close();

                Toast.makeText(EditActivity.this,"保存成功", Toast.LENGTH_LONG).show();

                // jump page
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                EditActivity.this.finish();
            }
        });

        dayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DT.changeDay(dayTextView, EditActivity.this);
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DT.changeTime(timeTextView, EditActivity.this);
            }
        });
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Rajdhani-Regular.ttf");
        dayTextView.setTypeface(font);
        timeTextView.setTypeface(font);
    }

    @Override
    public void onBackPressed() {
        saveOrNot();
    }

    private void saveOrNot() {
        String content = contentEditText.getText().toString();
        if("".equals(content) == false) {
            AlertDialog builder = new AlertDialog.Builder(EditActivity.this)
                    .setMessage("取消更改？")
                    .setPositiveButton("不要保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(intent);
                            EditActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", null).show();

            builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        }
        else {
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
            EditActivity.this.finish();
        }
    }
}