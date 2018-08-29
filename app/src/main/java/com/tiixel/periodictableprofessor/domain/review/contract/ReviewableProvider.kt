package com.tiixel.periodictableprofessor.domain.review.contract

import io.reactivex.Single

interface ReviewableProvider {

    fun getReviewableIds(): Single<List<Byte>>
}