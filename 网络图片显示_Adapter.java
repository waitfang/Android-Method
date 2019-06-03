package com.example.android.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.common.ImageUtils.ImageLoader;
import com.example.android.entity.Match;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<Match> matchList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private int mStart,mEnd;
    public static String[] URLS;
    public static String[] URLS1;
    public static String[] URLS2;
    private boolean mFirstIn;
    private  int irows = 0;
    private  int  mImghashMap = 0;
    public MatchAdapter(Context context, List<Match> matches, ListView listView) {
        int irows = 0;
        mImghashMap = 0;
        matchList = matches;
        mInflater = LayoutInflater.from(context);
       mImageLoader = new ImageLoader(listView);
        URLS = new String[matches.size()];
        URLS2 = new String[matches.size()];
        for (int i = 0;i<URLS.length;i++)
        {
            URLS[i] = matches.get(i).getName1con();
           // URLS[i] = matches.get(i).getName2con();
            System.out.println(URLS[i]);
        }

//        for (int i = 0;i<URLS2.length;i++)
//        {
//            URLS2[i] = matches.get(i).getName2con();
//            System.out.println(URLS2[i]);
//        }
        mFirstIn = true;
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder = null;
        if (convertView==null){
            viewHolder =new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_item,null);
            viewHolder.macth_Name1=convertView.findViewById(R.id.macth_Name1);
            viewHolder.macth_Time=convertView.findViewById(R.id.macth_Time);
            viewHolder.macth_Name1res=convertView.findViewById(R.id.macth_Name1res);
            viewHolder.macth_Name2=convertView.findViewById(R.id.macth_Name2);
            viewHolder.macth_Result=convertView.findViewById(R.id.macth_Result);
            viewHolder.macth_Name2res=convertView.findViewById(R.id.macth_Name2res);
            viewHolder.macth_Name1con=convertView.findViewById(R.id.macth_Name1con);
            viewHolder.macth_Name2con=convertView.findViewById(R.id.macth_Name2con);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.macth_Name1.setText(matchList.get(position).getName1());
        viewHolder.macth_Time.setText(matchList.get(position).getDate());
        viewHolder.macth_Name1res.setText(matchList.get(position).getName1res());
        viewHolder.macth_Name2.setText(matchList.get(position).getName2());
        viewHolder.macth_Result.setText(matchList.get(position).getResult());
        viewHolder.macth_Name2res.setText(matchList.get(position).getName2res());
        viewHolder.macth_Name1con.setTag( matchList.get(position).getName1con());
//        viewHolder.macth_Name2con.setImageBitmap( matchList.get(position).getName2con());
       // mImageLoader.showImageByAsyncTask(viewHolder.macth_Name1con,matchList.get(position).getName1con());
        //mImageLoader.showImageByAsyncTask(viewHolder.macth_Name2con,matchList.get(position).getName2con());


        HttpURLConnectionImg(matchList.get(position).getName2con(),handler1,viewHolder);

        convertView.setTag(viewHolder);
        return convertView;
    }



    private Handler handler1=new Handler(){
        public void handleMessage(Message msg){
//            Bitmap bitmap = (Bitmap) msg.obj;
//            mviewHolder.macth_Name2con.setImageBitmap(bitmap);
            ViewHolder  viewHolder = (ViewHolder) msg.obj;
            Bitmap bitmap = (Bitmap) ImghashMap.get(mImghashMap);
            viewHolder.macth_Name2con.setImageBitmap(bitmap);
            mImghashMap++;
        };
    };

    private Map ImghashMap = new HashMap();
    private  void HttpURLConnectionImg(final String  StrURL,final Handler bakhandler1 ,final ViewHolder viewHolder){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    URL url = new URL(StrURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //这里就简单的设置了网络的读取和连接时间上线，如果时间到了还没成功，那就不再尝试
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setConnectTimeout(8000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //这里直接就用bitmap取出了这个流里面的图片，哈哈，其实整篇文章不就主要是这一句嘛
                    Bitmap bm = BitmapFactory.decodeStream(inputStream);
                    //下面这是把图片携带在Message里面，简单，不多说
                    Message message = Message.obtain();
                    message.obj =  viewHolder;
                    ImghashMap.put(irows,bm);
                    irows++;
                    bakhandler1.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE)
        {
            //加载可见项
            mImageLoader.loadImages(mStart,mEnd);
        }else {
            //停止任务
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem+visibleItemCount;
        //第一次显示的时候调用
        if (mFirstIn && visibleItemCount > 0)
        {
          mImageLoader.loadImages(mStart,mEnd);
            mFirstIn = false;
        }
    }

    class ViewHolder{
        public ImageView macth_Name1con,ivIcon;
        public ImageView macth_Name2con;
        public TextView macth_Time;
        public TextView macth_Result;
        public TextView macth_Name1;
        public TextView macth_Name2;
        public TextView macth_Name1res;
        public TextView macth_Name2res;
        public ImageView macth_R;
    }
}

