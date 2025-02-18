package io.github.sustainow.repository.routine
//
//
//import io.github.sustainow.domain.model.Routine
//import io.github.sustainow.domain.model.RoutineTask
//import io.github.sustainow.domain.model.RoutineTaskMetaData
//
//interface RoutineRepository {
//    suspend fun addTaskToRoutine(routineId: Int, task: RoutineTaskMetaData): Routine
//    suspend fun updateTaskAsComplete(taskId: Int): RoutineTask
//    suspend fun deleteTask(taskId: Int): Boolean
//    suspend fun getRoutine(userId: String): Routine?
//    suspend fun getTasks(routineId: Int): List<RoutineTask>
//
//}