package vs.game.slices.view

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat

const val ASSET_PATH = "file:///android_asset/%s.jpg"

fun View.toggleGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
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

fun AppCompatTextView.precomputeText(text: String) {
    setPrecomputedText(PrecomputedTextCompat.create(text, textMetricsParamsCompat))
}

fun ViewGroup.childs() = Array(childCount, this::getChildAt)

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

private object TouchLocker {
    internal var lastTouchTime: Long = 0
    internal val TOUCH_FREEZE_TIME = 300L
}

fun singleClick(delay: Long = TouchLocker.TOUCH_FREEZE_TIME, touchEvent: () -> Unit) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - delay < TouchLocker.lastTouchTime) return

    TouchLocker.lastTouchTime = currentTime
    touchEvent.invoke()
}

fun View.scaleTap() {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.animate().scaleX(0.9f).setDuration(100).start()
                v.animate().scaleY(0.9f).setDuration(100).start()
                return@setOnTouchListener false
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                v.animate().cancel()
                v.animate().scaleX(1f).setDuration(200).start()
                v.animate().scaleY(1f).setDuration(200).start()
                return@setOnTouchListener false
            }
            else -> {
                false
            }
        }
    }
}