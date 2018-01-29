fun main(args: Array<String>) {
    
    val array = Array(3) { Test() }
    
    val i = array[0]
    
    i.hp = 3
    
    array.forEach {
        println(it.hp)
    }
    
}

class Test {
    
    var hp = 5
    
}