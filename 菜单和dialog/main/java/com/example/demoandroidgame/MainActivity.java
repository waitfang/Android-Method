package com.example.demoandroidgame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    private Button butMenu ;//定义菜单button
    private Button butActivity ;
    private Dialog dialog; //定义Dialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到button，并绑定click事件
        butMenu =(Button) findViewById(R.id.butMenu);
        butMenu.setOnClickListener(this);

        butActivity = (Button) findViewById(R.id.butActivity);
        butActivity.setOnClickListener(this);
    }

    //菜单Item的click事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.exit:
                Toast.makeText(this, R.string.menuItemexit, Toast.LENGTH_SHORT).show();
                break;
            case R.id.set:
                Toast.makeText(this, R.string.menuItemset, Toast.LENGTH_SHORT).show();
                break;
            case R.id.account:
                Toast.makeText(this,  R.string.menuItemaccount, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    //菜单button click事件
    @Override
    public void onClick(View v) {
        //菜单页面
        PopupMenu ObjMenu = new PopupMenu(this,v);//给v绑定一个menu
        MenuInflater objinFalter = ObjMenu.getMenuInflater();//菜单扩充器
        objinFalter.inflate(R.menu.menu_main,ObjMenu.getMenu());
        ObjMenu.setOnMenuItemClickListener(this); //绑定菜单项的点击事件
        ObjMenu.show();

        //新布局
        dialog = new Dialog(MainActivity.this, R.style.dialogstyle);

        View view = View.inflate(this, R.layout.activity_dialog, null);
        Button ButDialog = (Button) view.findViewById(R.id.ButDialog);//自定义activity的button

        dialog.setContentView(view);
//      dialog.setContentView(R.layout.activity_dialog);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams LayoutParams = dialogWindow.getAttributes();
        LayoutParams.width = 700;
        LayoutParams.height=800;
//      LayoutParams.alpha = 0.7f; //透明度
        dialog.setCanceledOnTouchOutside(false); //使得点击对话框外部不消失对话框 true
        ButDialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
