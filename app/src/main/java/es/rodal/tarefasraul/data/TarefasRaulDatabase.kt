package es.rodal.tarefasraul.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tarefa::class], version = 1, exportSchema = false)
abstract class TarefasRaulDatabase: RoomDatabase() {
    abstract fun tarefaDao(): TarefaDao

    companion object {

        @Volatile
        private var Instance: TarefasRaulDatabase? = null

        fun getDataBase(context: Context): TarefasRaulDatabase{
            return Instance ?: synchronized(this) { //construye la database si no esta creada, si no devuelve la misma
                Room.databaseBuilder(context, TarefasRaulDatabase::class.java, "tarefa_databse")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }

    }
}