package com.proyecto.tfg.superrunningnews.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.proyecto.tfg.superrunningnews.adapters.NoticiaAdapter;
import com.proyecto.tfg.superrunningnews.models.Noticia;
import com.proyecto.tfg.superrunningnews.R;

import java.util.ArrayList;
import java.util.Collections;

public class NoticiaFragment extends Fragment {

    private ArrayList<Noticia> tNoticia;
    private RecyclerView recyclerView;
    private NoticiaAdapter adapter;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    public NoticiaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_noticia, container, false);

        Bundle args = getArguments();
        if (args != null) {
            this.tNoticia = args.getParcelableArrayList("noticia");
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoticiaAdapter(tNoticia, getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(clickListener);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) v.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) v.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) v.findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab.setOnClickListener(onClickListener);
        fab1.setOnClickListener(onClickListener);
        fab2.setOnClickListener(onClickListener);
        fab3.setOnClickListener(onClickListener);

        return v;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    Collections.sort(tNoticia, Noticia.NoticiaOrdenadaProvincia);
                    adapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);
                    animateFAB();
                    break;
                case R.id.fab2:
                    Collections.sort(tNoticia, Noticia.NoticiaOrdenadaFechaInversa);
                    adapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);
                    animateFAB();
                    break;
                case R.id.fab3:
                    Collections.sort(tNoticia);
                    adapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);
                    animateFAB();
                    break;
                case R.id.fab:
                    animateFAB();
                    break;
            }
        }
    };

    public void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Noticia noticia = adapter.getItem(adapter.getItemPos());

            if (noticia != null) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(noticia.getLink()));
                startActivity(webIntent);
            }
        }
    };
}
