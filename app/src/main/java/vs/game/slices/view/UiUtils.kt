package vs.game.slices.view

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.toggleGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun Int.dp(context: Context): Int {
    if (this == 0) {
        return 0
    }
    val metrics = context.resources?.displayMetrics
    require(metrics != null) { "Metrics is null" }
    return (this * metrics.density).toInt()
}

fun View.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(context, id)

val Context.deviceWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.deviceHeight: Int
    get() = resources.displayMetrics.heightPixels