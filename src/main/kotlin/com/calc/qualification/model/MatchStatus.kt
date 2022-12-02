package com.calc.qualification.model

enum class MatchStatus {
    in_progress,
    future_scheduled,
    completed,
    future_unscheduled;

    override fun toString(): String {
        return super.toString().replace('_', ' ').uppercase()
    }
}