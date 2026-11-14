package com.nuvio.app.features.remote

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Home's "View All" for the phone remote: which rows show it on the PC, the phone's taps on it, and the page it
 * opened, which the phone follows (a catalog, a collection's folders, or Continue Watching).
 */
internal object RemoteCatalog {
    /** The key of the Continue Watching row; catalog rows use their section key, collection rows their id. */
    const val CONTINUE_WATCHING = "continue"

    data class Page(val title: String, val collectionId: String? = null)

    private val _page = MutableStateFlow<Page?>(null)

    /** The "View All" page open on the PC, or null when none is. */
    val page: StateFlow<Page?> = _page.asStateFlow()

    fun publishPage(page: Page?) {
        _page.value = page
    }

    private val _viewAll = MutableStateFlow<Set<String>>(emptySet())

    /** The keys of the Home rows that show "View All" on the PC. */
    val viewAll: StateFlow<Set<String>> = _viewAll.asStateFlow()

    fun publishViewAll(keys: Set<String>) {
        _viewAll.value = keys
    }

    private val _requests = MutableSharedFlow<String>(extraBufferCapacity = 1)

    /** Row keys whose "View All" was tapped on the phone, for Home to open like its own button. */
    val requests: SharedFlow<String> = _requests.asSharedFlow()

    fun open(rowKey: String) {
        if (rowKey in _viewAll.value) _requests.tryEmit(rowKey)
    }
}
