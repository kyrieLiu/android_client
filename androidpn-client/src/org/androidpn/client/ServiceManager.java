/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * This class is to manage the notificatin service and to load the
 * configuration.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class ServiceManager {

	private static final String LOGTAG = LogUtil
			.makeLogTag(ServiceManager.class);

	private Context context;

	private SharedPreferences sharedPrefs;

	private Properties props;

	private String version = "0.5.0";

	private String apiKey;

	private String xmppHost;

	private String xmppPort;

	private String callbackActivityPackageName;

	private String callbackActivityClassName;

	public ServiceManager(Context context) {
		this.context = context;

		if (context instanceof Activity) {
			Log.i(LOGTAG, "Callback Activity...");
			Activity callbackActivity = (Activity) context;
			callbackActivityPackageName = callbackActivity.getPackageName();
			callbackActivityClassName = callbackActivity.getClass().getName();
		}

		apiKey="1234567890";
		SharedPreferences preferences=context.getSharedPreferences("ip",Context.MODE_PRIVATE);
		xmppHost=preferences.getString("ipPush","");
		//xmppHost="192.168.1.186";
		Log.i("info","得到的xmppHost"+xmppHost);
		xmppPort="5222";

		sharedPrefs = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.API_KEY, apiKey);
		editor.putString(Constants.VERSION, version);
		editor.putString(Constants.XMPP_HOST, xmppHost);
		editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
		editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME,
				callbackActivityPackageName);
		editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME,
				callbackActivityClassName);
		editor.commit();
		// Log.i(LOGTAG, "sharedPrefs=" + sharedPrefs.toString());
	}
	private Intent intent;
	public void startService(final String name, final boolean isChangeName) {
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				intent=new Intent(context,NotificationService.class);
				intent.putExtra("name",name);
				intent.putExtra("isChangeName",isChangeName);
				context.startService(intent);
			}
		});
		serviceThread.start();
	}

	public void stopService() {
		//intent=new Intent(context,NotificationService.class);
		if (intent!=null) {
			Log.i("info", "停止服务");
			context.stopService(intent);
		}
	}

	public void setTags(final List<String> tagsList) {
		final String username = sharedPrefs.getString(Constants.XMPP_USERNAME,
				"");
		if (tagsList == null || tagsList.isEmpty()
				|| TextUtils.isEmpty(username)) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				NotificationService notificationService = NotificationService
						.getNotificationService();
				XmppManager xmppManager = notificationService.getXmppManager();

				if (xmppManager != null) {
					if (!xmppManager.isAuthenticated()) {

						try {
							synchronized (xmppManager) {
								Log.d(LOGTAG, "wai for authenticated...");
								xmppManager.wait();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Log.d(LOGTAG, "authenticated already,send Alias now");
					SetTagsIQ iq = new SetTagsIQ();
					iq.setType(IQ.Type.SET);
					iq.setUsername(username);
					iq.setTagList(tagsList);
					xmppManager.getConnection().sendPacket(iq);
				}
			}
		}).start();
	}

	public void setAlias(final String alias) {
		final String username = sharedPrefs.getString(Constants.XMPP_USERNAME,
				"");
		if (TextUtils.isEmpty(alias) || TextUtils.isEmpty(username)) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				NotificationService notificationService = NotificationService
						.getNotificationService();
				XmppManager xmppManager = notificationService.getXmppManager();

				if (xmppManager != null) {
					if (!xmppManager.isAuthenticated()) {

						try {
							synchronized (xmppManager) {
								xmppManager.wait();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					SetAliasIQ iq = new SetAliasIQ();
					iq.setType(IQ.Type.SET);
					iq.setUsername(username);
					iq.setAlias(alias);
					xmppManager.getConnection().sendPacket(iq);
				}

			}
		}).start();

	}

	/**
	 *发送消息
     */
	private NotificationService sendMessageService;
	private XmppManager xmppManager;

	public void sendMessage(final String message, final String targetUser, final File file){

		final String username = sharedPrefs.getString(Constants.XMPP_USERNAME,
				"");
		if (TextUtils.isEmpty(message) || TextUtils.isEmpty(username)) {
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				sendMessageService = NotificationService
						.getNotificationService();
				xmppManager = sendMessageService.getXmppManager();

				if (xmppManager != null) {
					if (!xmppManager.isAuthenticated()) {

						try {
							synchronized (xmppManager) {
								xmppManager.wait();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					SetMessageIQ iq = new SetMessageIQ();
					iq.setType(IQ.Type.SET);
					iq.setContent(message);
					iq.setSendUser(username);
					iq.setTargetUser(targetUser);
					XMPPConnection connection=xmppManager.getConnection();
					connection.sendPacket(iq);
				}

			}
		}).start();

	}

	private Properties loadProperties() {


		Properties props = new Properties();
		try {
			int id = context.getResources().getIdentifier("androidpn", "raw",
					context.getPackageName());
			props.load(context.getResources().openRawResource(id));
		} catch (Exception e) {
			Log.e(LOGTAG, "Could not find the properties file.", e);
			// e.printStackTrace();
		}
		return props;
	}


	public void setNotificationIcon(int iconId) {
		Editor editor = sharedPrefs.edit();
		editor.putInt(Constants.NOTIFICATION_ICON, iconId);
		editor.commit();
	}


}
