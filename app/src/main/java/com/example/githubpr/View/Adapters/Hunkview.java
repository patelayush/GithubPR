package com.example.githubpr.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.githubpr.R;

import java.util.List;

import io.reflectoring.diffparser.api.model.Hunk;
import io.reflectoring.diffparser.api.model.Line;

public class Hunkview extends FrameLayout {

    TextView header_tv;
    LinearLayout from_layout;
    LinearLayout to_layout;

    public Hunkview(Context context, Hunk hunk, List<String> headerString) {
        super(context);
        LayoutInflater layoutInflater = null;
        int fromlinestarting = 0, tolinestarting = 0;
        View view = layoutInflater.from(context).inflate(R.layout.diff_hunk_container, this);
        header_tv = view.findViewById(R.id.header_textview);
        from_layout = view.findViewById(R.id.layout_from_line);
        to_layout = view.findViewById(R.id.layout_to_line);

        header_tv.setText(headerString.get(0));
        for(String s:headerString)
            System.out.println(s);
        fromlinestarting = hunk.getFromFileRange().getLineStart();
        tolinestarting = hunk.getToFileRange().getLineStart();

        for (Line line : hunk.getLines()) {
            if (line.getLineType().equals(Line.LineType.FROM)) {
                from_layout.addView(new DiffRow(context, fromlinestarting++, line.getContent(), Line.LineType.FROM));
            } else if (line.getLineType().equals(Line.LineType.TO)) {
                to_layout.addView(new DiffRow(context, tolinestarting++, line.getContent(), Line.LineType.TO));
            } else if (line.getLineType().equals(Line.LineType.NEUTRAL)) {
                to_layout.addView(new DiffRow(context, tolinestarting++, line.getContent(), Line.LineType.NEUTRAL));
                from_layout.addView(new DiffRow(context, fromlinestarting++, line.getContent(), Line.LineType.NEUTRAL));
            }
        }
    }
}
