package com.proyecto.tfg.superrunningnews.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyecto.tfg.superrunningnews.adapters.AdapterNoticia;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;

import java.util.ArrayList;

public class NoticiaFragment extends Fragment {

    private ArrayList<Noticia> tNoticia;
    private RecyclerView recyclerView;
    private AdapterNoticia adapter;
    private RecyclerView.LayoutManager layoutManager;

    public NoticiaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noticia, container, false);

        // Traer array de BottomBarActivity.
        Bundle args = getArguments();
        if (args != null) {
            this.tNoticia = args.getParcelableArrayList("noticia");
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterNoticia(tNoticia, getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(clickListener);
        return v;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Noticia noticia = adapter.getItem(adapter.getItemPos());

            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(noticia.getLink()));
            startActivity(webIntent);
        }
    };

}
