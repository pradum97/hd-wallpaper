package com.pk.hdwallpaper;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pk.hdwallpaper.Activity.PrivacyPolicy;
import com.pk.hdwallpaper.Adapter.WallpaperAdapter;
import com.pk.hdwallpaper.Model.WallpaperModel;
import com.pk.hdwallpaper.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public Method method;
    private ActivityMainBinding binding;

    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;

    final int min = 70;

    int pagenumber = new Random().nextInt(min);
    int index = new Random().nextInt(19);


    Boolean isScrolling = false;
    int currentItem, totalItem, scrollOut;

    String show_first = "nature";


    // String url = "https://api.pexels.com/v1/curated/?page=" + pagenumber + "&per_page=30&query=nature";
    String url = "https://api.pexels.com/v1/search/?page=" + pagenumber + "&per_page=80&query=" + first_wallpaper_type().get(index);


    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(first_wallpaper_type().get(index).toUpperCase());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window w = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            w.setStatusBarColor(getResources().getColor(R.color.white));
        }
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        System.out.println("=============================" + first_wallpaper_type().get(index) + "====" + index);

        method = new Method(this);

        method.check_Permission();

        binding.refresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> {

            binding.refresh.setRefreshing(false);

            recreate();

        }, 1));

        setSupportActionBar(binding.toolbar);


        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, wallpaperModelList);
        binding.recyclerview.setAdapter(wallpaperAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerview.setLayoutManager(gridLayoutManager);

        binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    isScrolling = true;

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                currentItem = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrollOut = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (isScrolling && (currentItem + scrollOut == totalItem)) {

                    fetchwallpaper();

                }
            }
        });


        fetchwallpaper();
    }

    private List<String> first_wallpaper_type() {

        List<String> list = new ArrayList<>();

        list.add("natural");
        list.add("rose");
        list.add("love");
        list.add("dog");
        list.add("car");
        list.add("mountain");
        list.add("forest");
        list.add("flower");
        list.add("animal");
        list.add("bird");
        list.add("tree");
        list.add("holiday");
        list.add("world");
        list.add("food");
        list.add("popular");
        list.add("book");
        list.add("computer");
        list.add("laptop");
        list.add("trending");

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.optional_menu, menu);

        MenuItem search = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Ex- nature, rose, animals");
        searchView.setQueryHint(first_wallpaper_type().get(index).toUpperCase());

        searchView.setOnCloseListener(() -> {


            url = "https://api.pexels.com/v1/search/?page=" + pagenumber + "&per_page=80&query=" + first_wallpaper_type().get(index);
            fetchwallpaper();

            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                url = "https://api.pexels.com/v1/search/?page=2&per_page=80&query=" + s;
                wallpaperModelList.clear();
                fetchwallpaper();
                binding.progress.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                url = "https://api.pexels.com/v1/search/?page=" + pagenumber + "&per_page=80&query=" + s;
                wallpaperModelList.clear();
                binding.progress.setVisibility(View.VISIBLE);
                fetchwallpaper();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.share:

                String appname = getString(R.string.app_name);

                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = appname + "\n" + "All Category Wallpaper HD,FullHD , 4k wallpaper \n"
                        + "https://play.google.com/store/apps/details?id=" + getPackageName();

                String shareSub = "Download Wallpaper App";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
                break;

            case R.id.rate:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));

                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.more:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/dev?id=6426108633250697774")));

                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/dev?id=6426108633250697774")));
                }
                break;
            case R.id.privacy:

                startActivity(new Intent(this, PrivacyPolicy.class));
                break;

            case R.id.exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit");
                builder.setCancelable(true);
                builder.setPositiveButton("yes", (dialogInterface, i) -> {

                    System.exit(1);
                    finish();
                }).setNegativeButton("cancel", (dialogInterface, i) -> dialogInterface.cancel());
                builder.create().show();


                break;


        }

        if (item.getItemId() == R.id.search) {

            Toast.makeText(this, "cc", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchwallpaper() {

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("photos");

                        int length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("id");

                            JSONObject objectImages = object.getJSONObject("src");

                            String originalUrl = objectImages.getString("original");

                            String mediumUrl = objectImages.getString("medium");

                            WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumUrl);

                            wallpaperModelList.add(wallpaperModel);
                        }

                        wallpaperAdapter.notifyDataSetChanged();
                        pagenumber++;
                        binding.progress.setVisibility(View.GONE);


                    } catch (JSONException e) {
                        Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }, error -> {

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("Authorization", "563492ad6f917000010000018d060b67a8c14926be8695815ed224fa");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setMessage("Are you sure you want to exit");
        builder2.setCancelable(true);
        builder2.setPositiveButton("yes", (dialogInterface, i) -> {
            finish();
        }).setNegativeButton("cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder2.show();

    }

}