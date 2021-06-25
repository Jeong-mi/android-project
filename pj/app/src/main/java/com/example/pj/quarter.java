package com.example.pj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class quarter extends AppCompatActivity {

    RadioGroup rg1, rg2;

    Button btnStart4, btnNext, btnFinish4;

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quarter);

        setTitle("4강전 Fight");

        //라디오그룹 id 값 가져옴
        rg1 = (RadioGroup)findViewById(R.id.rg1);   rg2 = (RadioGroup)findViewById(R.id.rg2);

        //버튼 id값 가져옴
        btnStart4 = (Button)findViewById(R.id.btnStart4);   btnNext = (Button)findViewById(R.id.btnNext);   btnFinish4 = (Button)findViewById(R.id.btnFinish4);

        //라디오 버튼 id 값 배열 형태로 가져옴
        Integer rbID[] = {R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4};
        RadioButton rb[] = new RadioButton[4];
        for(int i=0; i<4; i++){
            rb[i] = (RadioButton)findViewById(rbID[i]);
        }

        //이미지뷰 id 값 배열 형태로 가져옴
        Integer imgID[] = {R.id.image1, R.id.image2, R.id.image3, R.id.image4};
        ImageView img[] = new ImageView[4];
        for(int i=0; i<4; i++){
            img[i] = (ImageView)findViewById(imgID[i]);
        }


        Intent intent = getIntent();
        //8강전에서 저장해둔 선택된 도서들의 인덱스를 quarter_index에 저장
        int[] quarter_index = intent.getIntArrayExtra("index");

        //8강전까지 저장된 투표수
        int[] voteCount = intent.getIntArrayExtra("voteCount");

        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM readTBL;", null);

        byte[] bytes;
        Bitmap bitmap;

        //8강전에서 선택된 책들에 대한 이미지 불러오기
        cursor.moveToPosition(quarter_index[0]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img[0].setImageBitmap(bitmap);

        cursor.moveToPosition(quarter_index[1]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img[1].setImageBitmap(bitmap);

        cursor.moveToPosition(quarter_index[2]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img[2].setImageBitmap(bitmap);

        cursor.moveToPosition(quarter_index[3]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img[3].setImageBitmap(bitmap);

        //처음엔 라디오그룹만 보이게 하고 모든 버튼을 보이지 않게 함
        btnNext.setVisibility(View.INVISIBLE);         btnFinish4.setVisibility(View.INVISIBLE);

        //시작버튼을 누르면 첫 번째 라디오그룹만 보이게 함
        btnStart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg1.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                rg2.setVisibility(View.INVISIBLE);
                btnFinish4.setVisibility(View.INVISIBLE);
            }
        });

        //결승전으로 올라갈 도서들 저장할 인덱스
        int fin_index[] = new int [2];

        //첫번째 선택을 한뒤 다음버튼 누르면, 두번째 라디오그룹을 보이게 하고 첫번째 라디오그룹은 숨김
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg1.setVisibility(View.INVISIBLE);
                btnNext.setVisibility(View.INVISIBLE);
                rg2.setVisibility(View.VISIBLE);
                btnFinish4.setVisibility(View.VISIBLE);

                if(rb[0].isChecked() == true){
                    fin_index[0] = quarter_index[0];
                    voteCount[quarter_index[0]]++;
                }
                else{
                    fin_index[0] = quarter_index[1];
                    voteCount[quarter_index[1]]++;
                }

            }
        });

        //마지막 Finish 버튼 누르면, 4강의 페이지로 이동하기
        btnFinish4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rb[2].isChecked() == true){
                    fin_index[1] = quarter_index[2];
                    voteCount[quarter_index[2]]++;

                }
                else{
                    fin_index[1] = quarter_index[3];
                    voteCount[quarter_index[3]]++;
                }

                Intent intent = new Intent(quarter.this, finals.class);
                intent.putExtra("fin_index", fin_index);
                intent.putExtra("voteCount", voteCount);
                startActivity(intent);
            }
        });

        cursor.close();
        sqlDB.close();

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
