package com.example.pj;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

public class result extends AppCompatActivity {

    ImageView first_cover;
    RatingBar first_rbar;
    TextView first_title, first_author;

    ImageView second_cover;
    RatingBar second_rbar;
    TextView second_title, second_author;

    RatingBar third_rbar;
    RatingBar fourth_rbar;

    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        setTitle("오늘 읽을 책은?");

        Intent intent = getIntent();

        //결승전까지 저장된 투표수
        int[] voteResult = intent.getIntArrayExtra("voteCount");

        //순위를 저장한 배열 변수, 0으로 초기화
        int[] resultID = new int[8];

        for(int i=0; i<8; i++){
            resultID[i] = 0;
        }

        for(int i=0; i<8; i++){
            if(voteResult[i] == 3)
                resultID[0] = i;
        }

        for(int i=0; i<8; i++){
            if(voteResult[i] == 2)
                resultID[1] = i;
        }

        int k=2;
        for(int i=0; i<8; i++){
            if(voteResult[i] == 1){
                resultID[k] = i;
                k++;
            }
        }

        for(int i=0; i<8; i++){
            if(voteResult[i] == 0){
                resultID[k] = i;
                k++;
            }
        }

        //resultID[i]에 높은 순위부터 차례로 저장돼있다.

        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM readTBL;", null);

        byte[] bytes;
        Bitmap bitmap;

        //1순위 레이아웃 커스텀
        //1순위 금상 이미지 id 값 배열 형태로 가져오고 이미지 넣기
        Integer first_rankID[] = {R.id.first_rank1, R.id.first_rank2};
        ImageView first_rank[] = new ImageView[2];
        for(int i=0; i<2; i++){
            first_rank[i] = (ImageView)findViewById(first_rankID[i]);
            first_rank[i].setImageResource(R.drawable.gold_prize);
        }

        //1순위 도서 표지
        first_cover = (ImageView)findViewById(R.id.first_cover);

        cursor.moveToPosition(resultID[0]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        first_cover.setImageBitmap(bitmap);

        //별의 갯수 변화를 더 잘보이게 하기 위해 숫자를 2배시키기 예) 3-> 6
        first_rbar = (RatingBar)findViewById(R.id.first_rbar);
        first_rbar.setRating((float) 3*2);

        //1순위 제목, 저자
        first_title = (TextView)findViewById(R.id.first_title);
        first_author = (TextView)findViewById(R.id.first_author);

        first_title.setText(cursor.getString(0));
        first_author.setText(cursor.getString(1));


        //2순위 레이아웃 커스텀
        //2순위 은상 이미지 id 값 배열 형태로 가져오고 이미지 넣기
        Integer second_rankID[] = {R.id.second_rank1, R.id.second_rank2};
        ImageView second_rank[] = new ImageView[2];
        for(int i=0; i<2; i++){
            second_rank[i] = (ImageView)findViewById(second_rankID[i]);
            second_rank[i].setImageResource(R.drawable.silver_prize);
        }

        //2순위 도서 표지
        second_cover = (ImageView)findViewById(R.id.second_cover);

        cursor.moveToPosition(resultID[1]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        second_cover.setImageBitmap(bitmap);

        second_rbar = (RatingBar)findViewById(R.id.second_rbar);
        second_rbar.setRating((float) 2*2);

        //2순위 제목, 저자
        second_title = (TextView)findViewById(R.id.second_title);
        second_author = (TextView)findViewById(R.id.second_author);

        second_title.setText(cursor.getString(0));
        second_author.setText(cursor.getString(1));

        //3순위 레이아웃 커스텀
        //3순위 금상 이미지 id 값 배열 형태로 가져오고 이미지 넣기
        Integer third_rankID[] = {R.id.third_rank1, R.id.third_rank2};
        ImageView third_rank[] = new ImageView[2];
        for(int i=0; i<2; i++){
            third_rank[i] = (ImageView)findViewById(third_rankID[i]);
            third_rank[i].setImageResource(R.drawable.copper_prize);
        }

        //3순위 레이팅바
        third_rbar = (RatingBar)findViewById(R.id.third_rbar);
        third_rbar.setRating((float) 1*2);

        //3순위 도서 표지 id 값 배열 형태로 가져옴
        Integer third_coverID[] = {R.id.third_cover1, R.id.third_cover2};
        ImageView third_cover[] = new ImageView[2];
        for(int i=0; i<2; i++){
            third_cover[i] = (ImageView)findViewById(third_coverID[i]);
        }

        //3순위 도서 제목 id 값 배열 형태로 가져옴
        Integer third_titleID[] = {R.id.third_title1, R.id.third_title2};
        TextView third_title[] = new TextView[2];
        for(int i=0; i<2; i++){
            third_title[i] = (TextView)findViewById(third_titleID[i]);
        }

        //3순위 도서 저자 id 값 배열 형태로 가져옴
        Integer third_authorID[] = {R.id.third_author1, R.id.third_author2};
        TextView third_author[] = new TextView[2];
        for(int i=0; i<2; i++){
            third_author[i] = (TextView)findViewById(third_authorID[i]);
        }

        //첫번째 표지, 제목, 저자
        cursor.moveToPosition(resultID[2]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        third_cover[0].setImageBitmap(bitmap);
        third_title[0].setText(cursor.getString(0));
        third_author[0].setText(cursor.getString(1));

        //두번째 표지, 제목, 저자
        cursor.moveToPosition(resultID[3]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        third_cover[1].setImageBitmap(bitmap);
        third_title[1].setText(cursor.getString(0));
        third_author[1].setText(cursor.getString(1));


        //4순위 레이아웃 커스텀
        //4순위 금상 이미지 id 값 배열 형태로 가져오고 이미지 넣기

        //4순위 레이팅바
        fourth_rbar = (RatingBar)findViewById(R.id.fourth_rbar);
        fourth_rbar.setRating((float) 0*2);

        //4순위 도서 표지 id 값 배열 형태로 가져옴
        Integer fourth_coverID[] = {R.id.fourth_cover1, R.id.fourth_cover2, R.id.fourth_cover3, R.id.fourth_cover4};
        ImageView fourth_cover[] = new ImageView[4];
        for(int i=0; i<4; i++){
            fourth_cover[i] = (ImageView)findViewById(fourth_coverID[i]);
        }

        //4순위 도서 제목 id 값 배열 형태로 가져옴
        Integer fourth_titleID[] = {R.id.fourth_title1, R.id.fourth_title2, R.id.fourth_title3, R.id.fourth_title4};
        TextView fourth_title[] = new TextView[4];
        for(int i=0; i<4; i++){
            fourth_title[i] = (TextView)findViewById(fourth_titleID[i]);
        }

        //4순위 도서 저자 id 값 배열 형태로 가져옴
        Integer fourth_authorID[] = {R.id.fourth_author1, R.id.fourth_author2, R.id.fourth_author3, R.id.fourth_author4};
        TextView fourth_author[] = new TextView[4];
        for(int i=0; i<4; i++){
            fourth_author[i] = (TextView)findViewById(fourth_authorID[i]);
        }

        //첫번째 표지, 제목, 저자
        cursor.moveToPosition(resultID[4]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        fourth_cover[0].setImageBitmap(bitmap);

        fourth_title[0].setText(cursor.getString(0));
        fourth_author[0].setText(cursor.getString(1));

        //두번째 표지, 제목, 저자
        cursor.moveToPosition(resultID[5]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        fourth_cover[1].setImageBitmap(bitmap);

        fourth_title[1].setText(cursor.getString(0));
        fourth_author[1].setText(cursor.getString(1));

        //세번째 표지, 제목, 저자
        cursor.moveToPosition(resultID[6]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        fourth_cover[2].setImageBitmap(bitmap);

        fourth_title[2].setText(cursor.getString(0));
        fourth_author[2].setText(cursor.getString(1));

        //네번째 표지, 제목, 저자
        cursor.moveToPosition(resultID[7]);
        bytes = cursor.getBlob(5);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        fourth_cover[3].setImageBitmap(bitmap);

        fourth_title[3].setText(cursor.getString(0));
        fourth_author[3].setText(cursor.getString(1));


        Button btnPrev, btnNext;
        final ViewFlipper vFlipper;

        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);

        vFlipper = (ViewFlipper) findViewById(R.id.viewFlipper1);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vFlipper.showPrevious();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vFlipper.showNext();
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
