package com.hirin.story.utils

import com.hirin.story.utils.constant.type.SnackBarEnum

data class SnackBarData(val type: SnackBarEnum, val messageRes: Int?, val messageStr: String = "")