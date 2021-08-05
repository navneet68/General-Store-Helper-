package com.example.generalstorehelper.scanner.CamController;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.nio.ByteBuffer;

import static android.graphics.ImageFormat.YUV_420_888;
import static android.graphics.ImageFormat.YUV_422_888;
import static android.graphics.ImageFormat.YUV_444_888;
//So , this is implementation of My image Analyzer
public class QRCodeImageAnalyzer implements ImageAnalysis.Analyzer {
    //Final , because it can only be assigned Once
    private final QRCodeFoundListener listener;

    //This is constructor for this Analyzer class
    public QRCodeImageAnalyzer(QRCodeFoundListener listener) {
        this.listener = listener;
    }

    //This analyze function from the Interface ImageAnalysis.Analyzer is the source of our image from CPU
    @Override
    public void analyze(@NonNull ImageProxy image) {
        if (image.getFormat() == YUV_420_888 || image.getFormat() == YUV_422_888 || image.getFormat() == YUV_444_888) {
            //Converting Image from CPU in a readable format for our Zxing Library to search for a Barcode
            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
            byte[] imageData = new byte[byteBuffer.capacity()];
            byteBuffer.get(imageData);
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    imageData,
                    image.getWidth(), image.getHeight(),
                    0, 0,
                    image.getWidth(), image.getHeight(),
                    false
            );
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            //Searching for a Barcode by Try and Catch, we will call different functions of Listener Interface
            try {
                Result result = new MultiFormatReader().decode(binaryBitmap);
                listener.onQRCodeFound(result.getText());
            }
            catch (NotFoundException e) {
                listener.qrCodeNotFound();
            }

        }

        //Closing the image , so that MEMORY becomes free for next images
        image.close();

    }
}
