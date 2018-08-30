package com.tiixel.periodictableprofessor.domain.exception

object NoNextReviewSoonException : NoSuchElementException("You asked for upcoming reviews due soon, but there are none.") {
}