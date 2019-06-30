package vs.game.slices.view.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import vs.game.slices.R
import vs.game.slices.view.deviceWidth
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

    private val halfScreen = context.deviceWidth / 2
    private val zoneOffset = context.deviceWidth.toFloat() / 2f

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
        val lp = (child.layoutParams as ViewGroup.MarginLayoutParams)
        val childMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                halfScreen - lp.marginStart - lp.marginEnd,
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
}