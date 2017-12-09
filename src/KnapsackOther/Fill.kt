package KnapsackOther

import java.util.*
import KnapsackSolution.GeneticSolution.solve

data class Fill(val cost: Int, val items: Set<Item>) {
    operator fun plus(fill: Fill) = Fill(cost + fill.cost, items + fill.items)

    constructor(cost: Int, vararg items: Item): this(cost, items.toSet())

    constructor(item: Item): this(item.cost, item)
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
    val sorted = items.sortedWith(Comparator<Item> {
        o1, o2 -> (o1.cost.toDouble() / o1.weight).compareTo(o2.cost.toDouble() / o2.weight)
    })
    return fillKnapsackGreedySorted(load, sorted)
}



fun main(args: Array<String>) {
    val items = listOf(Item(8, 8), Item(5, 6), Item(6, 8), Item(10, 15), Item(4, 2))
    val time1 = System.currentTimeMillis()
    println(fillKnapsackDynamic(30, items))
    val time1end = System.currentTimeMillis()
    val time2 = System.currentTimeMillis()
    println(fillKnapsackGreedy(30, items))
    val time2end = System.currentTimeMillis()
    val time3 = System.currentTimeMillis()
    println(solve(30, items))
    val time3end = System.currentTimeMillis()
    println("Dynamic : " + (time1end - time1) + "мс")
    println("Greedy : " + (time2end - time2) + "мс")
    println("Genetic : " + (time3end - time3) + "мс")
}
