package com.pk.hdwallpaper.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pk.hdwallpaper.BuildConfig;
import com.pk.hdwallpaper.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FullScreenWallpaperActivity extends AppCompatActivity {

    String origanlurl = "";
    String medium = "";
    PhotoView photoView;
    Button setwallpaper, s;
    OutputStream outputStream;
    ProgressBar pd;
    FloatingActionButton sharebn, downloadbn, whatsappbn;

    TextView setqualitytv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);


        setwallpaper = findViewById(R.id.setwallpaper);
        pd = findViewById(R.id.progress);
        setqualitytv = findViewById(R.id.textview);
        s = findViewById(R.id.s);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window w = getWindow();
           /* w.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);*/
            w.setStatusBarColor(getResources().getColor(R.color.black));


        }
        sharebn = findViewById(R.id.share);
        downloadbn = findViewById(R.id.download);
        whatsappbn = findViewById(R.id.whtasapp);

        final String url2 = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent intent = getIntent();

        origanlurl = intent.getStringExtra("originalUrl");
        medium = intent.getStringExtra("medium");


        photoView = findViewById(R.id.photoview);

        Glide.with(this).load(medium).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(FullScreenWallpaperActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.setVisibility(View.VISIBLE);

                sharebn.setVisibility(View.GONE);
                whatsappbn.setVisibility(View.GONE);
                downloadbn.setVisibility(View.GONE);


                setwallpaper.setVisibility(View.GONE);
                s.setVisibility(View.VISIBLE);


                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                sharebn.setVisibility(View.VISIBLE);
                whatsappbn.setVisibility(View.VISIBLE);
                downloadbn.setVisibility(View.VISIBLE);

                pd.setVisibility(View.GONE);
                setwallpaper.setVisibility(View.VISIBLE);
                s.setVisibility(View.GONE);

                return false;
            }
        }).placeholder(R.drawable.loading_progress_bar).into(photoView);

        setqualitytv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(FullScreenWallpaperActivity.this);

                builder.setTitle("Choose Quality");
                builder.setPositiveButton("FULL HD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        sharebn.setVisibility(View.GONE);
                        whatsappbn.setVisibility(View.GONE);
                        downloadbn.setVisibility(View.GONE);


                        setwallpaper.setVisibility(View.GONE);
                        s.setVisibility(View.VISIBLE);

                        setqualitytv.setText("FULL HD");

                        Glide.with(FullScreenWallpaperActivity.this).load(origanlurl).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Toast.makeText(FullScreenWallpaperActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                pd.setVisibility(View.VISIBLE);

                                sharebn.setVisibility(View.GONE);
                                whatsappbn.setVisibility(View.GONE);
                                downloadbn.setVisibility(View.GONE);


                                setwallpaper.setVisibility(View.GONE);
                                s.setVisibility(View.VISIBLE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                                sharebn.setVisibility(View.VISIBLE);
                                whatsappbn.setVisibility(View.VISIBLE);
                                downloadbn.setVisibility(View.VISIBLE);

                                pd.setVisibility(View.GONE);
                                setwallpaper.setVisibility(View.VISIBLE);
                                s.setVisibility(View.GONE);

                                return false;
                            }
                        }).placeholder(R.drawable.loading_progress_bar).into(photoView);



                    }
                }).setNegativeButton("MEDIUM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        setqualitytv.setText("Medium");

                        sharebn.setVisibility(View.GONE);
                        whatsappbn.setVisibility(View.GONE);
                        downloadbn.setVisibility(View.GONE);


                        setwallpaper.setVisibility(View.GONE);
                        s.setVisibility(View.VISIBLE);

                        Glide.with(FullScreenWallpaperActivity.this).load(medium).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Toast.makeText(FullScreenWallpaperActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                pd.setVisibility(View.VISIBLE);

                                sharebn.setVisibility(View.GONE);
                                whatsappbn.setVisibility(View.GONE);
                                downloadbn.setVisibility(View.GONE);


                                setwallpaper.setVisibility(View.GONE);
                                s.setVisibility(View.VISIBLE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                                sharebn.setVisibility(View.VISIBLE);
                                whatsappbn.setVisibility(View.VISIBLE);
                                downloadbn.setVisibility(View.VISIBLE);

                                pd.setVisibility(View.GONE);
                                setwallpaper.setVisibility(View.VISIBLE);
                                s.setVisibility(View.GONE);

                                return false;
                            }
                        }).placeholder(R.drawable.loading_progress_bar).into(photoView);


                    }
                });
                builder.create().show();


            }
        });

        setwallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(FullScreenWallpaperActivity.this)
                        .withPermission(Manifest.permission.SET_WALLPAPER)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullScreenWallpaperActivity.this);

                                Bitmap bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();


                                try {
                                    wallpaperManager.setBitmap(bitmap);

                                    Toast.makeText(FullScreenWallpaperActivity.this, "Wallpaper Set successful", Toast.LENGTH_SHORT).show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                Toast.makeText(FullScreenWallpaperActivity.this, "Click : App Permission/Storage,Click Allow Button", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        sharebn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dexter.withContext(FullScreenWallpaperActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Drawable drawable = photoView.getDrawable();
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


                                try {
                                    File file = new File(getApplicationContext().getExternalCacheDir(), "image.png");
                                    FileOutputStream fOut = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    file.setReadable(true, false);
                                    final Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

                                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setType("image/png");
                                    intent.putExtra(Intent.EXTRA_TEXT, url2);

                                    startActivity(Intent.createChooser(intent, "Share image via"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                Toast.makeText(FullScreenWallpaperActivity.this, "Click : App Permission/Storage,Click Allow Button", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                permissionToken.continuePermissionRequest();
                            }
                        }).check();


            }
        });

        downloadbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(FullScreenWallpaperActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                                Drawable drawable = photoView.getDrawable();
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                                File filepath = Environment.getExternalStorageDirectory();
                                File dir = new File(filepath.getAbsolutePath() + "/Pictures");
                                dir.mkdir();
                                File file = new File(dir, System.currentTimeMillis() + ".jpg");
                                try {


                                    try {
                                        outputStream = new FileOutputStream(file);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                    try {
                                        outputStream.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        outputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    intent.setData(Uri.fromFile(file));
                                    sendBroadcast(intent);

                                    Toast.makeText(FullScreenWallpaperActivity.this, "saved" + file, Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    Toast.makeText(FullScreenWallpaperActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                Toast.makeText(FullScreenWallpaperActivity.this, "Click : App Permission/Storage,Click Allow Button", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        whatsappbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(FullScreenWallpaperActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                                Drawable drawable = photoView.getDrawable();
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                                try {
                                    File file = new File(getApplicationContext().getExternalCacheDir(), "image.png");
                                    FileOutputStream fOut = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    file.setReadable(true, false);
                                    final Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

                                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setType("image/png");
                                    intent.putExtra(Intent.EXTRA_TEXT, url2);
                                    intent.setPackage("com.whatsapp");

                                    startActivity(Intent.createChooser(intent, "Share image via"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                Toast.makeText(FullScreenWallpaperActivity.this, "Click : App Permission/Storage,Click Allow Button", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });


    }

    public void setwallpaperloading(View view) {

        Toast.makeText(this, "Image loading please wait...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void more(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose Quality");
        builder.setPositiveButton("FULL HD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                setqualitytv.setText("FULL HD");
                sharebn.setVisibility(View.GONE);
                whatsappbn.setVisibility(View.GONE);
                downloadbn.setVisibility(View.GONE);


                setwallpaper.setVisibility(View.GONE);
                s.setVisibility(View.VISIBLE);

                Glide.with(FullScreenWallpaperActivity.this).load(origanlurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(FullScreenWallpaperActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.setVisibility(View.VISIBLE);

                        sharebn.setVisibility(View.GONE);
                        whatsappbn.setVisibility(View.GONE);
                        downloadbn.setVisibility(View.GONE);


                        setwallpaper.setVisibility(View.GONE);
                        s.setVisibility(View.VISIBLE);


                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                        sharebn.setVisibility(View.VISIBLE);
                        whatsappbn.setVisibility(View.VISIBLE);
                        downloadbn.setVisibility(View.VISIBLE);

                        pd.setVisibility(View.GONE);
                        setwallpaper.setVisibility(View.VISIBLE);
                        s.setVisibility(View.GONE);

                        return false;
                    }
                }).placeholder(R.drawable.loading_progress_bar).into(photoView);



            }
        }).setNegativeButton("MEDIUM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                setqualitytv.setText("Medium");
                sharebn.setVisibility(View.GONE);
                whatsappbn.setVisibility(View.GONE);
                downloadbn.setVisibility(View.GONE);


                setwallpaper.setVisibility(View.GONE);
                s.setVisibility(View.VISIBLE);

                Glide.with(FullScreenWallpaperActivity.this).load(medium).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(FullScreenWallpaperActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.setVisibility(View.VISIBLE);

                        sharebn.setVisibility(View.GONE);
                        whatsappbn.setVisibility(View.GONE);
                        downloadbn.setVisibility(View.GONE);


                        setwallpaper.setVisibility(View.GONE);
                        s.setVisibility(View.VISIBLE);


                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                        sharebn.setVisibility(View.VISIBLE);
                        whatsappbn.setVisibility(View.VISIBLE);
                        downloadbn.setVisibility(View.VISIBLE);

                        pd.setVisibility(View.GONE);
                        setwallpaper.setVisibility(View.VISIBLE);
                        s.setVisibility(View.GONE);

                        return false;
                    }
                }).placeholder(R.drawable.loading_progress_bar).into(photoView);


            }
        });
        builder.create().show();

    }

}