import KnapsackOther.*
import KnapsackSolution.*
import org.junit.Test
import java.util.*

/**
 *
 */
class AlgsTest {
    private val random = Random()
    private val items = mutableListOf<Item>()


    @Test
    fun testSmallList() {
        var dynamicFill: Fill
        var greedyFill: Fill
        var geneticFill: Fill
        var pass = 0
        for (i in 0..5) {
            val item = Item(random.nextInt(10 - 1) + 1, random.nextInt(10 - 1) + 1)
            items.add(item)
        }
        for (i in 0..100) {
            dynamicFill = fillKnapsackDynamic(30, items)
            greedyFill = fillKnapsackGreedy(30, items)
            geneticFill = GeneticSolution.solve(30, items)
            if (geneticFill.cost.toDouble() / geneticFill.items.size <=
                    dynamicFill.cost.toDouble() / dynamicFill.items.size.toDouble() ||
                    geneticFill.cost.toDouble() / geneticFill.items.size.toDouble() <=
                            greedyFill.cost.toDouble() / greedyFill.items.size.toDouble())
                pass++
        }
        assert(pass > 50)
    }

    @Test
    fun testBigList() {
        var dynamicFill: Fill
        var greedyFill: Fill
        var geneticFill: Fill
        var pass = 0
        for (i in 0..250) {
            val item = Item(random.nextInt(10 - 1) + 1, random.nextInt(10 - 1) + 1)
            items.add(item)
        }
        for (i in 0..25) {
            var begin = System.currentTimeMillis()
            dynamicFill = fillKnapsackDynamic(1500, items)
            var end = System.currentTimeMillis()
            println("DynamicTime: " + (end-begin))
            begin = System.currentTimeMillis()
            greedyFill = fillKnapsackGreedy(1500, items)
            end = System.currentTimeMillis()
            println("GreedyTime: " + (end-begin))
            begin = System.currentTimeMillis()
            geneticFill = GeneticSolution.solve(1500, items)
            end = System.currentTimeMillis()
            println("GeneticTime: " + (end-begin))
            if (geneticFill.cost.toDouble() / geneticFill.items.size <=
                    dynamicFill.cost.toDouble() / dynamicFill.items.size.toDouble() ||
                    geneticFill.cost.toDouble() / geneticFill.items.size.toDouble() <=
                            greedyFill.cost.toDouble() / greedyFill.items.size.toDouble())
                pass++
        }
        assert(pass > 15)
    }
}
