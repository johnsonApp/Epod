package com.jht.epod.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.jht.epod.R;
import com.jht.epod.activity.ClassClassifyActivity;
import com.jht.epod.model.ClassData;
import com.jht.epod.model.ClassDataManager;
import com.jht.epod.ui.MeasureListView;
import com.jht.epod.adapter.MyAdapter;
import com.jht.epod.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlanFragment extends Fragment {
    private static final String TAG = "MyPlanFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MyPlanFragment.OnFragmentInteractionListener mListener;

    private LinearLayout mPlanLayout1;
    private LinearLayout mPlanLayout2;
    private ClassDataManager mManager;
    private ArrayList<ClassData> mClassDataList;
    private MeasureListView mMyClassList;

    public MyPlanFragment() {
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
    public static MyPlanFragment newInstance(String param1, String param2) {
        MyPlanFragment fragment = new MyPlanFragment();
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
        View view = inflater.inflate(R.layout.fragment_myplan, container, false);

        mManager = ClassDataManager.getInstance(getActivity().getApplicationContext());

        mPlanLayout1 = view.findViewById(R.id.my_plan_layout1);
        mPlanLayout2 = view.findViewById(R.id.my_plan_layout2);

        Button addClassBtn1 = view.findViewById(R.id.add_course_btn1);
        Button addClassBtn2 = view.findViewById(R.id.add_course_btn2);
        addClassBtn1.setOnClickListener(listener);
        addClassBtn2.setOnClickListener(listener);

        mMyClassList = view.findViewById(R.id.my_class_list);

        MeasureListView mListClass = view.findViewById(R.id.list_class);
        ArrayList<HashMap<String, Object>> listClassValue = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            HashMap<String, Object> classValue = new HashMap<>();
            classValue.put("classPic", R.drawable.class_arm_small);
            classValue.put("classTitle", getResources().getString(R.string.list_class_title));
            classValue.put("classSubtitle1", "10" + getResources().getString(R.string.minutes));
            classValue.put("classSubtitle2", "89" + getResources().getString(R.string.calorie));
            classValue.put("classSubtitle3", getResources().getString(R.string.junior));
            listClassValue.add(classValue);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), listClassValue,
                R.layout.list_recommended_course,
                new String[]{"classPic", "classTitle", "classSubtitle1", "classSubtitle2", "classSubtitle3"},
                new int[]{R.id.class_pic, R.id.class_title, R.id.class_subtitle1, R.id.class_subtitle2, R.id.class_subtitle3});
        mListClass.setAdapter(adapter);
        return view;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ClassClassifyActivity.class);
            intent.putExtra(Utils.CLASSIFICATION, Utils.CLASS_TYPE_ALL);
            startActivity(intent);
        }
    };

    private String getStringByDegree(int degree) {
        String text = null;
        switch (degree){
            case Utils.DEGREE_JUNIOR:
                text = getResources().getString(R.string.junior);
                break;
            case Utils.DEGREE_MEDIUM:
                text = getResources().getString(R.string.medium);
                break;
            case Utils.DEGREE_SENIOR:
                text = getResources().getString(R.string.senior);
                break;
        }
        return text;
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
    public void onStart() {
        super.onStart();
        mClassDataList = mManager.queryClassSelected(Utils.SELECT);
        if (mClassDataList.size() == 0) {
            mPlanLayout1.setVisibility(View.VISIBLE);
            mPlanLayout2.setVisibility(View.GONE);
        } else {
            mPlanLayout1.setVisibility(View.GONE);
            mPlanLayout2.setVisibility(View.VISIBLE);
            final ArrayList<HashMap<String, Object>> listClassValue = new ArrayList<>();
            for (ClassData data : mClassDataList) {
                HashMap<String, Object> classValue = new HashMap<>();
                classValue.put("classPic", getResources().getIdentifier(data.getIconName(),"drawable", getActivity().getPackageName()));
                classValue.put("classTitle", data.getName());
                classValue.put("classSubtitle1", data.getTime() + getResources().getString(R.string.minutes));
                classValue.put("classSubtitle2", data.getCalorie() + getResources().getString(R.string.calorie));
                classValue.put("classSubtitle3", getStringByDegree(data.getDegree()));
                classValue.put("completedTime", switchExerciseTime(data.getExerciseTime()));
                classValue.put("storeId", data.getId());
                listClassValue.add(classValue);
            }
            final MyAdapter adapter = new MyAdapter(getActivity(), listClassValue,
                    R.layout.list_selected_class,
                    new String[]{"classPic", "classTitle", "classSubtitle1", "classSubtitle2", "classSubtitle3", "completedTime", "storeId"},
                    new int[]{R.id.class_pic, R.id.class_title, R.id.class_subtitle1, R.id.class_subtitle2, R.id.class_subtitle3, R.id.completed_time, R.id.store_id});
            mMyClassList.setAdapter(adapter);
//            mMyClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    TextView storeId = view.findViewById(R.id.store_id);
//                    Long classId = Long.parseLong(storeId.getText().toString());
//                    Log.i(TAG,"onItemClick get store id " + classId);
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    intent.putExtra(Utils.ID, classId);
//                    startActivity(intent);
//                }
//            });
            adapter.setOnItemDeleteClickListener(new MyAdapter.OnItemDeleteListener() {
                @Override
                public void onDeleteClick(final int position) {
                    new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确定删除该课程？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClassData classData = mClassDataList.get(position);
                                    classData.setSelected(Utils.UNSELECT);
                                    mManager.updateSelected(classData);
                                    listClassValue.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            }).create().show();
                }
            });
        }
    }

    private String switchExerciseTime (int time) {
        StringBuilder exerciseTime = new StringBuilder();
        if (time == 0) {
            exerciseTime.append("还未进行过训练");
        } else {
            exerciseTime.append("已进行至");
            int minute = time / 60;
            int second = time % 60;
            if (minute == 0) {
                exerciseTime.append("00");
            } else if (minute < 10) {
                exerciseTime.append("0" + minute);
            } else {
                exerciseTime.append(minute);
            }
            exerciseTime.append(":");
            if (second == 0) {
                exerciseTime.append("00");
            } else if (second < 10) {
                exerciseTime.append("0" + second);
            } else {
                exerciseTime.append(second);
            }
        }
        return exerciseTime.toString();
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
}
