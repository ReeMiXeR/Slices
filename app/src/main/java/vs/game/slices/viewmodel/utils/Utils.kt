package vs.game.slices.viewmodel.utils

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