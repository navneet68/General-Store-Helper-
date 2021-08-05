package com.example.generalstorehelper.FragmentsForTransactions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.generalstorehelper.MakeTransaction.SalePurchaseViewModel;
import com.example.generalstorehelper.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TransactionFragment extends Fragment {
    public static boolean isOpen = false;
    private SalePurchaseViewModel vModel;
    private TextView total;
    private RecyclerView RV;
    public static onFragmentLifeCycleChanges instance;
    RVAdapterForTransaction adapter;
    private LruCache<String , Bitmap> bitmapCache;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.transaction_fragment,container,false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isOpen=false;
        instance.Closed();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instance.Opened();
        isOpen=true;
        vModel= new ViewModelProvider(requireActivity()).get(SalePurchaseViewModel.class);
        total = view.findViewById(R.id.total_transaction);
        RV = view.findViewById(R.id.RV_transaction);
        adapter = new RVAdapterForTransaction(entry -> {
            if(!AddEditItemDetailsForSale.isOpen){
                vModel.choice=0;
                vModel.setItemForTransaction(entry);
                Fragment fragment = new AddEditItemDetailsForSale();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null).commit();
            }
        });
        adapter.setListToShow(vModel.getTransaction().getValue());
        RV.setAdapter(adapter);
        RV.setLayoutManager(new LinearLayoutManager(requireContext()));
        Double totalSum = 0.0;
        for(int i = 0; i< Objects.requireNonNull(vModel.getTransaction().getValue()).size(); i++){
            totalSum=totalSum+vModel.getTransaction().getValue().get(i).getPrice();
        }
        total.setText(String.valueOf(totalSum));
        setObserver();
        instance.Opened();
    }

    private void setObserver() {
        vModel.getTransaction().observe(getViewLifecycleOwner(), itemForTransactions -> {
            adapter.setListToShow(itemForTransactions);
            adapter.notifyDataSetChanged();
            Double totalSum = 0.0;
            for(int i=0;i<itemForTransactions.size();i++){
                totalSum=totalSum+itemForTransactions.get(i).getPrice();
            }
            total.setText(String.valueOf(totalSum));
        });
    }

   private int getHeight(int size){
       int height=0;

       int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 8192);
       bitmapCache = new LruCache<>(cacheSize);
    for(int i=0;i<size;i++){
        RVAdapterForTransaction.ViewHolder holder = adapter.onCreateViewHolder(RV , adapter.getItemViewType(i));
        adapter.onBindViewHolder(holder,i);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(RV.getWidth(),View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0,0,holder.itemView.getMeasuredWidth(),
                holder.itemView.getMeasuredHeight());
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        Bitmap drawingCache = holder.itemView.getDrawingCache();
        if(drawingCache !=null){
            bitmapCache.put(String.valueOf(i),drawingCache);
        }
        height = height+holder.itemView.getMeasuredHeight();
    }

       return height;
   }

   public Bitmap getSS(){
        int size = Objects.requireNonNull(vModel.getTransaction().getValue()).size();
        int height = getHeight(size);
        
        Bitmap SSBitmap = Bitmap.createBitmap(RV.getMeasuredWidth(),height,Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(SSBitmap);
        Drawable background =RV.getBackground();
        if(background instanceof ColorDrawable){
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int color = colorDrawable.getColor();
            canvas.drawColor(color);
        }
        int top = 0;
        Paint paint = new Paint();
        for(int i =0;i<size;i++){
            Bitmap bitmap = bitmapCache.get(String.valueOf(i));
            canvas.drawBitmap(bitmap,0f,top,paint);
            top = top+ bitmap.getHeight();
        }
        return SSBitmap;
   }
}
