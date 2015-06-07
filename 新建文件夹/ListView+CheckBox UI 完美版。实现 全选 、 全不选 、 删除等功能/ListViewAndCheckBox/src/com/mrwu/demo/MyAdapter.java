package com.mrwu.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mrwu.demo.DemoAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MyAdapter extends BaseAdapter{

	private Context context;
	private List<DemoBean> datas;
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
	
	
	public MyAdapter(Context context, List<DemoBean> datas) {
		super();
		this.context = context;
		this.datas = datas;
		
		configCheckMap(Boolean.FALSE);
	}
	
	/**
	 * 	public void configCheckMap(boolean bool) {
		for (int i = 0; i < datas.size(); i++) {
			isCheckMap.put(i, bool);
		}
	}
	 */
	
	public void configCheckMap(boolean bool){
		for (int i = 0; i < datas.size(); i++) {
			isCheckMap.put(i, bool);
		}
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		ViewGroup layout = null;
		if(convertview == null){
			layout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.listview_item_layout,
					parent, false);
		}else{
			layout = (ViewGroup) convertview;
		}
		DemoBean bean = datas.get(position);
		
		boolean canRemove = bean.isCanRemove();
		TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
		tvTitle.setText(bean.getTitle());
		
		CheckBox cbCheck = (CheckBox) layout.findViewById(R.id.cbCheckBox);
		cbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				/*
				 * 将选择项加载到map里面寄存
				 */
				isCheckMap.put(position, isChecked);
			}
		});
		
		if (!canRemove) {
			// 隐藏单选按钮,因为是不可删除的
			cbCheck.setVisibility(View.GONE);
			cbCheck.setChecked(false);
		} else {
			cbCheck.setVisibility(View.VISIBLE);

			if (isCheckMap.get(position) == null) {
				isCheckMap.put(position, false);
			}

			cbCheck.setChecked(isCheckMap.get(position));

			ViewHolder holder = new ViewHolder();

			holder.cbCheck = cbCheck;

			holder.tvTitle = tvTitle;

			/**
			 * 将数据保存到tag
			 */
			layout.setTag(holder);
		}
		return layout;
	}
	
	public void add(DemoBean bean) {
		this.datas.add(0, bean);

		// 让所有项目都为不选择
		configCheckMap(false);
	}
	
	public void remove(int position) {
		this.datas.remove(position);
	}
	
	public Map<Integer, Boolean> getCheckMap() {
		return this.isCheckMap;
	}
	
	public List<DemoBean> getDatas() {
		return datas;
	}
	
	class ViewHolder {
		public TextView tvTitle = null;
		public CheckBox cbCheck = null;
		public Object data = null;
	}
}
	
