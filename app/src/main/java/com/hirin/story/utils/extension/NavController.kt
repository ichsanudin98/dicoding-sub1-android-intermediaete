package com.hirin.story.utils.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.hirin.story.R

private fun buildHorizontalAnimOptions(
    popupTo: Int?,
    inclusive: Boolean = false
): NavOptions {
    val builder = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
    (builder)
    if (popupTo != null) builder.setPopUpTo(popupTo, inclusive)
    return builder.build()
}

fun NavController.navigateSlideHorizontalById(
    currentDestinationId: Int,
    direction: Int,
    args: Bundle?,
    popupTo: Int? = null,
    inclusive: Boolean = false
) {
    val options = buildHorizontalAnimOptions(popupTo, inclusive)
    safeNavigate(currentDestinationId, direction, args, options)
}

fun NavController.navigateSlideHorizontal(
    navDirections: NavDirections,
    popupTo: Int? = null,
    inclusive: Boolean = false
) {
    val options = buildHorizontalAnimOptions(popupTo, inclusive)
    safeNavigate(navDirections, options)
}

fun NavController.safeNavigate(direction: NavDirections, options: NavOptions) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction,options) }
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun NavController.safeNavigate(
    @IdRes currentDestinationId: Int,
    @IdRes id: Int,
    args: Bundle? = null,
    options: NavOptions
) {
    if (currentDestinationId == currentDestination?.id) {
        navigate(id, args, options)
    }
}