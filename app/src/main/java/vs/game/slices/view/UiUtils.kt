package vs.game.slices.view

import android.content.Context
import android.view.View

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