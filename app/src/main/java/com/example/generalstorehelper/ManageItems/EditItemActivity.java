package com.example.generalstorehelper.ManageItems;

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

import com.example.generalstorehelper.DialogueBoxes.DescriptionDialogueFragment;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.Repository.Repo;
import com.example.generalstorehelper.databinding.ActivityEditItemBinding;
import com.example.generalstorehelper.model.Item;
import com.example.generalstorehelper.scanner.barcodeScanner;

import java.util.Objects;

public class EditItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final int ScannerActivityRequestCode = 1;
    private ActivityEditItemBinding binding;
    private EditItemViewModel vModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vModel = new ViewModelProvider(this).get(EditItemViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_item);
        if(vModel.item==null)
        vModel.item= (Item) getIntent().getSerializableExtra("ItemToEdit");
        setUI();
        setWatchers();
    }

    private void setUI() {
        binding.nameE.setText(vModel.item.getName());
        binding.massVolumeE.setText(String.valueOf(vModel.item.getQuantity()));
        binding.cpE.setText(String.valueOf(vModel.item.getCP()));
        binding.spE.setText(String.valueOf(vModel.item.getSP()));
        binding.noItemsE.setText(String.valueOf(vModel.item.getInStore()));
        binding.textView9E.setText(vModel.sizeInString());
        if(Objects.equals(vModel.item.getUnit(), "Kg"))
            binding.spinnerE.setSelection(0);
        if(Objects.equals(vModel.item.getUnit(), "L"))
            binding.spinnerE.setSelection(1);
        if(Objects.equals(vModel.item.getUnit(), "g"))
            binding.spinnerE.setSelection(2);
        if(Objects.equals(vModel.item.getUnit(), "ml"))
            binding.spinnerE.setSelection(3);
        if(Objects.equals(vModel.item.getUnit(), "Unit"))
            binding.spinnerE.setSelection(4);
    }

    //IMPLEMENTING THE ADAPTER VIEW CLASS
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vModel.item.setUnit(parent.getItemAtPosition(position).toString());
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //UPDATING VIEW-MODEL AS DATA IS CHANGED IN UI BY USER
    public void setWatchers() {
        TextWatcher textWatcherForName = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.item.setName(s.toString());
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
                if(!s.toString().equals(""))
                vModel.item.setQuantity(Double.parseDouble(s.toString()));
                else
                    vModel.item.setQuantity(0.0);

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
                if(!s.toString().equals(""))
                vModel.item.setCP(Double.parseDouble(s.toString()));
                else
                    vModel.item.setCP(0.0);

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
                if(!s.toString().equals(""))
                    vModel.item.setSP(Double.parseDouble(s.toString()));
                else
                    vModel.item.setSP(0.0);
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
                if(!s.toString().equals(""))
                    vModel.item.setInStore(Double.parseDouble(s.toString()));
                else
                    vModel.item.setInStore(0.0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        binding.nameE.addTextChangedListener(textWatcherForName);
        binding.massVolumeE.addTextChangedListener(textWatcherForQuantity);
        binding.cpE.addTextChangedListener(textWatcherForCp);
        binding.spE.addTextChangedListener(textWatcherForSp);
        binding.noItemsE.addTextChangedListener(textWatcherForInStore);
        binding.spinnerE.setOnItemSelectedListener(this);
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


    //WHAT MY EDIT BUTTON DO->
    public void edit(View view) {
        if(vModel.item.getName().isEmpty() || binding.massVolumeE.getText().toString().isEmpty() || binding.spE.getText().toString().isEmpty() || binding.cpE.getText().toString().isEmpty()|| binding.noItemsE.getText().toString().isEmpty())
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
            promptUserForFurtherAction();
        }

    }
    //TAKE ENDING DIRECTIONS FROM USER, WEATHER TO ADD ANOTHER ITEM OR FINISH THE ACTIVITY
    private void promptUserForFurtherAction() {
        AlertDialog informing_dialog = new AlertDialog.Builder(this).setMessage("Confirm to save changes.!")
                .setPositiveButton("Confirm", (dialog, id) -> {
                    AsyncTask.execute(() -> Repo.getDao().update(vModel.item));
                    finish();
                    DescriptionDialogueFragment.instance.whenDestroyed();
                })
                .setNegativeButton("Cancel", (dialog, which) -> finish())
                .create();
        informing_dialog.show();
    }

}