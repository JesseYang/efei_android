package com.efei.lib.android.grammar;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;

import com.efei.lib.android.common.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RichText
{
	private static final String START_PROMPT_MATH = "math_";
	private static final String URL_API_IMAGE = "public/download/";
	private Spannable reformatText;

	// private List<String>

	// content��items�Լ�answer_content����˫��Ԫ���ţ�$$����Ϊ��ʼ����ֹ��
	// 1.Ƕ�빫ʽ��
	// 2.ͼƬ��
	// 3.���и�ʽ������
	// ����أ�
	// �� und_blabla����ʾblabla�Ǵ����»��ߵ�
	// �� sub_blabla����ʾblabla���±�
	// �� sup_blabla����ʾblabla���ϱ�
	// �� ita_blabla����ʾblabla��б��
	// �� equ_{name}*{width}*{height}����ʾһ����ʽͼƬ������nameΪ�ù�ʽͼƬ���ļ�����widthΪͼƬ��ȣ�heightΪͼƬ�߶ȡ���ͼƬ�����ص�ַΪ��#{image server}/public/download/#{name}.png��
	// �� math_{name}*{width}*{height}����equ_{name}*{width}*{height}��ȫһ��
	// �� fig_{name}*{width}*{height}����ʾһ��ͼƬ���������ͬ��
	public RichText(List<String> textLines)
	{
		StringBuilder sb = new StringBuilder();
		for (String line : textLines)
			sb.append(line);
		final String txt = sb.toString();

		final StringBuilder newSb = new StringBuilder();

		List<SpanModifyInfo> smis = new ArrayList<RichText.SpanModifyInfo>();

		String[] txtsBy$ = txt.split("\\$\\$");
		for (String txtBy$ : txtsBy$)
		{
			if (txtBy$.startsWith(START_PROMPT_MATH))
			{
				String[] txtsByStar = txtBy$.split("\\*");
				String imageFile = txtsByStar[0].substring(START_PROMPT_MATH.length());
				String link = Constants.Net.IMAGE_SERVER_URL + URL_API_IMAGE + imageFile + ".png";
				Bitmap bmp = ImageLoader.getInstance().loadImageSync(link);
//				BitmapDrawable bmpDrawable = new BitmapDrawable(EfeiApplication.getContext().getResources(), bmp);
//				bmpDrawable.setBounds(0, 0, right, bottom);
				@SuppressWarnings("deprecation")
				ImageSpan img = new ImageSpan(bmp, ImageSpan.ALIGN_BOTTOM);
				smis.add(new SpanModifyInfo(newSb.length(), img));
				newSb.append('F');
			} else
				newSb.append(txtBy$);
		}

		SpannableString ss = new SpannableString(newSb.toString());

		for (SpanModifyInfo info : smis)
		{
			ss.setSpan(info.imgSpan, info.pos, info.pos + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new RelativeSizeSpan(1f), info.pos, info.pos + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		reformatText = ss;
	}

	public Spannable getReformatText()
	{
		return reformatText;
	}

	private static class SpanModifyInfo
	{
		private int pos;
		private ImageSpan imgSpan;

		public SpanModifyInfo(int pos, ImageSpan imgSpan)
		{
			this.pos = pos;
			this.imgSpan = imgSpan;
		}

	}

}
