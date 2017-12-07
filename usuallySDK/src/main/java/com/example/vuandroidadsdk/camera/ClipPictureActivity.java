package com.example.vuandroidadsdk.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.vuandroidadsdk.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClipPictureActivity extends AppCompatActivity implements View.OnClickListener{
    private Uri imageUri;

    private ClipImageLayout mClip;
    private ImageView mBack;
    private ImageView mApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clip_picture);
        getIntentData();
        initView();
        setClick();
    }

    private void setClick() {
        mBack.setOnClickListener(this);
        mApply.setOnClickListener(this);
    }

    private void initView() {
        mClip = (ClipImageLayout) findViewById(R.id.clip_image_layout);
        mClip.mZoomImageView.setImageURI(imageUri);
//        mClip.mClipImageView.setmBorderColor(Color.parseColor("#6699ff"));
        mBack = (ImageView) findViewById(R.id.clip_cancel_btn);
        mApply = (ImageView) findViewById(R.id.clip_apply_btn);
    }


    private void getIntentData() {
        imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.clip_cancel_btn) {
            finish();

        } else if (i == R.id.clip_apply_btn) {
            clipImages();
            finish();

        }
    }

    private void clipImages() {
        Bitmap bitmap = mClip.clip();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        Intent intent = new Intent();
        intent.putExtra("imageUri",saveFile(baos.toByteArray()));
        setResult(RESULT_OK,intent);
    }

    //保存临时文件的方法
    private String saveFile(byte[] bytes){
        try {
            File mFile = File.createTempFile("img","");
            FileOutputStream fos = new FileOutputStream(mFile);
            fos.write(bytes);
            fos.flush();
            fos.close();

            return mFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
