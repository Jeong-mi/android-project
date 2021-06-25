package com.example.pj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class finals extends AppCompatActivity {
    //extends Activity면 스타일이 다 없어짐..!

    RadioGroup rg1;
    Button btnStart, btnFinish;

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finals);

        setTitle("결승전 Fight");

        //라디오그룹 id 값 가져옴
        rg1 = (RadioGroup)findViewById(R.id.rg1);

        //버튼 id값 가져옴
        btnStart = (Button)findViewById(R.id.btnStart); btnFinish = (Button)findViewById(R.id.btnFinish);

        //라디오 버튼 id 값 배열 형태로 가져옴
        Integer rbID[] = {R.id.radio1, R.id.radio2};
        RadioButton rb[] = new RadioButton[2];
        for(int i=0; i<2; i++){
            rb[i] = (RadioButton)findViewById(rbID[i]);
        }

        //이미지뷰 id 값 배열 형태로 가져옴
        Integer imgID[] = {R.id.image1, R.id.image2};
        ImageView img[] = new ImageView[2];
        for(int i=0; i<2; i++){
            img[i] = (ImageView)findViewById(imgID[i]);
        }


        Intent intent = getIntent();
        //4강전에 저장해둔 선택된 도서들의 인덱스를 fin_index에 저장
        int[] fin_index = intent.getIntArrayExtra("fin_index");

        //4강전까지 저장된 투표수
        int[] voteCount = intent.getIntArrayExtra("voteCount");

        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM readTBL;", null);

        byte[] bytes;
        Bitmap bitmap;

        //4강전에서 선택된 책들에 대한 이미지 불러오기
        cursor.moveToPosition(fin_index[0]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img[0].setImageBitmap(bitmap);

        cursor.moveToPosition(fin_index[1]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img[1].setImageBitmap(bitmap);

        //최종 읽을 책 int result;

        //마지막 Finish 버튼 누르면, 결과화면 페이지로 이동하기
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rb[0].isChecked() == true){
                    //result = fin_index[0];
                    voteCount[fin_index[0]]++;

                }
                else{
                    //result = fin_index[1];
                    voteCount[fin_index[1]]++;
                }

                Intent intent = new Intent(finals.this, result.class);
                intent.putExtra("voteCount", voteCount);
                startActivity(intent);
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
