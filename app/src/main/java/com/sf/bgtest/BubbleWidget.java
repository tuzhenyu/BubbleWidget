package com.sf.bgtest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 类描述：带有气泡背景的容器；并且可以动态调整气泡的尺寸属性（圆角角度，内边距，三角形高度）,背景色等。
 * Created by tzy on 2016/7/11.
 */
public class BubbleWidget extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener{

    private BackGroundConfig mBackGroundConfig = null;

    public BackGroundConfig getmBackGroundConfig() {
        return mBackGroundConfig;
    }

    public void setmBackGroundConfig(BackGroundConfig mBackGroundConfig) {
        this.mBackGroundConfig = mBackGroundConfig;
        setBubbleDrawable();
    }

    public BubbleWidget(Context context) {
        super(context);
        initConfig(context,null);
    }

    public BubbleWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initConfig(context,attrs);
    }

    public BubbleWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig(context,attrs);
    }

    private void initConfig(Context context, AttributeSet attrs){
        setClickable(true);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        mBackGroundConfig = new BackGroundConfig(context);
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleWidget);
            if(typedArray != null){
                mBackGroundConfig.bgColorNopressed = typedArray.getColor(R.styleable.BubbleWidget_bg_color_nopressed,mBackGroundConfig.DEFAULT_BACKGROUNG_COLOR_NOPRESSED);
                mBackGroundConfig.bgColorPressed = typedArray.getColor(R.styleable.BubbleWidget_bg_color_pressed,mBackGroundConfig.DEFAULT_BACKGROUNG_COLOR_PRESSED);
                mBackGroundConfig.radius = typedArray.getDimensionPixelSize(R.styleable.BubbleWidget_bradius,mBackGroundConfig.DEFAULT_RADIUS);
                mBackGroundConfig.lengthOfTriangle = typedArray.getDimensionPixelSize(R.styleable.BubbleWidget_triangle_length,mBackGroundConfig.DEFAULT_LENGTH_EQ_TRIANGLE);

                mBackGroundConfig.bPaddingLeft = typedArray.getDimensionPixelSize( R.styleable.BubbleWidget_bpaddingleft,0);
                mBackGroundConfig.bPaddingRight =typedArray.getDimensionPixelSize(R.styleable.BubbleWidget_bpaddingright,0);
                mBackGroundConfig.bPaddingTop =typedArray.getDimensionPixelSize(R.styleable.BubbleWidget_bpaddingtop,0);
                mBackGroundConfig.bPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.BubbleWidget_bpaddingbottom,0);

                if(typedArray.hasValue(R.styleable.BubbleWidget_bpadding)){
                    int bPadding = typedArray.getDimensionPixelSize(R.styleable.BubbleWidget_bpadding,0);
                    mBackGroundConfig.bPaddingLeft = bPadding;
                    mBackGroundConfig.bPaddingRight = bPadding;
                    mBackGroundConfig.bPaddingTop = bPadding;
                    mBackGroundConfig.bPaddingBottom = bPadding;
                }
            }
        }
    }

    public StateListDrawable getStateListDrawable(){
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        if(w <= 0 || h <= 0){return null;}
        Resources resources = getResources();
        Drawable bgDrawableNoPressed = getBubbleDrawable(w,h,mBackGroundConfig.bgColorNopressed,resources);
        Drawable bgDrawablePressed = getBubbleDrawable(w,h,mBackGroundConfig.bgColorPressed,resources);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{-android.R.attr.state_pressed},
                bgDrawableNoPressed);
        listDrawable.addState(new int[]{android.R.attr.state_pressed},
                bgDrawablePressed);
        return listDrawable;
    }

    /**
     *
     * 获取特定颜色的drwable
     * */
    public Drawable getBubbleDrawable(int width, int height, int color, Resources resources){
        Log.i("bgTest","开始绘制背景");
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint mPaint= new Paint();
        // 在左上角绘制一个等腰三角形
        Path path = new Path();
        mPaint.reset();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        path.moveTo(0, 0);// 此点为三角形的左顶点
        path.lineTo(mBackGroundConfig.lengthOfTriangle, mBackGroundConfig.lengthOfTriangle);
        path.lineTo(0, mBackGroundConfig.lengthOfTriangle + mBackGroundConfig.lengthOfTriangle);
        path.close();
        canvas.drawPath(path, mPaint);
        path = null;

        //绘制绘制圆角矩形
        mPaint.reset();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        int rectY = mBackGroundConfig.getLengthOfTriangle();
        RectF oval = new RectF(0, rectY, canvas.getWidth(), canvas.getHeight());
        canvas.drawRoundRect(oval, mBackGroundConfig.radius, mBackGroundConfig.radius, mPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG );
        canvas.restore();

        Log.i("bgTest","绘制完成");
        Drawable drawable =new BitmapDrawable(resources,bitmap);
        mPaint = null;
        canvas.setBitmap(null);
        canvas = null;

        return drawable;
    }

    @Override
    public void onGlobalLayout() {
        setBubbleDrawable();
        setBPadding(mBackGroundConfig);
    }

    public void setBPadding(BackGroundConfig backGroundConfig){
        mBackGroundConfig = backGroundConfig;
        int left = mBackGroundConfig.bPaddingLeft;
        int top = mBackGroundConfig.bPaddingTop + mBackGroundConfig.lengthOfTriangle;
        int right = mBackGroundConfig.bPaddingRight;
        int bottom = mBackGroundConfig.bPaddingBottom;
        setPadding(left,top,right,bottom);
    }

    /**
     * 设置气泡的内边距
     * */
    public void setBPadding(int bpadding){
        mBackGroundConfig.bPaddingLeft = bpadding;
        mBackGroundConfig.bPaddingTop = bpadding;
        mBackGroundConfig.bPaddingRight = bpadding;
        mBackGroundConfig.bPaddingBottom = bpadding;
        setBPadding(mBackGroundConfig);
    }

    /**
     * 设置气泡上边的内边距
     * */
    public void setBPaddingTop(int bPaddingTop){
        mBackGroundConfig.bPaddingTop = bPaddingTop ;
        setBPadding(mBackGroundConfig);
    }

    /**
     * 设置气泡低边的内边距
     * */
    public void setBPaddingBottom(int bPaddingBottom){
        mBackGroundConfig.bPaddingBottom = bPaddingBottom ;
        setBPadding(mBackGroundConfig);
    }

    /**
     * 设置气泡左边的内边距
     * */
    public void setBPaddingLeft(int bPaddingLeft){
        mBackGroundConfig.bPaddingLeft = bPaddingLeft ;
        setBPadding(mBackGroundConfig);
    }

    /**
     * 设置气泡右边的内边距
     * */
    public void setBPaddingRight(int bPaddingRight){
        mBackGroundConfig.bPaddingRight = bPaddingRight ;
        setBPadding(mBackGroundConfig);
    }

    private void setBubbleDrawable(){
        StateListDrawable bgStateListDrawable = getStateListDrawable();
      /*  setBackground(bgStateListDrawable);*/
        setBackgroundDrawable(bgStateListDrawable);
    }

    /**
     * 设置静态背景
     * @param color 背景色
     * */
    public void setStaticBackGround(int color){
        mBackGroundConfig.bgColorNopressed = color;
        mBackGroundConfig.bgColorPressed = color;
        setBubbleDrawable();
    }

    /**
     * 设置带状态的背景
     * @param noPressedColor 正常准状态下的颜色
     * @param pressedColor 触摸状态下的颜色
     * */
    public void setStateBackGrounds(int noPressedColor,int pressedColor){
        mBackGroundConfig.bgColorNopressed = noPressedColor;
        mBackGroundConfig.bgColorPressed = pressedColor;
        setBubbleDrawable();
    }

    /**
     * 设置气泡圆角属性
     * */
    public void setBRadius(int px){
        mBackGroundConfig.radius = px;
        setBubbleDrawable();
    }

    /**
     * 设置左上角三角形的高度
     * */
    public void setLengthOfTriangle(int length){
        mBackGroundConfig.lengthOfTriangle = length;
        setBubbleDrawable();
    }

    /**
     * 配置背景属性
     * */
    public static class BackGroundConfig{
        //以下尺寸单位都为dp，试用前会换算成px
        //默认的背景色（非触摸状态下）
        public  int DEFAULT_BACKGROUNG_COLOR_NOPRESSED   ;
        //默认的背景色（触摸状态下）
        public  int DEFAULT_BACKGROUNG_COLOR_PRESSED ;
        //圆角矩形的圆角角度的默认值
        public  int DEFAULT_RADIUS;

        //等边三角形的默认边长
        public int DEFAULT_LENGTH_EQ_TRIANGLE = 0;
        //背景色（非触摸状态下）
        private int bgColorNopressed = DEFAULT_BACKGROUNG_COLOR_NOPRESSED;
        //背景色（触摸状态下）
        private int bgColorPressed = DEFAULT_BACKGROUNG_COLOR_PRESSED;
        //圆角角度
        private int radius = 0;
        //左上角等腰直角三角形的腰的
        private int lengthOfTriangle = 0;
        //气泡内的左边距
        private int bPaddingLeft =0;
        //气泡内的右边距
        private int bPaddingRight =0;
        //气泡内的上边距
        private int bPaddingTop = 0;
        //气泡内的下边距
        private int bPaddingBottom = 0;

        public BackGroundConfig(Context context){
            //初始化默认值
            Resources resources = context.getResources();
            DEFAULT_BACKGROUNG_COLOR_NOPRESSED = resources.getColor(R.color.default_color_bubble_bg);
            DEFAULT_BACKGROUNG_COLOR_PRESSED = resources.getColor(R.color.default_color_bubble_bg);
            DEFAULT_RADIUS = resources.getDimensionPixelSize(R.dimen.default_bubble_roundRect_radius);
            DEFAULT_LENGTH_EQ_TRIANGLE = resources.getDimensionPixelSize(R.dimen.default_bubble_length_of_triangle);
        }

        public int getbPaddingLeft() {
            return bPaddingLeft;
        }

        public void setbPaddingLeft(int bPaddingLeft) {
            this.bPaddingLeft = bPaddingLeft;
        }

        public int getbPaddingRight() {
            return bPaddingRight;
        }

        public void setbPaddingRight(int bPaddingRight) {
            this.bPaddingRight = bPaddingRight;
        }

        public int getbPaddingTop() {
            return bPaddingTop;
        }

        public void setbPaddingTop(int bPaddingTop) {
            this.bPaddingTop = bPaddingTop;
        }

        public int getbPaddingBottom() {
            return bPaddingBottom;
        }

        public void setBPaddingBottom(int getbPaddingBottom) {
            this.bPaddingBottom = getbPaddingBottom;
        }

        public int getBgColorNopressed() {
            return bgColorNopressed;
        }

        public BackGroundConfig setBgColorNopressed(int bgColorNopressed) {
            this.bgColorNopressed = bgColorNopressed;
            return this;
        }

        public int getBgColorPressed() {
            return bgColorPressed;
        }

        public BackGroundConfig(int bgColorNopressed,int bgColorPressed){
            this.bgColorNopressed = bgColorNopressed;
            this.bgColorPressed = bgColorPressed;
        }

        public BackGroundConfig setBgColorPressed(int bgColorPressed) {
            this.bgColorPressed = bgColorPressed;
            return this;
        }

        public int getRadius() {
            return radius;
        }

        public BackGroundConfig setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public int getLengthOfTriangle() {
            return lengthOfTriangle;
        }

        public BackGroundConfig setLengthOfTriangle(int lengthOfTriangle) {
            this.lengthOfTriangle = lengthOfTriangle;
            return this;
        }
    }
}
