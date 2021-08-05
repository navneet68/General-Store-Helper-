package com.example.generalstorehelper.FragmentsForTransactions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.generalstorehelper.MakeTransaction.SalePurchaseViewModel;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.model.Item;
import com.example.generalstorehelper.model.ItemForTransaction;

import org.jetbrains.annotations.NotNull;

public class AddEditItemDetailsForSale extends DialogFragment {
    public static boolean isOpen;

    private SalePurchaseViewModel vModel;
    Item item;
    TextView description;
    EditText qty ;
    EditText total ;
    EditText noUnits;
    TextView Unit;
    Button cancel , save;
    ImageView delete;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.body_item_for_transaction,container,false);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isOpen=true;
        qty = view.findViewById(R.id.howMuch);
        noUnits = view.findViewById(R.id.inUnits);
        total = view.findViewById(R.id.totalPrice);
        Unit = view.findViewById(R.id.unitHere);
        description = view.findViewById(R.id.nameSelectedItem);
        cancel = view.findViewById(R.id.button8);
        save = view.findViewById(R.id.button7);
        delete = view.findViewById(R.id.delete_entry);
        vModel = new ViewModelProvider(requireActivity()).get(SalePurchaseViewModel.class);
        item = vModel.getItemForTransaction().getItem();
        initView();
        setUI();
        setOnClicks();
    }

    private void setOnClicks() {
        cancel.setOnClickListener(v -> requireActivity().onBackPressed());
        save.setOnClickListener(v -> saveIt());
        delete.setOnClickListener(v -> {
            vModel.choice=2;
            vModel.setItemForTransaction(null);
            requireActivity().onBackPressed();
        });

    }

    private void saveIt() {
        if(total.getText().toString().equals("")){
            Toast.makeText(getContext(),"Enter Data First !",Toast.LENGTH_SHORT).show();
        }else{
            ItemForTransaction itemForTransaction = new ItemForTransaction(item,
                    Double.parseDouble(qty.getText().toString()),
                    Double.parseDouble(total.getText().toString()));
            vModel.setItemForTransaction(itemForTransaction);
            requireActivity().onBackPressed();
        }

    }

    private void setUI() {
        if(vModel.getItemForTransaction().getPrice()!=null){
            total.setText(String.valueOf(vModel.getItemForTransaction().getPrice()));
            delete.setVisibility(View.VISIBLE);
            vModel.choice=2;
        }else
            vModel.choice=1;

        String name = item.getName()+"\n";
        name = name +item.getQuantity()+" "+item.getUnit()+"\n";
        name = name +item.getSP()+" RS";
        description.setText(name);

    }

    private void initView() {
        Unit.setText(item.getUnit());
        Double rate = item.getSP();
        Double quantity = item.getQuantity();
        TextWatcher textWatcherForTotal = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){

                    Double t=Double.parseDouble(s.toString());
                    if(rate!=0.0 && quantity!=0.0){
                        String s1 = String.valueOf((t * quantity) / rate);
                        if(qty.getText().toString().equals("") || Double.parseDouble(qty.getText().toString())!=((t * quantity) / rate))
                        qty.setText(s1);
                        if(noUnits.getText().toString().equals("") || Double.parseDouble(noUnits.getText().toString())!=(t/rate))
                        noUnits.setText(String.valueOf(t/rate));
                    }
                    else
                        Toast.makeText(getContext(),"Rate and Quantity of Item cannot be Zero",Toast.LENGTH_SHORT).show();

                }else{
                    if(!qty.getText().toString().equals(""))
                    qty.setText("");
                    if(!noUnits.getText().toString().equals(""))
                    noUnits.setText("");
                }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textWatcherForQty = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    Double q=Double.parseDouble(s.toString());

                    if(rate!=0.0 && quantity!=0.0){
                        String s1 = String.valueOf((q * rate) / quantity);
                        if(total.getText().toString().equals("") || Double.parseDouble(total.getText().toString())!=((q * rate) / quantity))
                        total.setText(s1);
                        if(noUnits.getText().toString().equals("") || Double.parseDouble(noUnits.getText().toString())!= (q/quantity))
                        noUnits.setText(String.valueOf(q/quantity));
                    }
                    else
                        Toast.makeText(getContext(),"Rate or Quantity of Item cannot be Zero",Toast.LENGTH_SHORT).show();

                }else{
                    if(!total.getText().toString().equals(""))
                        total.setText("");
                    if(!noUnits.getText().toString().equals(""))
                        noUnits.setText("");
                }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        TextWatcher textWatcherForNoUnits = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    Double n=Double.parseDouble(s.toString());

                    if(rate!=0.0 && quantity!=0.0){
                        if(total.getText().toString().equals("") || Double.parseDouble(total.getText().toString())!=(rate*n))
                        total.setText(String.valueOf(rate*n));
                        if(qty.getText().toString().equals("") || Double.parseDouble (qty.getText().toString())!= (n*quantity))
                        qty.setText(String.valueOf(n*quantity));
                    }
                    else
                        Toast.makeText(getContext(),"Rate or Quantity of Item cannot be Zero",Toast.LENGTH_SHORT).show();

                }else{
                    if(!qty.getText().toString().equals(""))
                        qty.setText("");
                    if(!total.getText().toString().equals(""))
                        total.setText("");
                }
              }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        qty.addTextChangedListener(textWatcherForQty);
        noUnits.addTextChangedListener(textWatcherForNoUnits);
        total.addTextChangedListener(textWatcherForTotal);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isOpen=false;
    }
}

