package com.proyecto.tfg.superrunningnews.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyecto.tfg.superrunningnews.AppUtils;
import com.proyecto.tfg.superrunningnews.R;
import com.proyecto.tfg.superrunningnews.models.Dialog;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

public abstract class DemoDialogFragment extends Fragment
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;

    public DemoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(getContext()).load(url).into(imageView);
            }
        };
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        AppUtils.showToast(
                getContext(),
                dialog.getDialogName(),
                false);
    }

}
