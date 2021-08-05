package com.example.generalstorehelper.FragmentsForTransactions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.generalstorehelper.DialogueBoxes.ScannerTip;
import com.example.generalstorehelper.MakeTransaction.SalePurchaseViewModel;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.scanner.CamController.CameraManager;
import com.example.generalstorehelper.scanner.CamController.QRCodeFoundListener;

import org.jetbrains.annotations.NotNull;

public class PortableScanner extends Fragment {

    private CameraManager myCAM;
    public static boolean isOpen;
    private QRCodeFoundListener qrFound;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private TextView code;
    private SalePurchaseViewModel vModel;
    public static onFragmentLifeCycleChanges instance;




    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.portable_scanner_fragment,container,false);
        isOpen =true;
        instance.Opened();
        code = view.findViewById(R.id.barcodeScanned);
        Button search = view.findViewById(R.id.searchBarcode);
        qrFound = new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String qrCode) {
                code.setText(qrCode);
                view.findViewById(R.id.dataX).setVisibility(View.VISIBLE);


            }

            @Override
            public void qrCodeNotFound() {

            }
        };
        LifecycleOwner LCO = this.getViewLifecycleOwner();
        AsyncTask.execute(() -> {
            myCAM = new CameraManager(qrFound,requireActivity(),view.findViewById(R.id.previewView),LCO);
            requestCamera();
        });
        vModel = new ViewModelProvider(requireActivity()).get(SalePurchaseViewModel.class);
        search.setOnClickListener(v -> {
            String x ="/"+code.getText().toString()+"/";
            vModel.getBarcodeToSearch().setValue(x);
            requireActivity().onBackPressed();
        });

        return view;
    }

    // Checking if activity has permission to start camera and managing the permissions prompts.
    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            myCAM.start();
        } else {
            requestPermissions( new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
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
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //SHOWING WARNING AKA TIP TO USER THAT SCANNER IS NOT PERFECT , THEY HAVE TO TRY THINGS IF IT DOES NOT WORK
    private void showTip() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ScannerTip newFragment = new ScannerTip();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isOpen=false;
        instance.Closed();
    }


}
