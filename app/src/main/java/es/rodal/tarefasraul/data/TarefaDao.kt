package es.rodal.tarefasraul.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TarefaDao {//Data Access Object

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tarefa: Tarefa)

    @Update
    suspend fun update(tarefa: Tarefa)

    @Delete
    suspend fun delete(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas WHERE id = :id")
    fun getTarefa(id: Int): Flow<Tarefa>

    @Query("SELECT * FROM tarefas ORDER BY name ASC")
    fun getAllTarefas(): Flow<List<Tarefa>>

}