package com.robert.viewdragdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyAdapter mMyAdapter;
    private List<String> mString;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mString = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            mString.add("item position" + i);
        }
        mMyAdapter = new MyAdapter(this, mString);
        recyclerView.setAdapter(mMyAdapter);
        Observable.interval(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mString.add("新来的" + aLong);
                        mMyAdapter.notifyDataSetChanged();
                    }
                });
    }


    static class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final Context mContext;
        List<String> mData;

        MyAdapter(Context context, List<String> data) {
            this.mContext = context;
            this.mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mText.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mText;

        ViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(android.R.id.text1);
        }
    }
}
