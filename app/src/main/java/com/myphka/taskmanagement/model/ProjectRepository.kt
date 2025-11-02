package com.myphka.taskmanagement.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface ProjectRepository {
    fun getAllProjects(): Flow<List<Project>>
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(projectId: String)
}

class ProjectRepositoryImpl : ProjectRepository {
    private val projects = mutableListOf<Project>()

    override fun getAllProjects(): Flow<List<Project>> = flowOf(projects)

    override suspend fun addProject(project: Project) {
        projects.add(project)
    }

    override suspend fun updateProject(project: Project) {
        val index = projects.indexOfFirst { it.id == project.id }
        if (index != -1) {
            projects[index] = project
        }
    }

    override suspend fun deleteProject(projectId: String) {
        projects.removeAll { it.id == projectId }
    }
}
