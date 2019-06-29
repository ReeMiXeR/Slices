package vs.game.slices.viewmodel.utils

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val <T> T.exhaustive: T
    get() = this

private object TouchLocker {
    internal var lastTouchTime: Long = 0
    internal val TOUCH_FREEZE_TIME = 300L
}

fun singleClick(delay: Long = TouchLocker.TOUCH_FREEZE_TIME, touchEvent: () -> Unit) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - delay < TouchLocker.lastTouchTime) return

    TouchLocker.lastTouchTime = currentTime
    touchEvent.invoke()
}

fun <T> Single<T>.schedulersIoToMain(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}