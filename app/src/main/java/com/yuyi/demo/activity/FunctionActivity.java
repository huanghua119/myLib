package com.yuyi.demo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import com.yuyi.demo.R;
import com.yuyi.lib.IFunctionManage;
import com.yuyi.lib.abs.BaseSwipeBackActivity;
import com.yuyi.lib.utils.JSONUtils;
import com.yuyi.lib.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author huanghua
 */

public class FunctionActivity extends BaseSwipeBackActivity {

    private IFunctionManage mFunctionManage;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyLog.i("FunctionActivity onServiceConnected:" + name);
            mFunctionManage = IFunctionManage.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mFunctionManage = null;
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_function;
    }

    @Override
    public void initView() {
        setReturnText(R.string.function_policy);
        Button btn = (Button) findViewById(R.id.btn);
        final TextView textView = (TextView) findViewById(R.id.text);
        btn.setOnClickListener(v -> {
            try {
                if (mFunctionManage != null) {
                    String resultJson = mFunctionManage.doFunction("getVersion", parasToJson(null));
                    JSONObject object = JSONUtils.stringToJSONObject(resultJson);
                    String type = JSONUtils.getJSONString(object, "type");
                    if ("success".equals(type)) {
                        String message = JSONUtils.getJSONString(object, "message");
                        textView.append("version:" + message);
                    }


                    final JSONArray array = new JSONArray();
                    JSONObject o1 = new JSONObject();
                    o1.put("type", "String");
                    o1.put("data", "参数1");
                    JSONObject o2 = new JSONObject();
                    o2.put("type", "int");
                    o2.put("data", 100);
                    JSONObject o3 = new JSONObject();
                    o3.put("type", "float");
                    o3.put("data", 4.3f);
                    JSONObject o4 = new JSONObject();
                    o4.put("type", "double");
                    o4.put("data", 192.39);
                    JSONObject o5 = new JSONObject();
                    o5.put("type", "short");
                    o5.put("data", 2);
                    JSONObject o6 = new JSONObject();
                    o6.put("type", "long");
                    o6.put("data", 43);
                    JSONObject o7 = new JSONObject();
                    o7.put("type", "boolean");
                    o7.put("data", false);

                    JSONObject o8 = new JSONObject();
                    o8.put("type", "String_array");
                    JSONArray a8 = new JSONArray();
                    a8.put("a91");
                    a8.put("a92");
                    a8.put("a93");
                    o8.put("data", a8);

                    JSONObject o9 = new JSONObject();
                    o9.put("type", "double_array");
                    JSONArray a9 = new JSONArray();
                    a9.put("4.5");
                    a9.put("10.9");
                    a9.put("39.3");
                    o9.put("data", a9);


                    JSONObject o10 = new JSONObject();
                    o10.put("type", "int_array");
                    JSONArray a10 = new JSONArray();
                    a10.put("1111");
                    a10.put("4234");
                    a10.put("33");
                    o10.put("data", a10);

                    JSONObject o11 = new JSONObject();
                    o11.put("type", "long_array");
                    JSONArray a11 = new JSONArray();
                    a11.put("11");
                    a11.put("31412123123131");
                    a11.put("45");
                    o11.put("data", a11);


                    JSONObject o12 = new JSONObject();
                    o12.put("type", "boolean_array");
                    JSONArray a12 = new JSONArray();
                    a12.put("false");
                    a12.put("true");
                    a12.put("true");
                    o12.put("data", a12);

                    JSONObject o13 = new JSONObject();
                    o13.put("type", "short_array");
                    JSONArray a13 = new JSONArray();
                    a13.put("12");
                    a13.put("24");
                    a13.put("6461");
                    o13.put("data", a13);

                    JSONObject o14 = new JSONObject();
                    o14.put("type", "float_array");
                    JSONArray a14 = new JSONArray();
                    a14.put("12");
                    a14.put("24");
                    a14.put("6461");
                    o14.put("data", a14);

                    array.put(o1);
                    array.put(o2);
                    array.put(o3);
                    array.put(o4);
                    array.put(o5);
                    array.put(o6);
                    array.put(o7);
                    array.put(o8);
                    array.put(o9);
                    array.put(o10);
                    array.put(o11);
                    array.put(o12);
                    array.put(o13);
                    array.put(o14);

                    String result = mFunctionManage.doFunction("getTestData", parasToJson(array));
                    textView.append(result);

                    result = mFunctionManage.doFunction("getTopActivityPackageName", parasToJson(null));
                    textView.append(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        attemptToBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
    }

    /**
     * 启动服务端的动态调用服务，因为使用aidl通信，客户端使用把aial文件引入
     */
    private void attemptToBindService() {
        Intent intent = new Intent("mephone.intent.action.function_service");
        intent.setClassName(getPackageName(), "com.yuyi.lib.function.service.FunctionService");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 参数转成json数据
     *
     * @param paras
     * @return
     * @throws JSONException
     */
    private String parasToJson(JSONArray paras) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("path", Environment.getExternalStorageDirectory().getAbsolutePath() + "/VirtualOS/functionPolicy.jar");  //jar包的路径，不传则使用默认路径
        object.put("classname", ""); //需要加载的类名，不传则使用默认类
        object.put("paras", paras);  //方法的参数，需要传入JsonArray类型，不传则说明方法不需要参数
        return object.toString();
    }
}
