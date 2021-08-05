package com.example.generalstorehelper.DialogueBoxes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.generalstorehelper.ManageItems.EditItemActivity;
import com.example.generalstorehelper.R;
import com.example.generalstorehelper.Repository.Repo;
import com.example.generalstorehelper.model.Item;

import org.jetbrains.annotations.NotNull;

public class DescriptionDialogueFragment extends DialogFragment {
    //I HAVE MADE THESE STATIC VARIABLES , SO THAT WHEN ROTATED , THEY DON'T LOSE THEIR VALUES
    private static Item item;

    public static onFragmentDestroy instance;

    private static boolean isOpen;

    //VARIABLES TO WORK WITH CUSTOM LAYOUT I MADE FOR MY DIALOG FRAGMENT
    private View view;

    private TextView name,quantity,CP,SP,noItems,profit;

    private ImageView delete, edit, close;

    //CONSTRUCTORS
    public DescriptionDialogueFragment() {

    }

    public DescriptionDialogueFragment(Item item) {
        DescriptionDialogueFragment.item = item;
    }

    //I HAVE MADE THIS STATIC FUNCTION TO KNOW IF FRAGMENT IS ALREADY OPEN OR NOT, FROM OUTSIDE THIS CLASS
    public static boolean isOpen(){

        return isOpen;
    }

    //CALLED ALL THE NECESSARY METHODS WHEN VIEW IS CREATED
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.descrption_dialogue_box,container,false);
        isOpen = true;
        initViews();
        setValues();
        setListeners();
        return view;
    }

    //FINDING VIEWS FROM LAYOUT TO WORK WITH THEM
    private void initViews() {
        name = view.findViewById(R.id.nameForDB);
        quantity = view.findViewById(R.id.quantityForDB);
        SP = view.findViewById(R.id.SPForDB);
        CP = view.findViewById(R.id.CPForDB);
        profit = view.findViewById(R.id.profitForDB);
        noItems = view.findViewById(R.id.noItemsForDB);
        delete = view.findViewById(R.id.deleteDB);
        edit = view.findViewById(R.id.editDB);
        close = view.findViewById(R.id.closeDB);
    }

    //SETTING UP VALUES OF TEXT VIEWS IN UI
    private void setValues() {
        name.setText(item.getName());
        String q = "("+ item.getQuantity() +item.getUnit()+")";
        quantity.setText(q);
        double p = item.getSP()-item.getCP();
        String pro = "Profit - "+ p +"Rs";
        profit.setText(pro);
        String cost = "Bought at - "+ item.getCP() +"Rs";
        CP.setText(cost);
        SP.setText(String.valueOf(item.getSP()));
        String availability = "Items Available in Store - "+ item.getInStore();
        noItems.setText(availability);
    }

    //SETTING UP LISTENERS AND WHAT WILL BUTTONS DO
    private void setListeners() {
        close.setOnClickListener(v -> requireActivity().onBackPressed());
        delete.setOnClickListener(v -> alertUserAndExecute());
        edit.setOnClickListener(v -> { startEditActivity(); requireActivity().onBackPressed();});
    }

    //Execution of tasks, when edit button is clicked
    private void startEditActivity() {
        Intent intent = new Intent(getActivity(), EditItemActivity.class);
        intent.putExtra("ItemToEdit",item);
        startActivity(intent);

    }

    //Building Delete Alert Dialog and wiring it to delete item from repository
    private void alertUserAndExecute() {
        AlertDialog warning_dialog = new AlertDialog.Builder(requireActivity()).setMessage(R.string.delete_alert)
                .setPositiveButton(R.string.delete_anyway, (dialog, id) -> {
                    AsyncTask.execute(() -> Repo.getDao().delete(item));

                    requireActivity().onBackPressed();
                })
                .setNegativeButton(R.string.dont_delete, null)
                .create();
        warning_dialog.show();
    }

    //Overriding onDestroyView to set isOpen to false and call whenDestroyed method of interface instance
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isOpen = false;
        instance.whenDestroyed();
    }

}
