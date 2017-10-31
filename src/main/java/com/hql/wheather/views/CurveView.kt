package com.hql.wheather.views

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Created by huangqianglong on 2017/10/23.
 */
class CurveView(context: Context, attr: AttributeSet) : View(context, attr) {
    /**
    整个view的宽
     */
    var viewWidth: Int = 0
    /**
    整个view的高
     */
    var viewHeigh: Int = 0

    /**
     * Y轴数据*/
    var DataY: ArrayList<String> = ArrayList<String>()
    /**
     * X轴数据*/
    var DataX: ArrayList<String> = ArrayList<String>()
    /**
     * 数据集*
     */
     var DataMap:HashMap<String,HashMap<String,Int>> = HashMap<String,HashMap<String,Int>>()


    /**
     * 数据*/
    var Data:  HashMap<String,Int> =  HashMap<String,Int>()
    /**
     * 坐标轴原点X
     * */
    var xOri = 0f

    /**
     * 坐标轴原点Y
     * */
    var yOri = 0f
    /**
     * xy坐标轴宽度
     * */
    var xylinewidth = 2
    /**
     * x轴数据间距
     * */
    var interval = 90

    /**
     * x轴第一个数据的x坐标
     * */
    var initX = 8f

    /**
     * x轴第一个数据滑到最左时的最小值
     * */
    var miniX = 8f

    /**
     * x轴第一个数据滑到最右时的最大值，即第一个数据点的最大x坐标
     * */
    var maxX = 8f

    /**
     * 是否画Y轴
     * */
    var needY = true

    lateinit var mPaint: Paint
    lateinit var mPointPaint: Paint
    lateinit var mTextPaint: Paint
    lateinit var mCurvePaint: Paint

    init {
        initRes()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            viewWidth = getWidth()
            viewHeigh = getHeight()
//Y轴文本宽度
            var yTextWidth = getTextBounds("0000", mTextPaint).width()


            for (index in 0..DataY.size-1) {
                var temp = getTextBounds(DataY.get(index), mTextPaint).width()
                if (temp > yTextWidth) {
                    yTextWidth = temp
                }
            }
            //Y轴文本高度
            var yTextHeigh = getTextBounds("0000", mTextPaint).height()
            //X轴文本高度
            var xTextHeigh = getTextBounds("0000", mTextPaint).height()


            yOri = height * 1f - 2/*view的下边距*/ - yTextHeigh/*x轴文本高度*/ - xylinewidth / 2/*轴线宽度的中点*/ - 2/*x轴文字和轴的编剧*/


            xOri = 2/*view的边距*/ + yTextWidth/*y轴数据宽度*/ + 2/*y轴数与y轴间距*/ + xylinewidth / 2 * 1f

            initX = (xOri + interval).toFloat()

            miniX = width - interval * (DataX.size - 1) - (width - xOri) * 0.1f
            maxX = initX

            if(0!= DataX.size){

                interval =interval + getTextBounds(DataX.get(0), mTextPaint).width()
            }

        }
        super.onLayout(changed, left, top, right, bottom)
        invalidate()
    }

    fun initRes() {
        Log.d("hql-curview","===================initRes")
        mPaint = Paint()
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = xylinewidth*1f
        mPaint.strokeCap= Paint.Cap.ROUND
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE

        mPointPaint = Paint()
        mPointPaint.color = Color.WHITE
        mPointPaint.strokeWidth = xylinewidth*10f
        mPointPaint.strokeCap= Paint.Cap.ROUND
        mPointPaint.isAntiAlias = true




        mTextPaint = Paint()
        mTextPaint.color = Color.WHITE
        mTextPaint.strokeWidth = xylinewidth*10f
        mTextPaint.strokeCap= Paint.Cap.ROUND
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = 25f

        mCurvePaint = Paint()
        mCurvePaint.color = Color.WHITE
        mCurvePaint.strokeWidth = xylinewidth*1f
        mCurvePaint.strokeCap= Paint.Cap.ROUND
        mCurvePaint.isAntiAlias = true

    }
    fun setData(yData:ArrayList<String>,xDtata:ArrayList<String>,data: HashMap<String,HashMap<String,Int>>){
        this.DataY = yData
        this.DataX = xDtata
        DataMap = data
        this.Data = data.get("temdata1")!!
        interval =interval + getTextBounds(DataX.get(0), mTextPaint).width()
        miniX = width - interval * (DataX.size - 1) - (width - xOri) * 0.1f
        maxX = initX
        invalidate()
    }
    /**
     * 获取文本大小的矩形*/
    fun getTextBounds(text: String, paint: Paint): Rect {
        var rect: Rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect
    }

    override fun onDraw(canvas: Canvas?) {
        // super.onDraw(canvas)
        if(0==Data.size)return
        //canvas?.drawColor(Color.GREEN)
        if(needY){
            drawYAxis(canvas)
        }
        drawXAxis(canvas)
        drawData(canvas)
    }

    /**
     * 绘制坐标轴*/
    fun drawYAxis(canvas: Canvas?) {

        //绘制y轴
        // Log.d("hql-cur","xy-----------"+xOri+","+ yOri+","+ xOri+","+ (height - 2f))
        canvas?.drawLine(xOri, yOri, xOri, (0 + 2f), mPaint)

        var yPath: Path = Path()

        yPath.moveTo(xOri - 12f,  12f)
        yPath.lineTo(xOri, 0f)
        yPath.lineTo(xOri + 12f, 12f)
        canvas?.drawPath(yPath,mPaint)

        //绘制y轴刻度
        var yLen = yOri - yOri/10
        var yInterval = yLen / DataY.size

        for (index in 1..DataY.size){
            canvas?.drawLine(xOri,yOri - index*yInterval,xOri+10,yOri - index*yInterval,mPaint)
            canvas?.drawText(DataY.get(index-1),2f,yOri - index*yInterval,mTextPaint)
        }


    }
    /**绘制x轴*/
    fun drawXAxis(canvas: Canvas?){
        //画x坐标轴
        canvas?.drawLine(xOri,yOri,viewWidth*1f,yOri,mPaint)
        //画箭头
        var xLen = xOri + interval*DataX.size + viewWidth*0.1f

        if(xLen > viewWidth){
            xLen = viewWidth*1f
        }
        var xPath :Path = Path()
        xPath.moveTo(xLen-8f,yOri-8f)
        xPath.lineTo(xLen,yOri)
        xPath.lineTo(xLen-8f,yOri+8f)
        canvas?.drawPath(xPath,mPaint)

        //画x坐标刻度 数据

        for (index in 0..DataX.size-1){
            var x :Float = initX + interval*index
            if (x >= xOri){
                canvas?.drawLine(x,yOri,x,yOri-10,mPaint)
               // var rect:Rect = getTextBounds(Data.get(DataX.get(index)).toString(),mTextPaint)
                var rect:Rect = getTextBounds(DataX.get(index).toString(),mTextPaint)
                //画刻度
                canvas?.drawText(DataX.get(index).toString(),x-rect.width()/2,yOri+rect.height()+2,mTextPaint)
            }
        }
    }
    //绘制折线数据
    fun drawData(canvas: Canvas?){
        var layerID = canvas?.saveLayer(0f,0f,viewWidth.toFloat(),viewHeigh.toFloat(),null,Canvas.ALL_SAVE_FLAG)

        for(pathData in 0..DataMap.size-1){

            var path:Path = Path()
            var x:Float = initX //+ interval*0
            var y:Float = yOri - DataMap.get("temdata"+(pathData+1).toString())!!.get(DataX.get(0))!! /(DataY.get(DataY.size-1).toFloat())*(0.9f*yOri)
            path.moveTo(x,y)
            canvas?.drawPoint(x,y,mPointPaint)
            canvas?.drawText(DataMap.get("temdata"+(pathData+1).toString())!!.get(DataX.get(0)).toString(),x,y+25,mTextPaint)
        for (index in 1..Data.size-1){
            x = initX + interval*index
            y =   yOri - Data.get(DataX.get(index))!! /(DataY.get(DataY.size-1).toFloat())*(0.9f*yOri)

            var dd: java.util.HashMap<String, Int>? = DataMap.get("temdata"+(pathData+1).toString())
            y =   yOri - dd!!.get(DataX.get(index))!! /(DataY.get(DataY.size-1).toFloat())*(0.9f*yOri)
            path.lineTo(x,y)
            canvas?.drawPoint(x,y,mPointPaint)
            canvas?.drawText(dd!!.get(DataX.get(index)).toString(),x,y+35,mTextPaint)
        }
        canvas?.drawPath(path,mPaint)
        }

        //擦除超出y轴的部分
        var rectf:RectF = RectF(0f,0f,xOri,viewHeigh.toFloat())
        mCurvePaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        canvas?.drawRect(rectf,mCurvePaint)
        canvas?.restoreToCount(layerID!!)

    }
    var mStarX :Float = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->actionDown(event)
            MotionEvent.ACTION_MOVE->actionMove(event)
            MotionEvent.ACTION_UP->actionUp(event)
        }
        return true
    }
    fun actionDown(event: MotionEvent?){
        getParent().requestDisallowInterceptTouchEvent(true)
        mStarX = event!!.x
    }
    fun actionMove(event: MotionEvent?){
        getParent().requestDisallowInterceptTouchEvent(true)
        if(Data.size*interval>viewWidth - xOri){
            var distance :Float= event!!.getX() - mStarX
            mStarX = event!!.getX()
            if (initX + distance < miniX){
                initX = miniX
            }else if (initX + distance >= maxX){
                initX = maxX
            }else{
                initX = initX +distance
            }
            invalidate()
        }
    }
    fun actionUp(event: MotionEvent?){
        getParent().requestDisallowInterceptTouchEvent(true)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        //return false
        return super.dispatchTouchEvent(event)
    }
    fun isNeedY(need:Boolean){
        needY = need
        var yTextWidth = getTextBounds("0000", mTextPaint).width()
        for (index in 0..DataY.size-1) {
            var temp = getTextBounds(DataY.get(index), mTextPaint).width()
            if (temp > yTextWidth) {
                yTextWidth = temp
            }
        }

        if(need){

            xOri = 2/*view的边距*/ + yTextWidth/*y轴数据宽度*/ + 2/*y轴数与y轴间距*/ + xylinewidth / 2 * 1f

        }else{
            xOri = 2/*view的边距*/ /*+ yTextWidth*//*y轴数据宽度*/ + 2/*y轴数与y轴间距*/ + xylinewidth / 2 * 1f

        }

        invalidate()
    }
}