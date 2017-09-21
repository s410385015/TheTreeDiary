package com.example.nako.thetreediary.myClass;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nako.thetreediary.EditActivity;
import com.example.nako.thetreediary.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Yo on 2016/12/20.
 */

public class ImageLoader extends BottomSheetDialogFragment implements View.OnClickListener {

    private int SELECT_FROM_CAMERA = 0;
    private int SELECT_FROM_GALLERY = 1;

    private View rootView;
    private ImageView from_Gallery, from_Camera;
    private Context context;

    private String path;
    private File file;

    private Intent intent;
    public Bitmap image;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        this.getDialog().setCanceledOnTouchOutside(true);
        rootView = inflater.inflate(R.layout.botdialog_gallery_or_camera, container);

        from_Gallery = (ImageView) rootView.findViewById(R.id.from_gallery);
        from_Camera = (ImageView) rootView.findViewById(R.id.from_camera);
        from_Gallery.setOnClickListener(this);
        from_Camera.setOnClickListener(this);

        return rootView;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.from_gallery:
                Intent intent = new Intent( Intent.ACTION_PICK );
                intent.setType( "image/*" );
                startActivityForResult( intent, SELECT_FROM_GALLERY );
                break;
            case R.id.from_camera:
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
                verifyStoragePermissions((EditActivity)getActivity());
                file = new File(path + "/" + "photo.png");
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, SELECT_FROM_CAMERA);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_FROM_CAMERA || requestCode == SELECT_FROM_GALLERY){
            if(resultCode == RESULT_OK){
                Uri u = data.getData();
                ContentResolver cr = ((EditActivity)getActivity()).getContentResolver();
                Bitmap image = null;
                try {
                    image = BitmapFactory.decodeStream(cr.openInputStream(u));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                image = CutToSquare(getResizedBitmap(image,image.getWidth()/3, image.getHeight()/3));
                Matrix m = new Matrix();
                try {
                    m.setRotate(GetDegree(u)+90);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);
                String base64 = Bitmap2String(image);
                String content = "<img src =\"" + base64+"\"></img>";
                EditText editBox=(EditText)((EditActivity)getActivity()).findViewById(R.id.editText);
                editBox.append(Html.fromHtml(content, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        byte[] data;
                        data = Base64.decode(source,Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Drawable d = new BitmapDrawable(bitmap);
                        d.setBounds(0,0,200,200);   // <-----
                        return d;
                    }
                }, null));

                //image.recycle();
            }
        }
        dismiss();
    }

    private Bitmap CutToSquare(Bitmap image){
        int w = image.getWidth();
        int h = image.getHeight();
        int wh = w > h ? h : w;
        int retX = w > h ? (w - h) / 2 : 0;
        int retY = w > h ? 0 : (h - w) / 2;
        return Bitmap.createBitmap(image, retX, retY, wh, wh, null, false);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public String Bitmap2String(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte bytes[] = stream.toByteArray();
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64;
    }

    public float GetDegree(Uri u) throws IOException {
        ExifInterface exif = null;
        exif = new ExifInterface(u.getEncodedPath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }
}
