package com.sielee.edvora.adapters

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemScrollIndicator():RecyclerView.ItemDecoration() {
    private val colorActive = Color.parseColor("#FFFFFFFF")
    private val colorInActive = Color.parseColor("#808080")


    private val indicatorHeight = (DP * 28)

    private val indicatorStrokeWidth = DP * 6

    private val indicatorItemLength = (DP * 0.1).toFloat()

    private val indicatorPadding = DP * 14

    private val interpolator = AccelerateDecelerateInterpolator()

    private val paint = Paint()

    init {
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = indicatorStrokeWidth.toFloat()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val itemCount = parent.adapter!!.itemCount

        val totalLength = indicatorItemLength * itemCount
        val paddingBetweenItems = Math.max(0,itemCount-1) * indicatorPadding
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX = (parent.width -indicatorTotalWidth) /2f

        val indicatorPosY = parent.height -indicatorHeight / 2f

        drawInactiveIndicators(c,indicatorStartX,indicatorPosY,itemCount)

        val layoutManager = parent.layoutManager as LinearLayoutManager
        val activePosition = layoutManager.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION){
            return
        }

        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild!!.left
        val width = activeChild.width

        val scrollProgress = interpolator.getInterpolation(left *-1 / width.toFloat())

        drawHighLights(c,indicatorStartX,indicatorPosY,activePosition,scrollProgress,itemCount)

    }

    private fun drawHighLights(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, activePosition: Int, scrollProgress: Float, itemCount: Int) {

        paint.color = colorActive

        val itemWidth = indicatorItemLength + indicatorPadding

        if (scrollProgress == 0f){
            val highLightStart = indicatorStartX + itemWidth * activePosition

            c.drawLine(highLightStart,indicatorPosY,highLightStart + indicatorItemLength, indicatorPosY, paint)

        }else{
            var highLightStart = indicatorStartX + itemWidth * activePosition

            val partialLength = indicatorItemLength * scrollProgress

            c.drawLine(highLightStart + partialLength, indicatorPosY,
                highLightStart + indicatorItemLength,indicatorPosY,paint)
            if (activePosition<itemCount - 1){
                highLightStart +=itemWidth
                c.drawLine(
                    highLightStart, indicatorPosY,
                    highLightStart+ partialLength,
                    indicatorPosY, paint
                )
            }
        }


    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = indicatorHeight.toInt()
    }

    private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
        paint.color =colorInActive

        val itemWidth = indicatorItemLength + indicatorPadding
        var start = indicatorStartX

        for (i in 0 until itemCount){
            c.drawLine(start,indicatorPosY,start+ indicatorItemLength, indicatorPosY,paint)
            start+=itemWidth
        }

    }

    companion object {
        private val DP = Resources.getSystem().displayMetrics.density
    }

}