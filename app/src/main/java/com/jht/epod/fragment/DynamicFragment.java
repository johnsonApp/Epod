package com.jht.epod.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jht.epod.R;
import com.jht.epod.activity.MessageActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DynamicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DynamicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView mTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout mHot;
    private TextView mHotText;
    private ImageView mHotImage;
    private LinearLayout mMoments;
    private TextView mMomentsText;
    private ImageView mMomentsImage;

    private ImageView mMessageImage;

    private Fragment mCurrentFragment = null;
    private HotViewFragment mHotView;
    private MomentsViewFragement mMomentsView;

    private boolean mHotViewSelected = true;

    private View mView;

    public DynamicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static DynamicFragment newInstance(String param1, String param2) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        mHotView = new HotViewFragment();
        mMomentsView = new MomentsViewFragement();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_dymnamic, container, false);
            init();
        }
        return mView;
    }

    View.OnClickListener mViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.uptab_hot:
                    if(mHotViewSelected){
                        return;
                    }
                    updateState(true);
                    break;
                case R.id.uptab_moments:
                    if(!mHotViewSelected){
                        return;
                    }
                    updateState(false);
                    break;
                case R.id.message:
                    startActivity(new Intent(getActivity(), MessageActivity.class));
                    break;

                /*case R.id.class_core:
                case R.id.class_arm:
                case R.id.class_hip:
                case R.id.class_junior:
                    startClassActivity();
                    break;*/
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void init() {
        if(mView != null){
            View view = mView;
            mHot = view.findViewById(R.id.uptab_hot);
            mHotText = view.findViewById(R.id.uptab_hot_text);
            mHotImage = view.findViewById(R.id.uptab_hot_image);

            mMoments = view.findViewById(R.id.uptab_moments);
            mMomentsText = view.findViewById(R.id.uptab_moments_text);
            mMomentsImage = view.findViewById(R.id.uptab_moments_image);

            mMessageImage = view.findViewById(R.id.message);

            updateState(mHotViewSelected);
            mHot.setOnClickListener(mViewListener);
            mMoments.setOnClickListener(mViewListener);

            mMessageImage.setOnClickListener(mViewListener);
        }
    }

    private void updateState(boolean state){
        mHotViewSelected = state;
        if(mHotViewSelected) {
            switchFragment(mHotView);
        }else {
            switchFragment(mMomentsView);
        }
        updateTab();
    }

    private void updateTab(){
        if(mHotViewSelected){
            mHotText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
            mHotImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
            mMomentsText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
            mMomentsImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }else{
            mHotText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
            mHotImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            mMomentsText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
            mMomentsImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
        }
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(!targetFragment.isAdded()) {
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.add(R.id.fragment_container,targetFragment,targetFragment.getClass().getName());
        }else {
            transaction
                    .hide(mCurrentFragment)
                    .show(targetFragment);
        }
        mCurrentFragment = targetFragment;
        transaction.commit();
    }

}
