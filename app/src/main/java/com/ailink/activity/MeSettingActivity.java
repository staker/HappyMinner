package com.ailink.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailink.constants.ConstantString;
import com.ailink.db.Configuration;
import com.ailink.http.ServerUtil;
import com.ailink.i.HttpObjectListener;
import com.ailink.i.ResultListener;
import com.ailink.logic.DialogChooseSex;
import com.ailink.logic.JumpActivityUtil;
import com.ailink.pojo.UserPojo;
import com.ailink.util.BitmapFactoryUtil;
import com.ailink.util.BroadcastUtil;
import com.ailink.util.ImageHandleUtil;
import com.ailink.util.Logg;
import com.ailink.util.StakerUtil;
import com.ailink.view.RoundImageView;
import com.ailink.view.util.DialogUtil;
import com.ailink.view.util.ToastUtil;
import com.ailink.view.util.TopBarLayoutUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;

import juhuiwan.ailink.android.R;
import libs.glide.GlideUtil;


/**
 */
public class MeSettingActivity extends BaseActivity implements  TakePhoto.TakeResultListener,InvokeListener {

    TopBarLayoutUtil topBar;
    private RoundImageView imgHead,imgReview;
    private TextView txtNickname,txtSex,txtPhone;
    private RelativeLayout layoutHead,layoutNickname,layoutPwd,layoutSex;


    boolean isFromRegister=false;
    final int Take_Photo = 1, Pick_Pictrue = 2;File tempFile;
   String tempPath ;






    InvokeParam invokeParam;
    TakePhoto takePhoto;



    int RequestStoragePermissionCode=100;//申请读取图片的写文件的权限


    @Override
    protected void initViews(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        imgHead=(RoundImageView)findViewById(R.id.img_head);
        imgReview=(RoundImageView)findViewById(R.id.img_review);
        txtNickname=(TextView)findViewById(R.id.txt_nickname);
        layoutHead=(RelativeLayout)findViewById(R.id.layout_head);
        layoutNickname=(RelativeLayout)findViewById(R.id.layout_nickname);
        txtSex=(TextView)findViewById(R.id.txt_sex);
        txtPhone=(TextView)findViewById(R.id.txt_phone);

        layoutSex=(RelativeLayout)findViewById(R.id.layout_sex);
        initIdsClickLinstener(R.id.txt_exit,R.id.layout_pwd);
        initViewsClickLinstener(layoutHead,layoutNickname,layoutSex);


        tempPath= StakerUtil.getSDPath()+"/happy_miner/miner_default_head.jpg";
        initTopBar();
        init();
        refreshUserData();
        getTakePhoto();


    }

    private void init() {
        isFromRegister=getIntent().getBooleanExtra("isFromRegister",false);
        Logg.e("isFromRegister="+isFromRegister);
        UserPojo user= Configuration.getInstance(this).getUserPojo();
        if(user==null){
           return;
        }
        if(user.sex==1){
            txtSex.setText("男");
            layoutSex.setEnabled(false);
        }else if(user.sex==2){
            txtSex.setText("女");
            layoutSex.setEnabled(false);
        }else{
            txtSex.setText("未选择");
            layoutSex.setEnabled(true);
        }
        txtNickname.setText(""+user.userName);
        if(user.userPhone!=null){
            txtPhone.setText(user.userPhone);
        }
        if(user.review){
            imgReview.setVisibility(View.VISIBLE);
        }else{
            imgReview.setVisibility(View.GONE);
        }

        GlideUtil.getInstance().setImage(imgHead,user.avatarUrl);
        tempFile = new File(tempPath);
        if (!tempFile.exists()) {
            // 如果这个临时文件不存在点话，则新建一个父的空文件夹
            File vDirPath = tempFile.getParentFile();
            vDirPath.mkdirs();
            try{
                tempFile.createNewFile();
            }catch (Exception e){

            }
        }else{
        }
    }
    private void initTopBar() {
        topBar = new TopBarLayoutUtil(this);
        topBar.seBottomLineColor(Color.argb(255, 234, 234, 234));
        topBar.setTitle("个人信息");
        topBar.setBackListener(new TopBarLayoutUtil.IOnClick() {
            @Override
            public void onClick() {
                if(isFromRegister){
                    JumpActivityUtil.showNormalActivity(mContext,MainHomeActivity.class);
                }
                finish();
            }
        });
    }

    private void refreshUserData(){
        ServerUtil.getMyDetailInfo(new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                init();
            }
        });
    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_exit:
                Configuration.getInstance(this).setUserToken(null);
                finish();
                BroadcastUtil.sendBroadcast(this, ConstantString.BroadcastActions.Action_Exit_App);
                JumpActivityUtil.showNormalActivityClearTop(mContext,LoginActivity.class);
                break;
            case R.id.layout_nickname:
                editNickName();
                break;
            case R.id.layout_head:
                doChangeAvatar();
                break;
            case R.id.layout_pwd:
               JumpActivityUtil.showNormalActivity(this,ChangePwdActivity.class);
                break;
            case R.id.layout_sex:
                new DialogChooseSex(this, 0, new ResultListener() {
                    @Override
                    public void onSucess(Object object) {
                        String sex=(String)object;
                        if("1".equals(sex)){
                            txtSex.setText("男");
                            layoutSex.setEnabled(false);
                            updateSex(sex);
                        }else if("2".equals(sex)){
                            txtSex.setText("女");
                            layoutSex.setEnabled(false);
                            updateSex(sex);
                        }
                    }
                }).showDialog();
                break;
        }
    }
    private void updateSex(String sex){
        ServerUtil.sendUpdateSex(sex, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ToastUtil.showToast("性别修改成功",mContext);
            }

            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(""+dataError,mContext);
                txtSex.setText("未选择");
                layoutSex.setEnabled(true);
            }
        });
    }
    private void  sendUpdateName(String nickName){
        ServerUtil.sendUpdateName(nickName, new HttpObjectListener() {
            @Override
            public void onSucess(Object object) {
                ToastUtil.showToast("昵称修改成功",mContext);
            }
            @Override
            public void onDataError(String dataError) {
                super.onDataError(dataError);
                ToastUtil.showToast(""+dataError,mContext);
            }
        });


    }
    private void editNickName() {
        DialogUtil.showDialogEditText(this, "修改昵称", txtNickname.getText()+"", null, null, new DialogUtil.OnEditEndListener() {
            @Override
            public void onClickIndex(int buttonIndex, String editContent) {
                if(editContent!=null&&editContent.length()>0){
                    txtNickname.setText(editContent);
                    sendUpdateName(editContent);
                }
            }
        });
    }



    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    @Override
    public void takeCancel() {
        Logg.e("takeCancel");
    }

    @Override
    public void takeSuccess(TResult tResult) {
                Logg.e("takeSuccess");
//                GlideUtil.getInstance().setImage(imgHead,tempPath);
                Bitmap bitmap= BitmapFactoryUtil.readBmpFromPath(tempPath,500,500);
                byte[] fileStream=ImageHandleUtil.compressBitmap(bitmap,100);
                String base64= Base64.encodeToString(fileStream,Base64.DEFAULT);
                ServerUtil.sendUpdateAvatar(base64, new HttpObjectListener() {
                    @Override
                    public void onSucess(Object object) {
                        ToastUtil.showToast("头像上传成功!",mContext);
                        refreshUserData();
                    }
                    @Override
                    public void onDataError(String dataError) {
                        super.onDataError(dataError);
                        ToastUtil.showToast(""+dataError,mContext);
                    }
                });
    }

    @Override
    public void takeFail(TResult tResult, String s) {
            Logg.e("takeFail");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
        switch (requestCode) {
            case 2000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tempFile = new File(tempPath);
                    if (!tempFile.exists()) {
                        // 如果这个临时文件不存在点话，则新建一个父的空文件夹
                        File vDirPath = tempFile.getParentFile();
                        vDirPath.mkdirs();
                    }
                }else{
                    ToastUtil.showToast("权限没有开启",mContext);
                }
                break;
        }

        if(requestCode ==RequestStoragePermissionCode){//读取图片和写文件的时候返回的权限
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tempFile = new File(tempPath);
                if (!tempFile.exists()) {
                    // 如果这个临时文件不存在点话，则新建一个父的空文件夹
                    File vDirPath = tempFile.getParentFile();
                    vDirPath.mkdirs();
                }
                doPickPictrue();
            }else{
                ToastUtil.showToast("权限没有开启",mContext);
            }
        }

    }
    private void doChangeAvatar() {
        DialogUtil.showDialogEditBtn3(this, "更换头像", "照相", "相册", "取消", new DialogUtil.OnIndexButtonListener() {
            @Override
            public void onClickIndex(int buttonIndex) {
                switch (buttonIndex) {
                    case 1:
                        Uri uri=Uri.parse(tempPath);
                        CropOptions.Builder builder = new CropOptions.Builder();
                        builder.setAspectX(1).setAspectY(1);
                        builder.setOutputX(200).setOutputY(200);
                        builder.setWithOwnCrop(false);
                        takePhoto.onPickFromCaptureWithCrop(uri,builder.create());
                        break;
                    case 2:

//                        Uri uri2=Uri.parse(tempPath);
//                        CropOptions.Builder builder2 = new CropOptions.Builder();
//                        builder2.setAspectX(1).setAspectY(1);
//                        builder2.setOutputX(200).setOutputY(200);
//                        builder2.setWithOwnCrop(false);
//                        takePhoto.onPickFromGalleryWithCrop(uri2,builder2.create());
                        checkPerm();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Logg.e("requestCode="+requestCode);
        switch (requestCode) {
            case Pick_Pictrue: // 调用Gallery返回的(包括直接选择和裁剪)
                takeSuccess(null);
//                GlideUtil.getInstance().setImage(imgHead,tempPath);
//                Bitmap bitmap= BitmapFactoryUtil.readBmpFromPath(tempPath,200,200);
//                String fileStream=ImageHandleUtil.compressBitmap(bitmap,100).toString();
//                ServerUtil.sendUpdateAvatar(fileStream, new HttpObjectListener() {
//                    @Override
//                    public void onSucess(Object object) {
//                        ToastUtil.showToast("头像上传成功!",mContext);
//                    }
//                    @Override
//                    public void onDataError(String dataError) {
//                        super.onDataError(dataError);
//                        ToastUtil.showToast(""+dataError,mContext);
//                    }
//                });
                break;
            case Take_Photo: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
//                doCropPhoto();
                break;
            }
        }
    }



    private void checkPerm() {
        /**1.在AndroidManifest文件中添加需要的权限。
         *
         * 2.检查权限
         *这里涉及到一个API，ContextCompat.checkSelfPermission，
         * 主要用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED
         * 或者PackageManager.PERMISSION_GRANTED。当返回DENIED就需要进行申请授权了。
         * */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){ //权限没有被授予
            /**3.申请授权
             * @param
             *  @param activity The target activity.（Activity|Fragment、）
             * @param permissions The requested permissions.（权限字符串数组）
             * @param requestCode Application specific request code to match with a result（int型申请码）
             *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
             *    int, String[], int[])}.
             * */
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RequestStoragePermissionCode);

        }else{//权限被授予
            doPickPictrue();
            //直接操作
        }

    }

    protected void doPickPictrue() {




        try {
            // 调用系统相册选择图片，并按照给定点大小进行裁剪
            // Launch picker to choose photo for selected contact
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            // DisplayMetrics dm = getResources().getDisplayMetrics();
            int width = 400;
            int height = 400;
            intent.putExtra("crop", "true");
            // 这里只是设置图片的裁剪比例，并不是图片的大小，所以直接用1：1就可以，因为头像是正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // 图片输出大小(指图片尺寸的大小)
             intent.putExtra("outputX", width);
             intent.putExtra("outputY", height);
            intent.putExtra("scale", true);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", false);
            intent.putExtra("output", Uri.fromFile(new File(tempPath)));// 裁剪之后的图片直接放在这个文件里面
            startActivityForResult(intent, Pick_Pictrue);
        } catch (ActivityNotFoundException e) {
            Logg.e("选择相片出异常", e);
        }
    }
//    protected void doCropPhoto() {
//        try {
//            // 启动gallery去剪辑这个照片
//            Uri uri = Uri.fromFile(new File(tempPath));// 读出这个文件点uri地址
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setDataAndType(uri, "image/*");// 从这个对应点uri里面选择资源图片
//            int width = 200;
//            int height = 200;
//            intent.putExtra("crop", "true");// 可裁剪
//            // 这里只是设置图片的裁剪比例，并不是图片的大小，所以直接用1：1就可以，因为头像是正方形
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//
//            // 图片输出大小(指图片尺寸的大小)
//            intent.putExtra("outputX", width);
//            intent.putExtra("outputY", height);
//
//            intent.putExtra("scale", true);
//            intent.putExtra("noFaceDetection", true);
//            intent.putExtra("return-data", false);// 表示不返回数据
//            intent.putExtra("output", uri);// 裁剪后点保存地址(即保存在原始地址)
//            startActivityForResult(intent, Pick_Pictrue);
//        } catch (Exception e) {
//            Logg.e("doCropPhoto error", e);
//        }
//
//    }



    @Override
    public void onBackPressed() {
        if(isFromRegister){
            JumpActivityUtil.showNormalActivity(mContext,MainHomeActivity.class);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
