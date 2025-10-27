package com.example.almacercaapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.almacercaapp.data.local.user.UserDao
import com.example.almacercaapp.data.local.user.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// @Database registra las entidades (tablas) y la versión del esquema.
@Database(
    entities = [UserEntity::class], // Lista de entidades (tablas) en la base de datos
    version = 1,                    // Incrementa esta versión cuando modifiques la base de datos
    exportSchema = true             // Permite exportar el esquema de la base de datos
)
abstract class AppDatabase : RoomDatabase() {

    //declara una funcion abstracto para obtener el dao de usuarios
    // Exponemos el DAO de usuarios. Room implementará esto.
    abstract fun userDao(): UserDao

    companion object { // Objeto de compilación (companion object), contiene la logica para crear y obtener la unica instancia de la bd
        //Asegura que haya una conexion a ala base de datos
        @Volatile // Asegura que INSTANCE sea siempre visible para todos los hilos
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "almacerca_app.db" // Nombre del archivo de la base de datos

        // Obtiene la instancia única (Singleton) de la base de datos
        fun getInstance(context: Context): AppDatabase {
            // Si ya existe una instancia, la devuelve. Si no, crea una nueva.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    // Callback para ejecutar código la PRIMERA VEZ que se crea la base de datos
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Usamos una corutina para insertar datos iniciales (opcional)
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).userDao()
                                // Puedes añadir usuarios de prueba aquí si quieres
                                // Ejemplo:
                                // val seedUser = UserEntity(name="Test", email="test@test.com", phone="12345678", password="Password1!")
                                // dao.insert(seedUser)
                            }
                        }
                    })
                    // Si cambias la versión sin definir una migración, destruye y recrea la BD
                    // Útil durante el desarrollo, pero ¡CUIDADO! borra todos los datos.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance // Guarda la instancia creada
                instance // Devuelve la instancia
            }
        }
    }
}