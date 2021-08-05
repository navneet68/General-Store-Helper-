package com.example.generalstorehelper.AddItem;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.generalstorehelper.R;
import com.example.generalstorehelper.Repository.Repo;
import com.example.generalstorehelper.databinding.ActivityAddItemBinding;
import com.example.generalstorehelper.model.Item;
import com.example.generalstorehelper.scanner.barcodeScanner;

public class AddItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final int ScannerActivityRequestCode = 1;
    private ActivityAddItemBinding binding;
    private AddItemViewModel vModel;

    //SETTING UP VIEW AND TEXT WATCHERS WHEN ACTIVITY STARTS AND RESUMES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vModel = new ViewModelProvider(this).get(AddItemViewModel.class);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_item);
        setUI();
        setWatchers();
    }


    //IMPLEMENTING THE ADAPTER VIEW CLASS
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vModel.setUnit(parent.getItemAtPosition(position).toString());
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    //SETTING UP UI ACCORDING TO DATA IN VIEW-MODEL
    private void setUI() {
        binding.name.setText(vModel.getName());
        binding.massVolume.setText(vModel.getQuantity());
        binding.cp.setText(vModel.getCP());
        binding.sp.setText(vModel.getSP());
        binding.noItems.setText(vModel.getInStore());
        binding.textView9.setText(vModel.sizeInString());
        if(vModel.getUnit().equals("Kg"))
            binding.spinner.setSelection(0);
        if(vModel.getUnit().equals("L"))
            binding.spinner.setSelection(1);
        if(vModel.getUnit().equals("g"))
            binding.spinner.setSelection(2);
        if(vModel.getUnit().equals("ml"))
            binding.spinner.setSelection(3);
        if(vModel.getUnit().equals("Unit"))
            binding.spinner.setSelection(4);

    }
    //UPDATING VIEW-MODEL AS DATA IS CHANGED IN UI BY USER
    public void setWatchers() {
        TextWatcher textWatcherForName = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textWatcherForQuantity = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.setQuantity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textWatcherForCp = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.setCP(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textWatcherForSp = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.setSP(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textWatcherForInStore = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.setInStore(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        binding.name.addTextChangedListener(textWatcherForName);
        binding.massVolume.addTextChangedListener(textWatcherForQuantity);
        binding.cp.addTextChangedListener(textWatcherForCp);
        binding.sp.addTextChangedListener(textWatcherForSp);
        binding.noItems.addTextChangedListener(textWatcherForInStore);
        binding.spinner.setOnItemSelectedListener(this);
    }


    //ON-CLICK METHOD OF SCAN BUTTON
    public void openScanner(View view) {
        Intent intent = new Intent(this, barcodeScanner.class);
        startActivityForResult(intent, ScannerActivityRequestCode);
    }
    //ON-ACTIVITY-RESULT OF SCANNER ACTIVITY
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScannerActivityRequestCode) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                vModel.addBarcodes(data.getStringArrayListExtra("barcodes"));
                setUI();
            }
        }
    }

    //WHAT MY CREATE BUTTON DO->
    public void add(View view) {
        if(vModel.name.isEmpty() || vModel.quantity.isEmpty() || vModel.SP.isEmpty() || vModel.CP.isEmpty() || vModel.inStore.isEmpty())
            //IF EMPTY FIELDS EXIST, SHOW TOAST
        {
            Toast toast=Toast.makeText(this," Fill Mandatory Fields !",Toast.LENGTH_SHORT);
            View viewT=toast.getView();
            TextView x=viewT.findViewById(android.R.id.message);
            x.setTextColor(Color.parseColor("#FF0033"));
            toast.show();
        }
        else
            //OTHERWISE ADD ITEM TO REPOSITORY AND PROMPT USER TO PROCEED FURTHER
            {
                StringBuilder x= new StringBuilder();
                for(int i=0;i<vModel.Barcodes.size();i++)
                    x.append("/").append(vModel.Barcodes.get(i)).append("/");
            Item item = new Item(vModel.name,
                    Double.parseDouble(vModel.quantity),
                    vModel.getUnit(),
                    Double.parseDouble(vModel.SP),
                    Double.parseDouble(vModel.CP),
                    Double.parseDouble(vModel.inStore),
                    x.toString(),
                    vModel.Barcodes.size());

                AsyncTask.execute(() -> Repo.getDao().insert(item));

            vModel.clearAll();
            setUI();
                promptUserForFurtherAction();
        }

    }
    //TAKE ENDING DIRECTIONS FROM USER, WEATHER TO ADD ANOTHER ITEM OR FINISH THE ACTIVITY
    private void promptUserForFurtherAction() {
        AlertDialog informing_dialog = new AlertDialog.Builder(this).setMessage(R.string.added_the_item)
                .setPositiveButton(R.string.add_item_close_activity, (dialog, id) -> finish())
                .setNegativeButton("Add Other Item", (dialog, which) -> {

                })
                .create();
        informing_dialog.show();
    }
}