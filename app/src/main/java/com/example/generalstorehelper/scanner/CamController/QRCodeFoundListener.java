package com.example.generalstorehelper.scanner.CamController;
//Interface to get these functions and implement them as when pattern of barcode is found or otherwise.
public interface QRCodeFoundListener {
    void onQRCodeFound(String qrCode);
    void qrCodeNotFound();
}
