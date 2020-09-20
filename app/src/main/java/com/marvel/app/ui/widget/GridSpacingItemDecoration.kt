package com.marvel.app.ui.widget

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(context: Context, private val spanCount: Int, spacing: Int) : RecyclerView.ItemDecoration() {

    private val paddingInDip: Int

    init {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        paddingInDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing.toFloat(), metrics).toInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        outRect.left = paddingInDip - column * paddingInDip / spanCount
        outRect.right = (column + 1) * paddingInDip / spanCount
        if (position < spanCount) {
            outRect.top = paddingInDip
        }
        outRect.bottom = paddingInDip
    }

}