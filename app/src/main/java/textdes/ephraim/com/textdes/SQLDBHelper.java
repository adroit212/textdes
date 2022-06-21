package textdes.ephraim.com.textdes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME="des.db";
    private static int DATABASE_VERSION=1;
    private SQLiteDatabase db;

    String createUsers = "create table users(email TEXT PRIMARY KEY, role TEXT, password TEXT," +
            "fullname TEXT, mobile TEXT)";
    String createMessagees = "create table messages(msgid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "sender TEXT, receiver TEXT, message BLOB, msg_date TEXT, ckey TEXT)";

    public static String USERS = "users";
    public static String MESSAGES = "messages";

    public SQLDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db=sqLiteDatabase;
        sqLiteDatabase.execSQL(createUsers);
        sqLiteDatabase.execSQL(createMessagees);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + USERS);
        sqLiteDatabase.execSQL("drop table " + MESSAGES);
        onCreate(sqLiteDatabase);
    }
}
