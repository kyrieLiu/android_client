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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import sql.ChatInformation;

/**
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class NotificationReceiver extends BroadcastReceiver {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationReceiver.class);

	// private NotificationService notificationService;

	public NotificationReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
		String action = intent.getAction();
		Log.d(LOGTAG, "action=" + action);

		if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
			String title = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
			if ("isMessage".equals(title)) {
				String messageContent = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
				String accountName = intent.getStringExtra(Constants.MESSAGE_SEND_USER);
				String sendTime = intent.getStringExtra(Constants.MESSAGE_SEND_TIME);
				String peopleName = intent.getStringExtra(Constants.MESSAGE_PEOPLE_NAME);
				ChatInformation information = new ChatInformation();
				information.setSendTime(sendTime);
				information.setTargetAccountName(accountName);
				information.setMessageContent(messageContent);
				information.setReceive(true);
				information.setPeopleName(peopleName);
				information.save();
				Intent messageIntent = new Intent(Constants.ACTION_MESSAGE_RECEIVED);
				context.sendBroadcast(messageIntent);
			}

		}

	}

}
