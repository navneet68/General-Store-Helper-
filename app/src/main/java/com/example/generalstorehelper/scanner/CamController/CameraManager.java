package com.example.generalstorehelper.scanner.CamController;

import android.app.Activity;
import android.content.Context;
import android.util.Size;
import android.widget.Toast;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

//A class which makes it abstract and easy for user to access camera and
// set it for analyze and tell it what to do on result of finding qr code.
public class CameraManager {
    private final QRCodeFoundListener listener;
    private final Context ActivityContext;
    private final Activity GivenActivity;
    private final PreviewView previewView;
    private final ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private final LifecycleOwner lifecycleOwner;

//Constructor for class, it is activity specific , i am proud on making this
    public CameraManager(QRCodeFoundListener listener, Activity givenActivity, PreviewView previewView,LifecycleOwner lifecycleOwner) {
        this.listener = listener;
        this.lifecycleOwner = lifecycleOwner;
        GivenActivity = givenActivity;
        this.previewView = previewView;
        ActivityContext = GivenActivity.getBaseContext();
        cameraProviderFuture = ProcessCameraProvider.getInstance(ActivityContext);

    }
//A call for activities to start the camera when they are ready..i.e when they have right permissions
    public void start() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(GivenActivity, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } , ContextCompat.getMainExecutor(ActivityContext));
    }

//A function which starts all the work assigned to this class,
//start the camera and show it on previewView and start analyzing each frame.
    private void bindCameraPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ActivityContext), new QRCodeImageAnalyzer(listener));

        //THis the statement which kickstart all the work, above lines are just for specifications.
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis, preview);
    }
}
