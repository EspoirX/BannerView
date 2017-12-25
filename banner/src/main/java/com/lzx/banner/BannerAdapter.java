package com.lzx.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * <p/>
 * Banner适配器
 */
public class BannerAdapter extends PagerAdapter {

    private List<View> mList;

    private int pos;

    private ViewPagerOnItemClickListener mViewPagerOnItemClickListener;


    void setmViewPagerOnItemClickListener(ViewPagerOnItemClickListener mViewPagerOnItemClickListener) {

        this.mViewPagerOnItemClickListener = mViewPagerOnItemClickListener;
    }


    BannerAdapter(List<View> list) {

        this.mList = list;
    }


    @Override
    public int getCount() {
        if (mList.size() == 1) {
            return mList.size();
        } else {
            return Integer.MAX_VALUE;
        }
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //对ViewPager页号求模取出View列表中要显示的项
        position %= mList.size();
        if (position < 0) {
            position = mList.size() + position;
        }
        View v = mList.get(position);
        pos = position;
        //   v.setScaleType(ImageView.ScaleType.CENTER);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = v.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(v);
        }
        final int finalPosition = position;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPagerOnItemClickListener != null) {
                    mViewPagerOnItemClickListener.onItemClick(finalPosition);
                }
            }
        });
        container.addView(v);
        return v;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    interface ViewPagerOnItemClickListener {

        void onItemClick(int position);
    }
}
