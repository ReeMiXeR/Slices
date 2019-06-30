package vs.game.slices.view.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import vs.game.slices.R
import vs.game.slices.view.deviceWidth
import vs.game.slices.view.dp
import vs.game.slices.view.view.SliceView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class ButtonBehavior<V : View> @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    companion object {
        private const val MAX_SCALE = 1.2f
        private const val MIN_SCALE = 0.9f
    }

    private var secondButton: AppCompatTextView? = null
    private val halfScreen = context.deviceWidth / 2
    private val zoneOffset = context.deviceWidth.toFloat() / 2f
    private val rect = Rect()

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        return dependency is SliceView
    }

    override fun onMeasureChild(
            parent: CoordinatorLayout,
            child: V,
            parentWidthMeasureSpec: Int,
            widthUsed: Int,
            parentHeightMeasureSpec: Int,
            heightUsed: Int
    ): Boolean {
        initSecondButtonIfNeed(child, parent)

        val childMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                halfScreen - 4.dp(child.context),
                View.MeasureSpec.EXACTLY
        )
        child.measure(childMeasureSpec, View.MeasureSpec.UNSPECIFIED)
        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {

        val ratio = min(
                1f + abs(dependency.translationX) / zoneOffset,
                MAX_SCALE
        )

        val ratio2 = max(
                1 - abs(dependency.translationX / 2) / zoneOffset,
                MIN_SCALE
        )

        when {
            dependency.translationX > 0f -> {
                child.animate()
                        .scaleY(if (child.id == R.id.game_button_right) ratio else ratio2)
                        .scaleX(if (child.id == R.id.game_button_right) ratio else ratio2)
                        .setDuration(0)
                        .start()
            }

            dependency.translationX < 0f -> {
                child.animate()
                        .scaleY(if (child.id == R.id.game_button_right) ratio2 else ratio)
                        .scaleX(if (child.id == R.id.game_button_right) ratio2 else ratio)
                        .setDuration(0)
                        .start()
            }
            dependency.translationX == 0f -> {
                child.animate()
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(150)
                        .start()
            }
        }
        return false
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        child.getGlobalVisibleRect(rect)
        return rect.contains(ev.x.toInt(), ev.y.toInt())
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                child.scaleTo(1.2f, 1.2f)
                secondButton?.scaleTo(0.9f, 0.9f)
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                child.scaleTo(1f, 1f)
                secondButton?.scaleTo(1f, 1f)
            }

            else -> {
                return false
            }
        }
        child.getGlobalVisibleRect(rect)
        if (ev.action == MotionEvent.ACTION_UP && rect.contains(ev.x.toInt(), ev.y.toInt())) {
            child.performClick()
        }

        return true
    }

    private fun View?.scaleTo(x: Float, y: Float) {
        this?.animate()
                ?.scaleX(x)
                ?.scaleY(y)
                ?.setDuration(200)
                ?.start()
    }

    private fun initSecondButtonIfNeed(child: V, parent: CoordinatorLayout) {
        if (secondButton == null) {
            if (child.id == R.id.game_button_right) {
                secondButton = parent.findViewById(R.id.game_button_left)
            } else if (child.id == R.id.game_button_left) {
                secondButton = parent.findViewById(R.id.game_button_right)
            }
        }
    }
}