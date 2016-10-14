package com.chinasoft.ctams.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.bean.bean_json.StatisticReceiveInfoBean;

import java.util.List;

/**
 * 自定义柱状图View
 */
public class HistogramView extends View {

	private Paint xLinePaint;// 坐标轴 轴线 画笔：
	private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
	private Paint titlePaint;// 绘制文本的画笔
	private Paint paint;// 矩形画笔 柱状图的样式信息
	private int[] progress;// 7 条
	private int[] aniProgress;// 实现动画的值
	private final int TRUE = 1;// 在柱状图上显示数字
	// 坐标轴左侧的数标
	private String[] ySteps;
	// 坐标轴底部的数值
	private String[] xWeeks;
	List<StatisticReceiveInfoBean> list;

	private HistogramAnimation ani;

	public HistogramView(Context context, List<StatisticReceiveInfoBean> list) {
		super(context);
		this.list=list;
		int len=list.size();
		xWeeks=new String[len];
		aniProgress=new int[len];
		for (int i=0;i<list.size();i++){
			String type=list.get(i).getType();
			if (type.length()>2){
				type=type.substring(0,2);
			}
			xWeeks[i]=type;
			aniProgress[i]=list.get(i).getCount();
		}
		init();
	}

	public HistogramView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		ySteps = new String[] { "100", "75", "50", "25","0" };
		ani = new HistogramAnimation();
		ani.setDuration(1000);

		xLinePaint = new Paint();
		hLinePaint = new Paint();
		titlePaint = new Paint();
		paint = new Paint();

		xLinePaint.setColor(Color.DKGRAY);
		hLinePaint.setColor(Color.LTGRAY);
	}

	public void setProgress(int[] progress) {
		this.progress = progress;
		this.startAnimation(ani);
	}


	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight()-50;
		Log.i("info","布局的宽度"+width+"   高度 "+height);
		int origin_X=width/12;

		// 1 绘制坐标线：startX, startY, stopX, stopY, paint
		canvas.drawLine(origin_X, 10, origin_X, height, xLinePaint);
		canvas.drawLine(origin_X, height, width - 10, height, xLinePaint);

		// 2 绘制坐标内部的水平线

		int leftHeight = height - 20;// 左侧外周的 需要划分的高度：

		int hPerHeight = leftHeight / 4;// 分成四部分

		hLinePaint.setTextAlign(Align.CENTER);
		for (int i = 0; i < 4; i++) {
			canvas.drawLine(origin_X, 20 + i * hPerHeight, width - 10, 20 + i
					* hPerHeight, hLinePaint);
		}

		// 3 绘制 Y 周坐标

		titlePaint.setTextAlign(Align.RIGHT);
		titlePaint.setTextSize(width/33);
		titlePaint.setColor(Color.YELLOW);
		titlePaint.setAntiAlias(true);
		titlePaint.setStyle(Paint.Style.FILL);
		for (int i = 0; i < ySteps.length; i++) {
			canvas.drawText(ySteps[i], origin_X*6/7, 30 + i * hPerHeight, titlePaint);
		}

		// 4 绘制 X 周 做坐标
		titlePaint.setColor(Color.WHITE);
		int xAxisLength = width - origin_X;
		int columCount = xWeeks.length + 1;
		int step = xAxisLength / columCount;

		for (int i = 0; i < columCount - 1; i++) {
			// text, baseX, baseY, textPaint
			canvas.drawText(xWeeks[i], origin_X + step * (i + 1), height + 30,
					titlePaint);
		}

		// 5 绘制矩形

		if (aniProgress != null && aniProgress.length > 0) {
			for (int i = 0; i < aniProgress.length; i++) {
				int value = aniProgress[i];
				paint.setAntiAlias(true);// 抗锯齿效果
				paint.setStyle(Paint.Style.FILL);
				paint.setTextSize(20);// 字体大小
				paint.setColor(Color.parseColor("#6DCAEC"));// 字体颜色
				Rect rect = new Rect();// 柱状图的形状

				rect.left = origin_X + step * (i + 1) - 45;
				rect.right = origin_X + step * (i + 1)-5 ;
				int rh = (int) (leftHeight - leftHeight * (value / 10000.0));
				rect.top = rh + 20;
				rect.bottom = height;

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.mipmap.bg_sky);

				canvas.drawBitmap(bitmap, null, rect, paint);
			}
		}
	}

	/**
	 * 集成animation的一个动画类
	 * 
	 */
	private class HistogramAnimation extends Animation {
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f) {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = (int) (progress[i] * interpolatedTime);
				}
			} else {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = progress[i];
				}
			}
			postInvalidate();
		}
	}

}