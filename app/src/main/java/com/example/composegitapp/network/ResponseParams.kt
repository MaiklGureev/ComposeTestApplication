package com.example.composegitapp.network

object ResponseParams {

    const val MIN_QUERY_LENGTH = 3
    const val PER_PAGE_DEFAULT_VALUE = 30

    object Order {
        const val DESC = "desc"
        const val ASC = "asc"
    }

    object Sort{
        const val STARS = "stars"
        const val FORKS = "forks"
        const val HELP_WANTED_ISSUES = "help-wanted-issues"
        const val UPDATED = "updated"
    }
}