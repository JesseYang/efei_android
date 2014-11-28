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

	// content、items以及answer_content会以双美元符号（$$）作为起始和终止：
	// 1.嵌入公式；
	// 2.图片；
	// 3.带有格式的文字
	// 具体地，
	// ● und_blabla：表示blabla是带有下划线的
	// ● sub_blabla：表示blabla是下标
	// ● sup_blabla：表示blabla是上标
	// ● ita_blabla：表示blabla是斜体
	// ● equ_{name}*{width}*{height}：表示一个公式图片，其中name为该公式图片的文件名，width为图片宽度，height为图片高度。该图片的下载地址为“#{image server}/public/download/#{name}.png”
	// ● math_{name}*{width}*{height}：和equ_{name}*{width}*{height}完全一致
	// ● fig_{name}*{width}*{height}：表示一张图片，具体解释同上
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
