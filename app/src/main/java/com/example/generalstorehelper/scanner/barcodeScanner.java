package com.example.generalstorehelper.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.generalstorehelper.DialogueBoxes.ScannerTip;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.databinding.ActivityBarcodeScannerBinding;
import com.example.generalstorehelper.scanner.CamController.CameraManager;
import com.example.generalstorehelper.scanner.CamController.QRCodeFoundListener;

public class barcodeScanner extends AppCompatActivity{

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private ActivityBarcodeScannerBinding binding;
    private CameraManager myCAM;
    private QRCodeFoundListener listener;
    private ScannerViewModel vModel;
    private LifecycleOwner LFOwner ;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_barcode_scanner);
        LFOwner = this;
        activity = this;
        binding.textView10.setVisibility(View.GONE);
        binding.button4.setVisibility(View.GONE);
        PreviewView previewView = binding.activityMainPreviewView;
        assignListener();
        //I AM KINDA PROUD ON THE FOLLOWING LINE OF CODE , THIS BRINGS SO MUCH MODULARITY TO MY CODE
        AsyncTask.execute(() -> {
            myCAM = new CameraManager(listener,activity, previewView,LFOwner);
            requestCamera();
        });

        setUI();
    }

    //Setting up data on screen if it exist
    private void setUI() {
        if(!(vModel.getText().equals("")) && (vModel.getText()!=null)){
            binding.textView10.setText(vModel.getText());
            binding.textView10.setVisibility(View.VISIBLE);
            binding.button4.setVisibility(View.VISIBLE);
        }
    }


    // Checking if activity has permission to start camera and managing the permissions prompts.
    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            myCAM.start();
        } else {
            ActivityCompat.requestPermissions(barcodeScanner.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myCAM.start();
                showTip();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //SHOWING WARNING AKA TIP TO USER THAT SCANNER IS NOT PERFECT , THEY HAVE TO TRY THINGS IF IT DOES NOT WORK
    private void showTip() {
        FragmentManager fragmentManager = barcodeScanner.this.getSupportFragmentManager();
        ScannerTip newFragment = new ScannerTip();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }


    //DECIDING WHAT WILL HAPPEN IF A BARCODE IS SCANNED
    private void assignListener(){
       listener = new QRCodeFoundListener() {
           @Override
           public void onQRCodeFound(String qrCode) {
               binding.textView10.setText(qrCode);
               binding.textView10.setVisibility(View.VISIBLE);
               binding.button4.setVisibility(View.VISIBLE);
               vModel.setText(qrCode);
           }

           @Override
           public void qrCodeNotFound() {

           }
       };
    }

    //What does my buttons do?
    public void add(View view) {
        vModel.addBarcode(binding.textView10.getText().toString());
        binding.textView10.setText("");
        binding.textView10.setVisibility(View.GONE);
        binding.button4.setVisibility(View.GONE);
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        vModel.setText("");

    }
    public void end(View view) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("barcodes",vModel.getBarcodes());
        setResult(RESULT_OK,intent);
        finish();
    }
}