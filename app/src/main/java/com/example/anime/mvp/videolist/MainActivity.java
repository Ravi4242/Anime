package com.example.anime.mvp.videolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.anime.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    SignInButton signInButton;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = findViewById(R.id.btn_google_sign_in);
        signInButton.setOnClickListener(this);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null);
            logIn(signInAccount);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_google_sign_in:
                Intent dashboard = googleSignInClient.getSignInIntent();
                startActivityForResult(dashboard,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            assert account != null;
            logIn(account);
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    private void logIn(GoogleSignInAccount account){
        Log.d("intent: ", "VideoDashboard");
        Intent logInIntent = new Intent(this, VideosDashboard.class);
        logInIntent.putExtra("Email",account.getEmail());
        startActivity(logInIntent);
        finish();
    }
}
