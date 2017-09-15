package com.myapp.waveform.trial1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;

import com.myapp.waveform.trial1.Services.*;

/**
 * Created by user on 9/5/17.
 */

public class MediaNotificationHelper {

    public static final String ACTION_PLAY = "com.myapp.waveform.trial1.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.myapp.waveform.trial1.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.myapp.waveform.trial1.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.myapp.waveform.trial1.ACTION_NEXT";
    public static final String ACTION_STOP = "com.myapp.waveform.trial1.ACTION_STOP";
    private static final int NOTIFICATION_ID = 101;

    public static void buildNotification(PlaybackStatus playbackStatus, Context context, MediaSessionCompat mediaSession){
        int notificationAction = android.R.drawable.ic_media_pause;

        PendingIntent play_pauseAction = null;
        MediaControllerCompat controller = mediaSession.getController();
        MediaMetadataCompat mMetadata = controller.getMetadata();
        //PlaybackStateCompat mPlaybackState = controller.getPlaybackState();

        if(mMetadata == null || playbackStatus == null){
            return;
        }

        if(playbackStatus == PlaybackStatus.PLAYING){
            notificationAction = android.R.drawable.ic_media_pause;
            play_pauseAction = playbackAction(1, context);
        }
        else if(playbackStatus == PlaybackStatus.PAUSED){
            notificationAction = android.R.drawable.ic_media_play;
            play_pauseAction = playbackAction(0, context);
        }
        Bitmap art = mMetadata.getDescription().getIconBitmap();
        if(art == null){
            //TODO: change to proper resource
            art = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_repeat_grey_25dp);
        }

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setShowWhen(false)
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0,1,2))
                .setColor(context.getResources().getColor(R.color.cardview_light_background))
                .setLargeIcon(art)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .setContentText(mMetadata.getDescription().getSubtitle().toString())
                .setContentTitle(mMetadata.getDescription().getTitle().toString())
                .setContentInfo(mMetadata.getDescription().getDescription().toString())
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3, context))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2, context));
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID,notificationBuilder.build());
    }

    private static PendingIntent playbackAction(int actionNumber, Context context) {
        Intent playbackAction = new Intent(context, com.myapp.waveform.trial1.Services.MusicService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            case 2:
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(context, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }
}
