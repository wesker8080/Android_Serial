package elite.com.smartcircle.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wesker
 * @create 2019-01-24 15:27
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas = new ArrayList<>();
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context mContext, int layoutId, LayoutInflater mInflater, List<T> mDatas) {
        this.mContext = mContext;
        this.layoutId = layoutId;
        this.mInflater = mInflater;
        this.mDatas = mDatas;
    }

    public CommonAdapter(Context context, String[] datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        for (String d : datas) {
            mDatas.add((T) d);
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position),position);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t,int position);
}

