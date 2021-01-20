package kr.ac.jbnu.se.stkim.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import kr.ac.jbnu.se.stkim.ProfileActivity;
import kr.ac.jbnu.se.stkim.R;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private String id;
    private String pwd;
    private boolean saveLoginData;

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private CheckBox checkBox;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //profile activity
            //start a profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }

        progressDialog = new ProgressDialog(this);

        appData = getSharedPreferences("appData", Activity.MODE_PRIVATE);
        load();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextpassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        if (saveLoginData) {
            editTextEmail.setText(id);
            editTextPassword.setText(pwd);
            checkBox.setChecked(saveLoginData);
        }

        editTextEmail.requestFocus();

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            // stopping the function execution further
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            // stopping the function execution further
        }

        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    finish();
                }
            }
        });
    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();

        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("ID", editTextEmail.getText().toString().trim());
        editor.putString("PWD", editTextPassword.getText().toString().trim());

        editor.apply();
    }

    protected void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
        pwd = appData.getString("PWD", "");
    }




    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            save();
            userLogin();
            startActivity(new Intent(this, MainActivity.class));
        }
        if(view == textViewSignUp){
            finish();
            startActivity(new Intent(this, RegisterAcitivity.class));
        }
    }
}
