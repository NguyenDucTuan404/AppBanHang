package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.LoaiSpAdapter;
import com.example.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.appbanhang.model.LoaiSp;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.model.User;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;


import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewtrangchinh;
    ListView listViewtrangchinh;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("user") != null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }

        Anhxa();
        ActionBar();

        if (isConnected(this)){

            ActionViewFipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();

        }else {
            Toast.makeText(getApplicationContext(), "Không có internet, vui lòng kết nối", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewtrangchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai", 1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), LaptopActivity.class);
                        laptop.putExtra("loai", 2);
                        startActivity(laptop);
                        break;
                    case 3:
                        Intent phukien = new Intent(getApplicationContext(), PhuKienActivity.class);
                        phukien.putExtra("loai", 3);
                        startActivity(phukien);
                        break;
                    case 4:
                        Intent thietbigiadung = new Intent(getApplicationContext(), ThietBiGiaDungActivity.class);
                        thietbigiadung.putExtra("loai", 4);
                        startActivity(thietbigiadung);
                        break;
                    case 5:
                        Intent dongho = new Intent(getApplicationContext(), DongHoActivity.class);
                        dongho.putExtra("loai", 5);
                        startActivity(dongho);
                        break;
                    case 6:
                        Intent thoitrang = new Intent(getApplicationContext(), ThoiTrangActivity.class);
                        thoitrang.putExtra("loai", 6);
                        startActivity(thoitrang);
                        break;
                    case 7:
                        Intent giaydep = new Intent(getApplicationContext(), GiayDepActivity.class);
                        giaydep.putExtra("loai", 7);
                        startActivity(giaydep);
                        break;
                    case 8:
                        Intent mypham = new Intent(getApplicationContext(), MyPhamActivity.class);
                        mypham.putExtra("loai", 8);
                        startActivity(mypham);
                        break;
                    case 9:
                        Intent xe = new Intent(getApplicationContext(), XeActivity.class);
                        xe.putExtra("loai", 9);
                        startActivity(xe);
                        break;
                    case 10:
                        Intent dochoi = new Intent(getApplicationContext(), DoChoiActivity.class);
                        dochoi.putExtra("loai", 10);
                        startActivity(dochoi);
                        break;
                    case 11:
                        Intent thethaovadulich = new Intent(getApplicationContext(), TheThaoVaDuLichActivity.class);
                        thethaovadulich.putExtra("loai", 11);
                        startActivity(thethaovadulich);
                        break;
                    case 12:
                        Intent raucuqua = new Intent(getApplicationContext(), RauCuQuaActivity.class);
                        raucuqua.putExtra("loai", 12);
                        startActivity(raucuqua);
                        break;
                    case 13:
                        Intent doan = new Intent(getApplicationContext(), DoAnActivity.class);
                        doan.putExtra("loai", 13);
                        startActivity(doan);
                        break;
                    case 14:
                        Intent lienhe = new Intent(getApplicationContext(), LienHeActivity.class);
                        startActivity(lienhe);
                        break;

                    case 16:
                        Intent donhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 17:
                        // xoa key user
                        Paper.book().delete("user");
                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangnhap);
                        finish();
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerViewtrangchinh.setAdapter(spAdapter);

                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với sever"+throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()){
                                mangloaisp = loaiSpModel.getResult();
                                mangloaisp.add(new LoaiSp("Đăng xuất", "https://img.icons8.com/external-kmg-design-outline-color-kmg-design/256/external-log-out-user-interface-kmg-design-outline-color-kmg-design.png"));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                                listViewtrangchinh.setAdapter(loaiSpAdapter);
                            }
                        }
                ));




    }

    private void ActionViewFipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        mangquangcao.add("https://cf.shopee.vn/file/vn-50009109-41a340b9f5727337e9ba5ca7dcb87e41");
        mangquangcao.add("https://img3.thuthuatphanmem.vn/uploads/2019/09/16/anh-banner-quang-cao-my-pham-dep_083546254.jpg");
        mangquangcao.add("https://suno.vn/blog/wp-content/uploads/2015/12/sale-off.jpg");


        for(int i = 0; i<mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_night);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_night);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    private void Anhxa() {
        imgsearch = findViewById(R.id.imgsearch);
        toolbar = findViewById(R.id.toolbartrangchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewtrangchinh = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewtrangchinh.setLayoutManager(layoutManager);
        recyclerViewtrangchinh.setHasFixedSize(true);
        listViewtrangchinh = findViewById(R.id.listviewtrangchinh);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        //khoi tao list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if (Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }else {
            int totalItem = 0;
            for (int i = 0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i = 0; i<Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected (Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }else
            return false;

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}