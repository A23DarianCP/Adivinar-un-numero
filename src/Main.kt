import java.io.File
import kotlin.random.Random

const val RESET = "\u001B[0m"
const val BG_BLACK = "\u001B[40m"
const val WHITE = "\u001B[37m"
const val RED = "\u001B[31m"
const val GREEN = "\u001B[32m"
const val YELLOW = "\u001B[33m"

const val NUM_DIGITOS = 4
const val INTENTOS_MAX = 10
val RANGO_DIGITOS = 1..6
val NOMBRE_ARCHIVO = "UltimaJugada.txt"

fun main() {
    var juegoActivo = true
    while (juegoActivo) {
        println("${BG_BLACK}${RED}1. Jugar${RESET}")
        println("${BG_BLACK}${GREEN}2. Ver último intento${RESET}")
        println("${BG_BLACK}${YELLOW}3. Salir${RESET}")
        print("${BG_BLACK}${WHITE}Selecciona una opción: ${RESET}")

        when (readLine()?.trim()) {
            "1" -> InicioJuego()
            "2" -> VerUltimoIntento()
            "3" -> {
                println("${BG_BLACK}${YELLOW}Saliendo del juego...${RESET}")
                juegoActivo = false
            }
            else -> println("${BG_BLACK}${WHITE}Opción no válida, intenta de nuevo.${RESET}")
        }
    }
}

fun InicioJuego() {
    val NumeroSecreto = GenerarNumeroSecreto()
    println("${BG_BLACK}${RED}Comienza el juego! Intenta adivinar el número de $NUM_DIGITOS dígitos. $NumeroSecreto")
    var intento = 0

    while (intento < INTENTOS_MAX) {
        print("Intento ${intento + 1}: ")
        val adivinar = readLine()?.trim() ?: ""

        if (adivinar.length != NUM_DIGITOS || adivinar.any { it.toString().toInt() !in RANGO_DIGITOS }) {
            println("Entrada no válida. Ingresa un número de $NUM_DIGITOS dígitos entre 1 y 6.")
            continue
        }

        intento++
        val (acierto, coincidencia) = calcularPistas(NumeroSecreto, adivinar)
        println("Aciertos: $acierto, Coincidencias: $coincidencia")

        if (acierto == NUM_DIGITOS) {
            println("${GREEN}¡Felicidades! Adivinaste el número en $intento intentos.$RESET")
            GuardarIntento(intento, NumeroSecreto)
            break
        }
    }

    if (intento == INTENTOS_MAX) {
        println("${RED}Lo siento, has alcanzado el máximo de intentos. El número era $NumeroSecreto.$RESET")
        GuardarIntento(intento, NumeroSecreto)
    }
}

fun GenerarNumeroSecreto(): String {
    val numeroSecreto = List(NUM_DIGITOS) {
        Random.nextInt(RANGO_DIGITOS.first, RANGO_DIGITOS.last + 1)
    }
    return numeroSecreto.joinToString("")
}

fun calcularPistas(secreto: String, adivinar: String): Pair<Int, Int> {
    var acierto = 0
    var coincidencia = 0
    for (i in secreto.indices) {
        when {
            adivinar[i] == secreto[i] -> acierto++
            adivinar[i] in secreto -> coincidencia++
        }
    }
    return Pair(acierto, coincidencia)
}

fun GuardarIntento(intento: Int, secreto: String) {
    File(NOMBRE_ARCHIVO).writeText("Intentos: $intento, Número Secreto: $secreto")
}

fun VerUltimoIntento() {
    val file = File(NOMBRE_ARCHIVO)
    if (file.exists()) {
        println("${YELLOW}Última jugada guardada:${RESET}")
        println(file.readText())
    } else {
        println("${RED}No hay intentos guardados.${RESET}")
    }
}