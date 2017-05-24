package com.example.weixu.accompanyme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class IntelligencePagerFragment extends Fragment implements View.OnClickListener {
    private ImageView ibOpenOne, ibOpenTwo;
    private TextView tvOpenOne, tvOpenTwo;

    private int mPage;//第几个Tab
    private Intent intent;
    public static final String ARGS_PAGE = "args_page";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public IntelligencePagerFragment() {
        // Required empty public constructor
    }

    public static IntelligencePagerFragment newInstance(int page) {
        IntelligencePagerFragment fragment = new IntelligencePagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);//键值对，Activity之间通信

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);//设置第几个tab

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intelligence_pager, container, false);
        ibOpenOne = (ImageView) view.findViewById(R.id.ibOpenOne);
        ibOpenTwo = (ImageView) view.findViewById(R.id.ibOpenTwo);
        tvOpenOne = (TextView) view.findViewById(R.id.tvOpenOne);
        tvOpenTwo = (TextView) view.findViewById(R.id.tvOpenTwo);
        if (mPage == 1) {
            ibOpenOne.setImageResource(R.drawable.painting);
            ibOpenTwo.setImageResource(R.drawable.colorin);
            tvOpenOne.setText("画板");
            tvOpenTwo.setText("填色");
        } else {
            ibOpenOne.setImageResource(R.drawable.whack);
            ibOpenTwo.setImageResource(R.drawable.piano);
            tvOpenOne.setText("打地鼠");
            tvOpenTwo.setText("弹钢琴");
        }
        ibOpenOne.setOnClickListener(this);
        ibOpenTwo.setOnClickListener(this);
        tvOpenOne.setOnClickListener(this);
        tvOpenTwo.setOnClickListener(this);
        return view;//这里修不修改应该没影响
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibOpenOne:
            case R.id.tvOpenOne:
                if (mPage == 1)
                    intent = new Intent(getActivity(), ScrawlActivity.class);
                else
                    intent = new Intent(getActivity(), WhackAMoleActivity.class);
                startActivity(intent);
                break;
            case R.id.ibOpenTwo:
            case R.id.tvOpenTwo:
                if (mPage == 1)
                    intent = new Intent(getActivity(), ColorInActivity.class);
                else
                    intent = new Intent(getActivity(), PlayPanioActivity.class);
                startActivity(intent);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
