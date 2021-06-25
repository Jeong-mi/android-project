package com.example.pj;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class lib_insert extends AppCompatActivity {

    AutoCompleteTextView autoSelect;
    Button btnSelect;

    Button btnInsert;

    ImageView ivCover;
    EditText etTitle, etAuthor, etPublisher, etPrice, etInform;

    myDBHelper Helper, BHelper;
    SQLiteDatabase sqlDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_insert);
        setTitle("도서관 도서 추가");

        //조회 검색창과 버튼 위젯
        autoSelect = (AutoCompleteTextView)findViewById(R.id.autoSelect);
        btnSelect = (Button)findViewById(R.id.btnSelect);

        //추가버튼 위젯
        btnInsert = (Button) findViewById(R.id.btnInsert);

        //결과창 위젯
        ivCover = (ImageView)findViewById(R.id.ivCover);

        etTitle = (EditText)findViewById(R.id.etTitle);
        etAuthor = (EditText)findViewById(R.id.etAuthor);
        etPublisher = (EditText)findViewById(R.id.etPublisher);
        etPrice = (EditText)findViewById(R.id.etPrice);
        etInform = (EditText)findViewById(R.id.etInform);

        //readTBL불러오기
        Helper = new myDBHelper(this, "readDB.db");

        //bookTBL 불러오기
        BHelper = new myDBHelper(this, "bookDB.db");

        //버튼 클릭시에만 보이게 함
        ivCover.setVisibility(View.INVISIBLE);
        etTitle.setVisibility(View.INVISIBLE);
        etAuthor.setVisibility(View.INVISIBLE);
        etPublisher.setVisibility(View.INVISIBLE);
        etPrice.setVisibility(View.INVISIBLE);
        etInform.setVisibility(View.INVISIBLE);
        btnInsert.setVisibility(View.INVISIBLE);

        //bookTBL의 도서(도서관의 도서) 검색
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //bookTBL에서 데이터 가져오기 위해 읽기모드로
                sqlDB = BHelper.getReadableDatabase();

                String[] items = { "작은 별이지만 빛나고 있어", "프리워커스", "운의 알고리즘", "달러구트 꿈 백화점",
                        "끌리는 말투 호감 가는 말투", "주린이가 가장 알고싶은 최다질문 TOP 77", "아몬드",  "공정하다는 착각", "우리는 조구만 존재야",
                        "미라클 모닝", "주린이도 술술 읽는 친절한 주식책", "작은 아씨들", "끌리는 문장은 따로 있다", "끌리는 사람은 매출이 다르다",
                        "프리즘", "미드나잇 라이브러리", "꽃을 보듯 너를 본다", "공간의 미래", "내가 원하는 것을 나도 모를 때",
                        "우리가 빛의 속도로 갈 수 없다면", "우리의 불행은 당연하지 않습니다", "운다고 달라지는 일은 아무것도 없겠지만",
                        "우리가 함께 장마를 볼 수도 있겠습니다"};

                AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.autoSelect);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, items);
                auto.setAdapter(adapter);

                String title = autoSelect.getText().toString();

                //입력받은 title 행의 데이터를 bookTBL에서 가져옴
                Cursor cursor = sqlDB.rawQuery("select * from bookTBL where title = ?", new String[] { title });
                cursor.moveToNext();
                int total = cursor.getCount();

                //조회한 책이 book 테이블에 없을 시 토스트 메시지 띄우기
                if(total==0){
                    //조회한 책이 book 테이블에 없을 경우 안보이게 함
                    ivCover.setVisibility(View.INVISIBLE);
                    etTitle.setVisibility(View.INVISIBLE);
                    etAuthor.setVisibility(View.INVISIBLE);
                    etPublisher.setVisibility(View.INVISIBLE);
                    etPrice.setVisibility(View.INVISIBLE);
                    etInform.setVisibility(View.INVISIBLE);
                    btnInsert.setVisibility(View.INVISIBLE);


                    //bookTBL에 관련 도서가 없을 경우 토스트 메시지
                    Toast.makeText(getApplicationContext(), "디비에 관련 데이터가 없습니다!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    //조회한 책이 book 테이블에 있을 경우만 안보이게 함
                    ivCover.setVisibility(View.VISIBLE);
                    etTitle.setVisibility(View.VISIBLE);
                    etAuthor.setVisibility(View.VISIBLE);
                    etPublisher.setVisibility(View.VISIBLE);
                    etPrice.setVisibility(View.VISIBLE);
                    etInform.setVisibility(View.VISIBLE);
                    btnInsert.setVisibility(View.VISIBLE);


                    etTitle.setText(cursor.getString(0));
                    etAuthor.setText(cursor.getString(1));
                    etPublisher.setText(cursor.getString(2));
                    etPrice.setText(cursor.getString(3));
                    etInform.setText(cursor.getString(4));

                    byte[] bytes;
                    Bitmap bitmap;
                    bytes = cursor.getBlob(5);
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ivCover.setImageBitmap(bitmap);
                }

                cursor.close();
                sqlDB.close();

            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //투표갯수를 8개로 제한하기 위해서 현재 read 테이블의 책의 개수를 계산하기
                sqlDB = Helper.getReadableDatabase();
                Cursor c;
                c = sqlDB.rawQuery("SELECT * FROM readTBL;", null);
                int cnt_candi=0;
                while(c.moveToNext()){
                    cnt_candi = cnt_candi + 1;
                }

                String[] items = { "작은 별이지만 빛나고 있어", "프리워커스", "운의 알고리즘", "달러구트 꿈 백화점",
                        "끌리는 말투 호감 가는 말투", "주린이가 가장 알고싶은 최다질문 TOP 77", "아몬드",  "공정하다는 착각", "우리는 조구만 존재야",
                        "미라클 모닝", "주린이도 술술 읽는 친절한 주식책", "작은 아씨들", "끌리는 문장은 따로 있다", "끌리는 사람은 매출이 다르다",
                        "프리즘", "미드나잇 라이브러리", "꽃을 보듯 너를 본다", "공간의 미래", "내가 원하는 것을 나도 모를 때",
                        "우리가 빛의 속도로 갈 수 없다면", "우리의 불행은 당연하지 않습니다", "운다고 달라지는 일은 아무것도 없겠지만",
                        "우리가 함께 장마를 볼 수도 있겠습니다"};

                AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.autoSelect);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, items);
                auto.setAdapter(adapter);

                //입력받은 문자열을 title에 저장
                String title = autoSelect.getText().toString();

                //bookTBL에서 데이터 가져오기 위해 읽기모드로
                sqlDB = BHelper.getReadableDatabase();

                //입력받은 title 행의 데이터를 bookTBL에서 가져옴
                Cursor cursor = sqlDB.rawQuery("select * from bookTBL where title = ?", new String[] { title });
                cursor.moveToNext();
                int total = cursor.getCount();

                //bookTBL에 관련 도서가 없을 경우 고려
                if(total != 0 && cursor != null){

                    //투표개수가 8개가 넘지 않을 때만을 고려
                    if(cnt_candi < 8){
                        String Title = cursor.getString(0);
                        String Author = cursor.getString(1);
                        String Publisher = cursor.getString(2);
                        String Price = cursor.getString(3);
                        String Inform = cursor.getString(4);
                        byte[] Cover = cursor.getBlob(5);

                        //가져온 데이터를 readTBL에 삽입하기
                        sqlDB = Helper.getWritableDatabase();
                        String sql = "INSERT INTO readTBL VALUES (?, ?, ?, ?, ?, ?)";
                        SQLiteStatement statement = sqlDB.compileStatement(sql);
                        statement.bindString(1, Title);
                        statement.bindString(2, Author);
                        statement.bindString(3, Publisher);
                        statement.bindString(4, Price);
                        statement.bindString(5, Inform);
                        statement.bindBlob(6, Cover);
                        statement.execute();

                        Toast.makeText(getApplicationContext(), "추가됐습니다!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //현재 투표개수가 8개 이상이면 거부하고 토스트 메시지
                        Toast.makeText(getApplicationContext(), "저장할 수 있는 데이터가 꽉 찼습니다!",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }

    public static class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context, String name) {
            super(context, name, null, 1);
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
