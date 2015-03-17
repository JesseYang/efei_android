package com.efei.lib.android.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;

import com.efei.lib.android.common.EfeiApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class UiUtils
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

	private UiUtils()
	{
	}

	/**
	 * content、items以及answer_content会以双美元符号（$$）作为起始和终止：</br> 1.嵌入公式；</br> 2.图片；</br> 3.带有格式的文字</br> 具体地， <li>und_blabla：表示blabla是带有下划线的 <li>
	 * sub_blabla：表示blabla是下标 <li>sup_blabla：表示blabla是上标 <li>ita_blabla：表示blabla是斜体 <li>
	 * equ_{name}*{width}*{height}：表示一个公式图片，其中name为该公式图片的文件名，width为图片宽度，height为图片高度。该图片的下载地址为“#{image server}/public/download/#{name}.png” <li>
	 * math_{name}*{width}*{height}：和equ_{name}*{width}*{height}完全一致 <li>fig_{name}*{width}*{height}：表示一张图片，具体解释同上
	 */
	public static SpannableString richTextToSpannable(final String txt, String imagePath)
	{
		final StringBuilder sbTmp = new StringBuilder();
		List<CharacterStyleInfo> csis = new ArrayList<CharacterStyleInfo>();

		String[] txtsBy$ = txt.split("\\$\\$");
		for (String txtBy$ : txtsBy$)
		{
			if (TextUtils.isBlank(txtBy$))
				continue;
			if (txtBy$.startsWith(START_PROMPT_MATH))
				parseImgAndConstructTmpText(sbTmp, csis, txtBy$, START_PROMPT_MATH, imagePath);
			else if (txtBy$.startsWith(START_PROMPT_EQU))
				parseImgAndConstructTmpText(sbTmp, csis, txtBy$, START_PROMPT_EQU, imagePath);
			else if (txtBy$.startsWith(START_PROMPT_FIG))
				parseImgAndConstructTmpText(sbTmp, csis, txtBy$, START_PROMPT_FIG, imagePath);
			else if (txtBy$.startsWith(START_PROMPT_UND))
			// parseStyleTextAndConstructTmpText(sbTmp, csis, txtBy$, new UnderlineSpan());
			{
				String realString = txtBy$.substring(4);
				if (TextUtils.isBlank(realString))
				{
					final int undSize = realString.length();
					for (int i = 0; i < undSize; i++)
						sbTmp.append('_');
				} else
					parseStyleTextAndConstructTmpText(sbTmp, csis, txtBy$, new UnderlineSpan());
			} else if (txtBy$.startsWith(START_PROMPT_ITA))
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
		// ss.setSpan(new SuperscriptSpanAdjuster(0.0), 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * content、items以及answer_content会以双美元符号（$$）作为起始和终止：</br> 1.嵌入公式；</br> 2.图片；</br> 3.带有格式的文字</br> 具体地， <li>und_blabla：表示blabla是带有下划线的 <li>
	 * sub_blabla：表示blabla是下标 <li>sup_blabla：表示blabla是上标 <li>ita_blabla：表示blabla是斜体 <li>
	 * equ_{name}*{width}*{height}：表示一个公式图片，其中name为该公式图片的文件名，width为图片宽度，height为图片高度。该图片的下载地址为“#{image server}/public/download/#{name}.png” <li>
	 * math_{name}*{width}*{height}：和equ_{name}*{width}*{height}完全一致 <li>fig_{name}*{width}*{height}：表示一张图片，具体解释同上
	 * 
	 * @param imagePath
	 */
	public static SpannableString richTextToSpannable(final List<String> lines, String imagePath)
	{
		if (CollectionUtils.isEmpty(lines))
			return new SpannableString("");
		StringBuilder sb = new StringBuilder();
		for (String line : lines)
			sb.append(line).append('\n');
		sb.replace(sb.length() - 1, sb.length(), "");
		return richTextToSpannable(sb.toString(), imagePath);
	}

	private static void parseImgAndConstructTmpText(final StringBuilder newSb, List<CharacterStyleInfo> smis, String txtBy$, String startPrompt,
			String imagePath)
	{
		CharacterStyleInfo smi = parseImageSync(newSb.length(), txtBy$, startPrompt, imagePath);
		smis.add(smi);
		newSb.append(IMG_PLACE_HOLDER);
	}

	private static void parseStyleTextAndConstructTmpText(final StringBuilder newSb, List<CharacterStyleInfo> smis, String txtBy$, CharacterStyle style)
	{
		String realString = txtBy$.substring(4);
		smis.add(new CharacterStyleInfo(newSb.length(), newSb.length() + realString.length(), style));
		newSb.append(realString);
	}

	private static CharacterStyleInfo parseImageSync(final int imgPos, String txtImage, String startPrompt, String imagePath)
	{
		String[] txtsByStar = txtImage.split("\\*");
		String imageFile = txtsByStar[0].substring(startPrompt.length());
		final String link = imagePath + imageFile + "." + txtsByStar[1];
		Bitmap bmp = ImageLoader.getInstance().loadImageSync(link, new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build());
		BitmapDrawable bmpDrawable = new BitmapDrawable(EfeiApplication.getContext().getResources(), bmp);

		final float fRadio = EfeiApplication.getContext().getResources().getDisplayMetrics().density;
		bmpDrawable.setBounds(0, 0, (int) (Float.parseFloat(txtsByStar[2]) * fRadio), (int) (Float.parseFloat(txtsByStar[3]) * fRadio));
		ImageSpan img = new ImageSpan(bmpDrawable, ImageSpan.ALIGN_BOTTOM);
		// ImageSpan img = new CenteredImageSpan(bmpDrawable);
		return new CharacterStyleInfo(imgPos, img);
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

	@Deprecated
	private static class CenteredImageSpan extends ImageSpan
	{
		public CenteredImageSpan(final Drawable drawable)
		{
			this(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
		}

		public CenteredImageSpan(final Drawable drawable, final int verticalAlignment)
		{
			super(drawable, verticalAlignment);
		}

		// Redefined locally because it is a private member from DynamicDrawableSpan
		private Drawable getCachedDrawable()
		{
			WeakReference<Drawable> wr = mDrawableRef;
			Drawable d = null;

			if (wr != null)
				d = wr.get();

			if (d == null)
			{
				d = getDrawable();
				mDrawableRef = new WeakReference<Drawable>(d);
			}

			return d;
		}

		private WeakReference<Drawable> mDrawableRef;
		private int transY;

		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint)
		{
			Drawable b = getCachedDrawable();
			canvas.save();

			// int bCenter = b.getIntrinsicHeight() / 2;
			// int fontTop = paint.getFontMetricsInt().top;
			// int fontBottom = paint.getFontMetricsInt().bottom;
			// int transY = (bottom - b.getBounds().bottom) - (((fontBottom - fontTop) / 2) - bCenter);
			transY = (int) (b.getBounds().height() - paint.getTextSize() > 5 ? bottom - b.getBounds().bottom * 0.7f : bottom - b.getBounds().bottom);

			canvas.translate(x, transY);
			b.draw(canvas);
			canvas.restore();
		}

		@Override
		public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm)
		{
			Drawable d = getCachedDrawable();
			Rect rect = new Rect(d.getBounds());
			rect.offset(0, transY);

			if (fm != null)
			{
				fm.ascent = -rect.bottom;
				fm.descent = 0;

				fm.top = fm.ascent;
				fm.bottom = 0;
			}

			return rect.right;
		}
	}

}
