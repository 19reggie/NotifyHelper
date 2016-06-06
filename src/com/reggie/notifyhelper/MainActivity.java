package com.reggie.notifyhelper;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {

	private Button add_notify_btn;
	private EditText input_notify_editText;
	private Button action_notify_btn;
	private Button progress_notify_btn;

	private Button start_activity_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		add_notify_btn = (Button) findViewById(R.id.add_notify_btn);
		input_notify_editText = (EditText) findViewById(R.id.input_notify_editText);
		action_notify_btn = (Button) findViewById(R.id.action_notify_btn);
		progress_notify_btn = (Button) findViewById(R.id.progress_notify_btn);
		start_activity_btn = (Button) findViewById(R.id.start_activity_btn);

		add_notify_btn.setOnClickListener(this);
		action_notify_btn.setOnClickListener(this);
		progress_notify_btn.setOnClickListener(this);
		start_activity_btn.setOnClickListener(this);
		Log.d("notifyhelper", "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_notify_btn:
			simpleNotify();
			break;
		case R.id.action_notify_btn:
			myNotify();
			break;
		case R.id.progress_notify_btn:
			progressNotify();
			break;
		case R.id.start_activity_btn:
			startActivity();
			break;
		}
	}

	private void simpleNotify() {
		// 判断input_notify_editText.getText()为空
		if (TextUtils.isEmpty(input_notify_editText.getText().toString())) {
			Toast.makeText(getApplicationContext(), "input is not null!", Toast.LENGTH_SHORT).show();
			return;
		} else {
			int count = Integer.parseInt(input_notify_editText.getText().toString());
			NotificationCompat.Builder mBuilder;
			for (int i = 0; i < count; i++) {
				mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notify_icon)
						.setContentTitle("My notification").setContentText("Hello World!").setAutoCancel(true);
				int mNotificationId = i;
				NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotifyMgr.notify(mNotificationId, mBuilder.build());
			}
		}
	}

	private void myNotify() {
		NotificationCompat.Builder mBuilder;
		mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notify_icon)
				.setContentTitle("Click me!").setContentText("Goto ResultActivity").setOngoing(true);

		Intent resultIntent = new Intent(this, ResultActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		int mNotificationId = 0;
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}

	private void progressNotify() {
		final NotificationCompat.Builder mBuilder;
		final int id = 1;
		final NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("Picture Download").setContentText("Download in progress")
				.setSmallIcon(R.drawable.notify_icon);
		new Thread(new Runnable() {

			@Override
			public void run() {
				int incr;
				for (incr = 0; incr <= 100; incr += 5) {
					mBuilder.setProgress(100, incr, false);// 下载进度间隔变化
					// mBuilder.setProgress(100, incr, true);// 下载进度持续变化
					mNotifyMgr.notify(id, mBuilder.build());
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mBuilder.setContentText("Download complete.").setProgress(0, 0, false);
				mBuilder.setAutoCancel(true);
				mNotifyMgr.notify(id, mBuilder.build());
			}
		}).start();
	}

	private void startActivity() {
		// 用intent实现页面跳转
		// AndroidManifest.xml配置ResultActivity
		Intent myIntent = new Intent(this, ResultActivity.class);
		startActivity(myIntent);
	}
}
