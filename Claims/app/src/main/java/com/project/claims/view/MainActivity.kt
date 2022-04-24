package com.project.claims.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.project.claims.R
import com.project.claims.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setTitle(getString(R.string.add_claims))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
    }

    /*This method is to show icons on toolbar based on requirement*/
    override fun onCreateOptionsMenu(menu:Menu):Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}