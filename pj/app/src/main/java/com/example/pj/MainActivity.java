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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    LinearLayout baseLayout;

    RadioGroup rg1, rg2, rg3, rg4;

    Button btnStart8, btnNext1, btnNext2, btnNext3, btnFinish8;

    myDBHelper Helper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("오늘 읽을 책은?");

        //메인 액티비티의 리니어 레이아웃의 id 값 가져옴
        baseLayout = (LinearLayout) findViewById(R.id.baseLayout);

        //라디오그룹 id 값 가져옴
        rg1 = (RadioGroup)findViewById(R.id.rg1);   rg2 = (RadioGroup)findViewById(R.id.rg2);   rg3 = (RadioGroup)findViewById(R.id.rg3);   rg4 = (RadioGroup)findViewById(R.id.rg4);

        //버튼 id값 가져옴
        btnStart8 = (Button)findViewById(R.id.btnStart8);   btnNext1 = (Button)findViewById(R.id.btnNext1);     btnNext2 = (Button)findViewById(R.id.btnNext2);
        btnNext3 = (Button)findViewById(R.id.btnNext3);     btnFinish8 = (Button)findViewById(R.id.btnFinish8);

        //라디오 버튼 id 값 배열 형태로 가져옴
        Integer rbID[] = {R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4, R.id.radio5, R.id.radio6, R.id.radio7, R.id.radio8};
        RadioButton rb[] = new RadioButton[8];
        for(int i=0; i<8; i++){
            rb[i] = (RadioButton)findViewById(rbID[i]);
        }

        //이미지뷰 id 값 배열 형태로 가져옴
        Integer imgID[] = {R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5, R.id.image6, R.id.image7, R.id.image8};
        ImageView img[] = new ImageView[8];
        for(int i=0; i<8; i++){
            img[i] = (ImageView)findViewById(imgID[i]);
        }

        Helper = new myDBHelper(this);
        sqlDB = Helper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM readTBL;", null);

        byte[] bytes;
        Bitmap bitmap;

        //4강전, 결승전으로 선택된 책 정보를 보내기 위함.
        String strTitle[] = new String[8];

        //디비의 이미지 가져와서 set하기
        for(int i=0; i<8; i++){
            cursor.moveToNext();
            strTitle[i] = cursor.getString(0);
            bytes = cursor.getBlob(5);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            img[i].setImageBitmap(bitmap);

        }

        cursor.close();
        sqlDB.close();

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

        // 투표수(배열)를 저장할 변수 선언 및 초기화
        final int voteCount[] = new int[8];
        for (int i = 0; i < 8; i++) {
            voteCount[i] = 0;
        }

        //4강전으로 올라갈 도서들 저장할 인덱스
        int index[] = new int [4];

        //첫번째 선택을 한뒤 다음버튼 누르면, 두번째 라디오그룹을 보이게 하고 첫번째 라디오그룹은 숨김
        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg1.setVisibility(View.INVISIBLE);  btnNext1.setVisibility(View.INVISIBLE);
                rg2.setVisibility(View.VISIBLE);    btnNext2.setVisibility(View.VISIBLE);

                if(rb[0].isChecked() == true){
                    index[0] = 0;
                    voteCount[0]++; //해당인덱스 투표수 증가
                }
                else{
                    index[0] = 1;
                    voteCount[1]++;
                }

            }
        });

        //두번째 선택을 한뒤 다음버튼 누르면, 세번째 라디오그룹을 보이게 하고 두번째 라디오그룹은 숨김
        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg2.setVisibility(View.INVISIBLE);  btnNext2.setVisibility(View.INVISIBLE);
                rg3.setVisibility(View.VISIBLE);    btnNext3.setVisibility(View.VISIBLE);

                if(rb[2].isChecked() == true){
                    index[1] = 2;
                    voteCount[2]++;
                }
                else{
                    index[1] = 3;
                    voteCount[3]++;

                }

            }
        });

        //세번째 선택을 한뒤 다음버튼 누르면, 네번째 라디오그룹을 보이게 하고 세번째 라디오그룹은 숨김
        btnNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rg3.setVisibility(View.INVISIBLE);  btnNext3.setVisibility(View.INVISIBLE);
                rg4.setVisibility(View.VISIBLE);    btnFinish8.setVisibility(View.VISIBLE);

                if(rb[4].isChecked() == true){
                    index[2] = 4;
                    voteCount[4]++;
                }
                else{
                    index[2] = 5;
                    voteCount[5]++;
                }

            }
        });

        //마지막 Finish 버튼 누르면, 4강의 페이지로 이동하기
        btnFinish8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rb[6].isChecked() == true){
                    index[3] = 6;
                    voteCount[6]++;
                }
                else{
                    index[3] = 7;
                    voteCount[7]++;
                }

                Intent intent = new Intent(MainActivity.this, quarter.class);
                intent.putExtra("index", index);
                intent.putExtra("voteCount", voteCount);
                startActivity(intent);
            }
        });

    }

    public static class myDBHelper extends SQLiteOpenHelper {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //위시리스트 조회 페이지로 이동
            case R.id.ListSelect:
                Intent intent1 = new Intent(getApplicationContext(), list_select.class);
                startActivity(intent1);
                return true;

            //도서관 도서 추가 페이지로 이동
            case R.id.LibInsert:
                Intent intent2 = new Intent(getApplicationContext(), lib_insert.class);
                startActivity(intent2);
                return true;

            //위시리스트 삭제 페이지로 이동
            case R.id.ListDelete:
                Intent intent3 = new Intent(getApplicationContext(), list_delete.class);
                startActivity(intent3);
                return true;

        }
        return false;
    }


}