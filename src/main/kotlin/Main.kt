import com.mongodb.client.MongoClients

const val NOM_SRV = "mongodb://Andrei:yA044SbY@34.224.116.178:27017"
const val NOM_BD = "warframeCompanion"
const val NOM_COLECCION = "warframes"

fun mostrarWarframes() {
    val cliente = MongoClients.create(NOM_SRV)
    val db = cliente.getDatabase(NOM_BD)
    val coleccion = db.getCollection(NOM_COLECCION)

    // Mostrar documentos de la colección plantas
    val cursor = coleccion.find().iterator()
    cursor.use {
        while (it.hasNext()) {
            val doc = it.next()
            println(doc.toJson())
        }
    }

    cliente.close()
}

fun main() {
    mostrarWarframes()
}