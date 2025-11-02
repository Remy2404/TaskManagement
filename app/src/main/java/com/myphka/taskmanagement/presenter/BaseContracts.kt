package com.myphka.taskmanagement.presenter

/**
 * Base interface for all Views in MVP pattern
 */
interface BaseView

/**
 * Base interface for all Presenters in MVP pattern
 */
interface BasePresenter<V : BaseView> {
    fun attachView(view: V)
    fun detachView()
}