package com.efei.lib.android.grammar;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;

import com.efei.lib.android.common.Constants;
import com.efei.lib.android.common.EfeiApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@Deprecated
public class RichText
{
	private static final char IMG_PLACE_HOLDER = 'F';
	private static final String START_PROMPT_MATH = "math_";
	private static final String START_PROMPT_EQU = "equ_";
	private static final String START_PROMPT_FIG = "fig_";
	private static final String START_PROMPT_UND = "und_";
	private static final String START_PROMPT_SUB = "sub_";
	private static final String START_PROMPT_SUP = "sup_";
	private static final String START_PROMPT_ITA = "ita_";

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
	public RichText(String text)
	{
		reformatText = toSpannable(text);
	}

	public static SpannableString toSpannable(final String txt)
	{
		final StringBuilder sbTmp = new StringBuilder();
		List<CharacterStyleInfo> csis = new ArrayList<CharacterStyleInfo>();

		String[] txtsBy$ = txt.split("\\$\\$");
		for (String txtBy$ : txtsBy$)
		{
			if (txtBy$.startsWith(START_PROMPT_MATH))
				parseImgAndConstructTmpText(sbTmp, csis, txtBy$, START_PROMPT_MATH);
			else if (txtBy$.startsWith(START_PROMPT_EQU))
				parseImgAndConstructTmpText(sbTmp, csis, txtBy$, START_PROMPT_EQU);
			else if (txtBy$.startsWith(START_PROMPT_FIG))
				parseImgAndConstructTmpText(sbTmp, csis, txtBy$, START_PROMPT_FIG);
			else if (txtBy$.startsWith(START_PROMPT_UND))
				parseStyleTextAndConstructTmpText(sbTmp, csis, txtBy$, new UnderlineSpan());
			else if (txtBy$.startsWith(START_PROMPT_ITA))
				parseStyleTextAndConstructTmpText(sbTmp, csis, txtBy$, new StyleSpan(android.graphics.Typeface.ITALIC));
			else if (txtBy$.startsWith(START_PROMPT_SUP))
				parseStyleTextAndConstructTmpText(sbTmp, csis, txtBy$, new SuperscriptSpan());
			else if (txtBy$.startsWith(START_PROMPT_SUB))
				parseStyleTextAndConstructTmpText(sbTmp, csis, txtBy$, new SubscriptSpan());
			else
				sbTmp.append(txtBy$);
		}

		SpannableString ss = new SpannableString(sbTmp.toString());

		for (CharacterStyleInfo info : csis)
			ss.setSpan(info.imgSpan, info.posStart, info.posEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	private static void parseImgAndConstructTmpText(final StringBuilder newSb, List<CharacterStyleInfo> smis, String txtBy$, String startPrompt)
	{
		CharacterStyleInfo smi = parseImageSync(newSb.length(), txtBy$, startPrompt);
		smis.add(smi);
		newSb.append(IMG_PLACE_HOLDER);
	}

	private static void parseStyleTextAndConstructTmpText(final StringBuilder newSb, List<CharacterStyleInfo> smis, String txtBy$, CharacterStyle style)
	{
		String realString = txtBy$.substring(4);
		smis.add(new CharacterStyleInfo(newSb.length(), newSb.length() + realString.length(), style));
		newSb.append(realString);
	}

	private static CharacterStyleInfo parseImageSync(final int imgPos, String txtImage, String startPrompt)
	{
		String[] txtsByStar = txtImage.split("\\*");
		String imageFile = txtsByStar[0].substring(startPrompt.length());
		final String link = Constants.Net.IMAGE_SERVER_URL + URL_API_IMAGE + imageFile + ".png";
		Bitmap bmp = ImageLoader.getInstance().loadImageSync(link, new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build());
		BitmapDrawable bmpDrawable = new BitmapDrawable(EfeiApplication.getContext().getResources(), bmp);
		final float fRadio = 0.8f;
		bmpDrawable.setBounds(0, 0, (int) (bmp.getWidth() * fRadio), (int) (bmp.getHeight() * fRadio));
		ImageSpan img = new ImageSpan(bmpDrawable, ImageSpan.ALIGN_BOTTOM);
		return new CharacterStyleInfo(imgPos, img);
	}

	public Spannable getReformatText()
	{
		// if (null == reformatText)
		// reformatText = toSpannable(text);
		return reformatText;
	}

	private static class CharacterStyleInfo
	{
		final private int posStart;
		final private int posEnd;
		final private CharacterStyle imgSpan;

		public CharacterStyleInfo(int pos, CharacterStyle cs)
		{
			this.posStart = pos;
			this.posEnd = posStart + 1;
			this.imgSpan = cs;
		}

		public CharacterStyleInfo(int posStart, int posEnd, CharacterStyle imgSpan)
		{
			this.posStart = posStart;
			this.posEnd = posEnd;
			this.imgSpan = imgSpan;
		}

	}

}
