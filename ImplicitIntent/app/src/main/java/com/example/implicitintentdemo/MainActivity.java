package com.example.implicitintentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AutomaticZenRule;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_SMS = 500;
    private final int REQUEST_CONTACT = 400;
    private final int REQUEST_GALLERY = 300;
    private final int REQUEST_CAMERA = 200;
    private final int REQUEST_CODE = 100;
    @BindView(R.id.txtfirst) TextInputEditText txtfirst;
    @BindView(R.id.txtsecond) TextInputEditText txtsecond;
    @BindView(R.id.imgpicture) ImageView imgpicture;
    @BindView(R.id.btncapturephoto) Button btncapturephoto;
    @BindView(R.id.btnsms) Button btnsms;
    @BindView(R.id.btnselectphoto) Button btnselectphoto;
    @BindView(R.id.btndial) Button btndial;
    @BindView(R.id.btncontact) ImageView btncontact;
    @BindView(R.id.imgcamerapicture) ImageView imgcamerapicture;


    Intent automaticIntent = null;
    Uri uri;

    @Optional
    @OnClick({R.id.btndial,R.id.btncall,R.id.btncapturephoto,R.id.btnselectphoto,R.id.btnshareimage,R.id.btnsharetext,R.id.btnshowlocation,R.id.btnsms,R.id.btnwebsite,R.id.btncontact})
    public void OnClick(View v){
        String first,second;
        first = txtfirst.getText().toString();
        second=txtsecond.getText().toString();

        switch (v.getId()){
            case R.id.btndial:
                if(txtfirst.getText().length()!=0){
                    automaticIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + first));
                    startActivity(automaticIntent);
                }
                else{
                    Toast. makeText(getApplicationContext(),"Please enter number",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btncall:
                if(txtfirst.getText().length()!=0){
                    automaticIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + first));
                    startActivity(automaticIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter the number",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnsms:
                if(txtfirst.getText().length()!=0 && txtsecond.getText().length()!=0){
                    automaticIntent = new Intent(Intent.ACTION_VIEW);
//                SmsManager manager = SmsManager.getDefault();
//                manager.sendTextMessage(first,null,second,null,null);
                    automaticIntent.setData(Uri.parse("smsto:"));
                    automaticIntent.setType("vnd.android-dir/mms-sms");
                    automaticIntent.putExtra("address", new String(first));
                    automaticIntent.putExtra("sms_body",second);
                    startActivity(automaticIntent);
                }
                else{
                    Toast. makeText(getApplicationContext(),"Please enter number and required text",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btnshowlocation:
                if(txtfirst.getText().length()!=0 && txtsecond.getText().length()!=0){
                    Uri LocationUrl = Uri.parse("geo:<" + first + ">,<" + second + ">?q=<"
                            + first + ">,<" + second + ">(Hi There)?z=10");
                    automaticIntent = new Intent(Intent.ACTION_VIEW,LocationUrl);
                    startActivity(automaticIntent);
                }
                else{
                    Toast. makeText(getApplicationContext(),"Please enter longitude and latitude",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnsharetext:
                if(txtfirst.getText().length()!=0){
                    automaticIntent = new Intent(Intent.ACTION_SEND);
                    automaticIntent.setType("text/plain");
                    automaticIntent.putExtra(Intent.EXTRA_TEXT,first);
                    startActivity(Intent.createChooser(automaticIntent,"Share This"));
                }
                else{
                    Toast. makeText(getApplicationContext(),"Please enter text",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnwebsite:
                if(txtfirst.getText().length()!=0){
                    automaticIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://" + first));
                    startActivity(automaticIntent);
                }
                else{
                    Toast. makeText(getApplicationContext(),"Please enter proper url",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnselectphoto:
                automaticIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(automaticIntent,REQUEST_GALLERY);
                break;
            case R.id.btncapturephoto:
                automaticIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (automaticIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(automaticIntent, REQUEST_CAMERA);
                }
                break;
            case R.id.btnshareimage:
                Log.d("de","hello sharing system");
                if(uri!=null){
                    automaticIntent = new Intent(Intent.ACTION_SEND);
                    automaticIntent.setType("image/*");
                    automaticIntent.putExtra(Intent.EXTRA_TEXT,first);
                    automaticIntent.putExtra(Intent.EXTRA_STREAM,uri);
                    startActivity(Intent.createChooser(automaticIntent, "Share Image Using"));
                    uri=null;
                }
                break;
            case R.id.btncontact:
                Intent switchIntent = new Intent(this,ContactContainer.class);
                startActivityForResult(switchIntent,REQUEST_CONTACT);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        checkPermisson();
    }

    private void checkPermisson() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //os in phone is marshmellow anf higher
            //check if user has already given permission or not
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                //Developer has to take one or more permission from user
                String[] permissionList = {Manifest.permission.CAMERA,Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE};
                requestPermissions(permissionList,REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]!=PackageManager.PERMISSION_GRANTED)
            btncapturephoto.setEnabled(false);
        if(grantResults[1]!=PackageManager.PERMISSION_GRANTED)
            btnsms.setEnabled(false);
        if(grantResults[2]!=PackageManager.PERMISSION_GRANTED)
            btncontact.setEnabled(false);
        if(grantResults[3]!=PackageManager.PERMISSION_GRANTED)
            btnselectphoto.setEnabled(false);
        if(grantResults[4]!=PackageManager.PERMISSION_GRANTED)
            btndial.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CAMERA){
            Bitmap image = (Bitmap)data.getExtras().get("data");
            imgcamerapicture.setImageBitmap(image);
        }
        if(requestCode==REQUEST_GALLERY){
            uri = data.getData();
            imgpicture.setImageURI(uri);
        }
        if(requestCode==REQUEST_CONTACT)
        {
            String contact = data.getStringExtra("contact");
            txtfirst.setText(contact);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
