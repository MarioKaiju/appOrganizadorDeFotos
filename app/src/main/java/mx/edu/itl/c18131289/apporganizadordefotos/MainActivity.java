package mx.edu.itl.c18131289.apporganizadordefotos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.MenuItem;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.view.Menu;
import modelo.CategoriaFoto;
import modelo.SpinnerCategoriasAdapter;
import util.permisos.ChecadorDePermisos;
import util.permisos.PermisoApp;

public class MainActivity extends AppCompatActivity {
    SpinnerCategoriasAdapter adaptador;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    private Spinner spinnerCategorias;
    private EditText editTextAgregarCategoria;
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
        sharedPref = this.getSharedPreferences(
                getString(R.string.app_name), Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit ();

        if ( sharedPref.getAll().isEmpty() ) {
            inicializarCategorias();
        }

        categorias = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChecadorDePermisos.checarPermisos ( this, permisosReq );

        spinnerCategorias = findViewById ( R.id.spinnerCategorias );
        editTextAgregarCategoria = findViewById ( R.id.editTextAñadirCategoría );
        
        recargarCategorias ();

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
        String [] categoriasIniciales = getResources().getStringArray ( R.array.categorias );
        for (int i = 0; i < categoriasIniciales.length; i++) {
            sharedPrefEditor.putString( categoriasIniciales[i], categoriasIniciales[i] );
        }
        sharedPrefEditor.apply();
    }

    private void recargarCategorias () {
        categorias.clear();
        String [] categoriasAgregadas = sharedPref.getAll().keySet().toArray(new String[0]);
        for (int i = 0; i < categoriasAgregadas.length; i++) {
            categorias.add ( new CategoriaFoto ( categoriasAgregadas [i] ) );
        }

        // Establecer el adaptador para el spinner
        adaptador = new SpinnerCategoriasAdapter ( this, categorias );
        adaptador.setDropDownViewResource ( android.R.layout.simple_spinner_dropdown_item );

        spinnerCategorias.setAdapter ( adaptador );
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

    public void buttonAgregarCategoria ( View v ) {
        String categoriaPorAñadir = editTextAgregarCategoria.getText().toString().trim();
        if ( categoriaPorAñadir.length() == 0 ) {
            editTextAgregarCategoria.setError ( "No puede estar vacío" );
            editTextAgregarCategoria.requestFocus ();
            return;
        }
        for ( int i = 0; i < categorias.size(); i++ ) {
            if ( categorias.get(i).getCategoria().toLowerCase().equals(categoriaPorAñadir)) {
                editTextAgregarCategoria.setError ( "Ya existe esa categoría" );
                editTextAgregarCategoria.requestFocus ();
                return;
            }
        }
        editTextAgregarCategoria.setText ("");

        sharedPrefEditor.putString (categoriaPorAñadir, categoriaPorAñadir);
        sharedPrefEditor.apply();

        editTextAgregarCategoria.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAgregarCategoria.getWindowToken(), 0);

        Toast.makeText(this, "Categoría añadida", Toast.LENGTH_SHORT).show();

        recargarCategorias();
    }

    public void buttonAbrirGaleria ( View v ) {
     Intent intent = new Intent(this,ImagenActivity.class);
     startActivity(intent);
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu_comun,menu);
            return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch ( id ) {
            case R.id.mniAcercaDe : Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);
            break;
            default               : return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
