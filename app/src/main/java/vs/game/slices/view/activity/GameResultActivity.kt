package vs.game.slices.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_game_result.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import vs.game.slices.R
import vs.game.slices.model.GameResultParams
import vs.game.slices.view.adapter.airport.GameResultAdapter
import vs.game.slices.view.adapter.decorator.BaseDecorator
import vs.game.slices.view.dp
import vs.game.slices.viewmodel.result.GameResultEvents
import vs.game.slices.viewmodel.result.GameResultState
import vs.game.slices.viewmodel.result.GameResultViewModel
import vs.game.slices.viewmodel.utils.exhaustive
import vs.game.slices.viewmodel.utils.singleClick

class GameResultActivity : AppCompatActivity() {

    companion object {
        const val TAG = "GameResultActivity"

        private const val KEY_PARAMS = "$TAG.params"

        fun getIntent(context: Context, params: GameResultParams): Intent {
            return Intent(context, GameResultActivity::class.java).apply {
                putExtra(KEY_PARAMS, params)
            }
        }
    }

    private val params by lazy { intent.getSerializableExtra(KEY_PARAMS) as GameResultParams }
    private val viewModel: GameResultViewModel by viewModel { parametersOf(params) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_result)

        initView()
        observeState()
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.events.observe(this, Observer {
            when (it) {
                GameResultEvents.SwitchToNewGameScreen -> {
                    finish()
                    startActivity(
                        GameActivity.getIntent(this)
                    )
                }
            }.exhaustive
        })
    }

    private fun observeState() {
        viewModel.state.observe(this, Observer {
            when (it) {
                is GameResultState.Content -> {
                    game_result_recycler.adapter = GameResultAdapter(it.data)
                    game_result_title_description.text = it.title
                    game_result_score.text = it.score
                }
            }.exhaustive
        })
    }

    private fun initView() {
        game_result_button.setOnClickListener {
            singleClick {
                viewModel.onNewGameClicked()
            }
        }
        game_result_recycler.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        game_result_recycler.addItemDecoration(BaseDecorator(8.dp(this), 80.dp(this)))

//        game_result_recycler.recycledViewPool.setMaxRecycledViews()
    }
}