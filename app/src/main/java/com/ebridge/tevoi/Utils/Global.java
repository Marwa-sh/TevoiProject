package com.ebridge.tevoi.Utils;


import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiClientDnn;
import com.ebridge.tevoi.rest.ApiInterface;
import com.ebridge.tevoi.rest.ApiInterfaceDnn;

import java.net.CookieManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class Global {
    //public static final String BASE_URL = "http://192.168.1.5/TevoiAPIEmulator/";

    //public static  final  String BASE_AUDIO_URL = "http://192.168.1.5/TevoiAPIEmulator/api/Services/GetStreamAudio?id=";
    public static final String BASE_URL = "http://h2817272.stratoserver.net/TevoiAPI/";

    public static  final  String BASE_AUDIO_URL = "http://h2817272.stratoserver.net/TevoiAPI/api/Services/GetStreamAudio?id=";
    //public static final String BASE_URL = "http://192.168.1.109/TevoiAPIEmulator/";

    //public static  final  String BASE_AUDIO_URL = "http://192.168.1.109/TevoiAPIEmulator/api/Services/GetStreamAudio?id=";

    public static final String HistoryFragmentName = "History";
    public static final String FavouriteFragmentName = "Favourite";
    public static final String PlayNowFragmentName = "PlayNow";
    public static final String ListTracksFragmentName = "ListTracks";
    public static final String MediaPlayerFragmentName = "MediaPlayer";
    public  static  final String PartnerNameFragment = "PartnerNameFragment";
    public static  final  String UserListTracksFragment = "UserListTracksFragment";
    public  static  final  String UserListsFragment = "UserListsFragment";
    public  static  final  String PlayNextListFragment = "PlayNextListFragment";
    public  static  final  String CarPlayFragment = "CarPlayFragment";

    public  static  final String GetStreamURL= "http://h2817272.stratoserver.net/TevoiAPI/api/Services/GetStreamAudio?id=";
    public  static  final  String LOGINURL ="http://h2817272.stratoserver.net/Tevoi/DesktopModules/TevoiAPIModuleFolder/";


    public final static ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
    public final static ApiInterfaceDnn clientDnn = ApiClientDnn.getClientDnn().create(ApiInterfaceDnn.class);

    public static  final int ListenUnitInSeconds = 60;

    public static String UserToken = "";
    public static final String DefaultLanguage = "en";
    public static final String Licence = "";


    // region media player constants

    public interface ACTION
    {
        public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
        public static String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
        public static String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
        public static String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
        public static String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";

    }
    public interface NOTIFICATION_ID
    {
        public static int FOREGROUND_SERVICE = 101;
    }

    // endregion
}
