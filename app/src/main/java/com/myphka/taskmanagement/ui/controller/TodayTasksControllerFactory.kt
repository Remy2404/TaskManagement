package com.myphka.taskmanagement.ui.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.presenter.TodayTasksPresenterImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Factory for creating TodayTasksController instances.
 * This provides a simple way to create controllers without requiring DI frameworks.
 */
@RequiresApi(Build.VERSION_CODES.O)
object TodayTasksControllerFactory {
    
    /**
     * Creates a new TodayTasksControllerImpl instance.
     * 
     * @param scope Optional coroutine scope. If not provided, a default scope is created.
     *              Should use Dispatchers.Main for UI state updates.
     * @return A new TodayTasksController instance
     */
    fun create(
        scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    ): TodayTasksController {
        return TodayTasksControllerImpl(scope)
    }

    /**
     * Creates an adapter that wraps an existing presenter for backward compatibility.
     * Use this during migration to allow existing presenter-based code to work with
     * controller-based views.
     * 
     * @param presenter The existing TodayTasksPresenterImpl to adapt
     * @param scope Optional coroutine scope. If not provided, a default scope is created.
     *              Should use Dispatchers.Main for UI state updates.
     * @return A TodayTasksController that wraps the presenter
     */
    fun createFromPresenter(
        presenter: TodayTasksPresenterImpl,
        scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    ): TodayTasksController {
        return PresenterControllerAdapter(presenter, scope)
    }
}
