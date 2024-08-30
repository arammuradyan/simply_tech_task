package com.example.simplytask.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavBarItem(
    @DrawableRes val icon: Int,
    @StringRes val descriptionId: Int,
    val isSelected: Boolean = false,
)

data class ActionItem(
    @DrawableRes val icon: Int,
    @StringRes val descriptionId: Int,
    val actionType: ActionType = ActionType.UNSPECIFIED
)

enum class ActionType {
    LOCK, UNLOCK, UNSPECIFIED
}

