package mx.edu.itl.c18131289.apporganizadordefotos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import modelo.CategoriaFoto;
import modelo.SpinnerCategoriasAdapter;
import util.permisos.ChecadorDePermisos;
import util.permisos.PermisoApp;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategorias;
    private ArrayList<CategoriaFoto> categorias;
    private String categoriaSeleccionada;
    private PermisoApp[] permisosReq = {
            new PermisoApp  ( Manifest.permission.CAMERA, "Camara", true  ),
            new PermisoApp  ( Manifest.permission.READ_EXTERNAL_STORAGE, "Almacenamiento", true  ),
            new PermisoApp  ( Manifest.permission.WRITE_EXTERNAL_STORAGE, "Almacenamiento", true )
    };
    private Uri uriFoto;
    public static final int CODIGO_CAPTURA_FOTO = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChecadorDePermisos.checarPermisos ( this, permisosReq );

        spinnerCategorias = findViewById ( R.id.spinnerCategorias );

        inicializarCategorias ();
        categoriaSeleccionada = categorias.get ( 0 ).getCategoria();

        SpinnerCategoriasAdapter adaptador = new SpinnerCategoriasAdapter ( this, categorias );
        adaptador.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );
        // Establecer el adaptador para el spinner
        spinnerCategorias.setAdapter ( adaptador );

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoriaSeleccionada = categorias.get ( i ).getCategoria ();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult ( requestCode, permissions, grantResults );

        if ( requestCode == ChecadorDePermisos.CODIGO_PEDIR_PERMISOS ) {
            ChecadorDePermisos.verificarPermisosSolicitados ( this, permisosReq, permissions, grantResults );
        }
    }

    private void inicializarCategorias () {
        categorias = new ArrayList<>();
        String [] categoriasAgregadas = getResources().getStringArray ( R.array.categorias );
        for (int i = 0; i < categoriasAgregadas.length; i++) {
            categorias.add ( new CategoriaFoto ( categoriasAgregadas [i] ) );
        }
    }

    public void buttonTomarFotografia ( View v ) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMddHHmmSS");
        String strFechaHora = simpleDateFormat.format ( new Date () );
        String archFoto = categoriaSeleccionada + "_" + strFechaHora + ".jpg";

        File directorioFoto = new File (
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator
                        + "DCIM"
                        + File.separator
                        + "Organizador"
                        + File.separator
                        + categoriaSeleccionada
                        + File.separator );

        if ( !directorioFoto.exists() ) {
            directorioFoto.mkdirs ();
        }

        File fileFoto = new File ( directorioFoto.getAbsolutePath() + File.separator + archFoto );
        uriFoto = FileProvider.getUriForFile ( this, BuildConfig.APPLICATION_ID + ".provider", fileFoto );

        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra ( MediaStore.EXTRA_OUTPUT, uriFoto );
        startActivityForResult ( intent, CODIGO_CAPTURA_FOTO );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == CODIGO_CAPTURA_FOTO ) {
            if ( resultCode == RESULT_OK ) {
                Toast.makeText( this, "Se guardó la foto", Toast.LENGTH_SHORT).show();
            }
        }
    }
}