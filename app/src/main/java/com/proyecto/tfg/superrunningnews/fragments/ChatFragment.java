package com.proyecto.tfg.superrunningnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyecto.tfg.superrunningnews.DefaultMessagesActivity;
import com.proyecto.tfg.superrunningnews.DialogsFixtures;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Dialog;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;

public class ChatFragment extends DemoDialogFragment {

    private ArrayList<Dialog> dialogs;
    private DialogsList dialogsList;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        dialogsList = (DialogsList) v.findViewById(R.id.dialogsList);
        initAdapter();

        return v;
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        DefaultMessagesActivity.open(getContext());
    }

    private void initAdapter() {
        super.dialogsAdapter = new DialogsListAdapter<>(super.imageLoader);
        super.dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(super.dialogsAdapter);
    }

}
