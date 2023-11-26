package converter

import java.lang.Exception
import java.text.ParseException

//initialize constants for each unit that might be converted
enum class Unit (val type: String = "", val abbreviated: String = "", val singular: String = "", val plural: String = "", val temperatureAlt1: String = "", val temperatureAlt2: String = "", val converter: Double = 1.0) {
    GRAM("weight", "g", "gram", "grams"),
    KILOGRAM("weight", "kg", "kilogram", "kilograms", "", "", 1000.0),
    MILLIGRAM("weight", "mg", "milligram", "milligrams", "", "", 0.001),
    POUND("weight", "lb", "pound", "pounds", "", "", 453.592),
    OUNCE("weight", "oz", "ounce", "ounces", "", "", 28.3495),

    METER("distance", "m", "meter", "meters"),
    KILOMETER("distance", "km", "kilometer", "kilometers", "", "", 1000.0),
    CENTIMETER("distance", "cm", "centimeter", "centimeters", "", "", 0.01),
    MILLIMETER("distance", "mm", "millimeter", "millimeters", "", "", 0.001),
    MILE("distance", "mi", "mile", "miles", "", "", 1609.35),
    YARD("distance", "yd", "yard", "yards", "", "", 0.9144),
    FOOT("distance", "ft", "foot", "feet", "", "", 0.3048),
    INCH("distance", "in", "inch", "inches", "", "", 0.0254),

    CELSIUS("temperature", "c", "degree celsius", "degrees celsius", "celsius", "dc"),
    FAHRENHEIT("temperature", "f", "degree fahrenheit", "degrees fahrenheit", "fahrenheit", "df"),
    KELVIN("temperature", "k", "kelvin", "kelvins", "k", "k"),

    NULL();
}

//function that will find the appropriate unit based on a string argument
fun findUnit(unit: String): Unit {
    for (i in Unit.values()) {
        when (i.type){
            "distance", "weight" ->
                if (unit == i.abbreviated || unit == i.singular || unit == i.plural) {
                    return i
                }
            "temperature" ->
                if (unit == i.abbreviated || unit == i.singular || unit == i.plural || unit == i.temperatureAlt1 || unit == i.temperatureAlt2) {
                return i
            }
        }
    }

    return Unit.NULL
}

//function that converts weight and distance units
fun convert(num: Double, from: Unit, to: Unit): Double {
    return num * from.converter / to.converter
}

//function that converts temperature units
fun convertTemperature(num: Double, from: Unit, to: Unit): Any {
    when (from.abbreviated) {
        "c" -> when (to.abbreviated) {
            "c" -> return num
            "f" -> return num * 9 / 5 + 32
            "k" -> return num + 273.15
        }
        "f" -> when (to.abbreviated) {
            "f" -> return num
            "c" -> return (num - 32) * 5 / 9
            "k" -> {
                return (num + 459.67) * 5 / 9}
        }
        "k" -> when (to.abbreviated) {
            "k" -> return num
            "c" -> return num - 273.15
            "f" -> return num * 9 / 5 - 459.67
        }
    }

    return false
}

fun main() {
    while (true) {
        //take input from user
        print("Enter what you want to convert (or exit): ")

        val response = readln()

        //exit the program is the input is 'exit'
        if (response == "exit") {
            return
        }

        //try to parse the input provided by the user and throw an error if unable to parse
        val string = response.split(" ")
        val num = string[0]
        var from: String
        var to: String

        var numDouble: Double

        try {
            numDouble = num.toDouble()
        } catch (e: Exception){
            println("Parse error")
            continue
        }

        when (string.size) {
            4 -> {
                from = string[1].lowercase()
                to = string[3].lowercase()
            }
            6 -> {
                from = string[2].lowercase()
                to = string[5].lowercase()
            }
            5 -> {
                if (string[1].lowercase() == "degrees" || string[1].lowercase() == "degree") {
                    from = string[2].lowercase()
                    to = string[4].lowercase()
                } else if (string[3].lowercase() == "degrees" || string[3].lowercase() == "degree") {
                    from = string[1].lowercase()
                    to = string[4].lowercase()
                } else {
                    println("Parse error")
                    continue
                }
            }
            else -> {
                println("Parse error")
                continue
            }
        }

        val unitFrom = findUnit(from)
        val unitTo = findUnit(to)

        //convert units as long as they are valid and of the same type
        if (unitFrom.type == "" && unitTo.type == "") {
            println("Conversion from ??? to ??? is impossible")
        } else if (unitFrom.type == "" && unitTo.type != "") {
            println("Conversion from ??? to ${unitTo.plural} is impossible")
        } else if (unitTo.type == "") {
            println("Conversion from ${unitFrom.plural} to ??? is impossible")
        } else if (unitFrom.type != unitTo.type) {
            println("Conversion from ${unitFrom.plural} to ${unitTo.plural} is impossible")
        } else {
            if (unitFrom.type == "distance" || unitFrom.type == "weight") {
                if (numDouble < 0.0) {
                    when (unitFrom.type) {
                        "distance" -> {
                            println("Length shouldn't be negative")
                        }
                        "weight" -> {
                            println("Weight shouldn't be negative")
                        }
                    }
                    continue
                }

                val convertedNum = convert(numDouble, unitFrom, unitTo)
                val finalFrom = if (numDouble == 1.0) unitFrom.singular else unitFrom.plural
                val finalTo = if (convertedNum == 1.0) unitTo.singular else unitTo.plural

                println("$numDouble $finalFrom is $convertedNum $finalTo")
            } else {
                val convertedNum = convertTemperature(numDouble, unitFrom, unitTo)
                val finalFrom = if (numDouble == 1.0) unitFrom.singular else unitFrom.plural
                val finalTo = if (convertedNum == 1.0) unitTo.singular else unitTo.plural

                println("$numDouble $finalFrom is $convertedNum $finalTo")
            }
        }
    }
}
