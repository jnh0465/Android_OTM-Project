package com.nuntteuniachim.otm.Main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nuntteuniachim.otm.R;

// 메인화면에서 광고 넘어가는 기능 구현

public class ViewPagerAdapter extends PagerAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.ad1, R.drawable.ad2, R.drawable.ad3, R.drawable.ad4, R.drawable.ad5}; //광고에 쓸 이미지, 이미지 수가 변경되면 MainActivity의 MyTimerTask도 변경해 주어야 함

    public ViewPagerAdapter(Context context){ this.context = context; }

    @Override
    public int getCount(){ return images.length; }
    @Override
    public boolean isViewFromObject(View view, Object object){ return view == object; }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);      //이미지뷰
        imageView.setImageResource(images[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
