package com.ebridge.tevoi.Utils;


import com.ebridge.tevoi.rest.ApiClient;
import com.ebridge.tevoi.rest.ApiInterface;

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
    //public static final String BASE_URL = "http://192.168.1.100/TevoiAPI/";

    //public static  final  String BASE_AUDIO_URL = "http://192.168.1.100/TevoiAPI/api/Services/GetStreamAudio?id=";

    //public static final String BASE_URL = "http://h2817272.stratoserver.net/TevoiAPI/";

   //public static  final  String BASE_AUDIO_URL = "http://h2817272.stratoserver.net/TevoiAPI/api/Services/GetStreamAudio?id=";

    public static final String BASE_URL = "http://192.168.1.114/TevoiAPIEmulator/";

    public static  final  String BASE_AUDIO_URL = "http://192.168.1.114/TevoiAPIEmulator/api/Services/GetStreamAudio?id=";

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

    public final static ApiInterface client = ApiClient.getClient().create(ApiInterface.class);

    public static  final int ListenUnitInSeconds = 60;

    public static final String UserToken = "";
    public static final String Language = "en";
    public static final String Licence = "";



}
