package com.example.myapplication;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by spc on 2017/3/16.
 */

public class DeviceAdapter extends BaseAdapter {

    List<BluetoothDevice> mList;
    Context context;


    public DeviceAdapter(List<BluetoothDevice> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_device, null);
            viewHolder.address = (TextView) view.findViewById(R.id.address);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.address.setText(mList.get(i).getAddress());
        viewHolder.name.setText(mList.get(i).getName());
        return view;
    }


    class ViewHolder {
        TextView name, address;
    }
}
