package com.jht.epod.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jht.epod.R;
import com.jht.epod.activity.ClassClassifyActivity;
import com.jht.epod.utils.Utils;

public class ClassViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ClassViewFragment.OnFragmentInteractionListener mListener;

    public ClassViewFragment() {
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
    public static ClassViewFragment newInstance(String param1, String param2) {
        ClassViewFragment fragment = new ClassViewFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_classview, container, false);

        view.findViewById(R.id.class_core).setOnClickListener(mViewListener);
        view.findViewById(R.id.class_arm).setOnClickListener(mViewListener);
        view.findViewById(R.id.class_hip).setOnClickListener(mViewListener);
        view.findViewById(R.id.class_junior).setOnClickListener(mViewListener);
        view.findViewById(R.id.class_medium).setOnClickListener(mViewListener);
        view.findViewById(R.id.class_senior).setOnClickListener(mViewListener);
        return view;
    }

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

    View.OnClickListener mViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ClassClassifyActivity.class);
            switch (v.getId()){
                case R.id.class_core:
                    intent.putExtra(Utils.CLASSIFICATION, Utils.CLASS_TYPE_BODY);
                    intent.putExtra(Utils.CLASSTYPE, Utils.TYPE_CORE);
                    break;
                case R.id.class_arm:
                    intent.putExtra(Utils.CLASSIFICATION, Utils.CLASS_TYPE_BODY);
                    intent.putExtra(Utils.CLASSTYPE, Utils.TYPE_ARM);
                    break;
                case R.id.class_hip:
                    intent.putExtra(Utils.CLASSIFICATION, Utils.CLASS_TYPE_BODY);
                    intent.putExtra(Utils.CLASSTYPE, Utils.TYPE_HIP);
                    break;
                case R.id.class_junior:
                    intent.putExtra(Utils.CLASSIFICATION, Utils.CLASS_TYPE_LEVEL);
                    intent.putExtra(Utils.DEGREE, Utils.DEGREE_JUNIOR);
                    break;
                case R.id.class_medium:
                    intent.putExtra(Utils.CLASSIFICATION, Utils.CLASS_TYPE_LEVEL);
                    intent.putExtra(Utils.DEGREE, Utils.DEGREE_MEDIUM);
                    break;
                case R.id.class_senior:
                    intent.putExtra(Utils.CLASSIFICATION, Utils.CLASS_TYPE_LEVEL);
                    intent.putExtra(Utils.DEGREE, Utils.DEGREE_SENIOR);
                    break;
            }
            startActivity(intent);
        }
    };

}
