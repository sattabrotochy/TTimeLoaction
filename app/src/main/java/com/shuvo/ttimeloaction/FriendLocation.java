package com.shuvo.ttimeloaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shuvo.ttimeloaction.Class.DataModel;
import com.shuvo.ttimeloaction.Volly.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendLocation extends AppCompatActivity {


    RecyclerView recyViewFndList;
    EditText edUserID;
    Button DoneBtn;
    String userCode, CURRENT_POST;
    LinearLayoutManager layoutManager;
    ArrayList<DataModel> dataModelArrayList;
    ProgressDialog progressDialog;
    Context context = FriendLocation.this;
    LoactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_location);


        edUserID = findViewById(R.id.edUserID);
        recyViewFndList = findViewById(R.id.recyViewFndList);
        DoneBtn = findViewById(R.id.DoneBtn);
        dataModelArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);


        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DataLoad();
            }
        });


    }

    private void DataLoad() {
        userCode = edUserID.getText().toString();
        if (userCode.equals("")) {
            edUserID.setError("Enter Code");
        } else {

            class GetJSON extends AsyncTask<Void, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressDialog.show();
                }

                @Override
                protected void onPostExecute(String shl) {
                    super.onPostExecute(shl);
                    CURRENT_POST = shl;
                    currentPostShow();
                    progressDialog.dismiss();
                }

                @Override
                protected String doInBackground(Void... params) {

                    RequestHandler handler3 = new RequestHandler();
                    String shl = handler3.sendGetRequest("https://newhmanagement.000webhostapp.com/userLocationRetrieve.php?firebase_id=" + userCode);
                    return shl;

                }
            }
            GetJSON gjdl = new GetJSON();
            gjdl.execute();

        }
    }

    private void currentPostShow() {
        try {
            JSONObject jsonObject = new JSONObject(CURRENT_POST);
            JSONArray result = jsonObject.getJSONArray("MyPost");

            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                dataModelArrayList.add(new DataModel(


                        object.getString("firebase_id"),
                        object.getString("loaction"),
                        object.getString("date"),
                        object.getString("time")


                ));
            }


            adapter = new LoactionAdapter(context, dataModelArrayList);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyViewFndList.setHasFixedSize(true);
            recyViewFndList.setLayoutManager(layoutManager);
            recyViewFndList.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(context);
    }
}
