package vs.game.slices.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import vs.game.slices.view.view.SliceView
import vs.game.slices.viewmodel.game.GameEvent
import vs.game.slices.viewmodel.game.GameState
import vs.game.slices.viewmodel.game.GameViewModel
import vs.game.slices.viewmodel.utils.exhaustive


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
                    game_container.showContent()
                    game_title_description.text = it.title
                    bindSlice(it.currentItem, it.nextItem == null)

                    it.nextItem?.let { nextItem ->
                        game_slice_card_second.bind(nextItem.character.name, nextItem.character.imageName)
                    } ?: run {
                        game_slice_card_second.alpha = 0f
                    }
                }

                is GameState.Stub -> {
                    game_container.showStub()
                    game_stub.text = it.error
                }

                is GameState.Loading -> {
                    game_container.showLoading()
                }
            }.exhaustive
        })
    }

    private fun bindSlice(item: GameItem, isLastItem: Boolean) {
        with(item.character) {
            game_slice_card_first.bind(name, imageName)
        }

        SwipeBehavior.from(game_slice_card_first).apply {
            this.isLastItem = isLastItem
            callback = {
                swap(game_slice_card_first, game_slice_card_second)
                when (it) {
                    SwipeBehavior.SwipeDirection.START -> game_button_left.performClick()
                    SwipeBehavior.SwipeDirection.END -> game_button_right.performClick()
                }.exhaustive
            }
        }

        val (leftButtonText, rightButtonText) = item.serialName.getShuffled()

        game_button_right.text = rightButtonText
        game_button_left.text = leftButtonText

        val clickListener = View.OnClickListener {
            (it as? TextView)?.let { castedView ->
                viewModel.onAnswerClicked(castedView.text.toString())
            } ?: throw IllegalStateException("Incorrect view type, use TextView instead")
        }

        game_button_right.setOnClickListener(clickListener)
        game_button_left.setOnClickListener(clickListener)
    }

    private fun swap(view1: SliceView, view2: SliceView) {
        clearFindViewByIdCache()
        val elevation1 = view1.elevation
        val elevation2 = view2.elevation
        val id1 = view1.id
        val id2 = view2.id

        view1.id = id2
        view1.elevation = elevation2

        view2.id = id1
        view2.elevation = elevation1

        listOf(game_button_left, game_button_right)
                .forEach {
                    it.animate()
                            .scaleY(1f)
                            .scaleX(1f)
                            .setDuration(150)
                            .start()
                }
    }
}