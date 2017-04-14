package com.proyecto.tfg.superrunningnews.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyecto.tfg.superrunningnews.R;

public class PerfilFragment extends Fragment {

    //private Button botonEjemplo;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Traer elementos del layout "fragment_perfil".
        // Para traer componentes de BottomBarActivity habría que hacer otra cosa pero no es el caso.

        // botonEjemplo = (Button) v.findViewById(R.id.btEjemplo);

        return v;
    }

}
