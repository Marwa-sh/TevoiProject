package com.tevoi.tevoi.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.SideMenu;
import com.tevoi.tevoi.model.TrackSerializableObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileHelper
{
    final static String  TAG = "Download";
    boolean isCurrentlyDowloading;
    SideMenu activity;

    public FileHelper(SideMenu activity)
    {
        this.activity = activity;
    }

    public static void downloadAudioFile(final SideMenu activity, final TrackSerializableObject track)
    {
       /* String path = activity.getFilesDir().getAbsolutePath() +
                Global.PlayNowListDirectory + File.separator + track.getId() + ".mp3";

        String v = Global.IMAGE_BASE_URL + "/Portals/0/Tevoi_Files/Audio/Track/Ar/1016.mp3" ;
        downloadByDownloadManager(activity,v, path );
        activity.AddListenTrack(track.getId());
        String result = activity.storageManager.addTrack(activity, track);*/

        Call<ResponseBody> call = Global.client.downloadFileByUrl(track.getId());
        Log.d(TAG, "marwa" + "Got the body for the file");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    Log.d(TAG, "Got the body for the file");

                    new AsyncTask<Void, Long, Void>()
                    {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            saveToDisk(response.body(),track ,activity);
                            return null;
                        }
                        /*@Override
                        protected void onProgressUpdate(Integer... progress) {

                            notificationBuilder.setContentText(""+intProgress+"%");
                            notificationBuilder.setProgress(100, intProgress,false);
                            notificationManager.notify(id, notificationBuilder.build());

                        }*/
                    }.execute();

                } else
                {
                    Log.d(TAG, "Connection failed " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    void showNotification(SideMenu activity, String trackName)
    {
        String NOTIFICATION_CHANNEL_ID = "com.tevoi.tevoi";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder
                .setOngoing(true)
                .setContentTitle(trackName)
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.skype)
                // Hide the timestamp
                .setShowWhen(false)
                .setAutoCancel(true)
                //.setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, notification);
    }

    private static void saveToDisk(ResponseBody body, TrackSerializableObject track, SideMenu activity)
    {
        final NotificationManager mNotifyManager;
        final NotificationCompat.Builder mBuilder;
        final int id = 1;
        mNotifyManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String NOTIFICATION_CHANNEL_ID = "com.tevoi.tevoi";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            chan.setLightColor(Color.BLUE);
            //chan.setSound(alarmSound, null);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            assert mNotifyManager != null;
            mNotifyManager.createNotificationChannel(chan);
        }

        mBuilder = new NotificationCompat.Builder(activity, NOTIFICATION_CHANNEL_ID);
        mBuilder.setContentTitle(track.getName())
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.tevoi_logo);

        String path = activity.getFilesDir().getAbsolutePath() +
                Global.PlayNowListDirectory + File.separator + track.getId() + ".mp3";

        /*FileOutputStream outputStream;
        boolean isDownloadInProgres= true;

        String f =   track.getId() + ".mp3";
        try
        {
            outputStream = activity.openFileOutput(f, Context.MODE_PRIVATE);
            outputStream.write(body.bytes());
            outputStream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        activity.AddListenTrack(track.getId());
        String result = activity.storageManager.addTrack(activity, track);
        mBuilder.setContentText("Download completed")
                // Removes the progress bar
                .setProgress(0,0,false);
        mNotifyManager.notify(id, mBuilder.build());*/
        try
        {
            InputStream is = null;
            OutputStream os = null;
            try
            {
                long filesize = body.contentLength();
                Log.d(TAG, "File Size=" + filesize);
                is = body.byteStream();
                os = new FileOutputStream(new File(path));

                byte data[] = new byte[16384];
                int count;
                int progress = 0;
                while ((count = is.read(data)) != -1)
                {
                    os.write(data, 0, count);
                    progress +=count;
                    // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
                    mBuilder.setProgress((int)filesize, progress, false);
                    // Displays the progress bar for the first time.
                    mNotifyManager.notify(id, mBuilder.build());

                    /*// Sleeps the thread, simulating an operation
                    try {
                        // Sleep for 1 second
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        Log.d("TAG", "sleep failure");
                    }*/

                    Log.d(TAG, "Progress: " + progress + "/" + filesize + " >>>> " + (float) progress/filesize);
                }

                os.flush();
                os.close();

                //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //mBuilder.setSound(alarmSound);
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                // When the loop is finished, updates the notification
                mBuilder.setContentText("Download completed")
                        // Removes the progress bar
                        .setProgress(0,0,false);
                        //
                //.setOnlyAlertOnce(true);
                mNotifyManager.notify(id, mBuilder.build());

                Log.d(TAG, "File saved successfully!");

                activity.AddListenTrack(track.getId());
                String result = activity.storageManager.addTrack(activity, track);
                return;
            } catch (IOException e)
            {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                // When the loop is finished, updates the notification
                mBuilder.setContentText("Download Failed")
                        // Removes the progress bar
                        .setProgress(0,0,false);
                mNotifyManager.notify(id, mBuilder.build());
                // TODO : delete the file from play now list
                activity.deleteTrackFromPlayNowList(track.getId());


                return;
            }
            finally
            {
                if (is != null) is.close();
                if (os != null) os.close();
                Log.d("Hi", "Marwaaaaaa");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }

    /*private static void saveToDisk(ResponseBody body, TrackSerializableObject track, SideMenu activity)
    {
        final NotificationManager mNotifyManager;
        final NotificationCompat.Builder mBuilder;
        final int id = 1;
        mNotifyManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "com.tevoi.tevoi";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            assert mNotifyManager != null;
            mNotifyManager.createNotificationChannel(chan);
        }

        mBuilder = new NotificationCompat.Builder(activity, NOTIFICATION_CHANNEL_ID);
        mBuilder.setContentTitle(track.getName())
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.tevoi_logo);


        // Start a the operation in a background thread
        *//*new Thread(
                new Runnable()
                {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5)
                        {
                            // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(id, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            try {
                                // Sleep for 1 second
                                Thread.sleep(1*1000);
                            } catch (InterruptedException e) {
                                Log.d("TAG", "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download completed")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotifyManager.notify(id, mBuilder.build());
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();*//*
        String path = activity.getFilesDir().getAbsolutePath() +
                Global.PlayNowListDirectory + File.separator + track.getId() + ".mp3";

        FileOutputStream outputStream;
        boolean isDownloadInProgres= true;

        String f =   track.getId() + ".mp3";
        try
        {
            outputStream = activity.openFileOutput(f, Context.MODE_PRIVATE);
            outputStream.write(body.bytes());
            outputStream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        activity.AddListenTrack(track.getId());
        String result = activity.storageManager.addTrack(activity, track);
        mBuilder.setContentText("Download completed")
                // Removes the progress bar
                .setProgress(0,0,false);
        mNotifyManager.notify(id, mBuilder.build());
       *//* try
        {
            InputStream is = null;
            OutputStream os = null;
            try
            {
                long filesize = body.contentLength();
                Log.d(TAG, "File Size=" + filesize);
                is = body.byteStream();
                os = new FileOutputStream(new File(path));

                byte data[] = new byte[16384];
                int count;
                int progress = 0;
                while ((count = is.read(data)) != -1)
                {
                    // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
                    mBuilder.setProgress((int)filesize, progress, false);
                    // Displays the progress bar for the first time.
                    mNotifyManager.notify(id, mBuilder.build());
                    // Sleeps the thread, simulating an operation
                    try {
                        // Sleep for 1 second
                        Thread.sleep(1*1000);
                    } catch (InterruptedException e) {
                        Log.d("TAG", "sleep failure");
                    }
                    os.write(data, 0, count);
                    progress +=count;
                    Log.d(TAG, "Progress: " + progress + "/" + filesize + " >>>> " + (float) progress/filesize);
                }

                os.flush();
                os.close();
                // When the loop is finished, updates the notification
                mBuilder.setContentText("Download completed")
                        // Removes the progress bar
                        .setProgress(0,0,false);
                mNotifyManager.notify(id, mBuilder.build());

                Log.d(TAG, "File saved successfully!");

                activity.AddListenTrack(track.getId());
                String result = activity.storageManager.addTrack(activity, track);
                return;
            } catch (IOException e)
            {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                // When the loop is finished, updates the notification
                mBuilder.setContentText("Download Failed")
                        // Removes the progress bar
                        .setProgress(0,0,false);
                mNotifyManager.notify(id, mBuilder.build());
                // TODO : delete the file from play now list
                activity.deleteTrackFromPlayNowList(track.getId());


                return;
            }
            finally
            {
                if (is != null) is.close();
                if (os != null) os.close();
                Log.d("Hi", "Marwaaaaaa");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }*//*
    }*/

    public static long GetNumberOfSeconds(String duration)
    {
        String [] parts = duration.split(":");
        long numOfSeconds = 0;
        try
        {
            numOfSeconds += Integer.parseInt(parts[0]) * 60*60;
            numOfSeconds += Integer.parseInt(parts[1]) * 60;
            numOfSeconds += Integer.parseInt(parts[2]);

            /*DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

            Date d = dateFormat.parse(duration);
            long h = d.getTime();
            numOfSeconds = d.getTime()/1000;*/
        }
        catch(Exception exc)
        {
            numOfSeconds = 0;
        }
        return  numOfSeconds;
    }

    public static boolean deleteFile(String path)
    {
        File file = new File(path);
        return file.delete();
    }
    public static boolean isFileExist(String path)
    {
        return new File(path).exists();
    }
    public static boolean deleteDirectory(String path) {
        return deleteDirectoryImpl(path);
    }
    /**
     * Delete the directory and all sub content.
     *
     * @param path The absolute directory path. For example:
     *             <i>mnt/sdcard/NewFolder/</i>.
     * @return <code>True</code> if the directory was deleted, otherwise return
     * <code>False</code>
     */
    private static boolean deleteDirectoryImpl(String path) {
        File directory = new File(path);

        // If the directory exists then delete
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return true;
            }
            // Run on all sub files and folders and delete them
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectoryImpl(files[i].getAbsolutePath());
                } else {
                    files[i].delete();
                }
            }
        }
        return directory.delete();
    }


   /* public static void downloadByDownloadManager(SideMenu activity,String url, String outputFileName)
    {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("A zip package with some files");
        request.setTitle("Zip package");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outputFileName);

        Log.d("MainActivity: ", "download folder>>>>" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }*/
}
