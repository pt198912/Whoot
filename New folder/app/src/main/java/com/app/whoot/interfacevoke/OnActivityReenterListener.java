package com.app.whoot.interfacevoke;

import android.content.Intent;

public interface OnActivityReenterListener{
        void onActivityReenterCallback(int resultCode, Intent data);
    }