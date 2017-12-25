package com.lzx.bannerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzx.banner.BannerView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    BannerView mBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBannerView = findViewById(R.id.banner);

        String[] array = new String[]{
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514186270980&di=3d717730f40285e7686dfaa13b1dd4cf&imgtype=0&src=http%3A%2F%2Fimage.tianjimedia.com%2FuploadImages%2F2015%2F209%2F42%2F6H182F668EP9.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514191976094&di=7348c7e235771dead0c86ec0319322c3&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fe1fe9925bc315c600dce09d386b1cb13495477b6.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514191994287&di=e624a24a8811428c0b6ecef893979bad&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F728da9773912b31bc2fe74138d18367adab4e17e.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514192006291&di=55e555fe0371ab049fe1f16674db776e&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160519%2F9-160519202315.jpg"
        };
        mBannerView
                .delayTime(3)
                .setPointsGravity(Gravity.RIGHT)
                .setPointsMargin(0, 0, 20, 0)
                .build(Arrays.asList(array), new BannerView.ViewHolderCreator<String>() {

                    @Override
                    public View createHolderView(String url) {
                        View bannerView = View.inflate(MainActivity.this, R.layout.item_banner, null);
                        ImageView mImageBanner = bannerView.findViewById(R.id.image);
                        Glide.with(MainActivity.this).load(url).into(mImageBanner);
                        return bannerView;
                    }
                });

        mBannerView.setOnItemClickListener(new BannerView.ViewPagerOnItemClickListener() {
            @Override
            public void onItemClick(int position, Object entity) {

            }
        }).setPageChangeListener(new BannerView.onPageChangeListener() {
            @Override
            public void onPageChange(int position) {

            }
        });

    }
}
