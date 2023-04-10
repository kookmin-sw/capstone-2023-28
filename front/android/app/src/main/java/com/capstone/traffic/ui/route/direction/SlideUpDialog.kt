package com.capstone.traffic.ui.route.direction

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.cardview.widget.CardView
import com.capstone.traffic.R

open class SlideUpDialog(context: Context, builder: Builder) : Dialog(
    context,
    android.R.style.Theme_Black_NoTitleBar_Fullscreen
) {

    var orgY: Float = 0f
    var containerView: CardView? = null
    var backgroundView: View? = null
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.slide_up_dialog_base)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        backgroundView = findViewById<View>(R.id.background)
        backgroundView?.alpha = 0f
        containerView = findViewById<CardView>(R.id.content_container)
        val screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        orgY = containerView!!.y
        containerView?.y = screenHeight

        builder.contentView?.let {
            containerView?.addView(it)
            setOnShowListener {
                val backAlpha = ObjectAnimator.ofFloat(backgroundView, "alpha", 1f)
                val transY: ObjectAnimator = ObjectAnimator.ofFloat(containerView!!, "translationY",
                    screenHeight, orgY)
                transY.duration = context.resources.getInteger(android.R.integer.config_longAnimTime).toLong()
                transY.interpolator = AccelerateDecelerateInterpolator()
                transY.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {}
                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                })
                val animSet = AnimatorSet()
                animSet.interpolator = DecelerateInterpolator()
                animSet.duration = 500
                animSet.playTogether(transY, backAlpha)
                animSet.start()
            }
        }
    }


    override fun cancel() {
        dismissAnim()
    }

    fun dismissAnim() {
        val screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        val backAlpha = ObjectAnimator.ofFloat(backgroundView, "alpha", 0f)
        val transY: ObjectAnimator = ObjectAnimator.ofFloat(containerView!!, "translationY",
            orgY, screenHeight)
        transY.duration = context.resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        transY.interpolator = AccelerateDecelerateInterpolator()
        transY.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                dismiss()
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        val animSet = AnimatorSet()
        animSet.interpolator = AccelerateInterpolator()
        animSet.duration = 300
        animSet.playTogether(transY, backAlpha)
        animSet.start()
    }

    open class Builder(val context: Context) {
        var contentView: View? = null

        fun setContentView(view: View): Builder {
            contentView = view
            return this
        }
        open fun create(): SlideUpDialog {
            return SlideUpDialog(context, this);
        }
    }

}