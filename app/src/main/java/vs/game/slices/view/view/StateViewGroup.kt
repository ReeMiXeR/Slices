package vs.game.slices.view.view


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import vs.game.slices.R
import vs.game.slices.view.setGone
import vs.game.slices.view.setVisible
import vs.game.slices.view.toggleGone
import vs.game.slices.viewmodel.utils.ifBothNotNull

class StateViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var stubView: View? = null
    private var contentView: View? = null

    private var stubId = NO_ID
    private var contentId = NO_ID

    private var currentState: State = State.CONTENT

    init {
        if (isInEditMode.not()) ifBothNotNull(
            arg1 = attrs,
            arg2 = context.obtainStyledAttributes(attrs, R.styleable.StateViewGroup)
        ) { _, typedArray ->
            try {
                with(typedArray) {
                    stubId = getResourceId(R.styleable.StateViewGroup_state_stub, NO_ID)
                    contentId = getResourceId(R.styleable.StateViewGroup_state_content, NO_ID)

                    currentState = State.of(getInt(R.styleable.StateViewGroup_state_init, State.CONTENT.ordinal))
                }
            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initViews()
    }

    fun showContent() {
        currentState = State.CONTENT
        reapplyState()
    }

    fun showStub() {
        currentState = State.STUB
        reapplyState()
    }

    private fun initViews() {
        if (!isInEditMode) {
            stubView = findViewById(stubId)
            contentView = findViewById(contentId)

            visibility = View.VISIBLE

            stubView?.toggleGone(currentState == State.STUB)
            contentView?.toggleGone(currentState == State.CONTENT)
        }
    }

    private fun reapplyState() {
        when (currentState) {
            State.CONTENT -> applyViewsVisibility(contentView, stubView)
            State.STUB -> applyViewsVisibility(stubView, contentView)
        }
    }

    private fun applyViewsVisibility(showView: View?, vararg hideViews: View?) {
        hideViews.forEach {
            it?.setGone()
        }

        showView?.setVisible()
    }

    private enum class State {
        CONTENT,
        STUB;

        companion object {
            fun of(value: Int): State {
                return values().firstOrNull { it.ordinal == value } ?: CONTENT
            }
        }
    }
}
