package com.indaco.cs146movieapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    /* declare member variables, do not define until onCreate
         */
    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button mSignUpButton;
    protected ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Define the member variables and assign them to respective Views from xml
        mUsername=(EditText)findViewById(R.id.signup_username);
        mPassword=(EditText)findViewById(R.id.signup_password);
        mEmail=(EditText)findViewById(R.id.signup_email);
        mSignUpButton=(Button)findViewById(R.id.signup_button);
        mProgress=(ProgressBar)findViewById(R.id.signup_progress);
        mProgress.setVisibility(View.INVISIBLE);

        //What happens when you click the button
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Local variables store string versions of user input
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                //Condition: if one of the fields is empty, alert user to fill it all in.
                if(username.isEmpty()||password.isEmpty()||email.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_messsage)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                }else{ //Otherwise, create a new user
                    //display progress bar
                    mProgress.setVisibility(View.VISIBLE);

                    //Create new user and set the fields. Parse.com made the set methods already
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);

                    /*signUpInBackground method attempts to add user in the background (good!)
                    * and to then get back to us using SignUpCallback whether things went well
                    * or not
                    */
                    user.signUpInBackground(new SignUpCallback() {
                        //done is automatically created, exception is thrown using ParseException
                        @Override
                        public void done(ParseException e) {
                            mProgress.setVisibility(View.INVISIBLE);

                            if(e==null){//Success!

                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{//Something went wrong, log and alert user
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.signup_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
