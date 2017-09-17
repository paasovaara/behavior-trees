package test.zombie;

import behave.execution.ExecutionContext;
import behave.execution.Executor;
import behave.models.*;

public class ZombieTest {

    public ZombieTest() {
        Executor exec = new Executor();
        exec.initialize(createTree());
        exec.start(1000, 1000);
    }

    private Node createTree() {
        return new ZombieNodes.HuntFromHouse();
    }

    public static void main(String[] args) {
        new ZombieTest();
    }
}
