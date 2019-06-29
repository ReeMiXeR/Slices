package vs.game.slices.viewmodel.result

import androidx.lifecycle.MutableLiveData
import vs.game.slices.model.GameResultParams
import vs.game.slices.viewmodel.utils.SingleLiveEvent

class GameResultViewModelImpl(params: GameResultParams) : GameResultViewModel() {

    override val state = MutableLiveData<GameResultState>()
    override val events = SingleLiveEvent<GameResultEvents>()

    init {
        state.value = GameResultState.Content(
            data = params.items,
            title = params.title,
            score = params.score
        )
    }

    override fun onNewGameClicked() {
        events.value = GameResultEvents.SwitchToNewGameScreen
    }
}