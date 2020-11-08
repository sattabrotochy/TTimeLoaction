package com.shuvo.ttimeloaction.InternertCheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetCheck {



    Context context;

    public InternetCheck(Context context) {
        this.context=context;


    }
    public boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null){
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if(info!=null){
                if(info.getState()==NetworkInfo.State.CONNECTED);
                return true;
            }
        }
        return false;
    }
}
