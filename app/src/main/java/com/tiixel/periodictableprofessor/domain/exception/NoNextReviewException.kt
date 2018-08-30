package com.tiixel.periodictableprofessor.domain.exception

object NoNextReviewException : NoSuchElementException("You asked for upcoming reviews, but there are none.")