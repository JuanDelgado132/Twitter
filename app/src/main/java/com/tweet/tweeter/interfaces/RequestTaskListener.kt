package com.tweet.tweeter.interfaces

/**
 * @author Juan Delgado
 * Class will allow the UI to perform certain functions after a task has been sent.
 */
interface RequestTaskListener {
    /**
     * Callback method that the async task will use to update the main UI.
     */
    fun onRequestFinished()
}