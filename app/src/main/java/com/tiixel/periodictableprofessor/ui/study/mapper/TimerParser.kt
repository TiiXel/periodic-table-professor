package com.tiixel.periodictableprofessor.ui.study.mapper

import android.content.Context

object TimerParser {

    fun timerToString(context: Context, dhm: List<Int>?): String {
        return if (dhm == null) {
            "Now"
        } else {
            var string = ""
            when {
                dhm[0] > 0 -> string = "${dhm[0]}d ${dhm[1]}h ${dhm[2]}min"
                dhm[1] > 0 -> string = "${dhm[1]}h ${dhm[2]}min"
                dhm[2] > 0 -> string = "${dhm[2]}min"
                else -> string = "${dhm[3]}s"
            }
            string
        }
    }
}