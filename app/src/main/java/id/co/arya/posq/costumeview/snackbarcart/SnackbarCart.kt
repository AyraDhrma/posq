package id.co.arya.posq.costumeview.snackbarcart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import id.co.arya.posq.R

class SnackbarCart(
    parent: ViewGroup,
    content: SnackbarCartView
) : BaseTransientBottomBar<SnackbarCart>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(
                view: View,
                message: String, duretion: Int,
                listener: View.OnClickListener?, icon: Int?, action_lable: String?, bg_color: Int
        ): SnackbarCart? {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            try {
                val customView = LayoutInflater.from(view.context).inflate(
                    R.layout.snackbar_cart,
                    parent,
                    false
                ) as SnackbarCartView
                // We create and return our Snackbar
                customView.tvMsg.text = message
                action_lable?.let {
                    customView.tvAction.text = action_lable
                    customView.tvAction.setOnClickListener {
                        listener?.onClick(customView.tvAction)
                    }
                }
                icon?.let { customView.imLeft.setImageResource(it) }
                customView.layRoot.setBackgroundColor(bg_color)


                return SnackbarCart(
                    parent,
                    customView
                ).setDuration(duretion)
            } catch (e: Exception) {
                e.message?.let { Log.v("exception ", it) }
            }

            return null
        }

        internal fun View?.findSuitableParent(): ViewGroup? {
            var view = this
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    // We've found a CoordinatorLayout, use it
                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {
                        // If we've hit the decor content view, then we didn't find a CoL in the
                        // hierarchy, so use it.
                        return view
                    } else {
                        // It's not the content view but we'll use it as our fallback
                        fallback = view
                    }
                }

                if (view != null) {
                    // Else, we will loop and crawl up the view hierarchy and try to find a parent
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)

            // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
            return fallback
        }

    }

}
