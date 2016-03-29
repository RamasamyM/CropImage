package com.example.admin.cropimage;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Admin on 3/17/2016.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Bitmap photo;
    private ImageView cameraImage, cameraImage1;
    public static final int id = R.id.my_camera;
    private Button get_image, get_camera;
    public static final int get_imageID = R.id.activity_button;
    public  static final  int get_image_action= R.id.camera_button;
    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_REQUEST = 8888;
    private static final int PIC_CROP = 2;
    private String selectedImagePath;
    String mCurrentPhotoPath;
    private Uri picUri;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraImage = (ImageView) findViewById(R.id.my_camera);
        cameraImage.setId(id);
        cameraImage1 = (ImageView) findViewById(R.id.my_camera);
        get_image = (Button) findViewById(R.id.activity_button);
        get_image.setOnClickListener(this);
        get_camera = (Button) findViewById(R.id.camera_button);
        get_camera.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case get_imageID:
                selectImage();
                break;

            case get_image_action:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.DATA.toString());
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            default:
                break;
        }
    }
    private void selectImage()
    {
        final CharSequence[] options = {"Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item)
            {
                if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_PICTURE);
                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE)
            {
                picUri = data.getData();
                performCrop();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(picUri,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("image  path ...", picturePath+"");
                cameraImage.setImageBitmap(thumbnail);
            }
        }

        if (requestCode == CAMERA_REQUEST)
        {
            Bundle bundle = data.getExtras();
            if (bundle != null)
            {
                photo = (Bitmap) bundle.get("data");
                cameraImage.setImageBitmap(photo);
            }
        }

    }
    private void performCrop(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
