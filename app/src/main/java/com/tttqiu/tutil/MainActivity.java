package com.tttqiu.tutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tttqiu.library.TUtil;
import com.tttqiu.library.network.RequestQueue;
import com.tttqiu.library.request.GsonRequest;
import com.tttqiu.library.request.Request;
import com.tttqiu.library.request.StringRequest;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;

    private String[] images = {
            "http://pic2.zhimg.com/bd24a341131d7fd8a6d832b8dbe08f15.jpg",
            "http://pic3.zhimg.com/fa745034caf4bd52fe2c981b3f6cbece.jpg",
            "http://pic3.zhimg.com/897ec7e842839dbcdfb80ccce0f6e0fa.jpg",
            "http://pic4.zhimg.com/1988a1d6e9d335ff03e08d455e35b00f.jpg",
            "http://pic2.zhimg.com/97890e1361b5105fd3f977927a15ecf1.jpg",
            "http://pic4.zhimg.com/f0a38aaadbf8c502e04df5fd11166b5b.jpg",
            "http://pic1.zhimg.com/4c9651ce231fec62a0ffce71a72fc658.jpg",
            "http://pic2.zhimg.com/25c1752b4c138e7e9f1ed1a8bc6b68d1.jpg",
            "http://pic2.zhimg.com/b6d4d79fb095f563cfe4fd340520f49d.jpg",
            "http://pic3.zhimg.com/65de36b17875e2423420aecf095423c2.jpg",
            "http://pic2.zhimg.com/77929b86371adb821dca18606e99c7b1.jpg",
            "http://pic1.zhimg.com/b42b4379ea1fc29a1691fc76ef327120.jpg",
            "http://pic1.zhimg.com/378ed297ae49db3be63041f3c3310c24.jpg",
            "http://pic2.zhimg.com/e2a40b9590140290b1895523bad5f1c9.jpg",
            "http://pic3.zhimg.com/36253900d0ac65850e5e45b0b412c97a.jpg",
            "http://pic1.zhimg.com/4d5173ce56066f6cc32ac76e77542ab4.jpg",
            "http://pic1.zhimg.com/2d7de68b203110440a645cc8ba02fae8.jpg",
            "http://pic4.zhimg.com/ec988b38950bebc68ea4a32e0fc7997f.jpg",
            "http://pic3.zhimg.com/459d87c3a26a95b787bc913c79fde7a6.jpg",
            "http://pic4.zhimg.com/6d0e3722900363b17a06c0f0fa740067.jpg",
            "http://pic4.zhimg.com/7cb19a84c01cf9e49e255a40dd1ea557.jpg",
            "http://pic4.zhimg.com/d8650b12c2437d3a35273226a52745f3.jpg",
            "http://pic3.zhimg.com/f10aafe7aff122039019e0a1d979c866.jpg",
            "http://pic4.zhimg.com/1b43b077b85b175df405dca5d130126f.jpg",
            "http://pic1.zhimg.com/0c34c59fc2de981da5c2be32928dbebc.jpg",
            "http://pic3.zhimg.com/ec0804c3ffd7e651cdf690e98bd92bbe.jpg",
            "http://pic4.zhimg.com/00dcdb7e8c767236d2b1caacbb052fff.jpg",
            "http://pic3.zhimg.com/238f1c3236fb288348baff8f81034ab6.jpg",
            "http://pic1.zhimg.com/909b5036a19c17de322177755a6fcecc.jpg",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, images);
//        recyclerView.setAdapter(adapter);

//        mRequestQueue=TUtil.startRequestQueue(RequestQueue.DEFAULT_THREAD_NUM);
//        GsonRequest<TitleBean> request=new GsonRequest<>("http://news-at.zhihu.com/api/4/news/latest",
//                TitleBean.class,new Request.RequestListener<TitleBean>() {
//                    @Override
//                    public void onComplete(TitleBean result) {
//                        Log.d("ppqq",""+result.getDate());
//                        Log.d("ppqq",""+result.getStories()[0].getTitle());
//                        Log.d("ppqq",""+result.getStories()[0].getImages()[0]);
//                        Log.d("ppqq",""+result.getStories()[0].getId());
//                    }
//
//                    @Override
//                    public void onError(String errorMessage) {
//
//                    }
//                });
//        mRequestQueue.addRequest(request);


//        mRequestQueue = TUtil.startRequestQueue(RequestQueue.DEFAULT_THREAD_NUM);
//        for (int i = 0; i < 20; i++) {
//            final int j=i;
//            StringRequest request = new StringRequest("http://blog.csdn.net/bboyfeiyu/article/details/43015859",
//                    new Request.RequestListener<String>() {
//                        @Override
//                        public void onComplete(String result) {
//                            Log.d("ppqq", ""+j+result);
//                        }
//
//                        @Override
//                        public void onError(String errorMessage) {
//                            Log.d("ppqq", errorMessage);
//                        }
//                    });
//            mRequestQueue.addRequest(request);
//        }

        mRequestQueue = TUtil.startRequestQueue(RequestQueue.DEFAULT_THREAD_NUM);
        StringRequest request = new StringRequest(Request.GET, "http://apis.baidu.com/datatiny/cardinfo_vip/cardinfo_vip",
                new Request.RequestListener<String>() {
                    @Override
                    public void onComplete(String result) {
                        Log.d("ppqq", "onComplete  "+result);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.d("ppqq", "onError  "+errorMessage);
                    }
                });
        request.addHeader("apikey","7143934c4da9e05981d9dd1076ecbb96");
        request.addParam("aaa","bbb");
        request.addParam("ccc","ddd");
        mRequestQueue.addRequest(request);
    }
}
