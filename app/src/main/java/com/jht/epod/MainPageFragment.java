package com.jht.epod;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.jht.epod.utils.CheckableLinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment{
    private static final String TAG = "MainPageFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView mTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout mClass;
    private TextView mClassText;
    private ImageView mClassImage;
    private LinearLayout mPlan;
    private TextView mPlanText;
    private ImageView mPlanImage;

    private Fragment mCurrentFragment = null;
    private ClassViewFragment mClassView;
    private MyPlanFragment mMyPlan;

    private boolean mClassSelected = true;

    public MainPageFragment() {
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
    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
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
        mClassView = new ClassViewFragment();
        mMyPlan = new MyPlanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_mainpage, container, false);
        mClass = view.findViewById(R.id.uptab_class);
        mClassText = view.findViewById(R.id.uptab_class_text);
        mClassImage = view.findViewById(R.id.uptab_class_image);

        mPlan = view.findViewById(R.id.uptab_plan);
        mPlanText = view.findViewById(R.id.uptab_plan_text);
        mPlanImage = view.findViewById(R.id.uptab_plan_image);

        updateState(mClassSelected);
        mClass.setOnClickListener(mViewListener);
        mPlan.setOnClickListener(mViewListener);

        return view;
    }

    View.OnClickListener mViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(TAG,"mClass clidked");
            switch (v.getId()){
                case R.id.uptab_class:
                    if(mClassSelected){
                        return;
                    }
                    updateState(true);
                    break;
                case R.id.uptab_plan:
                    if(!mClassSelected){
                        return;
                    }
                    updateState(false);
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

    public void onFragmentInteraction(Uri uri){

    }

    private void startClassActivity() {
        startActivity(new Intent(getActivity(),MainActivity.class));
    }

    private void updateState(boolean state){
        mClassSelected = state;
        if(mClassSelected) {
            switchFragment(mClassView);
        }else {
            switchFragment(mMyPlan);
        }
        updateTab();
    }

    private void updateTab(){
        if(mClassSelected){
            mClassText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
            mClassImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
            mPlanText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
            mPlanImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }else{
            mClassText.setTextColor(getResources().getColor(R.color.colorTabUnselect));
            mClassImage.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            mPlanText.setTextColor(getResources().getColor(R.color.colorUpTabSelect));
            mPlanImage.setBackgroundColor(getResources().getColor(R.color.colorUpTabSelect));
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
