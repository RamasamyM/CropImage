package com.example.admin.cropimage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Admin on 3/29/2016.
 */
public class Content extends Activity
    {
        /** Called when the activity is first created. */
        private static final int SELECT_PICTURE = 1;
        private static final int PICK_FROM_CAMERA = 2;
        private Uri my_uri;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content);
            final CharSequence[] items = {"Open Camera", "Pick gallery"};
            AlertDialog.Builder builder  = new AlertDialog.Builder(Content.this);
            builder.setTitle("Select Image");
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int item)
                {
                    Intent intent = new Intent();
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 730);
                    intent.putExtra("aspectY", 1115);
                    intent.putExtra("outputX", 730);
                    intent.putExtra("outputY", 1115);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile());
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    if(item==0)
                    {
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }
                    else if(item==1)
                    {
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECT_PICTURE);
                    }
                }
                private Uri getTempFile()
                {
                    my_uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"Image_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    return my_uri;
                }
            });
            final AlertDialog alert = builder.create();
            ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    alert.show();
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            switch(requestCode)
            {
                case PICK_FROM_CAMERA : if (resultCode == RESULT_OK)
                {
                    String filePath= my_uri.getPath();
                    Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    ImageView image = (ImageView)findViewById(R.id.selectedimage);
                    image.setImageBitmap(selectedImage);
                }
                    break;
                case SELECT_PICTURE : if (resultCode == RESULT_OK)
                {
                    String filePath= my_uri.getPath();
                    Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
                    Bitmap selectedImage =  BitmapFactory.decodeFile(filePath);
                    ImageView image = (ImageView)findViewById(R.id.selectedimage);
                    image.setImageBitmap(selectedImage);
                }
                    break;
                default:
                    break;
            }
        }
    }