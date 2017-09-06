package com.yuyi.lib.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.yuyi.lib.bean.FileBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author huanghua
 */

public class FileUtil {

    public static boolean isExits(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    public static void queryFiles(Context context, String[] end, Consumer<List<FileBean>> consumer) {
        Observable.just(end)
                .map(s -> {
                    if (s == null) {
                        return null;
                    }
                    StringBuffer ss = new StringBuffer();
                    for (String value : s) {
                        ss.append(TextUtils.isEmpty(ss) ? "" : " or ");
                        ss.append(MediaStore.Files.FileColumns.DATA + " like '%.").append(value).append("'");
                    }
                    return ss.toString();
                })
                .map(ss -> {
                    List<FileBean> list = new ArrayList<>();
                    if (TextUtils.isEmpty(ss)) {
                        return list;
                    }
                    String[] projection = new String[]{
                            MediaStore.Files.FileColumns._ID,
                            MediaStore.Files.FileColumns.DATA,
                            MediaStore.Files.FileColumns.SIZE
                    };

                    Cursor cursor = context.getContentResolver().query(
                            Uri.parse("content://media/external/file"),
                            projection,
                            ss, null,
                            null);

                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            int idindex = cursor
                                    .getColumnIndex(MediaStore.Files.FileColumns._ID);
                            int dataindex = cursor
                                    .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                            int sizeindex = cursor
                                    .getColumnIndex(MediaStore.Files.FileColumns.SIZE);

                            do {
                                FileBean bean = new FileBean();
                                String id = cursor.getString(idindex);
                                String path = cursor.getString(dataindex);
                                String size = cursor.getString(sizeindex);
                                bean.setId(id);
                                bean.setPath(path);
                                bean.setSize(size);
                                int dot = path.lastIndexOf("/");
                                String name = path.substring(dot + 1);
                                bean.setName(name);
                                list.add(bean);
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(consumer);
    }
}
