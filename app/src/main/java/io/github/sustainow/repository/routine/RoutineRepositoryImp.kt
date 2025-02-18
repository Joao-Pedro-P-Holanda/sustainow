package io.github.sustainow.repository.routine


import io.github.jan.supabase.SupabaseClient
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.domain.model.RoutineTaskMetaData
import io.github.sustainow.repository.mapper.SupabaseMapper
import javax.inject.Inject


class RoutineRepositoryImp @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val supabaseMapper: SupabaseMapper,
    private val taskTableName: String,
    private val routineTableName: String,
    private val taskMetadataTableName: String,
) : RoutineRepository {
    override suspend fun addTaskToRoutine(routineId: Int, task: RoutineTaskMetaData): Routine {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskAsComplete(taskId: Int): RoutineTask {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getRoutine(userId: String): Routine? {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(routineId: Int): List<RoutineTask> {
        TODO("Not yet implemented")
    }

}