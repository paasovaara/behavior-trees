# Behavior trees
This is a simple implementation of Behavior trees written in Java. Purpose is to learn how these work and create a simple framework to be used with MyRobotLab or other random game projects. 

Much of the work is based on [this article](https://www.gamasutra.com/blogs/ChrisSimpson/20140717/221339/Behavior_trees_for_AI_How_they_work.php)

## Building and running
Developed on IntelliJ and Java 8. The whole codebase does not have any dependencies so just import it to any IDE or use javac to compile it.

Package *test* contains some runnable test applications which also work as examples.

## Usage instructions
Package *behave* contains the actual framework and can be used to create your own trees.

Start by implementing your own leaves by extending LeafNode, which contain the actual business/game-logic. Using CompositeNodes and DecoratorNodes and your custom leaves create the behaviour tree and use Executor to run them.

Current Executor implementation contains a Timer to tick the tree but it should be trivial to get the ticks from external source, such as an event/game-loop.

## License
MIT

