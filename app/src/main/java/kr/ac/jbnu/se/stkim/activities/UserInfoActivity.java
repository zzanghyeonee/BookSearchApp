package kr.ac.jbnu.se.stkim.activities;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

import kr.ac.jbnu.se.stkim.R;

public class UserInfoActivity extends ActionBarActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button imageButton;
    private Button likeBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageButton = (Button) findViewById(R.id.imageButton);
        likeBooks = (Button) findViewById(R.id.like_book);

        likeBooks.setOnClickListener(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestcode,  int resultcode, Intent data){
        if(requestcode==1){
            if(resultcode==RESULT_OK){
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view==likeBooks){
            startActivity(new Intent(this, LikeBookActivity.class));
        }
    }
}
