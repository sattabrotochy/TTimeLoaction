package com.shuvo.ttimeloaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shuvo.ttimeloaction.Authentication.SignIn;
import com.shuvo.ttimeloaction.Foreground.ExampleService;

public class MainActivity extends AppCompatActivity {


    LinearLayout Start_serviceID, Stop_serviceID, locationId, friendLocationID;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        Start_serviceID = findViewById(R.id.Start_serviceID);
        Stop_serviceID = findViewById(R.id.Stop_serviceID);
        locationId = findViewById(R.id.locationId);
        friendLocationID = findViewById(R.id.friendLocationID);


        friendLocationID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FriendLocation.class));
            }
        });
        locationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyAllLocation.class));


            }
        });

        Start_serviceID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startServiceUser();
            }
        });
        Stop_serviceID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopserviceUser();
            }
        });

    }


    private void startServiceUser() {
        String input = "Hello user";
        Intent intent = new Intent(MainActivity.this, ExampleService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);

            startLoaction();
            Toast.makeText(this, "lalal", Toast.LENGTH_SHORT).show();
        } else {
            startService(intent);
            Toast.makeText(this, "lalal22", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLoaction() {

    }

    private void stopserviceUser() {
        Intent intent = new Intent(MainActivity.this, ExampleService.class);
        stopService(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu_item:
                auth.signOut();
                goToSignInActivity();
                return true;
            case R.id.getCode_menu_item:
                openDialog();

                break;

            default:
              break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToSignInActivity() {
        startActivity(new Intent(MainActivity.this, SignIn.class));
        finish();
    }
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
}