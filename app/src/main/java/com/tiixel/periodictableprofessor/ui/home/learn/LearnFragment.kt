package com.tiixel.periodictableprofessor.ui.home.learn

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tiixel.periodictableprofessor.R
import com.tiixel.periodictableprofessor.ui.elementlist.ElementTableActivity
import com.tiixel.periodictableprofessor.ui.review.ReviewActivity
import kotlinx.android.synthetic.main.home_fragment_learn.*

class LearnFragment : Fragment() {

    //<editor-fold desc="Life cycle methods">
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.home_fragment_learn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewListeners()
    }
    //</editor-fold>

    private fun setupViewListeners() {
        home_fragment_learn_browse_button.setOnClickListener {
            val intent = Intent(context, ElementTableActivity::class.java)
            startActivity(intent)
        }
        home_fragment_learn_review_button.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(ReviewActivity.Extra.DUE_TODAY_ONLY.key, true)
            startReviewActivity(bundle)
        }
        home_fragment_learn_cram_button.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(ReviewActivity.Extra.DUE_TODAY_ONLY.key, false)
            startReviewActivity(bundle)
        }
    }

    private fun startReviewActivity(bundle: Bundle) {
        val intent = Intent(context, ReviewActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}