package com.example.weixu.accompanyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weixu.adpter.VideoLinkAdapter;
import com.example.weixu.table.VideoLink;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class EntertainmentPagerFragment extends Fragment {
    private Intent intent;
    private SharedPreferences pref;
    private ListView lvVideoLink;

    private int mPage;//第几个Tab
    private TextView textView;
    public static final String ARGS_PAGE = "args_page";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EntertainmentPagerFragment() {
    }

    public static EntertainmentPagerFragment newInstance(int page) {
        EntertainmentPagerFragment fragment = new EntertainmentPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);//键值对，Activity之间通信
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(getActivity(), "96556b6d6dbe89f2ff4a7c1553d882ec");
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);//设置第几个tab

        getData();
    }

    //查询表中儿歌内容
    private void getData() {
        BmobQuery<VideoLink> query = new BmobQuery<VideoLink>();
        query.order("-updatedAt");
        if (mPage == 1)
            query.addWhereEqualTo("webVideoInfoName", "1");
        else
            query.addWhereEqualTo("webVideoInfoName", "2");
        query.findObjects(
                new FindListener<VideoLink>() {
                    @Override
                    public void done(List<VideoLink> list, BmobException e) {
                        try {
                            VideoLinkAdapter adapter = new VideoLinkAdapter(
                                    getActivity(),
                                    R.layout.video_listview_item, list);
                            lvVideoLink.setAdapter(adapter);
                            item(list);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

    }

    private void item(final List<VideoLink> vlinkList) {
        lvVideoLink.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                jump(vlinkList.get(position).getWebVideoLink());
            }
        });
    }

    public void jump(String link) {
        intent = new Intent(getActivity(), VideoWebActivity.class);
        intent.putExtra("_weblink", link);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entertainment_pager, container, false);
        lvVideoLink = (ListView) view.findViewById(R.id.lvVideoLink);
        return view;//这里修不修改应该没影响
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
        void onFragmentInteraction(Uri uri);
    }
}
