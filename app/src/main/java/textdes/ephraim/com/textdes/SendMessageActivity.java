package textdes.ephraim.com.textdes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SendMessageActivity extends AppCompatActivity {

    private EditText receiver, message;
    private Button send;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        openHelper = new SQLDBHelper(SendMessageActivity.this);
        db = openHelper.getWritableDatabase();
        uid = getIntent().getStringExtra("uid");
        receiver = (EditText) findViewById(R.id.sm_receiver_et);
        message = (EditText) findViewById(R.id.sm_message_et);
        send = (Button) findViewById(R.id.sm_send_btn);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message_val = String.valueOf(message.getText());
                TrippleDES trippleDES = new TrippleDES();
                byte[] new_msg = null;
                String msg_key = "dkey";
                try {
                    new_msg = trippleDES.encryptData(message_val);
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                String receiver_val = String.valueOf(receiver.getText());

                if(!"".equals(message_val) && !"".equals(receiver_val)){
                    if(checkReceiver(receiver_val)){
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                        String msg_date = sdf.format(new Date());
                        ContentValues cv = new ContentValues();
                        cv.put("sender", uid);
                        cv.put("receiver", receiver_val);
                        cv.put("message", new_msg);
                        cv.put("msg_date", msg_date);
                        cv.put("ckey", msg_key);

                        db.insert(SQLDBHelper.MESSAGES,null,cv);

                        AlertDialog.Builder alerter=new AlertDialog.Builder(SendMessageActivity.this);
                        alerter.setTitle("Success!");
                        alerter.setMessage("Message sent successful!");
                        alerter.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                Intent intent = new Intent(SendMessageActivity.this,
                                        HomeActivity.class);
                                intent.putExtra("uid",uid);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alerter.show();
                    }else{
                        Toast.makeText(SendMessageActivity.this, "Receiver not a registered user!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SendMessageActivity.this, "Empty field detected!",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private boolean checkReceiver(String receiver){
        String query = String.format("SELECT * FROM %s  WHERE %s = ?", SQLDBHelper.USERS, "email");
        String[] args = {receiver};
        Cursor cursor = db.rawQuery(query, args);

        return (cursor.moveToNext());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SendMessageActivity.this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}
