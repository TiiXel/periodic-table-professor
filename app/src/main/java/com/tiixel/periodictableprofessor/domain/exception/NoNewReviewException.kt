package com.tiixel.periodictableprofessor.domain.exception

object NoNewReviewException : NoSuchElementException("You asked for something never reviewed, but there is nothing.")