
package mx.edu.itl.c18131289.apporganizadordefotos;
/*------------------------------------------------------------------------------------------*/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import mx.edu.itl.c18131289.apporganizadordefotos.R;

/*------------------------------------------------------------------------------------------*/
public class AcercaDeActivity extends AppCompatActivity {
    /*------------------------------------------------------------------------------------------*/
    // Método oncreate desde el reescribimos el medoto run para iniciar nuestro MainActivity
    // con un retraso de 4 segundos en los que aparecerá la pantalla Splash
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_acercade);
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+ R.raw.sandip);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

    }
}
