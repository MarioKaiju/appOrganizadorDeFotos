package mx.edu.itl.c18131289.apporganizadordefotos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

import java.util.ArrayList;

import modelo.CategoriaFoto;
import modelo.SpinnerCategoriasAdapter;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategorias;
    private ArrayList<CategoriaFoto> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategorias = findViewById ( R.id.spinnerCategorias );

        inicializarCategorias ();

        SpinnerCategoriasAdapter adaptador = new SpinnerCategoriasAdapter ( this, categorias );
        adaptador.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        // Establecer el adaptador para el spinner
        spinnerCategorias.setAdapter ( adaptador );
    }

    private void inicializarCategorias () {
        categorias = new ArrayList<>();
        String [] categoriasAgregadas = getResources().getStringArray ( R.array.categorias );
        for (int i = 0; i < categoriasAgregadas.length; i++) {
            categorias.add ( new CategoriaFoto ( categoriasAgregadas [i] ) );
        }
    }
}