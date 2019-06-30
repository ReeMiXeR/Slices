package vs.game.slices.viewmodel.game

import androidx.lifecycle.MutableLiveData
import vs.game.slices.R
import vs.game.slices.data.exceptions.NotEnoughDataException
import vs.game.slices.model.*
import vs.game.slices.viewmodel.repository.GameRepository
import vs.game.slices.viewmodel.utils.SingleLiveEvent
import vs.game.slices.viewmodel.utils.schedulersIoToMain

class GameViewModelImpl(private val repository: GameRepository) : GameViewModel() {

    override val state = MutableLiveData<GameState>()
    override val events = SingleLiveEvent<GameEvent>()

    private lateinit var data: List<GameItem>
    private lateinit var title: String

    private var position: Int = 0
    private val answers: MutableList<String> = mutableListOf()
    private var isRedirected = false

    init {
        initData()
    }

    override fun onAnswerClicked(answer: String) {
        answers.add(answer)
        position = position.inc()

        if (position < data.size) {
            produceContent()
        } else if (isRedirected.not()) {
            produceSwitchEvent()
        }
    }

    private fun initData() {
        safeSubscribe {
            repository.getGameData()
                    .map { game ->
                        if (game.items.size <= 1) throw NotEnoughDataException()
                        else game.items.toGameList() to game.title
                    }
                    .schedulersIoToMain()
                    .doAfterSuccess { produceContent() }
                    .subscribe(
                            { (data, title) ->
                                this.data = data
                                this.title = title
                            },
                            {
                                state.value = when (it) {
                                    is NotEnoughDataException -> GameState.Stub(R.string.game_result_not_enough_error)
                                    else -> GameState.Stub(R.string.game_result_common_error)
                                }
                            })

        }
    }

    private fun List<SerialItem>.toGameList(): List<GameItem> {
        val serialNames = map { it.name }

        return map {
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
    }

    private fun produceContent() {
        state.value = data.getOrNull(position)?.let { gameItem ->
            GameState.Content(
                    currentItem = gameItem,
                    nextItem = data.getOrNull(position.inc()),
                    title = title
            )
        } ?: GameState.Stub(R.string.game_result_common_error)

    }

    private fun produceSwitchEvent() {
        isRedirected = true
        var correctAnswers = 0

        val gameResult = data.mapIndexed { index, gameItem ->
            val isCorrectAnswer = gameItem.serialName.correctName.equals(answers[index], false)
            if (isCorrectAnswer) correctAnswers += 1

            GameResultItem(
                    gameItem,
                    isCorrectAnswer
            )
        }

        events.value = GameEvent.SwitchToGameResultScreen(
                GameResultParams(
                        score = "$correctAnswers / ${data.size}",
                        items = gameResult,
                        title = title
                )
        )
    }
}