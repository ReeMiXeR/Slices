package vs.game.slices.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sign


class SwipeBehavior<V : View> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    companion object {
        private const val MAX_ROTATION = 20f

        fun from(view: View): SwipeBehavior<View> {
            return (view.layoutParams as CoordinatorLayout.LayoutParams).behavior as SwipeBehavior<View>
        }
    }

    private var dX: Float = 0f
    private var dY: Float = 0f
    private val rect = Rect()
    private val halfScreen = context.deviceWidth / 2

    var callback: ((SwipeDirection) -> Unit)? = null


    override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {


        val zoneOffset = (child.context.deviceWidth / 3).toFloat()

        child.getGlobalVisibleRect(rect)

        val isTappedOnChild = rect.contains(ev.x.toInt(), ev.y.toInt())

        if (isTappedOnChild) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = child.x - ev.rawX
                    dY = child.y - ev.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    val rotation = min(abs(child.translationX) / halfScreen, 1f) * MAX_ROTATION * child.translationX.sign

                    child.animate()
                        .x(ev.rawX + dX)
                        .y(ev.rawY + dY)
                        .rotation(rotation)
                        .setDuration(0)
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    val (x, y) = when {
                        child.translationX > zoneOffset -> (child.context.deviceWidth.toFloat() - rect.left) to 0f
                        child.translationX < -zoneOffset -> -rect.right.toFloat() to 0f
                        else -> 0f to 0f
                    }

                    val rotation = if (child.translationX > zoneOffset || child.translationX < -zoneOffset) child.rotation else 0f

                    child.animate()
                        .translationX(x)
                        .translationY(y)
                        .rotation(rotation)
                        .setInterpolator(OvershootInterpolator(1.2f))
                        .withEndAction {
                            with (child) {
                                translationX = 0f
                                translationX = 0f
                                setRotation(0f)
                            }
//                            child.animate()
//                                .setDuration(0)
//                                .translationY(0f)
//                                .translationX(0f)
//                                .rotation(0f)
//                                .start()
                        }
                        .setDuration(300)
                        .start()

                    when {
                        child.translationX > zoneOffset -> callback?.invoke(SwipeDirection.END)
                        child.translationX < -zoneOffset -> callback?.invoke(SwipeDirection.START)
                    }
                }

                else -> {
                    return false
                }
            }
        }
        return true
    }

    enum class SwipeDirection {
        START, END
    }
}