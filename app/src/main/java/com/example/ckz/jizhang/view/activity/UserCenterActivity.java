package com.example.ckz.jizhang.view.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.presenter.MUserCenterPresenter;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.StatusBarUtil;
import com.example.ckz.jizhang.view.view.ClearEditText;
import com.example.ckz.jizhang.view.mvpview.MUserCenterView;
import com.example.vuandroidadsdk.camera.CameraHelper;
import com.example.vuandroidadsdk.camera.ClipPictureActivity;
import com.example.vuandroidadsdk.showpop.ShowPopup;
import com.example.vuandroidadsdk.utils.MyDialogUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.Calendar;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class UserCenterActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,
        MUserCenterView{


    private ImageView backbtn;
    private RoundedImageView usericon;
    private RelativeLayout iconlayout;
    private TextView username;
    private RelativeLayout namelayout;
    private TextView userid;
    private RelativeLayout idlayout;
    private TextView userbirthday;
    private RelativeLayout birthlayout;
    private TextView usersex;
    private RelativeLayout sexlayout;
    private Button mOut;

    DatePickerDialog datePickerDialog;

    private int year,month,day;
    private MUserCenterPresenter presenter;

    private String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        initView();
        StatusBarUtil.setTransparent(this);
        getDate();
        presenter = new MUserCenterPresenter(this);
        datePickerDialog = new DatePickerDialog(this,DatePickerDialog.THEME_HOLO_LIGHT,this,year,month,day);

    }

    private void initView() {
        sexlayout = (RelativeLayout) findViewById(R.id.sex_layout);
        usersex = (TextView) findViewById(R.id.user_sex);
        birthlayout = (RelativeLayout) findViewById(R.id.birth_layout);
        userbirthday = (TextView) findViewById(R.id.user_birthday);
        idlayout = (RelativeLayout) findViewById(R.id.id_layout);
        userid = (TextView) findViewById(R.id.user_id);
        namelayout = (RelativeLayout) findViewById(R.id.name_layout);
        username = (TextView) findViewById(R.id.user_name);
        iconlayout = (RelativeLayout) findViewById(R.id.icon_layout);
        usericon = (RoundedImageView) findViewById(R.id.user_icon);
        backbtn = (ImageView) findViewById(R.id.back_btn);
        mOut = findViewById(R.id.login_out);
        iconlayout.setOnClickListener(this);
        namelayout.setOnClickListener(this);
        idlayout.setOnClickListener(this);
        birthlayout.setOnClickListener(this);
        sexlayout.setOnClickListener(this);
        mOut.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUI();
    }

    private void setUI(){
        MyUser user = MyUser.getCurrentUser(MyUser.class);
        if (user.getUserIcon()!=null){
            Glide.with(this).load(user.getUserIcon().getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(usericon);
        }else {
            usericon.setImageResource(R.drawable.skin_drawable_user);
        }
        username.setText(user.getUserName());
        userid.setText(user.getObjectId());
        userbirthday.setText(user.getUserBirthday());
        usersex.setText(user.getUserSex());
    }

    private void getDate(){
        Calendar c = Calendar.getInstance();
//        取得系统日期:
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.icon_layout:
                setIcon();
                break;
            case R.id.name_layout:
                setUserName();
                break;
            case R.id.birth_layout:
                datePickerDialog.show();
                break;
            case R.id.sex_layout:
                setUserSex();
                break;
            case R.id.login_out:
                MyDialogUtils.getInstance().createNormalDialog(this, "", "确定推出登陆吗?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyUser.logOut();
                        dialogInterface.dismiss();
                    }
                });
                break;
        }
    }

    private void setIcon() {
        UserCenterActivityPermissionsDispatcher.needWithCheck(this);
        //弹窗选择
        ShowPopup.getInstance(this).createSimplePopupWindow("拍照","相册","取消")
                .defaultAnim()
                .atBottom(usersex)
                .setPositionClickListener(new ShowPopup.OnPositionClickListener() {
                    @Override
                    public void OnPositionClick(PopupWindow popup, View view, int position) {
                        switch (position){
                            case 0:
                                //拍照
                                CameraHelper.getInstance(UserCenterActivity.this).openMyCamera();
                                popup.dismiss();
                                break;
                            case 1:
                                //打开相册
                                CameraHelper.getInstance(UserCenterActivity.this).openAlbum();
                                popup.dismiss();
                                break;
                            case 2:
                                popup.dismiss();
                                break;

                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CameraHelper.OPEN_MYCAMERA:
               if (resultCode == RESULT_OK){
                   Intent intent = new Intent(this,ClipPictureActivity.class);
                   intent.putExtra("imageUri",data.getStringExtra("imageUri"));
                   startActivityForResult(intent,CameraHelper.CLIP_PIC);
               }
               break;
               case CameraHelper.ALBUM_REQUEST_CODE:
                   if (resultCode == RESULT_OK){
                       Intent intent = new Intent(this,ClipPictureActivity.class);
                       intent.putExtra("imageUri",data.getData().toString());
                       startActivityForResult(intent,CameraHelper.CLIP_PIC);

                   }
                   break;
               case CameraHelper.CLIP_PIC:
                   if (resultCode == RESULT_OK){
                      final File file = new File(data.getStringExtra("imageUri"));
//
//      File file = new File("/storage/emulated/0/icon/1512016003534.jpg");
                       Log.d(TAG,file.getAbsolutePath());
                       usericon.setImageURI(Uri.fromFile(file));
                       presenter.saveIcon(file);
                   }
        }
    }

    private ShowPopup popup;
    private ClearEditText inputName;
    private void setUserName(){
       popup =   ShowPopup.getInstance(this).createLayoutPopupWindow(R.layout.pop_name).atBottom(sexlayout)
                .defaultAnim().setDismissClick(R.id.back_btn).setClick(R.id.apply_btn, new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (inputName.getText().length()!=0){
                           username.setText(inputName.getText().toString());
                           presenter.saveName(inputName.getText().toString());
                           popup.closePopupWindow();
                       }else {
                           Toast.makeText(UserCenterActivity.this,"请输入名字",Toast.LENGTH_SHORT).show();
                       }
                   }
               });

       inputName = popup.getView().findViewById(R.id.input_name);
       inputName.setText(username.getText());
    }

    private void setUserSex() {
        final String[] strings =  new String[]{"男", "女"};
        MyDialogUtils.getInstance().createSingleListDialog(this, "选择性别",strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usersex.setText(strings[i]);
                //保存性别
                presenter.saveUserSex(strings[i]);
                dialogInterface.dismiss();

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i+"-"+(i1+1)+"-"+i2;
       userbirthday.setText(date);
       //保存生日
        presenter.saveBirth(date);
    }

    @Override
    public void showSuccess() {
        Toast.makeText(this,"设置成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFaild() {
        Toast.makeText(this,"设置失败",Toast.LENGTH_SHORT).show();
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void need() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        UserCenterActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
