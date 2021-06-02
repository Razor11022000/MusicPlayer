package com.example.music_player;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.ListViewSongs);

        runtimePermission();

    }

    public void runtimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // doStuff();
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    /*public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                String currentTitle = songCursor.getString(songTitle);
                arrayList.add(currentTitle);
            } while (songCursor.moveToNext());
        }
    }*/

    /*public void doStuff() {
        listView = findViewById(R.id.ListViewSongs);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }*/

    //method to find the songs from external storage
    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        //for loop to check all the files whether its mp3 file or a folder
        for (File singleFile : files) {
            //checking whether the file we are getting is a directory or not
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                //If this happens we have to send the whole directory into an array
                //list again to check for songs inside that folder
                arrayList.addAll(findSong(singleFile));
            } else {
                //we check if the file is a mp3 file or wav file
                if (singleFile.getName().endsWith(".mp3") && singleFile.getName().endsWith(".wav")) {
                    //If this happens we will add this file to arrayList
                    //we are getting a song file here
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    //method to display songs inside the ListView
    private void displaySongs() {
        File directory = Environment.getExternalStorageDirectory();
        final ArrayList<File> mySongs = findSong(directory);
        items = new String[mySongs.size()];

        //create for to store all songs inside the items
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName();
        }
        //Here we will attach all the items into the ListView that is created
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        //attaching the adapter to ListView
        listView.setAdapter(myAdapter);
    }
}