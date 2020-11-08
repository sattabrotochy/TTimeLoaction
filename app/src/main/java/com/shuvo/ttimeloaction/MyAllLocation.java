package com.shuvo.ttimeloaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shuvo.ttimeloaction.Class.DataModel;
import com.shuvo.ttimeloaction.Volly.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAllLocation extends AppCompatActivity {


    RecyclerView userLocationRecyView;
    ProgressDialog progressDialog;
    String CURRENT_POST,currentUser;
    LinearLayoutManager layoutManager;
    Context context=MyAllLocation.this;
    LoactionAdapter adapter;
    ArrayList<DataModel> dataModelArrayList;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_all_location);

        userLocationRecyView=findViewById(R.id.userLocationRecyView);

        dataModelArrayList=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser().getUid();
        progressDialog=new ProgressDialog(this);


        dataLoad();


    }

    private void dataLoad() {

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
                String shl = handler3.sendGetRequest("https://newhmanagement.000webhostapp.com/userLocationRetrieve.php?firebase_id=" + currentUser);
                return shl;

            }
        }
        GetJSON gjdl = new GetJSON();
        gjdl.execute();
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
            userLocationRecyView.setHasFixedSize(true);
            userLocationRecyView.setLayoutManager(layoutManager);
            userLocationRecyView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Data not Found", Toast.LENGTH_SHORT).show();
        }

    }
}