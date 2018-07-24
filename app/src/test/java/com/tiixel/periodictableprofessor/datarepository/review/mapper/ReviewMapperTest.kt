package com.tiixel.periodictableprofessor.datarepository.review.mapper

import com.tiixel.periodictableprofessor.datarepository.review.generic.GenericReview
import com.tiixel.periodictableprofessor.domain.review.Review
import com.tiixel.periodictableprofessor.test.MockReview.e1past
import org.junit.Assume
import org.junit.Test

class ReviewMapperTest {

    // toGeneric()
    @Test
    fun toGeneric_maps() {
        val result = util_toGeneric_maps()

        assert(result)
    }

    // toDomain()
    @Test
    fun toDomain_maps() {
        Assume.assumeTrue(util_toGeneric_maps())

        val result = util_toDomain_maps()

        assert(result)
    }

    companion object {

        fun util_toGeneric_maps(): Boolean {
            val mapped = ReviewMapper.toGeneric(e1past)

            return domain_generic_are_equals(e1past, mapped)
        }

        fun util_toDomain_maps(): Boolean {
            val g1past = ReviewMapper.toGeneric(e1past)
            val mapped = ReviewMapper.toDomain(g1past)

            return domain_generic_are_equals(mapped, g1past)
        }

        fun domain_generic_are_equals(domain: Review, generic: GenericReview): Boolean {
            if (domain.item.itemId == generic.itemId)
                if (domain.face.ordinal == generic.face)
                    if (domain.reviewDate == generic.reviewDate)
                       if (domain.performance.ordinal == generic.performance)
                            if (domain.nextDueDate == generic.nextDueDate)
                                if (domain.aggregatedItemDifficulty == generic.aggregatedItemDifficulty)
                                    return true

            return false
        }
    }
}