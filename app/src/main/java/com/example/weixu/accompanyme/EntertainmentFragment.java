package com.example.weixu.accompanyme;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.example.weixu.adpter.EntertainmentFragmentPagerAdapter;

public class EntertainmentFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager vpFindPager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EntertainmentFragment() {
        // Required empty public constructor
    }

    public static EntertainmentFragment newInstance(int page) {
        EntertainmentFragment fragment = new EntertainmentFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entertainment, container, false);
        initTabLayout(view);
        return view;
    }

    private void initTabLayout(View view) {
        //ViewPager
        vpFindPager = (ViewPager) view.findViewById(R.id.vp_);
        EntertainmentFragmentPagerAdapter adapter = new EntertainmentFragmentPagerAdapter(getChildFragmentManager());
        vpFindPager.setAdapter(adapter);
        //TabLayout
        tabLayout = (TabLayout) view.findViewById(R.id.tl_);
        tabLayout.setupWithViewPager(vpFindPager);

    }

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
