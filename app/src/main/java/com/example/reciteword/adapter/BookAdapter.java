package com.example.reciteword.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.reciteword.R;
import com.example.reciteword.SeleBookFragDialog;
import com.example.reciteword.Word;
import com.example.reciteword.dao.DataUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookAdapter extends ArrayAdapter<String> {
    public int resourceId;
    private List data;
    private SeleBookFragDialog parentDialog;
    public BookAdapter(Context context, int textViewResourceId, List<String> data) {
        super(context, textViewResourceId, data);
        resourceId = textViewResourceId;
        this.data=data;

    }


    public void setParentActivity(SeleBookFragDialog parentDialog) {
        this.parentDialog = parentDialog;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView imageView = view.findViewById(R.id.book_img);
        TextView textView = view.findViewById(R.id.book_text);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

// Set the title, message, and buttons for the dialog
        builder.setTitle("确认操作!");
        builder.setMessage("是否切换单词书?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Yes" button, so perform the action
                if (parentDialog!=null){
                    parentDialog.dismiss();
                }
//                Toast.makeText(getContext(),textView.getText(),Toast.LENGTH_SHORT).show();

                DataUtil.UnZipAndLoadIntoSQLite(textView.getText().toString()+".zip");
                System.exit(0);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "No" button, so dismiss the dialog
                dialog.dismiss();
            }
        });

// Create the dialog and show it to the user
        AlertDialog dialog = builder.create();
        textView.setText(data.get(position).toString());
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(),textView.getText(),Toast.LENGTH_SHORT).show();
                // Create a new AlertDialog.Builder instance
                dialog.show();

            }
        });
        return view;
    }
}

