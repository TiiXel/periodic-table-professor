package com.tiixel.periodictableprofessor.widget.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.tiixel.periodictableprofessor.R
import kotlinx.android.synthetic.main.view_error.view.*

/**
 * Widget used to display an empty state to the user
 */
class ErrorView : RelativeLayout {

    var errorAction0: (() -> Unit)? = null
    var errorAction1: (() -> Unit)? = null
    var errorAction2: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr) {
        init()
    }

    fun hide() {
        visibility = View.GONE
    }

    fun show(message: String) {
        visibility = View.VISIBLE
        text_message.text = message

        button_try_again_0.visibility = View.INVISIBLE
        button_try_again_1.visibility = View.INVISIBLE
        button_try_again_2.visibility = View.INVISIBLE
    }

    fun show(message: String, vararg buttons: ErrorButton) {
        if (buttons.size > 3) throw IllegalArgumentException("You cannot pass more than 3 buttons")

        visibility = View.VISIBLE
        text_message.text = message

        if (buttons.size > 0) {
            button_try_again_0.visibility = View.VISIBLE
            button_try_again_0.text = buttons[0].message
            errorAction0 = buttons[0].action
        } else {
            button_try_again_1.visibility = View.INVISIBLE
            button_try_again_2.visibility = View.INVISIBLE
        }
        if (buttons.size > 1) {
            button_try_again_1.visibility = View.VISIBLE
            button_try_again_1.text = buttons[1].message
            errorAction1 = buttons[1].action
        } else {
            button_try_again_2.visibility = View.INVISIBLE
        }
        if (buttons.size > 2) {
            button_try_again_2.visibility = View.VISIBLE
            button_try_again_2.text = buttons[2].message
            errorAction2 = buttons[2].action
        }
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_error, this)
        button_try_again_0.setOnClickListener { errorAction0?.invoke() }
        button_try_again_1.setOnClickListener { errorAction1?.invoke() }
        button_try_again_2.setOnClickListener { errorAction2?.invoke() }
    }
}