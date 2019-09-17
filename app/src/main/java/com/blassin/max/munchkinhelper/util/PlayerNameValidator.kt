package com.blassin.max.munchkinhelper.util

import com.blassin.max.munchkinhelper.App
import com.blassin.max.munchkinhelper.R

class PlayerNameValidator(val name: String, val allNames: List<String>) {
    fun isValid(): Boolean {
        return  nameIsNotEmpty() &&
                nameIsUnique()
    }

    fun nameIsNotEmpty() = name.isNotEmpty()

    fun nameIsUnique() = !allNames.contains(name)

    fun getMessage() = when {
        !nameIsNotEmpty() -> App.instance.resources.getString(R.string.name_empty)
        !nameIsUnique() -> App.instance.resources.getString(R.string.character_exists, name)
        else -> ""
    }
}