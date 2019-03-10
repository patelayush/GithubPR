package com.example.githubpr.View.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.githubpr.R;

import io.reflectoring.diffparser.api.model.Line;

public class DiffRow extends FrameLayout {
    TextView line_number_tv;
    TextView content_tv;
    public DiffRow(Context context, int number, String content, Line.LineType lineType) {
        super(context);
        LayoutInflater layoutInflater = null;
        View view = layoutInflater.from(context).inflate(R.layout.diff_line_row, this, false);
        line_number_tv = view.findViewById(R.id.line_number_textview);
        content_tv = view.findViewById(R.id.content_textview);

        line_number_tv.setText("" + number);
        content_tv.setText(content);
        System.out.println("Content is here" + content);

        switch (lineType){
            case FROM:
                view.setBackgroundColor(Color.RED);
                break;

            case TO:
                view.setBackgroundColor(Color.GREEN);
                break;

        }

    }
}
