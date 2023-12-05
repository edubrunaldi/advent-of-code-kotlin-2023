import kotlin.collections.Map

class MapNumber {
    val map: MutableMap<LongRange, Long> = mutableMapOf()

    fun getTargetValue(source: Long): Long =
        map.keys.find { it.contains(source) }
            ?.let { source + map[it]!! }
            ?: source
}
data class AlmanacDto(
    var seeds: List<Long> = mutableListOf(),
    var seedToSoil: MapNumber = MapNumber(),
    var soilToFertilizer: MapNumber = MapNumber(),
    var fertilizerToWater: MapNumber = MapNumber(),
    var waterToLight: MapNumber = MapNumber(),
    var lightToTemperature: MapNumber = MapNumber(),
    var temperatureToHumidity: MapNumber = MapNumber(),
    var humidityToLocation: MapNumber = MapNumber()
)

private fun getSeeds(line: String): List<Long> = line
    .removePrefix("seeds: ")
    .split(" ")
    .map { it.toLong() }

private fun getMapNumber(input: List<String>, i: Int): MapNumber {
    val mapNumber = MapNumber()
    var j = i+1
    var line = input[j]
    while(line.any { it.isDigit() }) {
        val (target, source, range) = line.split(" ").map(String::toLong)
        val longRange = (source..<source+range)
        mapNumber.map[longRange] = target - source
        j++
        if(j >= input.size) {
            break
        }
        line = input[j]
    }
    return mapNumber
}


private fun toAlmanacDto(input: List<String>): AlmanacDto {
    val almanacDto = AlmanacDto()
    var i = 0
    while(i < input.size) {
        val line = input[i]
        if(line.contains("seeds:")) {
            almanacDto.seeds = getSeeds(line)
        } else if (line.contains("map:")) {
            val mapNumber = getMapNumber(input, i)
            if (line.contains("seed-to-soil map:")) {
                almanacDto.seedToSoil = mapNumber
            } else if (line.contains("soil-to-fertilizer map:")) {
                almanacDto.soilToFertilizer = mapNumber
            } else if (line.contains("fertilizer-to-water map:")) {
                almanacDto.fertilizerToWater = mapNumber
            } else if (line.contains("water-to-light map:")) {
                almanacDto.waterToLight = mapNumber
            } else if (line.contains("light-to-temperature map:")) {
                almanacDto.lightToTemperature = mapNumber
            } else if (line.contains("temperature-to-humidity map:")) {
                almanacDto.temperatureToHumidity = mapNumber
            } else if (line.contains("humidity-to-location map:")) {
                almanacDto.humidityToLocation = mapNumber
            }
        }
        i++
    }
    return almanacDto
}

fun main() {

    fun part1(input: List<String>): Long =
       with(toAlmanacDto(input)) {
           this.seeds
               .asSequence()
               .map { this.seedToSoil.getTargetValue(it) }
               .map { this.soilToFertilizer.getTargetValue(it) }
               .map { this.fertilizerToWater.getTargetValue(it) }
               .map { this.waterToLight.getTargetValue(it) }
               .map { this.lightToTemperature.getTargetValue(it) }
               .map { this.temperatureToHumidity.getTargetValue(it) }
               .map { this.humidityToLocation.getTargetValue(it) }
               .toList()
               .min()

       }

    fun part2(input: List<String>): Long =
        with(toAlmanacDto(input)) {
            val allSeeds: List<LongRange> = this.seeds.let {
                var i = 0
                val pairs = mutableListOf<LongRange>()
                while(i < this.seeds.size) {
                    pairs.add(this.seeds[i]..<(this.seeds[i] + this.seeds[i+1]))
                    i += 2
                }

                println("pairs ${pairs}")
                pairs
            }

            var lowestNumber = Long.MAX_VALUE
            allSeeds.forEach { seedRange ->
                seedRange.asSequence()
                .map { this.seedToSoil.getTargetValue(it) }
                .map { this.soilToFertilizer.getTargetValue(it) }
                .map { this.fertilizerToWater.getTargetValue(it) }
                .map { this.waterToLight.getTargetValue(it) }
                .map { this.lightToTemperature.getTargetValue(it) }
                .map { this.temperatureToHumidity.getTargetValue(it) }
                .map { this.humidityToLocation.getTargetValue(it) }
                .forEach { lowestNumber = minOf(lowestNumber, it) }
            }

            lowestNumber
        }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    toAlmanacDto(input)
    part1(input).println()
    part2(input).println()
}
