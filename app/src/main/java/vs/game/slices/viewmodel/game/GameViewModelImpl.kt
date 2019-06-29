package vs.game.slices.viewmodel.game

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vs.game.slices.viewmodel.repository.GameRepository
import vs.game.slices.viewmodel.utils.SingleLiveEvent
import vs.game.slices.model.GameItem
import vs.game.slices.model.GameResultItem
import vs.game.slices.model.GameResultParams
import vs.game.slices.model.SerialName

class GameViewModelImpl(private val repository: GameRepository) : GameViewModel() {

    override val state = MutableLiveData<GameState>()
    override val events = SingleLiveEvent<GameEvent>()

    private lateinit var data: List<GameItem>
    private lateinit var title: String

    private var position: Int = 0
    private val answers: MutableList<String> = mutableListOf()

    init {
        safeSubscribe {
            repository.getGameData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { game ->
                        // todo сделать маппинг на бэккграунде
                        if (game.items.size > 1) {
                            val serialNames = game.items.map { it.name }

                            title = game.title
                            data = game.items.map {
                                val correctSerialName = it.name
                                val wrongNames = serialNames
                                    .filter { serialName ->
                                        serialName.equals(correctSerialName, false).not()
                                    }

                                it.items.map { character ->
                                    GameItem(
                                        character = character,
                                        serialName = SerialName(
                                            correctName = correctSerialName,
                                            wrongName = wrongNames.random()
                                        )
                                    )
                                }
                            }.flatten().shuffled()

                            produceContent()
                        } else {
                            state.postValue(
                                GameState.Stub("Недостаточно данных для начала игры")
                            )
                        }
                    },
                    {
                        state.postValue(
                            GameState.Stub("Упс, что-то опшло не так")
                        )
                    })

        }
    }

    override fun onAnswerClicked(answer: String) {
        answers.add(answer)
        position = position.inc()

        if (position < data.size) {
            produceContent()
        } else {
            produceSwitchEvent()
        }
    }

    private fun produceContent() {
        state.postValue(
            data.getOrNull(position)?.let { gameItem ->

                GameState.Content(
                    currentItem = gameItem,
                    nextItem = data.getOrNull(position.inc()),
                    title = title
                )
            } ?: GameState.Stub("Упс, что-то опшло не так")
        )
    }

    private fun produceSwitchEvent() {
        var correctAnswers = 0

        val gameResult = data.mapIndexed { index, gameItem ->
            val isCorrectAnswer = gameItem.serialName.correctName.equals(answers[index], false)
            if (isCorrectAnswer) correctAnswers += 1

            GameResultItem(
                gameItem,
                isCorrectAnswer
            )
        }

        events.postValue(
            GameEvent.SwitchToGameResultScreen(
                GameResultParams(
                    score = "$correctAnswers / ${data.size}",
                    items = gameResult,
                    title = title
                )
            )
        )
    }
}