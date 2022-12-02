package modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import mx.edu.itl.c18131289.apporganizadordefotos.R;

public class SpinnerCategoriasAdapter extends ArrayAdapter<CategoriaFoto> {

    public SpinnerCategoriasAdapter (@NonNull Context context, ArrayList<CategoriaFoto> categorias ) {
        super ( context, 0, categorias );
    }

    @NonNull
    @Override
    public View getView (int positon, @Nullable View convertView, @NonNull ViewGroup parent ) {
        return  inicializarView ( positon, convertView, parent );
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return inicializarView ( position, convertView, parent );
    }

    private View inicializarView ( int position, @Nullable View convertView, @NonNull ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = LayoutInflater.from ( getContext() ).inflate (
                    R.layout.spinner_fila_texto,
                    parent,
                    false
            );
        }
        TextView categoriaNombre = convertView.findViewById ( R.id.textViewCategoria );

        CategoriaFoto categoriaFoto = getItem ( position );
        categoriaNombre.setText ( categoriaFoto.getCategoria() );

        return convertView;
    }
}
