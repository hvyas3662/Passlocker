package com.elevationsoft.passlocker.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SearchQueryUtils {

    private const val TYPE_NEXT_LATTER_DELAY = 500L
    private var searchQueryTextWatcher: TextWatcher? = null
    private var searchQueryJob: Job? = null
    private var lastQuery = ""

    fun EditText.setSearchQueryListener(lifecycle: Lifecycle, block: (String) -> Unit) {
        if (searchQueryTextWatcher != null) {
            removeTextChangedListener(searchQueryTextWatcher)
        }

        searchQueryTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                searchQueryJob?.cancel()
                searchQueryJob = lifecycle.coroutineScope.launch {
                    lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        if (!lastQuery.equals(query, ignoreCase = true)) {
                            delay(TYPE_NEXT_LATTER_DELAY)
                            lastQuery = query
                            block(query)
                        }
                    }
                }
            }
        }

        addTextChangedListener(searchQueryTextWatcher)
    }

    fun cancelJob() {
        searchQueryJob?.cancel()
    }
}