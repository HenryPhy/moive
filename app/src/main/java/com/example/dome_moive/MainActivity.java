package com.example.dome_moive;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    public String url =
            "http://baobab.kaiyanapp.com/api/v4/tabs/selected?udid=11111&vc=168&vn=3.3.1&deviceModel=Huawei6&first_channel=eyepetizer_baidu_market&last_channel=eyepetizer_baidu_market&system_version_code=20";
    private ListView videoLv;
    List<VideoBean.ItemListBean> mDatas = new ArrayList<>();
    private VideoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoLv = (ListView) findViewById(R.id.video_lv);
//        设置数据源
//        设置适配器
        adapter = new VideoAdapter(this,mDatas);
        videoLv.setAdapter(adapter);
//        加载网络数据
        loadWebData();
    }
    //加载网络数据
    private void loadWebData() {
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                String content = HttpUtils.getStringContent(url);
                return content;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s!=null&&!s.isEmpty()) {

//                    解析数据
                    Gson gson = new Gson();
                    VideoBean videoBean = gson.fromJson(s, VideoBean.class);
                    List<VideoBean.ItemListBean> itemList = videoBean.getItemList();
                    for (int i = 0; i < itemList.size(); i++) {
                        VideoBean.ItemListBean bean = itemList.get(i);
                        if (bean.getType().equals("video")) {
                            mDatas.add(bean);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }
}