package com.yuyi.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {


    public static void setInt(Context context, String name, int value) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Editor editor = config.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static void setLong(Context context, String name, long value) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Editor editor = config.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static void setString(Context context, String name, String value) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Editor editor = config.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static void setBoolean(Context context, String name, boolean value) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Editor editor = config.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static int getInt(Context context, String name) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getInt(name, 0);
    }

    public static int getInt(Context context, String name, int defvalue) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getInt(name, defvalue);
    }

    public static long getLong(Context context, String name) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getLong(name, 0);
    }

    public static long getLong(Context context, String name, long defValue) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getLong(name, defValue);
    }

    public static String getString(Context context, String name) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getString(name, "");
    }

    public static String getString(Context context, String name, String defValue) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getString(name, defValue);
    }

    public static Boolean getBoolean(Context context, String name) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getBoolean(name, false);
    }

    public static Boolean getBoolean(Context context, String name, boolean def) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return config.getBoolean(name, def);
    }

    public static void remove(Context context, String name) {
        SharedPreferences config = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        Editor editor = config.edit();
        editor.remove(name);
        editor.apply();
    }

}
