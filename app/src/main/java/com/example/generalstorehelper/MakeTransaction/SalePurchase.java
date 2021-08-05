package com.example.generalstorehelper.MakeTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.generalstorehelper.FragmentsForTransactions.AddEditItemDetailsForSale;
import com.example.generalstorehelper.FragmentsForTransactions.PortableItemsList;
import com.example.generalstorehelper.FragmentsForTransactions.PortableScanner;
import com.example.generalstorehelper.FragmentsForTransactions.TransactionFragment;
import com.example.generalstorehelper.FragmentsForTransactions.onFragmentLifeCycleChanges;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.Repository.Repo;
import com.example.generalstorehelper.databinding.ActivitySalePurchaseBinding;
import com.example.generalstorehelper.model.Item;
import com.example.generalstorehelper.model.ItemForTransaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SalePurchase extends AppCompatActivity {

    private ActivitySalePurchaseBinding binding;
    private SalePurchaseViewModel vModel ;
    private FragmentManager fragmentManager;
    private PortableItemsList itemsList;
    private final MutableLiveData<Bitmap> _finalBitmap = new MutableLiveData<>();
    private Bitmap finalBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sale_purchase);
        vModel = new ViewModelProvider(this).get(SalePurchaseViewModel.class);
        setInstancesOfFragments();
        binding.BusyFragment.setVisibility(View.VISIBLE);
        fragmentManager = SalePurchase.this.getSupportFragmentManager();
        if(savedInstanceState==null){
            itemsList = new PortableItemsList();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.BusyFragment, itemsList)
                    .setReorderingAllowed(true)
                    .addToBackStack(null).commit();
        }else{
            itemsList = (PortableItemsList) fragmentManager.getFragments().get(0);
        }
        binding.searchForSale.setText(vModel.getNameToSearch().getValue());
        setOnClicks();
        setObservers();
        setListeners();
    }

    private void setInstancesOfFragments() {
        PortableScanner.instance = new onFragmentLifeCycleChanges() {
            @Override
            public void Opened() {
                binding.ScanForSale.setText("CANCEL");
            }

            @Override
            public void Closed() {
                binding.ScanForSale.setText("SCAN");
            }
        };
        TransactionFragment.instance = new onFragmentLifeCycleChanges() {
            @Override
            public void Opened() {
                binding.BusyFragment.setVisibility(View.GONE);
                binding.cardView9.setVisibility(View.GONE);
                binding.listTransaction.setVisibility(View.VISIBLE);
                binding.textView15.setText("See less");
                binding.textView16.setText("<<");
            }

            @Override
            public void Closed() {
                binding.listTransaction.setVisibility(View.GONE);
                binding.BusyFragment.setVisibility(View.VISIBLE);
                binding.cardView9.setVisibility(View.VISIBLE);
                binding.textView15.setText("See More");
                binding.textView16.setText(">>");

            }
        };

    }

    private void setListeners() {
        TextWatcher textWatcherForSearch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vModel.getNameToSearch().setValue(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.searchForSale.addTextChangedListener(textWatcherForSearch);
    }

    private void setObservers() {
        Observer<? super List<Item>> observerForSearchResult = (Observer<List<Item>>) items -> itemsList.update(items);
        vModel.getSearchResult().observe(this,observerForSearchResult);

        Observer<? super String> observerForNameToSearch = (Observer<String>) s -> AsyncTask.execute(() -> vModel.getSearchResult().postValue(Repo.getDao().findByName(s)));
        vModel.getNameToSearch().observe(this,observerForNameToSearch);

        Observer<? super String> observerForBarcodeToSearch = (Observer<String>) s -> AsyncTask.execute(() -> vModel.getSearchResult().postValue(Repo.getDao().findByBarcode(s)));
        vModel.getBarcodeToSearch().observe(this,observerForBarcodeToSearch);

        vModel.getTransaction().observe(this, itemForTransactions -> {
            String s =""+itemForTransactions.size()+" Items in the List";
            binding.textView11.setText(s);
        });

            _finalBitmap.observe(this, bitmap -> {
                finalBitmap = bitmap;
                requestSaveImage();
            });
    }

    private void setOnClicks() {
        //OPEN CAMERA AND IMAGE ANALYSIS
        binding.ScanForSale.setOnClickListener(v -> {
            if(!PortableScanner.isOpen && !AddEditItemDetailsForSale.isOpen){
                PortableScanner newFragment = new PortableScanner();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(R.id.BusyFragment, newFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("SCANNER").commit();
            }
            if(PortableScanner.isOpen && !AddEditItemDetailsForSale.isOpen){
                fragmentManager.popBackStack();
            }
            if(AddEditItemDetailsForSale.isOpen)
                Toast.makeText(SalePurchase.this, "Close Current Window First", Toast.LENGTH_SHORT).show();


        });


        //OPEN THE FRAGMENT CONTAINING TRANSACTION
        binding.CustomActionBar.setOnClickListener(v -> {
            if(!TransactionFragment.isOpen&& !AddEditItemDetailsForSale.isOpen){


                TransactionFragment newFragment = new TransactionFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(R.id.listTransaction, newFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("TRANSACTION").commit();
            }
            if(TransactionFragment.isOpen && !AddEditItemDetailsForSale.isOpen){
                    fragmentManager.popBackStack();
            }
            if(AddEditItemDetailsForSale.isOpen)
                Toast.makeText(SalePurchase.this, "Close Current Window First", Toast.LENGTH_SHORT).show();

        });


        //Making Receipt and finalizing
        binding.button5.setOnClickListener(v -> {
            if (Objects.requireNonNull(vModel.getTransaction().getValue()).size() != 0) {
                AlertDialog progressDialog;
                AlertDialog.Builder x = new AlertDialog.Builder(this);
                x.setMessage("Saving...");
                x.setCancelable(false);
                progressDialog = x.create();
                progressDialog.show();
                UpdateDatabase();
                CreateBitmap();
            }else
                Toast.makeText(this,"List is Empty.!",Toast.LENGTH_SHORT).show();
        });
    }

    private void requestSaveImage() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
            SaveImage();
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                SaveImage();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 11){
            if(grantResults.length>=1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                SaveImage();
            }
            else{
                Toast.makeText(this,"Permission Denied .!",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void SaveImage() {
        String name = DateFormat.getDateTimeInstance().format(new Date()) + ".jpeg";
        final Uri[] addressSavedImage = new Uri[1];
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            File file = new File(getStoragePath(),name);
            try {
                FileOutputStream out = new FileOutputStream(file);
                if(finalBitmap!=null)
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                out.flush();
                out.close();
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, new String[]{"image/jpeg"},
                        (path, uri) -> {
                            addressSavedImage[0] = uri;
                            finish();
                        });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"Couldn't Save Image",Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            ContentResolver resolver = getApplicationContext().getContentResolver();
            ContentValues Receipt = new ContentValues();
            Receipt.put(MediaStore.Images.Media.DISPLAY_NAME,name);
            Receipt.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Receipt.put(MediaStore.Images.Media.ALBUM,"Store Receipts");
            }
            Receipt.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM+"/Store Receipts");
            Uri imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            addressSavedImage[0] = resolver.insert(imageCollection,Receipt);
            OutputStream out ;
            try {
                out = resolver.openOutputStream(addressSavedImage[0]);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                out.flush();
                out.close();
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this,"Couldn't make file on Disk",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();

        }
    }

    private String getStoragePath() {
        String address;
        address =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/Store Receipts";
        File Receipts = new File(address);
        if(Receipts.exists()){
            return Receipts.getAbsolutePath();
        }else {
            if(Receipts.mkdir())
                return Receipts.getAbsolutePath();
            else{
                Toast.makeText(this,"Couldn't Write on External Storage",Toast.LENGTH_SHORT).show();
                finish();
                return null;
            }
        }
    }

    private void CreateBitmap() {
        final View[] view = {null};
        final View[] view1 = new View[1];
        final Bitmap[] b1 = new Bitmap[1];
        final Bitmap[] b2 = new Bitmap[1];
        final Bitmap[] b3 = new Bitmap[1];
        View[] view3 = new View[1];
        final View[] temp = new View[1];
        if(TransactionFragment.isOpen)
            view[0] = Objects.requireNonNull(fragmentManager.findFragmentById(R.id.listTransaction)).getView();
        else{
            TransactionFragment newFragment = new TransactionFragment();
            TransactionFragment.instance = new onFragmentLifeCycleChanges() {
                @Override
                public void Opened() {
                    binding.BusyFragment.setVisibility(View.GONE);
                    binding.cardView9.setVisibility(View.GONE);
                    binding.listTransaction.setVisibility(View.VISIBLE);
                    binding.textView15.setText("See less");
                    binding.textView16.setText("<<");
                    temp[0] = newFragment.getView();
                }

                @Override
                public void Closed() {

                }
            };
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.listTransaction, newFragment)
                    .setReorderingAllowed(true)
                    .commitNowAllowingStateLoss();
        }
        long i = 1000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(view[0] == null)
                    view[0] = temp[0];
                view1[0] = Objects.requireNonNull(view[0]).findViewById(R.id.cardView12);
                view1[0].setDrawingCacheEnabled(true);
                view1[0].buildDrawingCache();
                b1[0] = Bitmap.createBitmap(view1[0].getDrawingCache(),0,0, view1[0].getMeasuredWidth(), view1[0].getMeasuredHeight());
                view1[0].destroyDrawingCache();
                b2[0] = ((TransactionFragment)Objects.requireNonNull(fragmentManager.findFragmentById(R.id.listTransaction))).getSS();
                view3[0] = Objects.requireNonNull(view[0]).findViewById(R.id.cardView13);
                view3[0].setDrawingCacheEnabled(true);
                view3[0].buildDrawingCache();
                b3[0] = Bitmap.createBitmap(view3[0].getDrawingCache(),0,0, view3[0].getMeasuredWidth(), view3[0].getMeasuredHeight());
                view3[0].destroyDrawingCache();
                Bitmap finalBitmap = Bitmap.createBitmap(b1[0].getWidth(),
                        b1[0].getHeight()+b2[0].getHeight()+b3[0].getHeight(),
                        Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(finalBitmap);
                Paint paint = new Paint();
                canvas.drawBitmap(b1[0], 0f, 0,paint);
                canvas.drawBitmap(b2[0], 0f , b1[0].getHeight(),paint);
                canvas.drawBitmap(b3[0], 0f, b1[0].getHeight()+b2[0].getHeight(),paint);


                _finalBitmap.postValue(finalBitmap);

            }
        },i );

    }

    private void UpdateDatabase() {
        List<ItemForTransaction> entries=vModel.getTransaction().getValue();
        Item item;
        Double counts;
        for(int i = 0; i< Objects.requireNonNull(entries).size(); i++){
            item = entries.get(i).getItem();
            if(item.getQuantity()!=0){
                counts=entries.get(i).getQty()/item.getQuantity();
                item.setInStore(item.getInStore()-counts);
                Item finalItem = item;
                AsyncTask.execute(() -> Repo.getDao().update(finalItem));
            }
            else
                Toast.makeText(this,"Can't Update Database",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getFragments().size()!=1)
        super.onBackPressed();
        else{
            super.onBackPressed();
            finish();
        }
    }
}