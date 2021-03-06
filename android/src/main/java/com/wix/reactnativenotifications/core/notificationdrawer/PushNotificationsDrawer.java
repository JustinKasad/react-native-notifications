package com.wix.reactnativenotifications.core.notificationdrawer;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import android.service.notification.StatusBarNotification;
import com.wix.reactnativenotifications.core.AppLaunchHelper;
import com.wix.reactnativenotifications.core.InitialNotificationHolder;

public class PushNotificationsDrawer implements IPushNotificationsDrawer {

    final protected Context mContext;
    final protected AppLaunchHelper mAppLaunchHelper;

    public static IPushNotificationsDrawer get(Context context) {
        return PushNotificationsDrawer.get(context, new AppLaunchHelper());
    }

    public static IPushNotificationsDrawer get(Context context, AppLaunchHelper appLaunchHelper) {
        final Context appContext = context.getApplicationContext();
        if (appContext instanceof INotificationsDrawerApplication) {
            return ((INotificationsDrawerApplication) appContext).getPushNotificationsDrawer(context, appLaunchHelper);
        }

        return new PushNotificationsDrawer(context, appLaunchHelper);
    }

    protected PushNotificationsDrawer(Context context, AppLaunchHelper appLaunchHelper) {
        mContext = context;
        mAppLaunchHelper = appLaunchHelper;
    }

    @Override
    public void onAppInit() {
        clearAll();
    }

    @Override
    public void onAppVisible() {
        clearAll();
    }

    @Override
    public void onNewActivity(Activity activity) {
        boolean launchIntentsActivity = mAppLaunchHelper.isLaunchIntentsActivity(activity);
        boolean launchIntentOfNotification = mAppLaunchHelper.isLaunchIntentOfNotification(activity.getIntent());
        if (launchIntentsActivity && !launchIntentOfNotification) {
            InitialNotificationHolder.getInstance().clear();
        }
    }

    @Override
    public void onNotificationOpened() {
        clearAll();
    }

    @Override
    public void onNotificationClearRequest(int id) {
        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    protected void clearAll() {
        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        	StatusBarNotification[] notifs = notificationManager.getActiveNotifications();

            	if(notifs.length > 0){
                	for (StatusBarNotification notif : notifs){
                    		//Close all notifications except MusicControl
				if (notif.getTag() != "MusicControl") {
                        		notificationManager.cancel(notif.getId());
                    		}
                	}
            	}
        } else {
            notificationManager.cancelAll();
        }


    }
}
