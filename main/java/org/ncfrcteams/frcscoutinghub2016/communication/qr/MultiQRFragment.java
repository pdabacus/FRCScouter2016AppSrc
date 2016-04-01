package org.ncfrcteams.frcscoutinghub2016.communication.qr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ncfrcteams.frcscoutinghub2016.R;

public class MultiQRFragment extends Fragment {
    public static final String MULTI_QR_TITLE = "org.ncfrcteams.FRCScoutingHub2016.MultiQRFragment.MULTI_QR_TEXT";
    public static final String MULTI_QR_TEXT = "org.ncfrcteams.FRCScoutingHub2016.MultiQRFragment.MULTI_QR_TEXT";

    private String[] multiQRTitle;
    private String[] multiQRText;

    private QRFragmentAdapter mQRFragmentAdapter;
    private ViewPager viewPager;

    public static MultiQRFragment newInstance(String[] titles, String[] contents) {
        MultiQRFragment fragment = new MultiQRFragment();
        Bundle args = new Bundle();
        args.putStringArray(MULTI_QR_TITLE, titles);
        args.putStringArray(MULTI_QR_TEXT, contents);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            multiQRTitle = getArguments().getStringArray(MULTI_QR_TITLE);
            multiQRText = getArguments().getStringArray(MULTI_QR_TEXT);
        }

        mQRFragmentAdapter = new QRFragmentAdapter(getFragmentManager(),multiQRTitle,multiQRText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View root=inflater.inflate(R.layout.h_fragment_multi_qr, container, false);

        viewPager = (ViewPager) root.findViewById(R.id.container);
        Log.e("ViewPager",(viewPager == null) ? "null" : "not null");
        viewPager.setAdapter(mQRFragmentAdapter);

        return root;
    }
}
