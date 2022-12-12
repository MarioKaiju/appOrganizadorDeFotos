package mx.edu.itl.c18131289.apporganizadordefotos;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import modelo.Image;
import modelo.ImageAdapter;

public class ImagenActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Image> arrayList = new ArrayList<>();
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    getImages();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclew_view);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(ImagenActivity.this));
        recyclerView.setHasFixedSize(true);

        if (ActivityCompat.checkSelfPermission(ImagenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else if (ActivityCompat.checkSelfPermission(ImagenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            getImages();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImages();
    }

    private void getImages(){
        arrayList.clear();
        String filePath = "/storage/emulated/0/Pictures";
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                if (file1.getPath().endsWith(".png") || file1.getPath().endsWith(".jpg")) {
                    arrayList.add(new Image(file1.getName(), file1.getPath(), file1.length()));
                }
            }
        }
        ImageAdapter adapter = new ImageAdapter(ImagenActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, path) -> startActivity(new Intent(ImagenActivity.this, ImageViewerActivity.class).putExtra("image", path)));
    }
}