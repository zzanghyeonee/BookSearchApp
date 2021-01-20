package kr.ac.jbnu.se.stkim.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import kr.ac.jbnu.se.stkim.R;
import kr.ac.jbnu.se.stkim.models.Book;
import kr.ac.jbnu.se.stkim.net.BookClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class BookDetailActivity extends ActionBarActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPageCount;
    private BookClient client;
    private Button likebutton;
    private Button dislikebutton;
    private EditText likeText;
    private EditText dislikeText;
    DatabaseReference myRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        Button FB = (Button) findViewById(R.id.like_book);
        final String FILENAME = "text.txt";
        // Use the book to populate the data into our views
        Book book = (Book) getIntent().getSerializableExtra(BookListActivity.BOOK_DETAIL_KEY);
        loadBook(book);

        likeText = (EditText) findViewById(R.id.likeText);
        likebutton = (Button) findViewById(R.id.likebutton);
        dislikeText = (EditText) findViewById(R.id.dislikeText);
        dislikebutton = (Button) findViewById(R.id.dislikebutton);

        myRef= FirebaseDatabase.getInstance().getReference().child("msg");
        myRef=FirebaseDatabase.getInstance().getReference().child("dislike");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long numberOfLike = (Long) dataSnapshot.getValue();
                likeText.setText(String.valueOf(numberOfLike));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long numberOfdisLike = (Long) dataSnapshot.getValue();
                dislikeText.setText(String.valueOf(numberOfdisLike));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef = FirebaseDatabase.getInstance().getReference().child("msg");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long numberOfLike = (Long) dataSnapshot.getValue();
                        numberOfLike++;
                        myRef.setValue(numberOfLike);
                        likeText.setText(String.valueOf(numberOfLike));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });




        dislikebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = FirebaseDatabase.getInstance().getReference().child("dislike");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long numberOfdisLike = (Long) dataSnapshot.getValue();
                        numberOfdisLike++;
                        myRef.setValue(numberOfdisLike);
                        dislikeText.setText(String.valueOf(numberOfdisLike));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        FB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                    File dir = new File("/data/data/"+getPackageName()+"/files");
                    if(!dir.exists()) {
                        dir.mkdir();
                    }
                    File file = new File("/data/data/"+getPackageName()+"/files/text.txt");
                    try{
                        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
                        PrintWriter writer = new PrintWriter(fos);
                        String str = tvTitle.getText().toString();
                        writer.println(str);
                        writer.close();
                        Toast.makeText(getApplicationContext(), "즐겨찾기 되었습니다.", Toast.LENGTH_SHORT)
                                .show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        // Populate data for the book
    private void loadBook(Book book) {
        //change activity title
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(this).load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        // fetch extra book data from books API
        client = new BookClient();

//        client.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                try {
//                    if (response.has("publishers")) {
//                        // display comma separated list of publishers
//                        final JSONArray publisher = response.getJSONArray("publishers");
//                        final int numPublishers = publisher.length();
//                        final String[] publishers = new String[numPublishers];
//                        for (int i = 0; i < numPublishers; ++i) {
//                            publishers[i] = publisher.getString(i);
//                        }
//                        tvPublisher.setText(TextUtils.join(", ", publishers));
//                    }
//                    if (response.has("number_of_pages")) {
//                        tvPageCount.setText(Integer.toString(response.getInt("number_of_pages")) + " pages");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            setShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent() {
        ImageView ivImage = (ImageView) findViewById(R.id.ivBookCover);
        final TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        // Construct a ShareIntent with link to image
        Intent shareIntent = new Intent();
        // Construct a ShareIntent with link to image
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, (String)tvTitle.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        // Launch share menu
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

    }

    // Returns the URI path to the Bitmap displayed in cover imageview
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
