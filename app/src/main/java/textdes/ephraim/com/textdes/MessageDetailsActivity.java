package textdes.ephraim.com.textdes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MessageDetailsActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private String uid, mid;
    private TextView sender, receiver, message, date;
    private Button decrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        openHelper = new SQLDBHelper(MessageDetailsActivity.this);
        db = openHelper.getWritableDatabase();
        uid = getIntent().getStringExtra("uid");
        mid = getIntent().getStringExtra("mid");
        sender = (TextView) findViewById(R.id.md_sender_tv);
        receiver = (TextView) findViewById(R.id.md_receiver_tv);
        message = (TextView) findViewById(R.id.md_message_tv);
        date = (TextView) findViewById(R.id.md_date_tv);
        decrypt = (Button) findViewById(R.id.md_dec_btn);

        String query = String.format("SELECT * FROM %s  WHERE %s = ?", SQLDBHelper.MESSAGES, "msgid");
        String[] args = {mid};
        Cursor cursor = db.rawQuery(query, args);
        cursor.moveToNext();
        String s_id = cursor.getString(cursor.getColumnIndex("sender"));
        String r_id = cursor.getString(cursor.getColumnIndex("receiver"));

        String s_fullname = getUserFullname(s_id);
        String r_fullname = getUserFullname(r_id);
        final String key = cursor.getString(cursor.getColumnIndex("ckey"));
        //final String message_val = cursor.getString(cursor.getColumnIndex("message"));
        final byte[] encodedMessage = cursor.getBlob(cursor.getColumnIndex("message"));
        String message_val = Base64.getEncoder().encodeToString(encodedMessage);
        String msg_date = cursor.getString(cursor.getColumnIndex("msg_date"));

        if(s_id.equals(uid)){
            s_fullname = "You";
        }
        //
        if(r_id.equals(uid)){
            r_fullname = "You";
        }

        sender.setText(s_fullname);
        receiver.setText(r_fullname);
        message.setText(message_val);
        date.setText(msg_date);

        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EncodeDecoder des3 = new EncodeDecoder(message_val);
                TrippleDES des3 = new TrippleDES();
                byte[] main_val = encodedMessage;
                decrypt.setVisibility(View.GONE);
                try {
                    message.setText(des3.decryptData(main_val));
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });

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
        Intent intent = new Intent(MessageDetailsActivity.this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}
