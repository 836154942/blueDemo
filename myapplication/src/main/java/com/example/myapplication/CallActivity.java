package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shipinchao on 2018/3/28.
 */

public class CallActivity extends Activity {

    RecyclerView recycleView;
    Adapter adapter;
    EditText editText;
    Button button;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        initView();
    }

    private void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        adapter = new Adapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        editText = (EditText) findViewById(R.id.edText);
        button = (Button) findViewById(R.id.btnScroller);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(editText.getText().toString());
                recycleView.smoothScrollToPosition(i + 3);
                adapter.setSelected(i);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private int[] numbers = {-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Context context;
        private int selected = 0;

        public void setSelected(int selected) {
            this.selected = selected;
        }

        public Adapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_number, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(String.valueOf(numbers[position]));
            if (selected == numbers[position]) {
                holder.textView.setTextColor(Color.WHITE);
                holder.rootView.setBackgroundColor(Color.parseColor("#000000"));
            } else {
                holder.textView.setTextColor(Color.BLACK);
                holder.rootView.setBackgroundColor(Color.parseColor("#00000000"));
            }
        }

        @Override
        public int getItemCount() {
            return numbers.length;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvNumber);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }
}

