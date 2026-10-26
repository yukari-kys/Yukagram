package com.github.yukagram;

import android.os.SystemClock;
import androidx.annotation.Nullable;

import org.thunderdog.challegram.util.DeviceStorageError;
import org.thunderdog.challegram.Log;
import org.thunderdog.challegram.unsorted.AppContext;
import me.vkryl.leveldb.LevelDB;
import me.vkryl.core.FileUtils;
import org.drinkmore.Tracer;

import java.io.File;
import java.io.FileNotFoundException;

public class YuSettings {

    public static final int VERSION = 1;

    // Keys

    public static final String KEY_VERSION = "version";
    public static final String KEY_HIDE_CONNECTION_STATUS_BAR = "hide_connection_status_bar";
    public static final String KEY_HIDE_CAMERA_BUTTON = "hide_camera_button";
    public static final String KEY_MOVE_ARCHIVE_TO_SIDEBAR = "move_archive_to_sidebar";
    public static final String KEY_MOVE_CALLS_TO_SIDEBAR = "move_calls_to_sidebar";
    public static final String KEY_HIDE_PHONE_NUMBERS = "hide_phone_numbers";
    public static final String KEY_HIDE_SIDEBAR_BUTTON_INVITE_FRIENDS = "hide_sidebar_button_invite_friends";
    public static final String KEY_HIDE_SIDEBAR_BUTTON_NIGHT_MODE = "hide_sidebar_button_night_mode";
    public static final String KEY_HIDE_SIDEBAR_BUTTON_HELP = "hide_sidebar_button_help";

    // Defaults

    public static final boolean DEFAULT_HIDE_CONNECTION_STATUS_BAR = true;
    public static final boolean DEFAULT_HIDE_CAMERA_BUTTON = true;
    public static final boolean DEFAULT_MOVE_ARCHIVE_TO_SIDEBAR = true;
    public static final boolean DEFAULT_MOVE_CALLS_TO_SIDEBAR = true;
    public static final boolean DEFAULT_HIDE_PHONE_NUMBERS = true;
    public static final boolean DEFAULT_HIDE_SIDEBAR_BUTTON_INVITE_FRIENDS = true;
    public static final boolean DEFAULT_HIDE_SIDEBAR_BUTTON_NIGHT_MODE = true;
    public static final boolean DEFAULT_HIDE_SIDEBAR_BUTTON_HELP = true;

    private static YuSettings instance;

    public static YuSettings getInstance() {
        return instance;
    }

    private LevelDB leveldb;

    private class YuLeveldbErrorHandler implements LevelDB.ErrorHandler {
        @Override
        public boolean onFatalError (LevelDB levelDB, Throwable error) {
            Tracer.onDatabaseError(error);
            return true;
        }

        @Override
        public void onError (LevelDB levelDB, String message, @Nullable Throwable error) {
            // Cannot use custom Log, since settings are not yet loaded
            android.util.Log.e(Log.LOG_TAG, message, error);
        }
    }

    public YuSettings() {
        instance = this;

        File pmcDir = new File(AppContext.get().getFilesDir(), "yupmc");
        boolean didNotExist = !pmcDir.exists();

        boolean fatalError;
        try {
            fatalError = !FileUtils.createDirectory(pmcDir);
        } catch (SecurityException e) {
            e.printStackTrace();
            fatalError = true;
        }

        if (fatalError) {
            throw new DeviceStorageError("Unable to create YuKaGrAmDb (Yukagram Kitty Addicted Grayish Absorbed Mini Data Base) working directory");
        }

        long ms = SystemClock.uptimeMillis();
        leveldb = new LevelDB(new File(pmcDir, "db").getPath(), true, new YuLeveldbErrorHandler());

        int pmcVersion;
        if (didNotExist) {
            pmcVersion = VERSION;
            leveldb.putInt(KEY_VERSION, VERSION);
        } else {
            pmcVersion = 0;
            try {
                pmcVersion = Math.max(0, leveldb.tryGetInt(KEY_VERSION));
            } catch (FileNotFoundException e) {
                pmcVersion = VERSION;
                leveldb.putInt(KEY_VERSION, pmcVersion);
            }
        }

        Log.i("Opened database in %dms", SystemClock.uptimeMillis() - ms);

        Log.i("⠀⠀⠀⠀⢀⣀⣠⣤⣤⣤⣀⣀⡀⠀⠀⠀⣀⣤⡀⠀⠀⠀⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        Log.i("⠀⢀⣴⣾⡿⠟⠛⠋⠉⠛⠛⠛⠿⢿⣶⣾⡿⢛⣿⣷⣶⣿⠿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        Log.i("⢀⣾⡿⠁⣀⣠⣤⢤⡄⠀⠀⠀⠀⠀⠀⠀⣠⣾⣿⣷⠙⠃⢰⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
        Log.i("⢼⣿⣱⣿⣿⠟⢡⣿⡇⠀⠀⣿⡇⣶⣶⡿⠟⣫⣿⣿⡄⠀⢸⣿⣶⣶⣶⣦⣀⣴⣶⣶⣦⣄⣤⣶⡶⣶⣄⠀⣀⣤⣶⣿⡿⣶⣤⣄⣠⣤⣀⠀");
        Log.i("⠀⠙⣿⣿⠃⢀⣿⣿⡇⠀⠀⣿⡇⣿⡇⠀⠸⣿⣿⣀⡀⠀⢸⡿⣛⠁⠈⢻⣿⢟⠁⠀⢙⣿⣿⡋⠀⠈⣻⣿⡿⠟⠋⣅⠀⠀⠉⠛⠿⠛⢹⣇");
        Log.i("⠀⢰⣿⡇⠀⢸⣿⣿⡇⠀⠀⣿⡇⣿⡇⠀⠀⠈⣿⣿⠀⠀⢨⣾⣿⡇⠀⠀⣿⣿⠀⠀⢸⣿⣿⡇⠀⠀⣿⣿⡇⠀⠀⣿⣿⣿⠂⠀⢹⣶⡾⠟");
        Log.i("⠀⢸⣿⠀⠀⠘⣿⣿⡇⠀⢠⣿⡇⣿⡇⢀⣤⣤⣿⣿⠀⠀⢸⣿⣿⡇⠀⠀⣿⣿⠀⠀⢸⣿⣿⡇⠀⠀⣿⣿⡇⠀⠀⣿⣿⣿⠀⠀⢸⡏⠀⠀");
        Log.i("⠀⠸⣿⣆⠀⠀⠹⣿⠃⣠⣿⣿⡇⣿⣧⣾⡿⣹⣿⣿⠀⠀⢸⣿⣿⡇⠀⠀⣿⣿⠀⠀⢸⣿⣿⡇⠀⠀⣿⣿⡇⠀⠀⣿⣿⣿⠀⠀⢸⡇⠀⠀");
        Log.i("⠀⠀⢻⣿⣄⠀⠀⠀⠘⠿⣿⣿⡇⣿⠟⠉⣰⣿⣿⣿⠀⠀⢸⣿⣿⡇⠀⠀⣿⣿⡄⠀⠸⣿⢟⣃⠀⠀⣿⣿⣇⠀⠀⠻⣿⢟⡁⠀⢸⣧⠀⠀");
        Log.i("⠀⠀⠀⠙⢿⣷⣤⣀⠀⠀⠀⠀⠀⢀⣠⣾⡿⠋⣿⣧⣄⢀⣴⣿⣿⠇⠀⣼⡿⣿⣷⡀⣠⣼⣿⣿⣄⢀⣴⣿⣿⣷⣄⢀⣴⣿⣧⡀⠀⢻⣷⠀");
        Log.i("⠀⠀⠀⠀⠀⠉⠛⠿⢿⣿⣿⣿⣿⣿⣟⣩⣤⣤⣬⣿⣿⣿⣿⢿⣋⣴⣾⣟⡁⠘⠻⠿⠟⢋⣈⣿⠿⠿⢋⣼⣿⡿⠁⠛⠛⠛⠛⠷⠄⢀⣽⡇");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⢠⣾⡟⠋⠉⠉⠙⠛⠟⣫⡿⠛⣯⣿⣿⣾⡿⠟⣿⡿⠿⢿⡄⢀⣤⣾⡿⠿⠿⢿⣦⢸⣿⣯⣠⣤⣤⣤⣤⣄⣠⣴⡿⠋⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⢿⣿⣴⣿⣶⣶⣶⠋⣸⡟⠀⠀⣿⡏⠉⠀⠀⢸⣿⢇⣴⣿⠟⣿⡟⠉⠀⣤⣀⣼⣿⠀⠉⠛⠋⠉⠉⠉⠙⠛⠋⠁⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠀⢸⣿⠃⠀⣿⡇⠀⠀⣿⡇⠀⠀⢀⣼⣿⢿⣿⣧⣴⣿⡇⠀⠀⣿⣿⣿⡁⣀⣤⣾⡿⢿⣶⣄⠀⠀⠀⠀⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣶⣿⣿⠀⠀⣿⡇⠀⠀⣿⡇⠀⠀⢸⣿⡁⠀⠈⣿⣿⣤⡄⠀⠀⣤⣤⣿⣿⡿⠛⢩⡀⠀⠙⢿⣷⡄⠀⠀⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⣿⣤⣄⠀⠀⢹⡇⠀⢀⣿⡇⠀⠀⠘⣿⠁⠀⠀⣿⡏⣿⡇⠀⠀⣿⡟⠉⣿⡇⠀⠀⣿⣦⡀⣠⣿⡇⠀⠀⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⣟⡛⠃⠀⣾⡇⠀⣼⣿⠁⠀⠀⠀⣿⠀⠀⠀⣿⡇⣿⡇⠀⠀⣿⡇⠀⣿⡇⠀⠰⣟⣵⣾⠿⠋⠀⠀⠀⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢿⣿⡆⢠⡿⢁⣼⣿⣷⣦⣄⣀⢀⣿⠀⠀⠀⣿⡇⣿⡇⠀⠀⣿⣧⢀⣿⡇⠀⠀⣿⡟⣁⣀⣀⠀⠀⠀⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⡿⢠⠟⠒⠋⠉⠉⠉⠉⠻⠿⢿⣿⠀⠀⠀⠿⣿⣿⠁⠀⠀⠛⠿⢟⣿⠇⠀⠀⠻⠿⠿⣛⣿⠁⠀⠀⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⡿⣋⣤⣴⣶⣶⣶⣶⣦⣄⡀⢀⣴⣿⣿⣷⣄⣠⣾⡿⠿⣿⣶⣤⣀⣴⣿⢿⣶⣦⣄⣀⣴⣾⡿⠋⠀⠀⠀⠀⠀⠀");
        Log.i("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠛⠻⠿⠛⠉⠉⠀⠀⠉⠙⠻⠿⠿⠟⠁⠈⠛⠛⠛⠋⠀⠀⠀⠉⠛⠛⠛⠁⠀⠉⠙⠛⠛⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀");
    }

    public boolean isConnectionStatusBarHidden() {
        return leveldb.getBoolean(KEY_HIDE_CONNECTION_STATUS_BAR, DEFAULT_HIDE_CONNECTION_STATUS_BAR);
    }

    public void setConnectionStatusBarHidden(boolean value) {
        leveldb.putBoolean(KEY_HIDE_CONNECTION_STATUS_BAR, value);
    }

    public boolean isHideCameraButton() {
        return leveldb.getBoolean(KEY_HIDE_CAMERA_BUTTON, DEFAULT_HIDE_CAMERA_BUTTON);
    }

    public void setHideCameraButton(boolean value) {
        leveldb.putBoolean(KEY_HIDE_CAMERA_BUTTON, value);
    }

    public boolean isMoveArchiveToSidebar() {
        return leveldb.getBoolean(KEY_MOVE_ARCHIVE_TO_SIDEBAR, DEFAULT_MOVE_ARCHIVE_TO_SIDEBAR);
    }

    public void setMoveArchiveToSidebar(boolean value) {
        leveldb.putBoolean(KEY_MOVE_ARCHIVE_TO_SIDEBAR, value);
    }

    public boolean isMoveCallsToSidebar() {
        return leveldb.getBoolean(KEY_MOVE_CALLS_TO_SIDEBAR, DEFAULT_MOVE_CALLS_TO_SIDEBAR);
    }

    public void setMoveCallsToSidebar(boolean value) {
        leveldb.putBoolean(KEY_MOVE_CALLS_TO_SIDEBAR, value);
    }

    public boolean isHidePhoneNumbers() {
        return leveldb.getBoolean(KEY_HIDE_PHONE_NUMBERS, DEFAULT_HIDE_PHONE_NUMBERS);
    }

    public void setHidePhoneNumbers(boolean value) {
        leveldb.putBoolean(KEY_HIDE_PHONE_NUMBERS, value);
    }

    public boolean isHideSidebarButtonInviteFriends() {
        return leveldb.getBoolean(KEY_HIDE_SIDEBAR_BUTTON_INVITE_FRIENDS, DEFAULT_HIDE_SIDEBAR_BUTTON_INVITE_FRIENDS);
    }

    public void setHideSidebarButtonInviteFriends(boolean value) {
        leveldb.putBoolean(KEY_HIDE_SIDEBAR_BUTTON_INVITE_FRIENDS, value);
    }

    public boolean isHideSidebarButtonNightMode() {
        return leveldb.getBoolean(KEY_HIDE_SIDEBAR_BUTTON_NIGHT_MODE, DEFAULT_HIDE_SIDEBAR_BUTTON_NIGHT_MODE);
    }

    public void setHideSidebarButtonNightMode(boolean value) {
        leveldb.putBoolean(KEY_HIDE_SIDEBAR_BUTTON_NIGHT_MODE, value);
    }

    public boolean isHideSidebarButtonHelp() {
        return leveldb.getBoolean(KEY_HIDE_SIDEBAR_BUTTON_HELP, DEFAULT_HIDE_SIDEBAR_BUTTON_HELP);
    }

    public void setHideSidebarButtonHelp(boolean value) {
        leveldb.putBoolean(KEY_HIDE_SIDEBAR_BUTTON_HELP, value);
    }
}
