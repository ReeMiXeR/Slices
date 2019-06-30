package vs.game.slices.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import vs.game.slices.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class ButtonBehavior<V : View> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    companion object {
        private const val TAG = "SwipeBehavior"
    }

    private val zoneOffset = context.deviceWidth.toFloat() / 3f

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        return dependency.id == R.id.game_slice_card_first
    }

    override fun onMeasureChild(parent: CoordinatorLayout, child: V, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        Log.e("ag", "ratio - ")
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {

        val ratio = 1f + min(abs(dependency.translationX) / zoneOffset, 0.1f)

        Log.e("ag", "ratio - $ratio")

                when {
            dependency.translationX > 0f -> {
                child.animate()
                    .scaleY(if (child.id == R.id.game_button_right) max(ratio * 1.1f, 1f) else max(ratio * 0.9f, 0.9f))
                    .scaleX(if (child.id == R.id.game_button_right) max(ratio * 1.1f, 1f) else max(ratio * 0.9f, 0.9f))
                    .setDuration(0)
                    .start()
//                child.layoutParams.height = if (child.id == R.id.game_button_right) 200.dp(child.context) else WRAP_CONTENT
            }

            dependency.translationX < 0f -> {
//                child.layoutParams.height = if (child.id == R.id.game_button_right) WRAP_CONTENT else 200.dp(child.context)
                child.animate()
                    .scaleY(if (child.id == R.id.game_button_right) max(ratio * 0.9f, 0.9f) else max(ratio * 1.1f, 1f))
                    .scaleX(if (child.id == R.id.game_button_right) max(ratio * 0.9f, 0.9f) else max(ratio * 1.1f, 1f))
                    .setDuration(0)
                    .start()
            }
            dependency.translationX == 0f -> {
//                child.layoutParams.height = WRAP_CONTENT
                child.animate()
                    .scaleY(1f)
                    .scaleX(1f)
                    .setDuration(0)
                    .start()
            }
        }
//        child.requestLayout()

        return false
    }

}