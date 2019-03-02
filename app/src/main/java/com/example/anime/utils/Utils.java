package com.example.anime.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.example.anime.mvp.videolist.MainActivity;
import com.example.anime.mvp.videolist.VideosDashboard;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class Utils {
    private Context context;
    private FirebaseAuth mAuth;
    public Utils(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean setDurationToZero(long currentDuration, long totalDuration){
        return (currentDuration) >= (totalDuration - 10);
    }

    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Firebase sign out
                mAuth.signOut();
                //Google sign out
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context,googleSignInOptions);
                Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient()).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                      Intent intent = new Intent(context, MainActivity.class);
                      context.startActivity(intent);
                    }
                });
                ((VideosDashboard)context).finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
