import com.mongodb.client.MongoClients
import org.bson.Document
import java.util.Scanner

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

fun iteradorNumerosValidos(descripcionNumero: String): Int {
    //método usado para pedir numeros válidos
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


fun insertarWarframe() {
    val scanner = Scanner(System. `in`)

    val cliente = MongoClients.create(NOM_SRV)
    val db = cliente.getDatabase((NOM_BD))
    val coleccion = db.getCollection(NOM_COLECCION)

    print("ID del Warframe: ")
    val id_warframe = iteradorNumerosValidos("El ID")
    print("Nombre del Warframe: ")
    val nombre = scanner.nextLine()
    print("Vida del Warframe: ")
    val vida = iteradorNumerosValidos("La vida")
    print("Armadura del Warframe: ")
    val armadura = iteradorNumerosValidos("La armadura")
    print("Energía del Warframe: ")
    val energia = iteradorNumerosValidos("La energía")

    fun insertarPlanta() {


        print("Nombre común: ")
        val nombre_comun = scanner.nextLine()
        print("Nombre científico: ")
        val nombre_cientifico = scanner.nextLine()

        var altura: Int? = null
        while (altura == null) {
            print("Altura (en cm): ")
            val entrada = scanner.nextLine()
            altura = entrada.toIntOrNull()
            if (altura == null) {
                println("¡¡¡ La altura debe ser un número !!!")
            }
        }

        val doc = Document("id_planta", id_planta)
            .append("nombre_comun", nombre_comun)
            .append("nombre_cientifico", nombre_cientifico)
            .append("altura", altura)

        coleccion.insertOne(doc)
        println("Planta insertada con ID: ${doc.getObjectId("_id")}")

        cliente.close()
        println("Conexión cerrada")
    }


}

fun actualizarWarframe() {

}

fun eliminarWarframe() {

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
                actualizarWarframe()
            }

            4 -> {
                eliminarWarframe()
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida. Intenta de nuevo")

        }
    }while (opcion != 0)
}