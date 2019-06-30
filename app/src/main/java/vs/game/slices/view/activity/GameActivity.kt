package vs.game.slices.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.layout_game_content.*
import kotlinx.android.synthetic.main.layout_stub.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import vs.game.slices.R
import vs.game.slices.model.GameItem
import vs.game.slices.model.getShuffled
import vs.game.slices.view.afterMeasured
import vs.game.slices.view.behavior.SwipeBehavior
import vs.game.slices.view.setGone
import vs.game.slices.viewmodel.game.GameEvent
import vs.game.slices.viewmodel.game.GameState
import vs.game.slices.viewmodel.game.GameViewModel
import vs.game.slices.viewmodel.utils.exhaustive


class GameActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, GameActivity::class.java)
        }
    }

    private val viewModel by viewModel<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initView()
    }

    private fun initView() {
        game_content.afterMeasured {
            val size = game_button_left.top - game_title_description.bottom
            listOf(game_slice_card_first, game_slice_card_second)
                    .forEach {
                        it.layoutParams.height = size
                        it.requestLayout()
                    }
            observeState()
            observeEvents()
        }
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

                    bindMainCard(it.currentItem, it.nextItem == null)
                    it.nextItem?.let { nextItem ->
                        game_slice_card_second.bind(nextItem.character.name, nextItem.character.imageName)
                    } ?: run {
                        game_slice_card_second.setGone()
                    }
                }

                is GameState.Stub -> {
                    game_container.showStub()
                    game_stub.text = getString(it.error)
                }

                is GameState.Loading -> {
                    game_container.showLoading()
                }
            }.exhaustive
        })
    }

    private fun bindMainCard(item: GameItem, isLastItem: Boolean) {
        with(item.character) {
            game_slice_card_first.bind(name, imageName)
        }

        SwipeBehavior.from(game_slice_card_first).apply {
            this.isLastItem = isLastItem
            listener = {
                restoreButtons()
                viewModel.onAnswerClicked(
                        when (it) {
                            SwipeBehavior.SwipeDirection.START -> {
                                game_button_left.text.toString()
                            }

                            SwipeBehavior.SwipeDirection.END -> {
                                game_button_right.text.toString()
                            }
                        }
                )
            }
        }

        val (leftButtonText, rightButtonText) = item.serialName.getShuffled()
        game_button_right.text = rightButtonText
        game_button_left.text = leftButtonText

        val clickListener = View.OnClickListener {
            SwipeBehavior.from(game_slice_card_first).swipeOutOrReset(
                    child = game_slice_card_first,
                    force = when {
                        it.id == R.id.game_button_right -> SwipeBehavior.SwipeDirection.END
                        else -> SwipeBehavior.SwipeDirection.START
                    }
            )
        }
        game_button_right.setOnClickListener(clickListener)
        game_button_left.setOnClickListener(clickListener)
    }

    private fun restoreButtons() {
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