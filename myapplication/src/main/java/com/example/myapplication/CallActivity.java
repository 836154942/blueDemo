package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Queue;

/**
 * Created by shipinchao on 2018/3/28.
 */

public class CallActivity extends Activity {

    RecyclerView recycleView;
    RecyclerView secondRecycleView;
    Adapter adapter;
    EditText editText;
    Button button;
    LinearLayoutManager layoutManager;
    SecondAdapter secondAdapter;

    private String[][] codes = {{}, {"1"}, {"2", "A", "B", "C"}, {"3", "D", "E", "F"}, {"4", "G", "H", "I"}, {"5", "J", "K", "L"},
            {"6", "M", "N", "O"}, {"7", "P", "Q", "R", "S"}, {"8", "T", "U", "V"}, {"9", "W", "X", "Y", "Z"}};

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
                recycleView.smoothScrollToPosition(i);
                adapter.setSelected(i);
                adapter.notifyDataSetChanged();
                if (i != 0) {
                    secondAdapter.setCurrentPosition(0);
                    secondAdapter.setList(codes[i]);
                    secondRecycleView.setVisibility(View.VISIBLE);
                    secondAdapter.notifyDataSetChanged();

                } else {
                    secondRecycleView.setVisibility(View.GONE);
                }

            }
        });
        secondRecycleView= (RecyclerView) findViewById(R.id.secondRecycleView);
        secondAdapter = new SecondAdapter(this);
        secondRecycleView.setLayoutManager(new GridLayoutManager(this, 5));
        secondRecycleView.setAdapter(secondAdapter);
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
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


    public static class SecondAdapter extends RecyclerView.Adapter<SecondViewHolder> {
        Context context;
        String[] list;
        int currentPosition = 0;

        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        public void setList(String[] list) {
            this.list = list;
        }

        public SecondAdapter(Context context) {
            this.context = context;
        }

        @Override
        public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SecondViewHolder(LayoutInflater.from(context).inflate(R.layout.item_secode_number, parent, false));
        }

        @Override
        public void onBindViewHolder(SecondViewHolder holder, int position) {
            if (position == currentPosition) {
                holder.textView.setBackgroundColor(Color.parseColor("#0cce80"));
                holder.textView.setTextColor(Color.WHITE);
            } else {
                holder.textView.setTextColor(Color.BLACK);
                holder.textView.setBackgroundColor(Color.WHITE);
            }
            holder.textView.setText(list[position]);

        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.length;
        }
    }

    public static class SecondViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView textView;

        public SecondViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
            textView = (TextView) itemView.findViewById(R.id.tvText);
        }
    }
}

