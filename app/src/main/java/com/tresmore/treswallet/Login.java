package com.tresmore.treswallet;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * A login screen that offers login via email/password.
 */
public class Login extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private ProgressBar pbbar;

    private ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //connectionClass = new ConnectionClass();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        pbbar = (ProgressBar) findViewById(R.id.login_progress);
        pbbar.setVisibility(View.GONE);
        password.setTypeface(null, Typeface.BOLD);
        password.setTransformationMethod(new PasswordTransformationMethod());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String STRusername = username.getText().toString();
//                String STRpassword = password.getText().toString();
//                SharedPreferences prefs = getSharedPreferences("MA", MODE_PRIVATE);
//                prefs.edit().putString("UN", STRusername).commit();
//                prefs.edit().putString("PW", STRpassword).commit();
//                Intent intent = new Intent(Login.this, Dashboard.class);
//                startActivity(intent);
//            }
//        });
    }

    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        String STRusername = username.getText().toString();
        String STRpassword = password.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(Login.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                SharedPreferences prefs = getSharedPreferences("MA", MODE_PRIVATE);
                prefs.edit().putString("UN", STRusername).commit();
                prefs.edit().putString("PW", STRpassword).commit();
                Intent i = new Intent(Login.this, Dashboard.class);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (STRusername.trim().equals("") || STRpassword.trim().equals(""))
                z = "Please enter username and password!";
            else {
                try {
                    Connection con = connectionClass.CONN();

                    if (con == null) {
                        z = "Error in connection with SQL server!";
                    } else {
                        String query = "select loginid, password from users where loginid='" + STRusername + "' and password='" + STRpassword + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            z = "Login Successful!";
                            isSuccess = true;
                        } else {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }
}

