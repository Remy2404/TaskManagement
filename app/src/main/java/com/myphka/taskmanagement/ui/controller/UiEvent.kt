package com.myphka.taskmanagement.ui.controller

/**
 * Sealed class representing UI events that can be emitted by the controller.
 * These are one-shot events like navigation, snackbar messages, etc.
 */
sealed class UiEvent {
    object NavigateToAddProject : UiEvent()
    object NavigateToAddTask : UiEvent()
    data class ShowError(val message: String) : UiEvent()
    data class ShowMessage(val message: String) : UiEvent()
}
