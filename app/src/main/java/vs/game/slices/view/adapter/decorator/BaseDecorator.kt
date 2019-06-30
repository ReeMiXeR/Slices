package vs.game.slices.view.adapter.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseDecorator(
        private val defaultPadding: Int,
        private val endBottomPadding: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val childPosition = parent.getChildAdapterPosition(view)

        with(outRect) {
            top = defaultPadding
            when (childPosition % 2) {
                0 -> {
                    right = defaultPadding / 2
                    left = defaultPadding * 2
                }

                1 -> {
                    left = defaultPadding / 2
                    right = defaultPadding * 2
                }
            }
            when (state.itemCount % 2) {
                0 -> if (childPosition in (state.itemCount - 2) until state.itemCount) {
                    outRect.bottom = endBottomPadding
                }

                1 -> if (state.itemCount - 1 == childPosition) {
                    outRect.bottom = endBottomPadding
                }

            }
        }
    }
}