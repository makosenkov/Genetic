package KnapsackOther

import java.util.*
import KnapsackSolution.GeneticSolution.solve

data class Fill(val cost: Int, val items: Set<Item>) {
    operator fun plus(fill: Fill) = Fill(cost + fill.cost, items + fill.items)

    constructor(cost: Int, vararg items: Item) : this(cost, items.toSet())

    constructor(item: Item) : this(item.cost, item)
}

data class LoadCount(val load: Int, val count: Int)

data class Item(val cost: Int, val weight: Int)

fun fillKnapsackDynamic(load: Int, items: List<Item>,
                        storage: HashMap<LoadCount, Fill> = hashMapOf()): Fill {
    if (load <= 0 || items.isEmpty()) return Fill(0, emptySet())
    val loadCount = LoadCount(load, items.size)
    return storage.getOrPut(loadCount) {
        val itemsWithoutLast = items.subList(0, items.size - 1)
        val fillWithoutLast = fillKnapsackDynamic(load, itemsWithoutLast, storage)
        val last = items.last()
        if (last.weight > load) fillWithoutLast
        else {
            val fillWithLast = fillKnapsackDynamic(load - last.weight, itemsWithoutLast, storage) + Fill(last)
            if (fillWithLast.cost > fillWithoutLast.cost) fillWithLast
            else fillWithoutLast
        }
    }
}

private fun fillKnapsackGreedySorted(load: Int, items: List<Item>): Fill {
    if (load <= 0 || items.isEmpty()) return Fill(0, emptySet())
    val itemsWithoutLast = items.subList(0, items.size - 1)
    val last = items.last()
    if (last.weight > load) return fillKnapsackGreedySorted(load, itemsWithoutLast)
    else return fillKnapsackGreedySorted(load - last.weight, itemsWithoutLast) + Fill(last)
}

fun fillKnapsackGreedy(load: Int, items: List<Item>): Fill {
    val sorted = items.sortedWith(Comparator<Item> { o1, o2 ->
        (o1.cost.toDouble() / o1.weight).compareTo(o2.cost.toDouble() / o2.weight)
    })
    return fillKnapsackGreedySorted(load, sorted)
}

fun main(args: Array<String>) {
    val random = Random()
    //Test 1
    val items = mutableListOf<Item>()
    for (i in 0..5) {
        val item = Item(random.nextInt(10 - 1) + 1, random.nextInt(10 - 1) + 1)
        items.add(item)
    }
    var dynamicFill: Fill
    var greedyFill: Fill
    var geneticFill: Fill
    var pass = 0
    for (i in 0..100) {
        dynamicFill = fillKnapsackDynamic(30, items)
        greedyFill = fillKnapsackGreedy(30, items)
        geneticFill = solve(30, items)
        if (geneticFill.items.size >= dynamicFill.items.size || geneticFill.items.size >= greedyFill.items.size)
            pass++
    }

    if (pass > 50)
        println("Test 1 passed")
    else println("Test 1 failed")

    //Test 2
    val items1 = mutableListOf<Item>()

    for (i in 0..250) {
        val item = Item(random.nextInt(10 - 1) + 1, random.nextInt(10 - 1) + 1)
        items1.add(item)
    }
    for (i in 0..25) {
        dynamicFill = fillKnapsackDynamic(1500, items1)
        println("Dynamic: " + dynamicFill.items.size)
        greedyFill = fillKnapsackGreedy(1500, items1)
        println("Greedy: " + greedyFill.items.size)
        geneticFill = solve(1500, items1)
        println("Genetic: " + geneticFill.items.size)
        if (geneticFill.items.size >= dynamicFill.items.size || geneticFill.items.size >= greedyFill.items.size)
            pass++
    }
    if (pass > 50)
        println("Test 2 passed")
    else println("Test 2 failed")
}
