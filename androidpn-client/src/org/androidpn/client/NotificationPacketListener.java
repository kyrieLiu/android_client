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

import android.content.Intent;
import android.util.Log;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

/**
 * This class notifies the receiver of incoming notifcation packets
 * asynchronously.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationPacketListener implements PacketListener {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationPacketListener.class);

	private final XmppManager xmppManager;

	public NotificationPacketListener(XmppManager xmppManager) {
		this.xmppManager = xmppManager;
	}

	@Override
	public void processPacket(Packet packet) {
		Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
		Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

		if (packet instanceof NotificationIQ) {
			NotificationIQ notification = (NotificationIQ) packet;

			if (notification.getChildElementXML().contains(
					"androidpn:iq:notification")) {

				Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
				String notificationTitle = notification.getTitle();
				String notificationId = notification.getId();

				if (notificationTitle.equals("isMessage")){
					String message=notification.getMessage();
					String sendUser=notification.getSendUser();
					String sendTime=notification.getSendTime();
					String peopleName=notification.getPeopleName();

					intent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
					intent.putExtra(Constants.NOTIFICATION_MESSAGE,
							message);
					intent.putExtra(Constants.MESSAGE_SEND_TIME,sendTime);
					intent.putExtra(Constants.MESSAGE_SEND_USER,sendUser);
					intent.putExtra(Constants.MESSAGE_PEOPLE_NAME,peopleName);
					xmppManager.getContext().sendBroadcast(intent);
				}
				else{
					String notificationApiKey = notification.getApiKey();
					String notificationMessage = notification.getMessage();
					String notificationUri = notification.getUri();
					 String notificationImageUrl = notification.getImageUrl();
					intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
					intent.putExtra(Constants.NOTIFICATION_API_KEY,
							notificationApiKey);
					intent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
					intent.putExtra(Constants.NOTIFICATION_MESSAGE,
							notificationMessage);
					intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
					intent.putExtra(Constants.NOTIFICATION_IMAGE_URL,
							notificationImageUrl);

					Intent appIntent=new Intent("com.chinasoft.ctams.broadcast.MyBroadcastReceiver");
					appIntent.putExtra(Constants.NOTIFICATION_API_KEY,
							notificationApiKey);
					appIntent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
					appIntent.putExtra(Constants.NOTIFICATION_MESSAGE,
							notificationMessage);
					appIntent.putExtra(Constants.NOTIFICATION_URI,
							notificationUri);
					xmppManager.getContext().sendBroadcast(appIntent);
				}

				DeliverConfirmIQ deliverConfirmIQ = new DeliverConfirmIQ();
				deliverConfirmIQ.setUuid(notificationId);
				deliverConfirmIQ.setType(IQ.Type.SET);

				xmppManager.getConnection().sendPacket(deliverConfirmIQ);
			}

		}

	}

}
