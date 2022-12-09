package mx.edu.itl.c18131289.apporganizadordefotos;
/*------------------------------------------------------------------------------------------*/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/*------------------------------------------------------------------------------------------*/
public class SplashActivity extends AppCompatActivity {
    /*------------------------------------------------------------------------------------------*/
    // Método oncreate desde el reescribimos el medoto run para iniciar nuestro MainActivity
    // con un retraso de 4 segundos en los que aparecerá la pantalla Splash
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.splash_layout );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                // This method will be executed once the timer is over
                Intent intent = new Intent ( SplashActivity.this, MainActivity.class );
                startActivity ( intent );
                finish ();
            }
        }, 0 );
    }
}
