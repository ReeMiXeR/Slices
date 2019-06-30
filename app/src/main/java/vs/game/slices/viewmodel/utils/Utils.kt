package vs.game.slices.viewmodel.utils

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val <T> T.exhaustive: T
    get() = this

fun <T> Single<T>.schedulersIoToMain(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

inline fun <T1, T2> ifBothNotNull(arg1: T1?, arg2: T2?, block: (T1, T2) -> Unit): Unit? {
    return if (arg1 != null && arg2 != null) {
        block(arg1, arg2)
    } else null
}