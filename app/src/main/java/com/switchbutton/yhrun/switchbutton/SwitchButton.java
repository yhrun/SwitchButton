package com.switchbutton.yhrun.switchbutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.WeakHashMap;

/**
 * Created by yhrun on 15-9-12.
 */
public class SwitchButton extends View {
    private Context context;
    private Bitmap BtFrame;           //边框
    private Bitmap BtBg;              //背景
    private Bitmap BtPress;           //按钮
    private Bitmap BtCheckBg;         //选中后的背景
    private int retX = 0 ;            //滑动距离
    private boolean isCheck = true;   //是否被选中
    private int Width = 0;
    private int Height = 0;
    private int maxScrollDistance = 80;//最大滚动距离
    private Paint mPaint;
    private Handler mHandler;
    private boolean isMoving = false;
    private OnSwitchButtonChangeListener listener;

    private int MOVE = 0x0;
    private int FINISH = 0x1;
    private int RESET = 0x2;

    public SwitchButton(Context context) {
        super(context);
        this.context = context;
        initialization();
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialization();
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialization();
    }

    public void setOnSwitchButtonChangerListener(OnSwitchButtonChangeListener listener ){
        this.listener = listener;
    }

    private void initialization(){
        mPaint = new Paint();
        BtFrame = BitmapFactory.decodeResource(this.context.getResources(),R.mipmap.switch_button_frame);
        BtBg = BitmapFactory.decodeResource(this.context.getResources(),R.mipmap.switch_button_mask);
        BtPress = BitmapFactory.decodeResource(this.context.getResources(),R.mipmap.switch_button_btn_pressed);
        BtCheckBg = BitmapFactory.decodeResource(this.context.getResources(),R.mipmap.switch_button_bottom);
        Width = BtFrame.getWidth();
        Height = BtFrame.getHeight();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MOVE){
                    UpdateUi();
                }else if(msg.what == FINISH){
                    isMoving = false;
                    if(isCheck) isCheck = false;
                    else isCheck = true;
                    if(listener != null)listener.onSwitchButtonChange(isCheck);
                }else if(msg.what == RESET){
                    reSet();
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(BtBg,0,0,mPaint);
        canvas.drawBitmap(BtFrame,0,0,mPaint);
        //Rect rect = new Rect(0,0,BtCheckBg.getWidth()-Math.abs(retX),BtCheckBg.getHeight());
        //RectF rectf = new RectF(0,0,BtFrame.getWidth(),BtFrame.getHeight());
        Bitmap tmp = Bitmap.createBitmap(BtCheckBg,0,0,BtCheckBg.getWidth()/2-Math.abs(retX),BtCheckBg.getHeight());
        canvas.drawBitmap(tmp,0,0,mPaint);
        canvas.drawBitmap(BtPress,retX,0,mPaint);
    }

    private void UpdateUi(){
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Move();
        return true;
    }


    private void Move(){
        if(isMoving)return;
        isMoving = true;
        new Thread(){
            @Override
            public void run() {
                for(int i = 0 ;i < 10;i++){
                    if(isCheck)retX -= 8;
                    else retX += 8;
                    mHandler.sendEmptyMessage(MOVE);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(RESET);
                    }
                }
                mHandler.sendEmptyMessage(FINISH);
            }
        }.start();
    }

    public void reSet(){
        isCheck = true;
        isMoving = false;
        retX = 0;
        UpdateUi();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(Width,Height);
    }

    public interface OnSwitchButtonChangeListener{
        void onSwitchButtonChange(boolean isCheck);
        void onSwitchButtonReset();
    }

    public boolean getSwitchButtonStatus(){
        return isCheck;
    }

}
