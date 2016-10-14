package com.chinasoft.ctams.fragment.mediafragment.monitorPlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_CLIENTINFO;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.RealPlayCallBack;

import org.MediaPlayer.PlayM4.Player;

public class Sjrs08SurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private HCNetSDK videoCtr;    //�����sdk
	private Player myPlayer = null;  //���ſ�sdk
	public int playPort = -1;   //���Ŷ˿�
	public boolean playFlag = false;   //���ű�־
	public int userId = -1;   //��¼�ʺ�id
	public MonitorCameraInfo cameraInfo = null;   //��ص���Ϣ

	private SurfaceHolder holder = null;

	public Sjrs08SurfaceView(Context paramContext) {
		super(paramContext);
		initSurfaceView();

	}

	public Sjrs08SurfaceView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initSurfaceView();
	}

	public Sjrs08SurfaceView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet);
		initSurfaceView();
	}

	private void initSurfaceView() {

		getHolder().addCallback(this);

	}

	public boolean onDown(MotionEvent paramMotionEvent) {
		return false;
	}

	public boolean onFling(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return false;
	}

	public void onLongPress(MotionEvent paramMotionEvent) {
	}

	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return false;
	}

	public void onShowPress(MotionEvent paramMotionEvent) {
	}

	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
		return false;
	}

	public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1,
			int paramInt2, int paramInt3) {
	}

	public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {

		holder = this.getHolder();

	}

	public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {

	}

	public void setMonitorInfo(MonitorCameraInfo setMonitorInfo) {
		this.cameraInfo = setMonitorInfo;
	}

	/**
	 * ʱ����ˮ2013-7-10
	 * ���ܣ��ݶ�����
	 * flag 1/��ͣ 0/�ָ�
	 */
	public void pausePaly(int flag) {
		myPlayer.pause(playPort, flag);
	}

	public void stopPlay() {

		try {
			playFlag = false;
			videoCtr.NET_DVR_StopRealPlay(playPort);
			videoCtr.NET_DVR_Logout_V30(userId);

			userId = -1;

			videoCtr.NET_DVR_Cleanup();

			if (myPlayer != null) {
				myPlayer.stop(playPort);
				myPlayer.closeStream(playPort);
				myPlayer.freePort(playPort);

				playPort = -1;

				destroyDrawingCache();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	/**
	 * ʱ����ˮ2013-7-10
	 *  ���ܣ���ʼʵʱԤ��*/
	public void startPlay() {

		try {
			
			// ʵ���API
			myPlayer = Player.getInstance();

			// ʵ�������android sdk
			videoCtr = new HCNetSDK();

			videoCtr.NET_DVR_Init();  //��ʼ����������android sdk

			videoCtr.NET_DVR_SetExceptionCallBack(mExceptionCallBack);  //���ô���ص�����

			// �������ӳ�ʱʱ��
			videoCtr.NET_DVR_SetConnectTime(60000);

			playPort = myPlayer.getPort();  //��ȡ���в��Ŷ˿�

			NET_DVR_DEVICEINFO_V30 deviceInfo = new NET_DVR_DEVICEINFO_V30();
			// ��¼������
			userId = videoCtr.NET_DVR_Login_V30(cameraInfo.serverip,
					cameraInfo.serverport, cameraInfo.username,
					cameraInfo.userpwd, deviceInfo);


			byte[] sbbyte = deviceInfo.sSerialNumber;
			String sNo = "";
			for (int i = 0; i < sbbyte.length; i++) {
				sNo += String.valueOf(sbbyte[i]);
			}

			NET_DVR_CLIENTINFO clientInfo = new NET_DVR_CLIENTINFO();

			clientInfo.lChannel = cameraInfo.channel;
			clientInfo.lLinkMode = 0x80000000; //
			clientInfo.sMultiCastIP = null;

			int playFlag = videoCtr.NET_DVR_RealPlay_V30(userId, clientInfo,
					mRealPlayCallBack, false);
			System.out.println("playFlag=" + playFlag);
			System.out.println("GetLastError="
					+ videoCtr.NET_DVR_GetLastError());

		} catch (Exception e) {
			e.printStackTrace();

			stopPlay();
		}

	}

	private ExceptionCallBack mExceptionCallBack = new ExceptionCallBack() {

		@Override
		public void fExceptionCallBack(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			System.out.println("�쳣�ص��������У�");

		}
	};

	private RealPlayCallBack mRealPlayCallBack = new RealPlayCallBack() {

		@Override
		public void fRealDataCallBack(int lRealHandle, int dataType,
				byte[] paramArrayOfByte, int byteLen) {
			// TODO Auto-generated method stub


			if (playPort == -1)
				return;

			switch (dataType) {
			case 1: // ͷ���

				if (myPlayer.openStream(playPort, paramArrayOfByte, byteLen,
						1024 * 1024)) {
					if (myPlayer.setStreamOpenMode(playPort, 1)) {
						if (myPlayer.play(playPort, holder)) {
							playFlag = true;
						} else {
							playError(3);
						}
					} else {
						playError(2);
					}
				} else {
					playError(1);
				}

				break;
			case 4:

				if (playFlag
						&& myPlayer.inputData(playPort, paramArrayOfByte,
								byteLen)) {
					playFlag = true;
				} else {
					playError(4);
					playFlag = false;
				}

			}

		}

	};

	private void playError(int step) {

		switch (step) {
		case 1:
			System.out.println("openStream error,step=" + step);
			break;
		case 2:
			System.out.println("setStreamOpenMode error,step=" + step);
			break;
		case 3:
			System.out.println("play error,step=" + step);
			break;
		case 4:
			System.out.println("inputData error,step=" + step);
			break;
		}

		stopPlay();
	}

}