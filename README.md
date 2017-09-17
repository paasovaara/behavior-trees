# Behavior trees
This is a simple implementation of Behavior trees written in java. Purpose is to learn how these work and create a simple framework to be used with MyRobotLab 

Much of the work is based on [this article](https://www.gamasutra.com/blogs/ChrisSimpson/20140717/221339/Behavior_trees_for_AI_How_they_work.php)

## Building
Developed on IntelliJ and Java 8

TODO packaging into one jar

TODO tests

## Usage instructions
Package *behave* contains the actual framework and can be used to create your own trees.

Start by implementing your own leaves by extending LeafNode, which contain the actual business/game-logic. Using CompositeNodes and DecoratorNodes and the Leaves create the behaviour tree and use Executor to run them. package *test* contains test apps and examples.


## License
MIT

