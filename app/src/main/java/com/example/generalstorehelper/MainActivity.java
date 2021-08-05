package com.example.generalstorehelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.generalstorehelper.AddItem.AddItem;
import com.example.generalstorehelper.MakeTransaction.SalePurchase;
import com.example.generalstorehelper.ManageItems.SearchAndEdit;
import com.example.generalstorehelper.Repository.Repo;
import com.example.generalstorehelper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        AsyncTask.execute(() -> new Repo(getApplicationContext()));
        setOnclicks();
    }

    private void setOnclicks() {
        binding.Activity1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddItem.class);
            startActivity(intent);
        });

        binding.Activity2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchAndEdit.class);
            startActivity(intent);
        });

        binding.Activity3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SalePurchase.class);
            startActivity(intent);
        });
    }

}