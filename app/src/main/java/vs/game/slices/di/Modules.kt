package vs.game.slices.di

import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import vs.game.slices.data.GameRepositoryImpl
import vs.game.slices.model.GameResultParams
import vs.game.slices.viewmodel.game.GameViewModel
import vs.game.slices.viewmodel.game.GameViewModelImpl
import vs.game.slices.viewmodel.repository.GameRepository
import vs.game.slices.viewmodel.result.GameResultViewModel
import vs.game.slices.viewmodel.result.GameResultViewModelImpl

val appModule = module {
    single { Gson() }
}

val gameModule = module {
    factory<GameRepository> { GameRepositoryImpl(get(), get()) }
    viewModel<GameViewModel> { GameViewModelImpl(get()) }
}

val gameResultModule = module {
    viewModel<GameResultViewModel> { (params: GameResultParams) -> GameResultViewModelImpl(params) }
}