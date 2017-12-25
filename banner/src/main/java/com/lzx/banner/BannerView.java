package com.lzx.banner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * <p/>
 * 自定义Banner无限轮播控件
 */
public class BannerView<T> extends RelativeLayout
        implements BannerAdapter.ViewPagerOnItemClickListener {

    private ViewPager viewPager;
    private LinearLayout points;
    private Handler mHandler;
    private List<View> imageViewList;
    private List<T> bannerList = new ArrayList<>();
    //轮播时间，单位秒
    private int delayTime = 5;
    //选中显示Indicator
    private int selectRes = R.drawable.shape_dots_select;
    //非选中显示Indicator
    private int unSelectRes = R.drawable.shape_dots_default;
    //当前页的下标
    private int currentPos;
    //是否停止轮播标志位
    private boolean isStopScroll = false;
    //点击事件
    private ViewPagerOnItemClickListener mViewPagerOnItemClickListener;
    private onPageChangeListener mPageChangeListener;

    private LinearLayout.LayoutParams paramsNormal, paramsSelect;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_banner, this, true);
        viewPager = findViewById(R.id.layout_banner_viewpager);
        points = findViewById(R.id.layout_banner_points_group);
        imageViewList = new ArrayList<>();
        int dotSize = dip2px(getContext(), 5);
        paramsNormal = new LinearLayout.LayoutParams(dotSize, dotSize);
        paramsSelect = new LinearLayout.LayoutParams(dotSize, dotSize);
        paramsNormal.leftMargin = 10;
        paramsSelect.leftMargin = 10;
    }

    /**
     * 设置轮播间隔时间
     *
     * @param time 轮播间隔时间，单位秒
     */
    public BannerView delayTime(int time) {
        this.delayTime = time;
        return this;
    }

    /**
     * 设置Points资源 Res
     *
     * @param selectRes   选中状态
     * @param unSelectRes 非选中状态
     */
    public BannerView setPointsRes(int selectRes, int unSelectRes) {
        this.selectRes = selectRes;
        this.unSelectRes = unSelectRes;
        return this;
    }

    /**
     * 设置下标的Params
     *
     * @param paramsNormal 正常情况下的Params
     * @param paramsSelect 选中情况下的Params
     * @return
     */
    public BannerView setPointsParams(LinearLayout.LayoutParams paramsNormal, LinearLayout.LayoutParams paramsSelect) {
        this.paramsNormal = paramsNormal;
        this.paramsSelect = paramsSelect;
        return this;
    }

    /**
     * 设置点Layout的对齐方式
     *
     * @param gravity 对齐方式
     * @return
     */
    public BannerView setPointsGravity(int gravity) {
        LayoutParams params = (LayoutParams) points.getLayoutParams();
        if (gravity == Gravity.LEFT) {
            params.addRule(ALIGN_PARENT_LEFT);
        } else if (gravity == Gravity.CENTER) {
            params.addRule(CENTER_HORIZONTAL);
        } else if (gravity == Gravity.RIGHT) {
            params.addRule(ALIGN_PARENT_RIGHT);
        }
        return this;
    }

    /**
     * 设置点Layout的边距
     *
     * @param leftMargin   左边距
     * @param topMargin    上边距
     * @param rightMargin  右边距
     * @param bottomMargin 下边距
     * @return
     */
    public BannerView setPointsMargin(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LayoutParams params = (LayoutParams) points.getLayoutParams();
        params.leftMargin = dip2px(getContext(), leftMargin);
        params.topMargin = dip2px(getContext(), topMargin);
        params.rightMargin = dip2px(getContext(), rightMargin);
        params.bottomMargin = dip2px(getContext(), bottomMargin);
        return this;
    }

    /**
     * 点击事件
     * @param mViewPagerOnItemClickListener
     */
    public BannerView setOnItemClickListener(ViewPagerOnItemClickListener mViewPagerOnItemClickListener) {
        this.mViewPagerOnItemClickListener = mViewPagerOnItemClickListener;
        return this;
    }

    /**
     * 监听当前是哪个页面
     * @param pageChangeListener
     */
    public BannerView setPageChangeListener(onPageChangeListener pageChangeListener) {
        mPageChangeListener = pageChangeListener;
        return this;
    }

    /**
     * 图片轮播需要传入参数
     */
    public void build(List<T> list, ViewHolderCreator creator) {
        destory();
        if (list.size() == 0) {
            this.setVisibility(GONE);
            return;
        } else {
            this.setVisibility(VISIBLE);
        }
        currentPos = 0;
        imageViewList.clear();
        bannerList.clear();
        bannerList.addAll(list);
        final int pointSize = bannerList.size();
        if (pointSize == 2) {
            bannerList.addAll(list);
        }
        //判断是否清空 指示器点
        points.removeAllViews();

        //初始化与个数相同的指示器点
        for (int i = 0; i < pointSize; i++) {
            View dot = new View(getContext());
            dot.setBackgroundResource(unSelectRes);
            dot.setLayoutParams(paramsNormal);
            dot.setEnabled(false);
            points.addView(dot);
        }

        points.getChildAt(0).setBackgroundResource(selectRes);
        points.getChildAt(0).setLayoutParams(paramsSelect);

        for (int i = 0; i < bannerList.size(); i++) {
            View view = creator.createHolderView(bannerList.get(i));
            if (view == null) {
                break;
            }
            imageViewList.add(view);
        }

        //监听图片轮播，改变指示器状态
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int pos) {
                pos = pos % pointSize;
                currentPos = pos;
                for (int i = 0; i < points.getChildCount(); i++) {
                    points.getChildAt(i).setBackgroundResource(unSelectRes);
                    points.getChildAt(i).setLayoutParams(paramsNormal);
                }
                points.getChildAt(pos).setBackgroundResource(selectRes);
                points.getChildAt(pos).setLayoutParams(paramsSelect);
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageChange(currentPos);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (isStopScroll) {
                            startScroll();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        stopScroll();
                        mHandler.removeCallbacks(mScrollRunnable);
                        break;
                    default:
                        break;
                }
            }
        });
        BannerAdapter bannerAdapter = new BannerAdapter(imageViewList);
        viewPager.setAdapter(bannerAdapter);
        bannerAdapter.notifyDataSetChanged();
        bannerAdapter.setmViewPagerOnItemClickListener(this);
        //图片开始轮播
        startScroll();
    }

    public interface ViewHolderCreator<T> {
        View createHolderView(T t);
    }

    /**
     * 图片开始轮播
     */
    private void startScroll() {
        isStopScroll = false;
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(mScrollRunnable, delayTime * 1000);
    }

    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (isStopScroll) {
                return;
            }
            isStopScroll = true;
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    /**
     * 图片停止轮播
     */
    private void stopScroll() {
        isStopScroll = true;
    }

    public void destory() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mScrollRunnable);
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * 设置ViewPager的Item点击回调事件
     */
    @Override
    public void onItemClick(int position) {
        if (mViewPagerOnItemClickListener != null) {
            mViewPagerOnItemClickListener.onItemClick(currentPos, bannerList.get(currentPos));
        }
    }

    public interface ViewPagerOnItemClickListener<T> {
        void onItemClick(int position, T entity);
    }

    public interface onPageChangeListener {
        void onPageChange(int position);
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
