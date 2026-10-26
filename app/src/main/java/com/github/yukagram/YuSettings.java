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

    // Defaults

    public static final boolean DEFAULT_HIDE_CONNECTION_STATUS_BAR = true;

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
}
