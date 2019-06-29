package vs.game.slices.view.view

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

class ViewGroupTarget(frameLayout: ViewGroup) : CustomViewTarget<ViewGroup, Drawable>(frameLayout) {
    override fun onLoadFailed(errorDrawable: Drawable?) {
    }

    override fun onResourceCleared(placeholder: Drawable?) {
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        view.background = resource
    }
}