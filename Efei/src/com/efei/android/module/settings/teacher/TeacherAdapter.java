package com.efei.android.module.settings.teacher;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.bean.Subject;
import com.efei.lib.android.bean.net.common_data.Teacher;

class TeacherAdapter extends BaseAdapter
{
	final List<Teacher> teachers;
	private boolean bAdd;

	TeacherAdapter(List<Teacher> teachers, boolean add)
	{
		this.teachers = teachers;
		this.bAdd = add;
	}

	@Override
	public int getCount()
	{
		return teachers.size();
	}

	@Override
	public Object getItem(int position)
	{
		return teachers.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = View.inflate(parent.getContext(), R.layout.item_teacher, null);
			ViewHolder holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvSchool = (TextView) convertView.findViewById(R.id.tvSchool);
			holder.tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
			holder.ivAdd = (ImageView) convertView.findViewById(R.id.ivAdd);
			if (bAdd)
				holder.ivAdd.setVisibility(View.VISIBLE);
			else
				holder.ivAdd.setVisibility(View.GONE);
			convertView.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		Teacher teacher = teachers.get(position);
		holder.tvName.setText(teacher.getName());
		holder.tvSchool.setText(teacher.getSchool());
		holder.tvSubject.setText(Subject.getSubjectByIndex(teacher.getSubject()).name);

		updateItemView(holder, teacher);

		return convertView;
	}

	protected void updateItemView(ViewHolder holder, Teacher teacher)
	{

	}

	static class ViewHolder
	{
		TextView tvName;
		TextView tvSchool;
		TextView tvSubject;
		ImageView ivAdd;
	}
}