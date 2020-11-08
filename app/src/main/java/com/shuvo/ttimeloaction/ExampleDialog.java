package com.shuvo.ttimeloaction;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText edTxTGetCode;
    FirebaseAuth auth;
    String currentUser;
    Button CodeId;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        edTxTGetCode=view.findViewById(R.id.edTxTGetCode);
        CodeId=view.findViewById(R.id.CodeId);
        CodeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataLoad();
            }
        });


        return builder.create();
    }

    private void dataLoad() {
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser().getUid();
        edTxTGetCode.setText(currentUser);


    }

}
