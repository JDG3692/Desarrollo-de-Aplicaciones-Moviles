package com.example.agendapersonal

import com.example.agendapersonal.model.AgendaDatabase

// Cierra y olvida la instancia única de la base de datos. Robolectric usa una
// carpeta nueva en cada prueba, pero los campos estáticos se conservan.
fun reiniciarBaseDeDatos() {
    val campo = AgendaDatabase::class.java.getDeclaredField("INSTANCE")
    campo.isAccessible = true
    (campo.get(null) as AgendaDatabase?)?.close()
    campo.set(null, null)
}
