package com.wanderrful.baewoo.enum

import java.time.temporal.ChronoUnit

/**
 * Source of truth for Word Ratings.
 *
 * @param rating Rating as a number, for software purposes.
 * @param displayName Rating as a string, for display purposes.
 * @param interval Represents the time needed to advance to the next rating.
 */
enum class WordRating(
    val rating: Int,
    val displayName: String,
    val interval: Long
) {
    NONE(0,
        "Not Seen",
        0),  // None
    APPRENTICE_1(1,
        "Apprentice",
        ChronoUnit.HOURS.duration.toMillis().times(4)),
    APPRENTICE_2(2,
        "Apprentice",
        ChronoUnit.HOURS.duration.toMillis().times(8)),
    APPRENTICE_3(3,
        "Apprentice",
        ChronoUnit.DAYS.duration.toMillis().times(1)),
    APPRENTICE_4(4,
        "Apprentice",
        ChronoUnit.DAYS.duration.toMillis().times(2)),
    GURU_1(5,
        "Guru",
        ChronoUnit.WEEKS.duration.toMillis().times(1)),
    GURU_2(6,
        "Guru",
        ChronoUnit.WEEKS.duration.toMillis().times(2)),
    MASTER(7,
        "Master",
        ChronoUnit.MONTHS.duration.toMillis().times(1)),
    ENLIGHTENED(8,
        "Enlightened",
        ChronoUnit.MONTHS.duration.toMillis().times(4)),
    BURNED(9,
        "Burned",
        Long.MAX_VALUE)  // Forever
}