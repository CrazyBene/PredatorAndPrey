import java.util.concurrent.ThreadLocalRandom

class World(val width: Int, val height: Int) {
    
    var creatures = Array(width) { Array(height) { Creature(Creature.Type.NONE) } }
    
    init {
        for(y in 0 until height) {
            for(x in 0 until width) {
                val r = ThreadLocalRandom.current().nextInt(0, 1000)
                when {
                    r > 100 -> creatures[x][y].type = Creature.Type.NONE
                    r > 50  -> creatures[x][y].type = Creature.Type.Prey
                    else    -> creatures[x][y].type = Creature.Type.Predator
                }
            }
        }
    }
    
    // NOTE: Problem with this loop is, that it is possible that a creature can move to a position where he gets checked again
    fun step() {
        for(y in 0 until height) {
            for(x in 0 until width) {
                
                // Get the current creature
                val creature = creatures[x][y]
                
                // If it is nothing (none) just ignore it
                if(creature.type == Creature.Type.NONE)
                    continue
                
                
                // Get the creature on the tile the creature wants to move
                val otherCreature = getRandomNeighborCreature(x, y)
                
                // HERE are the rules of this cellular automata
                when(creature.type) {
                    Creature.Type.Predator -> stepPredator(creature, otherCreature)
                    Creature.Type.Prey     -> stepPrey(creature, otherCreature)
                    
                    Creature.Type.NONE     -> {
                    }
                }
            }
        }
    }
    
    // The rules for a predator
    private fun stepPredator(creature: Creature, otherCreature: Creature) {
        creature.hp--
        if(creature.hp <= 0) {
            creature.type = Creature.Type.NONE
            return
        }
        
        when(otherCreature.type) {
            Creature.Type.Prey     -> {
                // Heal for as much as the other had hp
                creature.hp += otherCreature.hp
                
                // The other creature becomes a predator
                otherCreature.type = Creature.Type.Predator
            }
            
            Creature.Type.Predator -> {
                // The creature can not move
            }
            
            Creature.Type.NONE     -> {
                // Set the creature where to move to to the current creature
                otherCreature.type = creature.type
                otherCreature.hp = creature.hp
                // The current creature dies at its old place
                creature.type = Creature.Type.NONE
            }
        }
        
    }
    
    // The rules for the prey
    private fun stepPrey(creature: Creature, otherCreature: Creature) {
        creature.hp++
        when(otherCreature.type) {
            Creature.Type.Prey     -> {
                // The creature con not move
            }
            
            Creature.Type.Predator -> {
                // The creature can not move
            }
            
            Creature.Type.NONE     -> {
                if(creature.hp >= Creature.MAX_HEALTH) {
                    creature.hp = 10
                    // "Spawn" a new Prey at the target position
                    otherCreature.type = Creature.Type.Prey
                    otherCreature.hp = 10
                } else {
                    // Set the creature where to move to to the current creature
                    otherCreature.type = creature.type
                    otherCreature.hp = creature.hp
                    // The current creature dies at its old place
                    creature.type = Creature.Type.NONE
                }
            }
        }
    }
    
    fun getRandomNeighborCreature(x: Int, y: Int): Creature {
        // Get a movement
        val moveX = ThreadLocalRandom.current().nextInt(-1, 2)
        val moveY = ThreadLocalRandom.current().nextInt(-1, 2)
    
        var nextX = x + moveX
        var nextY = y + moveY
    
        nextX = if(nextX == width) 0 else if(nextX < 0) width - 1 else nextX
        nextY = if(nextY == height) 0 else if(nextY < 0) height - 1 else nextY
        
        return creatures[nextX][nextY]
    }
    
}