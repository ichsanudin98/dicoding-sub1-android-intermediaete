package com.hirin.story.utils.constant.type

import com.hirin.story.R

enum class PageEnum(val route: String, val target: Int)  {
    REGISTER("route:app/register", R.id.nav_register),
    LOGIN("route:app/login", R.id.nav_login),
    WELCOME("route:app/welcome", R.id.nav_welcome),
    MOMENT_LIST("route:app/moment_list", R.id.nav_moment_list),
    MOMENT_DETAIL("route:app/moment_detail", R.id.nav_moment_detail),
}