package kr.ac.jbnu.se.stkim.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kr.ac.jbnu.se.stkim.ProfileActivity;
import kr.ac.jbnu.se.stkim.R;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    private Button bookSearch;
    private Button logoutButton;
    private Button siteButton;
    private Button userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookSearch = (Button) findViewById(R.id.bookSearch);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        siteButton = (Button) findViewById(R.id.siteButton);
        userInfo = (Button) findViewById(R.id.userInfo);


        bookSearch.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        siteButton.setOnClickListener(this);
        userInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==bookSearch){
            startActivity(new Intent(this, BookListActivity.class));
        }
        if(view==logoutButton){
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if(view==siteButton){
            startActivity(new Intent(this, WebActivity.class));
        }
        if(view==userInfo){
            startActivity(new Intent(this, UserInfoActivity.class));
        }
    }
}
