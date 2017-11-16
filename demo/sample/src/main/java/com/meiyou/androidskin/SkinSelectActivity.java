package com.meiyou.androidskin;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.meiyou.skinlib.AndroidSkin;
import com.meiyou.skinlib.SkinListener;
import com.meiyou.skinlib.loader.SkinLoader;
import com.meiyou.skinlib.util.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: meetyou
 * Date: 17/11/13 09:01.
 */

/*
public class SkinSelectActivity extends ListActivity {
    private ListView mListView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skin);
        mListView = (ListView)findViewById(R.id.lv_skin);

    }
}
*/

public class SkinSelectActivity extends ListActivity {
    private static final String TAG ="SkinSelectActivity" ;
    private String[] mSkinTitles = { "默认", "皮肤一", "皮肤二"};
    private String[] mSkinDess = { "原始皮肤", "皮肤名：静之夜", "皮肤名：我们在路上" };
    private String[] mSkinPackageName = { "", "com.meiyou.skinone", "com.meiyou.skintwo" };
    private String[] mSkinFileName = { "", "skinone-debug.apk", "skintwo-debug.apk" };
    ListView mListView = null;
    ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mListView = getListView();

        int lengh = mSkinTitles.length;
        for(int i =0; i < lengh; i++) {
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("image", R.drawable.apk_all_bottommine);
            item.put("title", mSkinTitles[i]);
            item.put("text", mSkinDess[i]);
            mData.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.list_item_skin,
                new String[]{"image","title","text"},new int[]{R.id.image,R.id.title,R.id.text});
        setListAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position,
                                    long id) {
                //Toast.makeText(SkinSelectActivity.this,"您选择了：" + mSkinTitles[position], Toast.LENGTH_LONG).show();

                 String apkFile = mSkinFileName[position];
                if(apkFile==null || apkFile.length()==0){
                    AndroidSkin.getInstance().clearSkinAndApply();
                    SkinSelectActivity.this.finish();
                }else{
                    AndroidSkin.getInstance().saveSkinAndApply(apkFile, SkinLoader.ASSETS, new SkinListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(),"换肤成功",Toast.LENGTH_SHORT).show();
                            SkinSelectActivity.this.finish();
                        }

                        @Override
                        public void onFail(String message) {
                            Toast.makeText(getApplicationContext(),"换肤失败",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                /*new AsyncTask<String, Void, String>(){

                    @Override
                    protected String doInBackground(String... strings) {
                        return FileUtils.copyAssetsFileToSd(getApplicationContext(),apkFile,"AndroidSkin");
                    }

                    @Override
                    protected void onPostExecute(String str) {
                        super.onPostExecute(str);    //To change body of overridden methods use File | Settings | File Templates.
                        Log.d(TAG,"onPostExecute:"+str);
                        String apkPackageName = mSkinPackageName[position];
                        SpInstance.getInstance().saveString("skin_filename",str);
                        SpInstance.getInstance().saveString("skin_packagename",apkPackageName);
                        if(str==null || str.length()==0){
                            AndroidSkin.getInstance().clearSkinAndApply();
                        }else{
                            AndroidSkin.getInstance().saveSkinAndApply(str,apkPackageName);
                        }
                        SkinSelectActivity.this.finish();
                    }

                    @Override
                    protected void onCancelled() {
                        super.onCancelled();    //To change body of overridden methods use File | Settings | File Templates.
                    }
                }.execute();*/
                //SpInstance.getInstance().saveString("skin_");
            }
        });
        super.onCreate(savedInstanceState);
    }

    class CopyTask extends AsyncTask<String, Void, String> {

        private String mFileName;
        public CopyTask(String filename){
            mFileName = filename;
        }

        @Override
        protected String doInBackground(String... strings) {
            return FileUtils.copyAssetsFileToSd(getApplicationContext(),mFileName,"AndroidSkin");
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);    //To change body of overridden methods use File | Settings | File Templates.
            Log.d(TAG,"onPostExecute:"+str);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

}