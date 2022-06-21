package textdes.ephraim.com.textdes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewMessagesActivity extends AppCompatActivity {

    private ListView listview;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private String[] titles,ids;
    private boolean flag;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);

        uid = getIntent().getStringExtra("uid");
        openHelper = new SQLDBHelper(ViewMessagesActivity.this);
        db = openHelper.getWritableDatabase();
        listview = (ListView) findViewById(R.id.vm_list_view);
        flag = false;
        getAllMessages();

        if(flag){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewMessagesActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, titles);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ViewMessagesActivity.this,
                            MessageDetailsActivity.class);
                    intent.putExtra("mid", String.valueOf(ids[i]));
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void getAllMessages(){
        String query = String.format("SELECT * FROM %s WHERE %s = ? OR %s = ?",
                SQLDBHelper.MESSAGES, "sender", "receiver");
        String[] args = {uid
                , uid};
        Cursor cursor = db.rawQuery(query, args);
        int total = cursor.getCount();
        if(total > 0){
            flag = true;
            int array_index = 0;
            ids = new String[total];
            titles = new String[total];

            while (cursor.moveToNext()){
                ids[array_index] = String.valueOf(cursor.getInt(cursor.getColumnIndex("msgid")));
                String fullname = "";

                if(uid.equals(cursor.getString(cursor.getColumnIndex("sender")))){
                    fullname = "You";
                }else {
                    fullname = getUserFullname(cursor.getString(cursor.getColumnIndex("sender")));
                }

                titles[array_index] = fullname + " - ";
                titles[array_index] += " (" + cursor.getString(cursor.getColumnIndex("msg_date")) + ")";
                array_index++;
            }
        }
    }

    private String getUserFullname(String email){
        String query = String.format("SELECT * FROM %s WHERE %s = ?",
                SQLDBHelper.USERS, "email");
        String[] args = {email};
        Cursor cursor = db.rawQuery(query, args);
        cursor.moveToNext();

        return cursor.getString(cursor.getColumnIndex("fullname"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewMessagesActivity.this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}
