package com.example.alexandroskarargyris.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;


    static {
        System.loadLibrary("MyLib");
        System.loadLibrary("opencv_java3");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.testTextView);
        tv.setText("");


        final Button buttonClassify = (Button) findViewById(R.id.button);
        buttonClassify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long tStart = System.currentTimeMillis();

                TextView tv = (TextView) findViewById(R.id.testTextView);

                tv.setText(NativeClass.getStringFromNative());

                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;


                TextView clock = (TextView) findViewById(R.id.clock);
                clock.setText("Classified in "+Double.toString(elapsedSeconds) + " secs");
            }
        });


        final Button btnSelect = (Button) findViewById(R.id.button2);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            ImageView imgview = (ImageView) findViewById(R.id.image);
            Bitmap img;
            if (requestCode == REQUEST_CAMERA) {
                img = (Bitmap) data.getExtras().get("data");
                imgview.setImageBitmap(img);
            } else {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                img = BitmapFactory.decodeFile(selectedImagePath, options);

                imgview.setImageBitmap(img);
            }

            String destFolder = getCacheDir().getAbsolutePath();

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(destFolder + "/image.jpg");
                img.compress(Bitmap.CompressFormat.JPEG, 100, out);
                TextView tv = (TextView) findViewById(R.id.testTextView);
                tv.setText(destFolder + "/image.jpg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

}
