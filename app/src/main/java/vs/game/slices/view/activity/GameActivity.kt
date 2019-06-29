package vs.game.slices.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vs.game.slices.R
import vs.game.slices.model.GameItem
import vs.game.slices.model.getShuffled
import vs.game.slices.view.SwipeBehavior
import vs.game.slices.view.setGone
import vs.game.slices.view.view.SliceView
import vs.game.slices.viewmodel.game.GameEvent
import vs.game.slices.viewmodel.game.GameState
import vs.game.slices.viewmodel.game.GameViewModel
import vs.game.slices.viewmodel.utils.exhaustive
import vs.game.slices.viewmodel.utils.singleClick


class GameActivity : AppCompatActivity() {

    companion object {
        const val ASSET_PATH = "file:///android_asset/%s.jpg"

        private const val TAG = "GameActivity"

        fun getIntent(context: Context): Intent {
            return Intent(context, GameActivity::class.java)
        }
    }

    private val viewModel by viewModel<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        observeState()
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.events.observe(this, Observer {
            when (it) {
                is GameEvent.SwitchToGameResultScreen -> {
                    finish()
                    startActivity(
                        GameResultActivity.getIntent(
                            this,
                            it.data
                        )
                    )
                }
            }.exhaustive
        })
    }

    private fun observeState() {
        viewModel.state.observe(this, Observer {
            when (it) {
                is GameState.Content -> {
                    clearFindViewByIdCache()
                    main_title_description.text = it.title
                    bindSlice(it.currentItem)
                    it.nextItem?.let {
                        slice_card_second.bind(it.character.name, it.character.imageName)
                    } ?: run {
                        slice_card_second.setGone()
                    }
                }

                is GameState.Stub -> {
                    Timber.tag(TAG).d("")
                }

                is GameState.Loading -> {
                    Timber.tag(TAG).d("")
                }
            }.exhaustive
        })
    }

    private fun bindSlice(item: GameItem) {
        with(item.character) {
            slice_card_first.bind(name, imageName)
        }

        SwipeBehavior.from(slice_card_first).callback = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                slice_card_first.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            }

            swap(slice_card_first, slice_card_second)

            when (it) {
                SwipeBehavior.SwipeDirection.START -> main_buttom_left.performClick()
                SwipeBehavior.SwipeDirection.END -> main_buttom_right.performClick()
            }.exhaustive
        }

        val (leftButtonText, rightButtonText) = item.serialName.getShuffled()

        main_buttom_right.text = rightButtonText
        main_buttom_left.text = leftButtonText

        val clickListener = View.OnClickListener {
            singleClick {
                (it as? TextView)?.let { castedView ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                    }
                    viewModel.onAnswerClicked(castedView.text.toString())
                } ?: throw IllegalStateException("Incorrect view type, use TextView instead")
            }
        }

        main_buttom_right.setOnClickListener(clickListener)
        main_buttom_left.setOnClickListener(clickListener)
    }

    private fun swap(view1: SliceView, view2: SliceView) {
        val elevation1 = view1.elevation
        val elevation2 = view2.elevation
        val id1 = view1.id
        val id2 = view2.id

        view1.id = id2
        view1.elevation = elevation2

        view2.id = id1
        view2.elevation = elevation1
    }
}