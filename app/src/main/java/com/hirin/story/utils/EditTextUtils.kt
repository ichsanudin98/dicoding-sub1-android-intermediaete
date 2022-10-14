package com.hirin.story.utils

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText

object EditTextUtils {
    fun disableCopyPasteOperations(editText: EditText) {
        editText.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(actionMode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(actionMode: ActionMode?) {
                //Code to implement in the future
            }
        }

        editText.isLongClickable = false
        editText.setTextIsSelectable(false)
        editText.isCursorVisible = false
        editText.movementMethod = null
    }
}