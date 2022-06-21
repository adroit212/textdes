package textdes.ephraim.com.textdes;

import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    GridView grid;
    String[] labels={
            "Send Message",
            "Open Messages",
            "Sign Out"
    };

    int[] images={
            R.drawable.send,
            R.drawable.open,
            R.drawable.quit
    };

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        uid = getIntent().getStringExtra("uid");
        GridAdapter adapter=new GridAdapter(HomeActivity.this, labels, images);
        grid=(GridView) findViewById(R.id.main_grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){ //Send Message
                    Intent intent=new Intent(HomeActivity.this, SendMessageActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                    finish();
                }else if(i==1){ //Open messages
                    Intent intent=new Intent(HomeActivity.this, ViewMessagesActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                    finish();
                }else if(i==2){ //logout
                    AlertDialog.Builder alerter=new AlertDialog.Builder(HomeActivity.this);
                    alerter.setTitle("Logout");
                    alerter.setMessage("Logout session!");
                    alerter.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Do nothing
                        }
                    });
                    alerter.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //close application
                            Intent logout_intent = new Intent(HomeActivity.this, SignInActivity.class);
                            startActivity(logout_intent);
                            finish();
                        }
                    });
                    alerter.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alerter=new AlertDialog.Builder(HomeActivity.this);
        alerter.setTitle("Logout");
        alerter.setMessage("Logout session!");
        alerter.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing
            }
        });
        alerter.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close application
                Intent logout_intent = new Intent(HomeActivity.this, SignInActivity.class);
                startActivity(logout_intent);
                finish();
            }
        });
        alerter.show();
    }
}
