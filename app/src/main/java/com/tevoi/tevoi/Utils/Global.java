package com.tevoi.tevoi.Utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tevoi.tevoi.R;
import com.tevoi.tevoi.rest.ApiClient;
import com.tevoi.tevoi.rest.ApiClientDnn;
import com.tevoi.tevoi.rest.ApiInterface;
import com.tevoi.tevoi.rest.ApiInterfaceDnn;

public class Global {
    //public static final String BASE_URL = "http://192.168.1.3/TevoiAPI/";
    //public static  final  String BASE_AUDIO_URL = "http://192.168.1.3/TevoiAPI/api/Services/GetStreamAudio?id=";

    public static final String IMAGE_BASE_URL = "http://h2817272.stratoserver.net/TevoiTest/";

    public static final String BASE_URL = "http://h2817272.stratoserver.net/TevoiAPI/";

    public static  final  String BASE_AUDIO_URL = "http://h2817272.stratoserver.net/TevoiAPI/api/Services/GetStreamAudio?id=";

    //public static final String BASE_URL = "http://h2817272.stratoserver.net/TevoiAPI/";

    //public static  final  String BASE_AUDIO_URL = "http://h2817272.stratoserver.net/TevoiAPI/api/Services/GetStreamAudio?id=";

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
    //public  static  final  String LOGINURL ="http://h2817272.stratoserver.net/Tevoi/DesktopModules/TevoiAPIModuleFolder/";
    public  static  final  String LOGINURL ="http://h2817272.stratoserver.net/TevoiTest/DesktopModules/TevoiAPIModuleFolder/";


    public final static ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
    public final static ApiInterfaceDnn clientDnn = ApiClientDnn.getClientDnn().create(ApiInterfaceDnn.class);

    public static  final int ListenUnitInSeconds = 60;

    public static String UserToken = "";
    public static final String DefaultUILanguage = "en";
    public static final String DefaultTrackLanguage = "en-US";
    public static final String Licence = "";
    public static int CurrentUserId =0;

    public static  int PAGE_SIZE = 6;

    // region media player constants

    public interface ACTION
    {
        public static String MAIN_ACTION = "com.tevoi.tevoi.action.main";
        public static String INIT_ACTION = "com.tevoi.tevoi.action.init";
        public static String PREV_ACTION = "com.tevoi.tevoi.action.prev";
        public static String PLAY_ACTION = "com.tevoi.tevoi.action.play";
        public static String NEXT_ACTION = "com.tevoi.tevoi.action.next";
        public static String STARTFOREGROUND_ACTION = "com.tevoi.tevoi.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.tevoi.tevoi.action.stopforeground";

    }
    public interface NOTIFICATION_ID
    {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_album_art, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

    // endregion


    // region Language Enumerations

    public static int Arabic = 2;
    public static int English = 3;

    // endregion

}
