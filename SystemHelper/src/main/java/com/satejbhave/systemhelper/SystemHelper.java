package com.satejbhave.systemhelper;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemHelper {

    public static class Dialog{

        Context context;

        public Dialog(Context context) {
            this.context = context;
        }

        public void showDialog(String title , int drawableIconID , String message , String positiveBtnText, DialogInterface.OnClickListener onPositiveButtonClickListener , String negativeBtnText, DialogInterface.OnClickListener onNegativeButtonClickListener ){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setIcon(drawableIconID);
            builder.setMessage(message).setCancelable(true);
            if (positiveBtnText== null || onPositiveButtonClickListener == null){

            }else{
                builder.setPositiveButton(positiveBtnText, onPositiveButtonClickListener);
            }

            if (negativeBtnText == null || onNegativeButtonClickListener == null){

            }else{
                builder.setNegativeButton(negativeBtnText, onNegativeButtonClickListener);
            }

            AlertDialog alert = builder.create();
            alert.show();
        }
        public void showNonCancellableDialog(String title , int drawableIconID , String message , String positiveBtnText,  DialogInterface.OnClickListener onPositiveButtonClickListener , String negativeBtnText, DialogInterface.OnClickListener onNegativeButtonClickListener ){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setIcon(drawableIconID);
            builder.setMessage(message).setCancelable(false);
            if (positiveBtnText== null || onPositiveButtonClickListener == null){

            }else{
                builder.setPositiveButton(positiveBtnText, onPositiveButtonClickListener);
            }

            if (negativeBtnText == null || onNegativeButtonClickListener == null){

            }else{
                builder.setNegativeButton(negativeBtnText, onNegativeButtonClickListener);
            }

            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    public static class CopyPaste{

        Context context ;
        ClipboardManager clipboardManager;

        public CopyPaste(Context context) {
            this.context = context;
            clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        }

        public void copy(Object dataToBeCopied ){
            String text;
            text = dataToBeCopied.toString();

            ClipData myClip = ClipData.newPlainText("Copied Text", text);
            clipboardManager.setPrimaryClip(myClip);
        }
        public String paste(){
            ClipData abc = clipboardManager.getPrimaryClip();
            ClipData.Item item = abc.getItemAt(0);
            String text = item.getText().toString();
            return text;
        }



    }

    public static class PermissionManager{

        public static final int PERMISSION_REQUEST_CODE = 30;

        Context context;

        public PermissionManager(Context context){
            this.context = context;
        }

        //You have to give permission string in functions
        //Eg. Manifest.permission.INTERNET


        public boolean isPermissionGranted( String permissionString ){

            //Permission Not Granted
            //You need to request these permissions
            //permission Granted
            return ContextCompat.checkSelfPermission(context, permissionString) == PackageManager.PERMISSION_GRANTED;
        }

        public boolean isPermissionGranted(String[] permissionStrings){

            for (String permissionString : permissionStrings) {
                if (ContextCompat.checkSelfPermission(context, permissionString) != PackageManager.PERMISSION_GRANTED) {
                    //Some Permissions Are Not Granted
                    return false;
                }
            }
            //All Permission Are Granted
            return true;
        }

        public void requestPermission(Activity activity , String permissionString){

            ActivityCompat.requestPermissions(activity, new String[]{permissionString} , PERMISSION_REQUEST_CODE  );
            //Permission Is Requested Here.
            // You Can Also Enter PERMISSION_REQUEST_CODE In onRequestPermissionsResult() Method In Activity To Get Permission Status
        }

        public void requestPermission(Activity activity , String[] permissionStrings){

            ActivityCompat.requestPermissions(activity, permissionStrings , PERMISSION_REQUEST_CODE  );
            //Permission Is Requested Here.
            // You Can Also Enter PERMISSION_REQUEST_CODE In onRequestPermissionsResult() Method In Activity To Get Permission Status
        }


    }

    public static class ApplicationManager{

        Context context;

        public ApplicationManager(Context context) {
            this.context = context;
        }
        public List<Application> getInstalledAppList(){

            List<Application > applicationList = new ArrayList<>();

            PackageManager pm = context.getPackageManager();
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);


            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);

                String appName = p.applicationInfo.loadLabel(pm).toString();
                Drawable icon = p.applicationInfo.loadIcon(pm);
                String packages = p.applicationInfo.packageName;

                Application application = new Application(packages , appName , icon);
                application.setPackageInfo(p);

                if (p.requestedPermissions == null ){
                    List<String> permissionList = new ArrayList<>();
                    application.setPermissionList( permissionList );

                }else{
                    List<String> permissionList = Arrays.asList(p.requestedPermissions);
                    application.setPermissionList( permissionList );

                }

                long lastUpdatedTime = p.lastUpdateTime ;
                application.setLastTimeUpdated( lastUpdatedTime );

                long firstInstalledTime = p.lastUpdateTime ;
                application.setFirstTimeInstall( firstInstalledTime );

                int appVersionCode = p.versionCode;
                String appVersionName = p.versionName;

                application.setVersionCode(appVersionCode , appVersionName);


                applicationList.add(application);


            }

            return applicationList;

        }
        public List<Application> getUserInstalledAppList() throws PackageManager.NameNotFoundException {

            List<Application > applicationList = new ArrayList<>();

            PackageManager pm = context.getPackageManager();
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);


            for (int i = 0; i < packs.size(); i++) {
                PackageInfo p = packs.get(i);
                ApplicationInfo ai = pm.getApplicationInfo(p.packageName, 0);

                if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

                }else {


                    String appName = p.applicationInfo.loadLabel(pm).toString();
                    Drawable icon = p.applicationInfo.loadIcon(pm);
                    String packages = p.applicationInfo.packageName;

                    Application application = new Application(packages, appName, icon);
                    application.setPackageInfo(p);

                    if (p.requestedPermissions == null) {
                        List<String> permissionList = new ArrayList<>();
                        application.setPermissionList(permissionList);

                    } else {
                        List<String> permissionList = Arrays.asList(p.requestedPermissions);
                        application.setPermissionList(permissionList);

                    }

                    long lastUpdatedTime = p.lastUpdateTime;
                    application.setLastTimeUpdated(lastUpdatedTime);

                    long firstInstalledTime = p.lastUpdateTime;
                    application.setFirstTimeInstall(firstInstalledTime);

                    int appVersionCode = p.versionCode;
                    String appVersionName = p.versionName;

                    application.setVersionCode(appVersionCode, appVersionName);


                    applicationList.add(application);

                }
            }

            return applicationList;

        }
        public boolean isAppInstalled(String packageName){
            List<Application> applications = getInstalledAppList();
            for (Application application : applications ){
                if (application.packageName.contains(packageName)){
                    return true;
                }

            }
            return false;

        }
        public void openApp(String packageName){
            if (isAppInstalled(packageName)){
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(intent);
            }
        }
        public static class Application{
            public String packageName , title ;
            public Drawable icon;
            public List<String> permissionList;
            public long lastTimeUpdated ;
            public long firstTimeInstall ;
            public int versionCode ;
            public String versionName;
            public PackageInfo packageInfo;





            public Application(String packageName, String title, Drawable icon) {
                this.packageName = packageName;
                this.title = title;
                this.icon = icon;
            }

            public void setPermissionList(List<String> permissionList) {
                this.permissionList = permissionList;
            }

            public void setLastTimeUpdated(long lastTimeUpdated) {
                this.lastTimeUpdated = lastTimeUpdated;
            }

            public void setFirstTimeInstall(long firstTimeInstall) {
                this.firstTimeInstall = firstTimeInstall;
            }

            public void setVersionCode(int versionCode , String versionName) {
                this.versionCode = versionCode;
                this.versionName = versionName;

            }

            public void setPackageInfo(PackageInfo packageInfo) {
                this.packageInfo = packageInfo;
            }
        }


    }

    public static class SnackBarClass {


        View view;

        public SnackBarClass(View view) {
            this.view = view;
        }
        public SnackBarClass(View view , String message ) {
            this.view = view;
            Snackbar mySnackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT);
            mySnackbar.setTextColor(Color.WHITE);
            mySnackbar.show();
        }
        public SnackBarClass(View view , String message, String btnText , View.OnClickListener onClickListener ) {
            this.view = view;
            Snackbar mySnackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT);
            mySnackbar.setTextColor(Color.WHITE);
            mySnackbar.setActionTextColor(Color.YELLOW);
            mySnackbar.setAction(btnText ,onClickListener);
            mySnackbar.show();
        }


        public void setMessage(String message){
            Snackbar mySnackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT);
            mySnackbar.setTextColor(Color.WHITE);
            mySnackbar.show();

        }
        public void setMessage(String message , String btnText , View.OnClickListener onClickListener){

            Snackbar mySnackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_SHORT);
            mySnackbar.setTextColor(Color.WHITE);
            mySnackbar.setActionTextColor(Color.YELLOW);
            mySnackbar.setAction(btnText ,onClickListener);
            mySnackbar.show();

        }
    }

    public static class NotificationHelper {


        Context context;
        String CHANNEL_ID = "SatejBhaveNotification";
        int REQUEST_CODE = 124;


        public NotificationHelper(Context context) {
            this.context = context;
        }

        public void createNotification(String title , String text , @NonNull int NotificationID  , int smallIconId , Intent onNotificationClickIntent){
            //create Notification
            Intent intent = onNotificationClickIntent;
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(smallIconId)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Name";
                String description = "Discription";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                channel.enableVibration(true);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(NotificationID, builder.build());

        }
        public void createNotification(String title , String text , @NonNull int NotificationID , int smallIconId , PendingIntent pendingIntent ){
            //create Notification

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(smallIconId)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Name";
                String description = "Discription";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                channel.enableVibration(true);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(NotificationID, builder.build());

        }

    }

}
