package com.chinasoft.ctams.util;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class TouchListener implements OnTouchListener{
	
	private PointF startPoint = new PointF();
	private Matrix matrix = new Matrix();
	private Matrix currentMaritx = new Matrix();
	
	private int mode = 0;//���ڱ��ģʽ
	private static final int DRAG = 1;//�϶�
	private static final int ZOOM = 2;//�Ŵ�
	private float startDis = 0;
	private PointF midPoint;//���ĵ�
	
	private ImageView imageView;
	
	public TouchListener(ImageView imageView){
		this.imageView=imageView;
	}
	
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			currentMaritx.set(imageView.getImageMatrix());//��¼ImageView���ڵ��ƶ�λ��
			startPoint.set(event.getX(),event.getY());//��ʼ��
			break;

		case MotionEvent.ACTION_MOVE://�ƶ��¼�
			if (mode == DRAG) {//ͼƬ�϶��¼�
				float dx = event.getX() - startPoint.x;//x���ƶ�����
				float dy = event.getY() - startPoint.y;
				matrix.set(currentMaritx);//�ڵ�ǰ��λ�û������ƶ�
				matrix.postTranslate(dx, dy);
				
			} else if(mode == ZOOM){//ͼƬ�Ŵ��¼�
				float endDis = distance(event);//��������
				if(endDis > 10f){
					float scale = endDis / startDis;//�Ŵ���
					matrix.set(currentMaritx);
					matrix.postScale(scale, scale, midPoint.x, midPoint.y);
				}
				
				
			}

			break;
			
		case MotionEvent.ACTION_UP:
			mode = 0;
			break;
		//����ָ�뿪��Ļ������Ļ���д���(��ָ)
		case MotionEvent.ACTION_POINTER_UP:
			mode = 0;
			break;
		//����Ļ���Ѿ��д��㣨��ָ��,����һ����ָѹ����Ļ
		case MotionEvent.ACTION_POINTER_DOWN:
			mode = ZOOM;
			startDis = distance(event);
			
			if(startDis > 10f){//������ָ����������
				midPoint = mid(event);
				currentMaritx.set(imageView.getImageMatrix());//��¼��ǰ�����ű���
			}
			
			break;


		}
		imageView.setImageMatrix(matrix);
		return true;
	}
	


/**
 * ����֮��ľ���
 * @param event
 * @return
 */
private static float distance(MotionEvent event){
	//�����ߵľ���
	float dx = event.getX(1) - event.getX(0);
	float dy = event.getY(1) - event.getY(0);
	return (float) Math.sqrt(dx*dx + dy*dy);
}
/**
 * ��������֮�����ĵ�ľ���
 * @param event
 * @return
 */
private static PointF mid(MotionEvent event){
	float midx = event.getX(1) + event.getX(0);
	float midy = event.getY(1) - event.getY(0);
	
	return new PointF(midx/2, midy/2);
}
}