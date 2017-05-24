package com.example.weixu.accompanyme;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weixu.view.MyMarkerView;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import java.util.ArrayList;


public class GrowLineFragment extends Fragment {


    private int mPage;//第几个Tab
    public static final String ARGS_PAGE = "args_page";

    private LineChart chartTall;

    private SharedPreferences pref;
    private String userBabyName,babySex;

    public GrowLineFragment() {
    }

    public static GrowLineFragment newInstance(int page) {
        GrowLineFragment fragment = new GrowLineFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);//键值对，Activity之间通信
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);//设置第几个tab
        getSex();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grow_line,container,false);
        chartTall = (LineChart) view.findViewById(R.id.chartTall);



        // 刷新图表
        chartTall.invalidate();
        return view;//这里修不修改应该没影响
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mPage==1){
            setType("身高曲线图","cm");
        }else
        {
            setType("体重曲线图","kg");
        }
    }

    private void getSex() {
        pref = getActivity().getSharedPreferences("userBabyInfo", Context.MODE_PRIVATE);
        userBabyName = pref.getString("userBabyName", "");
        babySex=pref.getString("userBabySex","");
    }

    private void setType(String typeStr,String unitStr) {
        // 设置在Y轴上是否是从0开始显示
        chartTall.setStartAtZero(true);
        //是否在Y轴显示数据，就是曲线上的数据
        chartTall.setDrawYValues(true);
        //设置网格
        chartTall.setDrawBorder(true);
        chartTall.setBorderPositions(new BarLineChartBase.BorderPosition[] {
                BarLineChartBase.BorderPosition.BOTTOM});
        //在chart上的右下角加描述
        chartTall.setDescription(typeStr);
        //设置Y轴上的单位
        chartTall.setUnit(unitStr);
        //设置透明度
        chartTall.setAlpha(0.8f);
        //设置网格底下的那条线的颜色
        chartTall.setBorderColor(Color.rgb(213, 216, 214));
        //设置Y轴前后倒置
        chartTall.setInvertYAxisEnabled(false);
        //设置高亮显示
        chartTall.setHighlightEnabled(true);
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        chartTall.setTouchEnabled(true);
        //设置是否可以拖拽，缩放
        chartTall.setDragEnabled(true);
        chartTall.setScaleEnabled(true);
        //设置是否能扩大扩小
        chartTall.setPinchZoom(true);
        //设置点击chart图对应的数据弹出标注
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
        chartTall.setMarkerView(mv);
        chartTall.setHighlightIndicatorEnabled(false);
        //设置字体格式，如正楷
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "OpenSans-Regular.ttf");
        chartTall.setValueTypeface(tf);

        XLabels xl = chartTall.getXLabels();
        xl.setPosition(XLabels.XLabelPosition.BOTTOM); // 设置X轴的数据在底部显示
        xl.setTypeface(tf); // 设置字体
        xl.setTextSize(10f); // 设置字体大小
        xl.setSpaceBetweenLabels(3); // 设置数据之间的间距

        YLabels yl = chartTall.getYLabels();
        yl.setTypeface(tf); // 设置字体
        yl.setTextSize(10f); // s设置字体大小
        yl.setLabelCount(5); // 设置Y轴最多显示的数据个数
        // 加载数据
        if(mPage==1) {
            setStatureData();
        }else{
            setWeightData();
        }
        //从X轴进入的动画
        chartTall.animateX(4000);
        chartTall.animateY(3000);   //从Y轴进入的动画
        chartTall.animateXY(3000, 3000);    //从XY轴一起进入的动画

        //设置最小的缩放
        chartTall.setScaleMinima(0.5f, 1f);

    }

    private void setStatureData() {
        //在这里设置身高的信息
        SharedPreferences pref=getActivity().getSharedPreferences("babyGrowInfo", Context.MODE_PRIVATE);
        String babyZero=pref.getInt("tallZero",0)+"";
        String babyOne=pref.getInt("tallOne",0)+"";
        String babyTwo=pref.getInt("tallTwo",0)+"";
        String babyThree=pref.getInt("tallThree",0)+"";
        String babyFour=pref.getInt("tallFour",0)+"";
        String babyFive=pref.getInt("tallFive",0)+"";

        String[] babAge = {"0","1","2","3","4","5"};     //连线的x轴数据
        String[] babyTall = {babyZero,babyOne,babyTwo,babyThree,babyFour,babyFive};
        LineData data=new LineData(babAge,setLine(babAge,babyTall,1,"宝宝身高"));    //创建LineData实体类并添加第一条曲线
        if(babySex.equals("男")) {
            String[] usuaTallBoy = {"50", "75", "88", "95", "102", "110"};//连线的y轴数据
            data.addDataSet(setLine(babAge,usuaTallBoy,2,"正常身高"));      //添加第二条曲线
        }
        else {
            String[] usuaTallGirl = {"50", "73", "85", "94", "102", "108"};//连线的y轴数据
            data.addDataSet(setLine(babAge,usuaTallGirl,2,"正常身高"));      //添加第二条曲线
        }


        chartTall.setData(data);
    }

    private void setWeightData() {
        //在这里设置体重的信息
        SharedPreferences pref=getActivity().getSharedPreferences("babyGrowInfo", Context.MODE_PRIVATE);
        String babyZero=pref.getInt("WeightZero",0)+"";
        String babyOne=pref.getInt("WeightOne",0)+"";
        String babyTwo=pref.getInt("WeightTwo",0)+"";
        String babyThree=pref.getInt("WeightThree",0)+"";
        String babyFour=pref.getInt("WeightFour",0)+"";
        String babyFive=pref.getInt("WeightFive",0)+"";


        String[] babAge = {"0","1","2","3","4","5"};     //连线的x轴数据
        String[] babyWeight = {babyZero,babyOne,babyTwo,babyThree,babyFour,babyFive};
        LineData data=new LineData(babAge,setLine(babAge,babyWeight,1,"宝宝体重"));    //创建LineData实体类并添加第一条曲线
        if(babySex.equals("男")) {
            String[] usuaWeightBoy = {"3.3", "10.0", "13.0", "15.0", "16.0", "19.0"};//连线的y轴数据
            data.addDataSet(setLine(babAge,usuaWeightBoy,2,"正常体重"));      //添加第二条曲线
        }else{
            String[] usuaWeightGirl = {"3.0", "9.5", "12.0", "14.0", "16.0", "18.0"};//连线的y轴数据
            data.addDataSet(setLine(babAge,usuaWeightGirl,2,"正常体重"));      //添加第二条曲线
        }
        chartTall.setData(data);
    }

    //画线
    private LineDataSet setLine(String[] babAge, String[] Tall, int flag, String name) {
        ArrayList<String> xValsAge = new ArrayList<String>();
        for (int i = 0; i < babAge.length; i++) {
            xValsAge.add(babAge[i]);
        }
        ArrayList<Entry> yValsBabyTall = new ArrayList<Entry>();
        for (int i = 0; i < Tall.length; i++) {
            if(!Tall[i].equals("0"))                                          //如果不为空，为空代表该点没有添加
                yValsBabyTall.add(new Entry(Float.parseFloat(Tall[i]), i));   //添加点
        }
        //设置baby的成长曲线
        LineDataSet setData = new LineDataSet(yValsBabyTall,name);
        setData.setDrawCubic(true);  //设置曲线为圆滑的线
        setData.setCubicIntensity(0.2f);
        setData.setDrawFilled(false);  //设置包括的范围区域填充颜色
        setData.setDrawCircles(true);  //设置有圆点
        setData.setLineWidth(2f);    //设置线的宽度
        setData.setCircleSize(5f);   //设置小圆的大小
        setData.setHighLightColor(Color.rgb(244, 117, 117));
        //设置曲线颜色
        if(flag==1)
            setData.setColor(Color.rgb(104, 241, 175));    //宝宝身高曲线颜色
        else if(flag==2)
            setData.setColor(Color.rgb(255, 0, 0));    //普通身高曲线颜色
        return setData;    //返回曲线
    }
}
