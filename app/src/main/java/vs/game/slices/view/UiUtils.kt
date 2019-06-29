package vs.game.slices.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.toggleGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setGone() {
    visibility = View.GONE
}

fun Int.dp(context: Context): Int {
    if (this == 0) {
        return 0
    }
    val metrics = context.resources?.displayMetrics
    require(metrics != null) { "Metrics is null" }
    return (this * metrics.density).toInt()
}

val Context.deviceWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.deviceHeight: Int
    get() = resources.displayMetrics.heightPixels

fun ViewGroup.childs() = Array(childCount, this::getChildAt)