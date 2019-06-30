package vs.game.slices.view.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import vs.game.slices.view.deviceWidth
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

typealias OnItemSwiped = ((SwipeBehavior.SwipeDirection) -> Unit)?

class SwipeBehavior<V : View> @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    companion object {
        private const val TAG = "SwipeBehavior"
        private const val MAX_ROTATION = 20f

        fun from(view: View): SwipeBehavior<View> {
            return (view.layoutParams as CoordinatorLayout.LayoutParams).behavior as SwipeBehavior<View>
        }
    }

    private var dX: Float = 0f
    private var dY: Float = 0f
    private val rect = Rect()
    private val screen = context.deviceWidth.toFloat()
    private val halfScreen = context.deviceWidth / 2
    private val zoneOffset = context.deviceWidth.toFloat() / 3f

    var listener: OnItemSwiped = null
    var isLastItem = false

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        child.getHitRect(rect)
        val isTappedOnChild = rect.contains(ev.x.toInt(), ev.y.toInt())

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isTappedOnChild) {
                    dX = child.x - ev.rawX
                    dY = child.y - ev.rawY
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isTappedOnChild) {
                    val rotation =
                            min(abs(child.translationX) / halfScreen, 1f) * MAX_ROTATION * child.translationX.sign

                    child.animate()
                            .x(ev.rawX + dX)
                            .y(ev.rawY + dY)
                            .rotation(rotation)
                            .setDuration(0)
                            .start()
                }
            }

            MotionEvent.ACTION_UP -> {
                swipeOutOrReset(child)
            }

            else -> {
                return false
            }
        }
        return true
    }

    fun swipeOutOrReset(child: V, force: SwipeDirection? = null) {
        val startX = child.translationX

        val direction = when {
            force == SwipeDirection.END || startX > zoneOffset -> SwipeDirection.END
            force == SwipeDirection.START || startX < -zoneOffset -> SwipeDirection.START
            else -> null
        }

        val (x, y) = when (direction) {
            SwipeDirection.END -> screen to child.translationY
            SwipeDirection.START -> -screen to child.translationY
            else -> 0f to 0f
        }

        val duration = max(
                if (force == null) 150L else 350L,
                (250 * min(1f, max(abs(child.translationY), abs(startX)) / halfScreen)).toLong()
        )

        val rotation = when (force) {
            SwipeDirection.START -> -MAX_ROTATION
            SwipeDirection.END -> MAX_ROTATION
            null -> 0f
        }

        val interpolator = when (force) {
            null -> OvershootInterpolator(1.4f)
            else -> DecelerateInterpolator(1.25f)
        }

        child.animate()
                .translationX(x)
                .translationY(y)
                .rotation(rotation)
                .setInterpolator(interpolator)
                .withEndAction {
                    if (isLastItem.not()) {
                        child.animate()
                                .translationX(0f)
                                .translationY(0f)
                                .rotation(0f)
                                .setDuration(0)
                                .start()
                    }

                    when (direction) {
                        SwipeDirection.END -> listener?.invoke(SwipeDirection.END)
                        SwipeDirection.START -> listener?.invoke(SwipeDirection.START)
                    }
                }
                .setDuration(duration)
                .start()
    }

    enum class SwipeDirection {
        START, END
    }
}