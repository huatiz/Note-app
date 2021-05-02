package com.example.NOTE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.graphics.Typeface;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView addItemBtn, noteNum;
    private ListView itemView;
    private DBService myDB;
    private MyBaseAdapter myBaseAdapter;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setFont();
        readDB();
        setListener();
    }

    private void findView() {
        addItemBtn = (TextView) findViewById(R.id.add_item_btn);
        itemView = (ListView) findViewById(R.id.item_view);
        noteNum = (TextView) findViewById(R.id.note_num);
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Rajdhani-Regular.ttf");
        noteNum.setTypeface(font);
    }

    private void readDB() {
        List<Values> valuesList = new ArrayList<>();
        getAllItem(valuesList);

        // create Adapter
        myBaseAdapter = new MyBaseAdapter(valuesList,this,R.layout.item);
        itemView.setAdapter(myBaseAdapter);
    }

    private void setListener() {
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        // click on ListView
        itemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ModifyActivity.class);

                // pass value to new page
                Values value = (Values) itemView.getItemAtPosition(position);
                intent.putExtra(DBService.CONTENT, value.getContent());
                intent.putExtra(DBService.DAY, value.getDay().trim());
                intent.putExtra(DBService.TIME, value.getTime().trim());
                intent.putExtra(DBService.ID, value.getId());

                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        // long click on ListView
        itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "刪除");
                menu.add(0, 1, 0, "複製內容");
                menu.add(0, 2, 0, "取消");
            }
        });
    }

    public void getAllItem(List<Values> valuesList) {
        myDB = new DBService(this);
        SQLiteDatabase db = myDB.getReadableDatabase();

        // query data from DB
        Cursor cursor = db.query(DBService.TABLE,null,null,
                null,null,null,DBService.DAY+" DESC, "+DBService.TIME+" DESC");
        if(cursor.moveToFirst()){
            Values values;
            while (!cursor.isAfterLast()){
                values = new Values();

                //set values
                values.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DBService.ID))));;
                values.setContent(cursor.getString(cursor.getColumnIndex(DBService.CONTENT)));
                values.setDay(cursor.getString(cursor.getColumnIndex(DBService.DAY)));
                values.setTime(cursor.getString(cursor.getColumnIndex(DBService.TIME)));

                //add value to list
                valuesList.add(values);
                num++;
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        noteNum.setText(""+num);
    }

    class ViewHolder {
        TextView content;
        TextView day;
        TextView time;
    }

    // About ListView
    class MyBaseAdapter extends BaseAdapter {
        private List<Values> valuesList;
        private Context context;
        private int layoutId;

        public MyBaseAdapter(List<Values> valuesList, Context context, int layoutId) {
            this.valuesList = valuesList;
            this.context = context;
            this.layoutId = layoutId;
        }

        @Override
        public int getCount() {
            if (valuesList != null && valuesList.size() > 0)
                return valuesList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (valuesList != null && valuesList.size() > 0)
                return valuesList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // show ListView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(
                        getApplicationContext()).inflate(R.layout.item, parent,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.content = convertView.findViewById(R.id.content);
                viewHolder.day = (TextView) convertView.findViewById(R.id.day);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(viewHolder);


                // setFont
                Typeface font = Typeface.createFromAsset(getAssets(), "Rajdhani-Regular.ttf");
                viewHolder.day.setTypeface(font);
                viewHolder.time.setTypeface(font);
                TextView title = findViewById(R.id.title);
                title.setTypeface(font);
            }
            else
                viewHolder = (ViewHolder) convertView.getTag();

            String content = valuesList.get(position).getContent();
            viewHolder.content.setText(content);
            viewHolder.day.setText(valuesList.get(position).getDay());
            viewHolder.time.setText(valuesList.get(position).getTime());
            return convertView;
        }
        // remove ListView
        public void removeItem(int position){
            this.valuesList.remove(position);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Values values = (Values) itemView.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case 0: // delete item
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("確定要刪除嗎？")
                        .setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = myDB.getWritableDatabase();
                                db.delete(DBService.TABLE,DBService.ID+"=?",new String[]{String.valueOf(values.getId())});
                                db.close();
                                myBaseAdapter.removeItem(info.position);
                                itemView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        myBaseAdapter.notifyDataSetChanged();
                                    }
                                });
                                num--;
                                noteNum.setText(""+num);
                            }
                        })
                        .setNegativeButton("取消",null).show();
                break;
            case 1: // copy content to clipboard
                ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", values.getContent());
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(MainActivity.this, "已複製", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }
}