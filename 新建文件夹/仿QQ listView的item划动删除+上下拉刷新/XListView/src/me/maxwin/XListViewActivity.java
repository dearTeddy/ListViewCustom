package me.maxwin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import me.maxwin.view.XListView.RemoveListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class XListViewActivity extends Activity implements IXListViewListener ,
	RemoveListener,OnItemClickListener{
	private XListView mListView;
	private ItemAdapter adapter;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private Calendar calendar = Calendar.getInstance();
	private Date date = calendar.getTime();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		geneItems();
		init();
		
		mHandler = new Handler();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		mListView.setRemoveListener(this);
		mListView.setOnItemClickListener(this);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		adapter=new ItemAdapter(this);
		adapter.setData(items);
		mListView.setAdapter(adapter);
	}
	
	private void geneItems() {
		for (int i = 0; i != 20; ++i) {
			items.add("refresh cnt " + (++start));
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(sdf.format(date));
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				items.clear();
				geneItems();
				// mAdapter.notifyDataSetChanged();
				adapter=new ItemAdapter(XListViewActivity.this);
				adapter.setData(items);
				mListView.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
	@Override
	public void removeItem(int position) {
		// TODO Auto-generated method stub
		mListView.isSlide = false;
		mListView.itemView.findViewById(R.id.tv_coating).setVisibility(View.VISIBLE);
		items.remove(position);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
		intent.putExtra("item", items.get(position));
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.remain_original_location);
	}

}