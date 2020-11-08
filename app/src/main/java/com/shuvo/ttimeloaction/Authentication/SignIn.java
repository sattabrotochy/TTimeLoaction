package com.shuvo.ttimeloaction.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shuvo.ttimeloaction.InternertCheck.InternetCheck;
import com.shuvo.ttimeloaction.MainActivity;
import com.shuvo.ttimeloaction.R;

public class SignIn extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    String currentuser;
    TextView google_signIn,Enter;
    FirebaseAuth mAuth;
    LinearLayout EnterLiner,googleliner;
    ProgressDialog progressDialog;
    private  static  String TAG="SignIn";
    Context context=SignIn.this;
    InternetCheck check;
    private final static  int Request_code=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        google_signIn=findViewById(R.id.google_signIn);
        Enter=findViewById(R.id.Enter);
        mAuth=FirebaseAuth.getInstance();
        EnterLiner=findViewById(R.id.EnterLiner);
        googleliner=findViewById(R.id.googleliner);
        locationCheck();

        progressDialog=new ProgressDialog(this);
        check=new InternetCheck(this);
        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                interNetCheck();

            }
        });


        createAccount();


        google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }



    private void interNetCheck() {
        if (check.isConnected())
        {
            goToMainActivity();
        }
        else

        {
            Toast.makeText(context, "Internet connection is required ", Toast.LENGTH_SHORT).show();

        }

    }

    private void goToMainActivity()
    {
        Intent intent=new Intent(context, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(context);
        finish();

    }

    private void locationCheck() {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        // notify user
        if(!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(context)
                    .setMessage("Please turn on Location to continue")
                    .setPositiveButton("Open Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("cancel",null)
                    .show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){

           googleliner.setVisibility(View.INVISIBLE);
           EnterLiner.setVisibility(View.VISIBLE);
        }

        AllowLoaction();
    }
    private void AllowLoaction()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, Request_code);

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Request_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Your location On", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void createAccount() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        progressDialog.setMessage("wait.........");
        progressDialog.show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(SignIn.this, "Successful login", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                Toast.makeText(this, e.toString()+"Google sign in failed", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            googleliner.setVisibility(View.INVISIBLE);
                            EnterLiner.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignIn.this, "fail ", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }


}