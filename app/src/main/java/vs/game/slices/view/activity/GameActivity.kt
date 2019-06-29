package vs.game.slices.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import vs.game.slices.R
import vs.game.slices.model.GameItem
import vs.game.slices.model.getShuffled
import vs.game.slices.view.dp
import vs.game.slices.view.view.ViewGroupTarget
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
                    main_title_description.text = it.title
                    bindCard(it.currentItem)
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

    private fun bindCard(item: GameItem) {
        with(item.character) {
            main_item_title.text = name

            Glide.with(this@GameActivity)
                .load(Uri.parse(ASSET_PATH.format(imageName)))
                .transform(CenterCrop(), RoundedCorners(16.dp(this@GameActivity)))
                .placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(main_character_card_image)
        }

        val (leftButtonText, rightButtonText) = item.serialName.getShuffled()

        main_buttom_right.text = rightButtonText
        main_buttom_left.text = leftButtonText

        val clickListener = View.OnClickListener {
            singleClick {
                (it as? TextView)?.let { castedView ->
                    viewModel.onAnswerClicked(castedView.text.toString())
                } ?: throw IllegalStateException("Incorrect view type, use TextView instead")
            }
        }

        main_buttom_right.setOnClickListener(clickListener)
        main_buttom_left.setOnClickListener(clickListener)
    }
}