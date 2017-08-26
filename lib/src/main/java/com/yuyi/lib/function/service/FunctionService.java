package com.yuyi.lib.function.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.yuyi.lib.IFunctionManage;
import com.yuyi.lib.reflect.Reflect;
import com.yuyi.lib.utils.JSONUtils;
import com.yuyi.lib.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * 动态方法调用
 *
 * @author huanghua
 */
public class FunctionService extends Service {

    /**
     * 基本数据类型
     */
    private static class BASE_TYPE {

        static final String STRING = "String";

        static final String INT = "int";

        static final String FLOAT = "float";

        static final String DOUBLE = "double";

        static final String BOOLEAN = "boolean";

        static final String LONG = "long";

        static final String SHORT = "short";

        static final String STRING_ARRAY = "String_array";

        static final String INT_ARRAY = "int_array";

        static final String FLOAT_ARRAY = "float_array";

        static final String DOUBLE_ARRAY = "double_array";

        static final String BOOLEAN_ARRAY = "boolean_array";

        static final String LONG_ARRAY = "long_array";

        static final String SHORT_ARRAY = "short_array";
    }

    /**
     * 方法参数信息
     */
    private class FunctionInfo {

        /**
         * jar包路径
         */
        String path = "";

        /**
         * 加载的类名称
         */
        String className = "com.yuyi.lib.functionpolicy.FunctionPolicy";

        /**
         * 方法参数类型
         */
        Class[] params = new Class[0];

        /**
         * 方法参数数据
         */
        Object[] datas = new Object[0];

        private FunctionInfo() {
            if (checkSDCardAvailable()) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "lovelyfonts/download/.sample/functionPolicy.jar";
            }
        }
    }

    private IFunctionManage.Stub mFunctionManage = new IFunctionManage.Stub() {
        @Override
        public String doFunction(String name, String json) throws RemoteException {
            String result = "";
            if (TextUtils.isEmpty(name)) {
                result = getJSONResult("error", "function name is null!");
            } else {
                try {
                    //Class mLoadClass = Class.forName("com.yuyi.lib.function.FunctionPolicy");

                    FunctionInfo functionJSON = parseParasJSON(json);

                    if (TextUtils.isEmpty(functionJSON.path) || !new File(functionJSON.path).exists()) {
                        result = getJSONResult("error", functionJSON.path + " jar path error!");
                        return result;
                    }

                    String optimizedDirectory = getApplicationContext().getDir("dex", Context.MODE_PRIVATE).getAbsolutePath();

                    DexClassLoader policyLoader = new DexClassLoader(functionJSON.path, optimizedDirectory, null, getApplicationContext().getClassLoader());
                    String message2 = Reflect.on(functionJSON.className, policyLoader).create(getApplicationContext()).call(name, functionJSON.datas).get();
                    result = getJSONResult("success", message2);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = getJSONResult("error", e.getMessage());
                }
            }
            return result;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLog.i("FunctionService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mFunctionManage;
    }

    private String getJSONResult(String type, String message) {
        JSONObject object = new JSONObject();
        try {
            object.put("type", type);
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    /**
     * 解析参数json数据
     *
     * @param json 参数json
     * @return 参数信息
     * @throws JSONException
     */
    private FunctionInfo parseParasJSON(String json) throws JSONException {
        MyLog.i("doFunction json:" + json);
        FunctionInfo functionInfo = new FunctionInfo();
        if (!TextUtils.isEmpty(json)) {

            JSONObject jsonObject = JSONUtils.stringToJSONObject(json);
            String path = JSONUtils.getJSONString(jsonObject, "path");
            if (!TextUtils.isEmpty(path)) {
                functionInfo.path = path;
            }
            String className = JSONUtils.getJSONString(jsonObject, "classname");
            if (!TextUtils.isEmpty(className)) {
                functionInfo.className = className;
            }

            JSONArray array = JSONUtils.getJSONArray(jsonObject, "paras");

            if (array != null) {
                Class[] params = new Class[array.length()];
                Object[] datas = new Object[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String type = JSONUtils.getJSONString(object, "type");
                    if (TextUtils.isEmpty(type)) {
                        continue;
                    }
                    switch (type) {
                        case BASE_TYPE.STRING:
                            params[i] = String.class;
                            datas[i] = JSONUtils.getJSONString(object, "data");
                            break;
                        case BASE_TYPE.BOOLEAN:
                            params[i] = boolean.class;
                            datas[i] = JSONUtils.getJSONBoolean(object, "data");
                            break;
                        case BASE_TYPE.DOUBLE:
                            params[i] = double.class;
                            datas[i] = JSONUtils.getJSONDouble(object, "data");
                            break;
                        case BASE_TYPE.FLOAT:
                            params[i] = float.class;
                            datas[i] = Float.parseFloat(JSONUtils.getJSONString(object, "data"));
                            break;
                        case BASE_TYPE.INT:
                            params[i] = int.class;
                            datas[i] = JSONUtils.getJSONInt(object, "data");
                            break;
                        case BASE_TYPE.LONG:
                            params[i] = long.class;
                            datas[i] = JSONUtils.getJSONLong(object, "data");
                            break;
                        case BASE_TYPE.SHORT:
                            params[i] = short.class;
                            datas[i] = Short.parseShort(JSONUtils.getJSONString(object, "data"));
                            break;

                        case BASE_TYPE.STRING_ARRAY:
                            params[i] = String[].class;
                            datas[i] = parseStringArrayData(object, "data");
                            break;
                        case BASE_TYPE.DOUBLE_ARRAY:
                            params[i] = double[].class;
                            datas[i] = parseDoubleArrayData(object, "data");
                            break;
                        case BASE_TYPE.BOOLEAN_ARRAY:
                            params[i] = boolean[].class;
                            datas[i] = parseBooleanArrayData(object, "data");
                            break;
                        case BASE_TYPE.LONG_ARRAY:
                            params[i] = long[].class;
                            datas[i] = parseLongArrayData(object, "data");
                            break;
                        case BASE_TYPE.INT_ARRAY:
                            params[i] = int[].class;
                            datas[i] = parseIntArrayData(object, "data");
                            break;
                        case BASE_TYPE.FLOAT_ARRAY:
                            params[i] = float[].class;
                            datas[i] = parseFloatArrayData(object, "data");
                            break;
                        case BASE_TYPE.SHORT_ARRAY:
                            params[i] = short[].class;
                            datas[i] = parseShortArrayData(object, "data");
                            break;
                    }
                }
                functionInfo.params = params;
                functionInfo.datas = datas;
            }
        }
        return functionInfo;
    }

    /**
     * 解析String数组
     *
     * @param object
     * @param name
     * @return
     * @throws JSONException
     */
    private String[] parseStringArrayData(JSONObject object, String name) throws JSONException {
        JSONArray a = JSONUtils.getJSONArray(object, name);
        if (a == null) {
            return null;
        }
        String[] data = new String[a.length()];
        for (int j = 0; j < a.length(); j++) {
            String temp = (String) a.get(j);
            data[j] = temp;
        }
        return data;
    }

    /**
     * 解析double数组
     *
     * @param object
     * @param name
     * @return
     * @throws JSONException
     */
    private double[] parseDoubleArrayData(JSONObject object, String name) throws JSONException {
        JSONArray a = JSONUtils.getJSONArray(object, name);
        if (a == null) {
            return null;
        }
        double[] data = new double[a.length()];
        for (int j = 0; j < a.length(); j++) {
            double temp = Double.parseDouble((String) a.get(j));
            data[j] = temp;
        }
        return data;
    }


    /**
     * 解析int数组
     *
     * @param object
     * @param name
     * @return
     * @throws JSONException
     */
    private int[] parseIntArrayData(JSONObject object, String name) throws JSONException {
        JSONArray a = JSONUtils.getJSONArray(object, name);
        if (a == null) {
            return null;
        }
        int[] data = new int[a.length()];
        for (int j = 0; j < a.length(); j++) {
            int temp = Integer.parseInt((String) a.get(j));
            data[j] = temp;
        }
        return data;
    }


    /**
     * 解析boolean数组
     *
     * @param object
     * @param name
     * @return
     * @throws JSONException
     */
    private boolean[] parseBooleanArrayData(JSONObject object, String name) throws JSONException {
        JSONArray a = JSONUtils.getJSONArray(object, name);
        if (a == null) {
            return null;
        }
        boolean[] data = new boolean[a.length()];
        for (int j = 0; j < a.length(); j++) {
            boolean temp = Boolean.parseBoolean((String) a.get(j));
            data[j] = temp;
        }
        return data;
    }

    /**
     * 解析long数组
     *
     * @param object
     * @param name
     * @return
     * @throws JSONException
     */
    private long[] parseLongArrayData(JSONObject object, String name) throws JSONException {
        JSONArray a = JSONUtils.getJSONArray(object, name);
        if (a == null) {
            return null;
        }
        long[] data = new long[a.length()];
        for (int j = 0; j < a.length(); j++) {
            long temp = Long.parseLong((String) a.get(j));
            data[j] = temp;
        }
        return data;
    }

    /**
     * 解析float数组
     *
     * @param object
     * @param name
     * @return
     * @throws JSONException
     */
    private float[] parseFloatArrayData(JSONObject object, String name) throws JSONException {
        JSONArray a = JSONUtils.getJSONArray(object, name);
        if (a == null) {
            return null;
        }
        float[] data = new float[a.length()];
        for (int j = 0; j < a.length(); j++) {
            float temp = Float.parseFloat((String) a.get(j));
            data[j] = temp;
        }
        return data;
    }


    /**
     * 解析short数组
     *
     * @param object
     * @param name
     * @return
     * @throws JSONException
     */
    private short[] parseShortArrayData(JSONObject object, String name) throws JSONException {
        JSONArray a = JSONUtils.getJSONArray(object, name);
        if (a == null) {
            return null;
        }
        short[] data = new short[a.length()];
        for (int j = 0; j < a.length(); j++) {
            short temp = Short.parseShort((String) a.get(j));
            data[j] = temp;
        }
        return data;
    }

    public boolean checkSDCardAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
}
