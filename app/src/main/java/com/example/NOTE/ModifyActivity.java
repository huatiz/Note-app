package com.example.NOTE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

public class ModifyActivity extends AppCompatActivity {
    private DBService myDB;
    private changeDayTime DT;
    private TextView dayTextView_m, timeTextView_m;
    private EditText contentEditText_m;
    private Button cancelBtn_m, saveBtn_m;
    private Values value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        findView();
        setFont();
        initView();
        setListener();
    }

    private void findView() {
        contentEditText_m = (EditText) findViewById(R.id.modify_content);
        dayTextView_m = (TextView) findViewById(R.id.modify_day);
        timeTextView_m = (TextView) findViewById(R.id.modify_time);
        cancelBtn_m = (Button) findViewById(R.id.modify_cancel);
        saveBtn_m = (Button) findViewById(R.id.modify_save);
    }

    private void initView() {
        Intent intent = getIntent();
        value = new Values();
        myDB = new DBService(this);
        value.setId(intent.getIntExtra(DBService.ID, 0));
        value.setDay(intent.getStringExtra(DBService.DAY));
        value.setTime(intent.getStringExtra(DBService.TIME));
        value.setContent(intent.getStringExtra(DBService.CONTENT));

        dayTextView_m.setText(value.getDay());
        timeTextView_m.setText(value.getTime());
        contentEditText_m.setText(value.getContent());

        // move cursor to the end of the content & force open keyboard
        contentEditText_m.setSelection(value.getContent().length());
        contentEditText_m.requestFocus();
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        DT = new changeDayTime();
        DT.setDateTime(value.getDay(), value.getTime());
        DT.getDateTime();
    }

    private void setListener() {
        cancelBtn_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrNot();
            }
        });

        saveBtn_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getContent
                String content = contentEditText_m.getText().toString();
                String day = dayTextView_m.getText().toString();
                String time = timeTextView_m.getText().toString();

                if("".equals(content)) {
                    Toast.makeText(ModifyActivity.this,"內容不能為空", Toast.LENGTH_LONG).show();
                    return;
                }

                // write data to DB

                SQLiteDatabase db = myDB.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(DBService.CONTENT, content);
                values.put(DBService.DAY, day);
                values.put(DBService.TIME, time);
                db.update(DBService.TABLE, values,DBService.ID+"=?", new String[]{ value.getId().toString() });
                db.close();

                if ((value.getDay().equals(day) == false) || (value.getTime().equals(time) == false) || (value.getContent().equals(content) == false))
                    Toast.makeText(ModifyActivity.this,"修改成功", Toast.LENGTH_LONG).show();

                // jump page
                Intent intent = new Intent(ModifyActivity.this, MainActivity.class);
                startActivity(intent);
                ModifyActivity.this.finish();
            }
        });

        dayTextView_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DT.changeDay(dayTextView_m, ModifyActivity.this);
            }
        });

        timeTextView_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DT.changeTime(timeTextView_m, ModifyActivity.this);
            }
        });
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Rajdhani-Regular.ttf");
        dayTextView_m.setTypeface(font);
        timeTextView_m.setTypeface(font);
    }

    @Override
    public void onBackPressed() {
        saveOrNot();
    }

    private void saveOrNot() {
        String content = contentEditText_m.getText().toString();
        if (value.getContent().equals(content) == false) {
            AlertDialog builder = new AlertDialog.Builder(ModifyActivity.this)
                    .setMessage("取消更改？")
                    .setPositiveButton("不要保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ModifyActivity.this, MainActivity.class);
                            startActivity(intent);
                            ModifyActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", null).show();

            builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        }
        else {
            Intent intent = new Intent(ModifyActivity.this, MainActivity.class);
            startActivity(intent);
            ModifyActivity.this.finish();
        }
    }
}