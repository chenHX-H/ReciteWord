package com.example.reciteword;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.reciteword.adapter.BookAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeleBookFragDialog extends DialogFragment {
    private GridView gridView;
    private FragmentActivity parentActivity;
    public SeleBookFragDialog(FragmentActivity activity) {
       this.parentActivity=activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.sele_book_frag_dialog, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (GridView) view.findViewById(R.id.gridView);
        List data = null;
        try {
            data = preData();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("999");
        System.out.println(gridView);
        BookAdapter bookAdapter = new BookAdapter(getContext(), R.layout.gridview_item, data);
        bookAdapter.setParentActivity(this);
        gridView.setAdapter(bookAdapter);
    }

    private List preData() throws IOException {
        AssetManager assetManager = getActivity().getAssets();
//        String[] fileNames = assetManager.list("");

        List<String> strings = new ArrayList<>();
//        for (int i = 2; i < fileNames.length; i++) {
//            String s = fileNames[i];
//            if (s.endsWith(".zip")) {
//                s = s.substring(0, s.length() - ".zip".length());
//            }
//           strings.add(s);
//        }
        strings.add("中考单词");
        strings.add("中考核心单词");
        strings.add("六级核心单词");
        strings.add("四级核心单词");
        strings.add("高考核心单词");
        strings.add("默认单词书");
        System.out.println("ccsssaaaaaaaaaaaaaaaas");
        System.out.println(strings.get(0));

        return strings;
    }

}
