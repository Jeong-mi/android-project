package com.example.pj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    RadioGroup rg1; RadioGroup rg2; RadioGroup rg3; RadioGroup rg4;

    ImageView image1;   ImageView image2;   ImageView image3;   ImageView image4;   ImageView image5;   ImageView image6;   ImageView image7;   ImageView image8;

    Button btnStart8;   Button btnNext1;    Button btnNext2;    Button btnNext3;    Button btnFinish8;


    EditText result;

    myDBHelper Helper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("8강전 Fight");

        //라디오그룹 id 값 가져옴
        rg1 = (RadioGroup)findViewById(R.id.rg1);   rg2 = (RadioGroup)findViewById(R.id.rg2);   rg3 = (RadioGroup)findViewById(R.id.rg3);   rg4 = (RadioGroup)findViewById(R.id.rg4);

        image1 = (ImageView)findViewById(R.id.image1);  image2 = (ImageView)findViewById(R.id.image2);  image3 = (ImageView)findViewById(R.id.image3);  image4 = (ImageView)findViewById(R.id.image4);
        image5 = (ImageView)findViewById(R.id.image5);  image6 = (ImageView)findViewById(R.id.image6);  image7 = (ImageView)findViewById(R.id.image7);  image8 = (ImageView)findViewById(R.id.image8);



        //디비 잘 업로드됐는지 확인용
        result = (EditText) findViewById(R.id.result);

        Helper = new myDBHelper(this);
        sqlDB = Helper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM readTBL;", null);

        cursor.moveToFirst();

        String strTitle = cursor.getString(1);
        result.setText(strTitle);

        byte[] bytes;
        Bitmap bitmap;
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        image1.setImageBitmap(bitmap);

        cursor.moveToNext();
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        image2.setImageBitmap(bitmap);

        cursor.close();
        sqlDB.close();

        /*
        sqlDB = Helper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO readTBL VALUES ( 'title' , 'author', 'publisher', 50, 'inform');");
        cursor.close();
        sqlDB.close();
         */


        //image1.setImageResource(R.drawable.book1);
        //image2.setImageResource(R.drawable.book2);

        //버튼 id값 가져옴
        btnStart8 = (Button)findViewById(R.id.btnStart8);   btnNext1 = (Button)findViewById(R.id.btnNext1);     btnNext2 = (Button)findViewById(R.id.btnNext2);
        btnNext3 = (Button)findViewById(R.id.btnNext3);     btnFinish8 = (Button)findViewById(R.id.btnFinish8);

        //처음엔 라디오그룹만 보이게 하고 모든 버튼을 보이지 않게 함
        btnNext1.setVisibility(View.INVISIBLE);     btnNext2.setVisibility(View.INVISIBLE);     btnNext3.setVisibility(View.INVISIBLE);     btnFinish8.setVisibility(View.INVISIBLE);

        //시작버튼을 누르면 첫 번째 라디오그룹만 보이게 함
        btnStart8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg1.setVisibility(View.VISIBLE);      btnNext1.setVisibility(View.VISIBLE);
                rg2.setVisibility(View.INVISIBLE);    btnNext2.setVisibility(View.INVISIBLE);
                rg3.setVisibility(View.INVISIBLE);    btnNext3.setVisibility(View.INVISIBLE);
                rg4.setVisibility(View.INVISIBLE);    btnFinish8.setVisibility(View.INVISIBLE);
            }
        });

        //첫번째 선택을 한뒤 다음버튼 누르면, 두번째 라디오그룹을 보이게 하고 첫번째 라디오그룹은 숨김
        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg1.setVisibility(View.INVISIBLE);  btnNext1.setVisibility(View.INVISIBLE);
                rg2.setVisibility(View.VISIBLE);    btnNext2.setVisibility(View.VISIBLE);
            }
        });

        //두번째 선택을 한뒤 다음버튼 누르면, 세번째 라디오그룹을 보이게 하고 두번째 라디오그룹은 숨김
        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg2.setVisibility(View.INVISIBLE);  btnNext2.setVisibility(View.INVISIBLE);
                rg3.setVisibility(View.VISIBLE);    btnNext3.setVisibility(View.VISIBLE);
            }
        });

        //세번째 선택을 한뒤 다음버튼 누르면, 네번째 라디오그룹을 보이게 하고 세번째 라디오그룹은 숨김
        btnNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg3.setVisibility(View.INVISIBLE);  btnNext3.setVisibility(View.INVISIBLE);
                rg4.setVisibility(View.VISIBLE);    btnFinish8.setVisibility(View.VISIBLE);
            }
        });

        //마지막 Finish 버튼 누르면, 4강의 페이지로 이동하기
        btnFinish8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Intent intent = getIntent(getApplicationContext(), QuarterFinalActivity.class);
                startActivity(this); */
            }
        });

    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {

            super(context, "readDB.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL("CREATE TABLE  readTBL ( title TEXT PRIMARY KEY, author TEXT, publisher TEXT, price INTEGER, inform TEXT, cover BLOB);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS readTBL");
            onCreate(db);
        }
    }



}