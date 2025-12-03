import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import org.bson.Document
import org.bson.json.JsonWriterSettings
import java.io.File
import java.util.Scanner

const val NOM_SRV = "mongodb://Andrei:yA044SbY@34.224.116.178:27017"
const val NOM_BD = "warframeCompanion"
const val NOM_COLECCION = "warframes"

fun iteradorNumerosValidosInteger(descripcionNumero: String): Int {
    //método usado para pedir numeros enteros válidos
    //recibe un string con el nombre del numero pedido
    val scanner = Scanner(System. `in`)
    var numeroValido: Int? = null
    while (numeroValido == null) {
        val entrada = scanner.nextLine()
        numeroValido = entrada.toIntOrNull()
        if (numeroValido == null) {
            println(descripcionNumero + " debe ser un número!!: ")
        }
    }
    return numeroValido
}

fun iteradorNumerosValidosDouble(descripcionNumero: String): Double {
    //método usado para pedir numeros decimales válidos
    //recibe un string con el nombre del numero pedido
    val scanner = Scanner(System. `in`)
    var numeroValido: Double? = null
    while (numeroValido == null) {
        val entrada = scanner.nextLine()
        numeroValido = entrada.toDoubleOrNull()
        if (numeroValido == null) {
            println(descripcionNumero + " debe ser un número!!: ")
        }
    }
    return numeroValido
}

fun mostrarWarframes() {
    val cliente = MongoClients.create(NOM_SRV)
    val db = cliente.getDatabase(NOM_BD)
    val coleccion = db.getCollection(NOM_COLECCION)

    // Mostrar documentos de la colección plantas
    val cursor = coleccion.find().iterator()
    cursor.use {
        while (it.hasNext()) {
            val doc = it.next()

            val warframeId = doc.getInteger("warframe_id")
            val warframeName = doc.getString("name")
            val warframeHealth = doc.getInteger("health")
            val warframeArmor = doc.getInteger("armor")
            val warframeEnergy = doc.getInteger("energy")
            val warframeSprintSpeed = doc.getDouble("sprint_speed")
            val warframePassive = doc.getString("passive")

            println("Warframe [${warframeId}]" +
                    "\n\tName: $warframeName" +
                    "\n\tHealth: $warframeHealth" +
                    "\n\tArmor: $warframeArmor" +
                    "\n\tEnergy: $warframeEnergy" +
                    "\n\tSprint Speed: $warframeSprintSpeed" +
                    "\n\tPassive: $warframePassive")
        }
    }

    cliente.close()
}

fun insertarWarframe() {
    val scanner = Scanner(System. `in`)

    val cliente = MongoClients.create(NOM_SRV)
    val db = cliente.getDatabase((NOM_BD))
    val coleccion = db.getCollection(NOM_COLECCION)

    println("ID del Warframe: ")
    val id_warframe = iteradorNumerosValidosInteger("El ID")
    println("Nombre del Warframe: ")
    val nombre = scanner.nextLine()
    println("Vida del Warframe: ")
    val vida = iteradorNumerosValidosInteger("La vida")
    println("Armadura del Warframe: ")
    val armadura = iteradorNumerosValidosInteger("La armadura")
    println("Energía del Warframe: ")
    val energia = iteradorNumerosValidosInteger("La energía")
    println("Velocidad Sprint: ")
    val velocidadSprint = iteradorNumerosValidosDouble("la velocidad de sprint")
    println("Pasiva: ")
    val pasiva = scanner.nextLine()

    val doc = Document("warframe_id", id_warframe)
        .append("name", nombre)
        .append("health", vida)
        .append("armor", armadura)
        .append("energy", energia)
        .append("sprint_speed", velocidadSprint)
        .append("passive", pasiva)

    coleccion.insertOne(doc)
    println("Warframe insertado con ID: ${doc.getObjectId("_id")}")

    cliente.close()
    println("Conexión cerrada")
}

fun actualizarVidaWarframe() {
    val scanner = Scanner(System. `in`)

    val cliente = MongoClients.create(NOM_SRV)
    val db = cliente.getDatabase(NOM_BD)
    val coleccion = db.getCollection(NOM_COLECCION)

    var id_warframe = iteradorNumerosValidosInteger("El ID")
    var warframe = coleccion.find(Filters.eq("warframe_id", id_warframe)).firstOrNull()

    if (warframe == null) {
        println("No se encontró ningun warframe con ID = \"$id_warframe\".")
    }
    else {
        println("Warframe encontrado: ${warframe.getString("name")} (vida: ${warframe.get("health")})")

        println("La nueva vida sera: ")
        var vida = iteradorNumerosValidosInteger("La vida")

        // Actualizar el documento
        val result = coleccion.updateOne(
            Filters.eq("warframe_id", id_warframe),
            Document("\$set", Document("health", vida))
        )

        if (result.modifiedCount > 0)
            println("Vida actualizada correctamente (${result.modifiedCount} documento modificado).")
        else
            println("No se modificó ningún documento (la vida quizá ya era la misma).")
    }

    cliente.close()
    println("Conexión cerrada.")
}

fun eliminarWarframe() {
    val cliente = MongoClients.create(NOM_SRV)
    val db = cliente.getDatabase(NOM_BD)
    val coleccion = db.getCollection(NOM_COLECCION)

    println("ID del warframe a eliminar: ")
    var id_warframe = iteradorNumerosValidosInteger("El ID")

    val result = coleccion.deleteOne(Filters.eq("warframe_id", id_warframe))
    if (result.deletedCount > 0)
        println("Warframe eliminado correctamente.")
    else
        println("No se encontró ningun warframa con ese ID.")

    cliente.close()
    println("Conexión cerrada.")
}

fun consultarWarframesConFiltros() {
    val client = MongoClients.create(NOM_SRV)
    val col = client.getDatabase(NOM_BD).getCollection(NOM_COLECCION)

    println("***** Warframes con más de 300 de vida")
    col.find(Filters.gt("health", 300)).forEach { println(it.toJson()) }

    println("\n***** Warframes con armadura menor a 200")
    col.find(Filters.lt("armor", 200)).forEach { println(it.toJson()) }

    println("\n***** Warframe con ID 5")
    col.find(Filters.eq("warframe_id", 5)).forEach { println(it.toJson()) }

    println("\n***** Warframes con velocidad de sprint >= 1.2")
    col.find(Filters.gte("sprint_speed", 1.2)).forEach { println(it.toJson()) }

    client.close()
}

fun proyeccionesWarframes() {
    val client = MongoClients.create(NOM_SRV)
    val col = client.getDatabase(NOM_BD).getCollection(NOM_COLECCION)

    println("***** Solo nombres de los Warframes")
    col.find()
        .projection(Projections.include("name"))
        .forEach { println(it.toJson()) }

    println("\n***** Nombres y vida de los Warframes")
    col.find()
        .projection(Projections.include("name", "health"))
        .forEach { println(it.toJson()) }

    client.close()
}

fun agregacionesWarframes() {
    val client = MongoClients.create(NOM_SRV)
    val col = client.getDatabase(NOM_BD).getCollection(NOM_COLECCION)

    // 1) Promedio de health
    println("***** Vida media de los Warframes")
    val avgPipeline = listOf(
        Document("\$group", Document("_id", null)
            .append("vidaMedia", Document("\$avg", "\$health")))
    )
    col.aggregate(avgPipeline).forEach { println(it.toJson()) }

    // 2) Armadura Máxima
    println("\n***** Armadura máxima")
    val maxPipeline = listOf(
        Document("\$group", Document("_id", null)
            .append("maxArmor", Document("\$max", "\$armor")))
    )
    col.aggregate(maxPipeline).forEach { println(it.toJson()) }

    // 3) Número total de Warframes
    println("\n***** Cantidad total de Warframes")
    val countPipeline = listOf(
        Document("\$group", Document("_id", null)
            .append("totalWarframes", Document("\$sum", 1)))
    )
    col.aggregate(countPipeline).forEach { println(it.toJson()) }

    client.close()
}

fun exportarBD(coleccion: MongoCollection<Document>, rutaJSON: String) {
    val settings = JsonWriterSettings.builder().indent(true).build()
    val file = File(rutaJSON)
    file.printWriter().use { out ->
        out.println("[")
        val cursor = coleccion.find().iterator()
        var first = true
        while (cursor.hasNext()) {
            if (!first) out.println(",")
            val doc = cursor.next()
            out.print(doc.toJson(settings))
            first = false
        }
        out.println("]")
        cursor.close()
    }

    println("Exportación de ${coleccion.namespace.collectionName} completada")
}

fun exportarWarframes() {
    val client = MongoClients.create(NOM_SRV)
    val db = client.getDatabase(NOM_BD)
    val col = db.getCollection(NOM_COLECCION)

    val ruta = "warframes.json"

    exportarBD(col, ruta)

    client.close()
}

fun main() {

    val scanner = Scanner(System.`in`)
    var opcion: Int? = null

    do {
        println("***Menu Warframes***")
        println(
            "\t1- Listar todos los Warframes" +
                    "\n\t2- Insertar un nuevo Warframe" +
                    "\n\t3- Actualizar un warframe" +
                    "\n\t4- Eliminar un warframe (por ID)" +
                    "\n\t5- Consultas con filtros" +
                    "\n\t6- Mostrar campos específicos" +
                    "\n\t7- Agregaciones" +
                    "\n\t8- Exportar la base de datos a archivo JSON"+
                    "\n\t0- Salir del programa"
        )


        opcion = scanner.nextLine().toIntOrNull()

        when (opcion) {
            1 -> {
                mostrarWarframes()
            }

            2 -> {
                insertarWarframe()
            }

            3 -> {
                actualizarVidaWarframe()
            }

            4 -> {
                eliminarWarframe()
            }

            5 -> {
                consultarWarframesConFiltros()
            }

            6 -> {
                proyeccionesWarframes()
            }

            7 -> {
                agregacionesWarframes()
            }

            8 -> {
                exportarWarframes()
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida. Intenta de nuevo")

        }
    }while (opcion != 0)
}