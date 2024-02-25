package com.example.chooseu.core.registration.cache.keys

enum class RegistrationKeys(
    val label: String,
    val key: String
) {
    FirstName(
        label = "First Name:",
        key = "firstName",
    ),
    LastName(
        label = "Last Name:",
        key = "lastName",
    ),
    EMAIL(
        label = "Email:",
        key = "email",
    ),
    PASSWORD(
        label = "Password:",
        key = "password",
    ),
    GENDER(
        label = "Gender:",
        key = "gender",
    ),
    BIRTHDATE(
        label = "Birth date:",
        key = "birthday",
    ),
    HEIGHT(
        label = "Height:",
        key = "height",
    ),
    HEIGHT_METRIC(
        label = "Height metric:",
        key = "heightUnit",
    ),
    WEIGHT(
        label = "Weight:",
        key = "weight",
    ),
    WEIGHTUNIT(
        label = "Weight unit:",
        key = "weightUnit",
    ),
    GoalType(
        label = "Goal Type:",
        key = "goalType",
    ),
    TargetWeight(
        label = "Targeted weight:",
        key = "targetWeight"
    ),
    AccomplishGoalByDate(
        label = "Accomplish goal by:",
        key = "accomplishGoalByDate"
    ),
    WeeklyTarget(
        label = "Weekly target:",
        key = "WeeklyTarget"
    )
}