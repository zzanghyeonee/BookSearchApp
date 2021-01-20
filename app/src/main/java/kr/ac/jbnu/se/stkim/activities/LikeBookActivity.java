package kr.ac.jbnu.se.stkim.activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import kr.ac.jbnu.se.stkim.R;
import kr.ac.jbnu.se.stkim.adapters.BookAdapter;
import kr.ac.jbnu.se.stkim.models.Book;
import kr.ac.jbnu.se.stkim.net.BookClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Scanner;


public class LikeBookActivity extends ActionBarActivity {

    private TextView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_book);
        edit = (TextView) findViewById(R.id.textView01);
        try {
            FileInputStream is;
            is = openFileInput("text.txt");
            if(is !=null){
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String str = reader.readLine();
                StringBuffer sb = new StringBuffer();
                while((str != null)) {
                    sb.append(str+"\n");
                    str = reader.readLine();
                }
                is.close();
                edit.setText(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
