package main;

/**
 *
 * @author Andres
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class Fact {

    String attribute;
    String value;

    public Fact(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public String toString() {
        return attribute + " = " + value;
    }
}

class Condition extends Fact {

    public Condition(String attribute, String value) {
        super(attribute, value);
    }
}

class Hypothesis {

    Fact fact;
    SingleLinkedList<Condition> conditions;

    public Hypothesis(Fact fact, Condition[] conditions) {
        this.fact = fact;
        this.conditions = new SingleLinkedList<>(Arrays.asList(conditions));
    }

    @Override
    public String toString() {
        return fact.toString();
    }
}

class SingleLinkedList<T> {

    Node<T> head;

    public SingleLinkedList(List<T> list) {
        for (T item : list) {
            addLast(item);
        }
    }

    private void addLast(T item) {
        Node<T> newNode = new Node<>(item);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data).append(" ");
            current = current.next;
        }
        return sb.toString();
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public T next() {
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    private static class Node<T> {

        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
        }
    }
}

class DoubleLinkedList<T> {

    Node<T> head;
    Node<T> tail;

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data).append(" ");
            current = current.next;
        }
        return sb.toString();
    }

    private static class Node<T> {

        T data;
        Node<T> next;
        Node<T> prev;

        public Node(T data) {
            this.data = data;
        }
    }
}

public class BaseDeConocimientos {

    static ArrayList<Fact> facts = new ArrayList<>(Arrays.asList(
            new Fact("a", "verdadero"),
            new Fact("b", "verdadero"),
            new Fact("c", "verdadero"),
            new Fact("w", "verdadero")
    ));

    static Hypothesis[] hypotheses = {
        new Hypothesis(new Fact("d", "verdadero"),
        new Condition[]{
            new Condition("a", "verdadero"),
            new Condition("b", "verdadero")
        }),
        new Hypothesis(new Fact("h", "verdadero"),
        new Condition[]{
            new Condition("b", "verdadero"),
            new Condition("c", "verdadero")
        }),
        new Hypothesis(new Fact("u", "verdadero"),
        new Condition[]{
            new Condition("h", "verdadero"),
            new Condition("w", "verdadero")
        })
    };

    private static Iterator<Condition> conditionIterator; // Declaración de la variable conditionIterator

    public static void main(String[] args) {
        System.out.println("Array de hechos iniciales:");
        for (Fact fact : facts) {
            System.out.println(fact);
        }
        System.out.println();

        DoubleLinkedList<Hypothesis> hypothesisList = new DoubleLinkedList<>();
        for (Hypothesis hypothesis : hypotheses) {
            hypothesisList.addLast(hypothesis);
        }

        System.out.println("Lista doblemente enlazada de hipótesis:");
        System.out.println(hypothesisList + "\n");

        Hypothesis targetHypothesis = new Hypothesis(new Fact("u", "verdadero"), new Condition[0]);
        System.out.println("Encadenamiento hacia atrás para la hipótesis: " + targetHypothesis);
        boolean isValid = backwardChaining(targetHypothesis, new ArrayList<>());

        if (isValid) {
            System.out.println("La hipótesis " + targetHypothesis + " es válida.");
        } else {
            System.out.println("La hipótesis " + targetHypothesis + " no es válida.");
        }
    }

    private static boolean backwardChaining(Hypothesis targetHypothesis, ArrayList<Hypothesis> appliedRules) {
        for (Hypothesis hypothesis : hypotheses) {
            if (hypothesis.fact.attribute.equals(targetHypothesis.fact.attribute)
                    && hypothesis.fact.value.equals(targetHypothesis.fact.value)) {
                if (appliedRules.contains(hypothesis)) {
                    // Ciclo infinito
                    return false;
                }
                appliedRules.add(hypothesis);
                boolean allConditionsSatisfied = true;

                Iterator<Condition> conditionIterator = hypothesis.conditions.iterator();
                while (conditionIterator.hasNext()) {
                    Condition condition = conditionIterator.next();
                    boolean conditionSatisfied = false;
                    for (Fact fact : facts) {
                        if (condition.attribute.equals(fact.attribute) && condition.value.equals(fact.value)) {
                            conditionSatisfied = true;
                            break;
                        }
                    }
                    if (!conditionSatisfied) {
                        conditionSatisfied = backwardChaining(new Hypothesis(condition, new Condition[0]), appliedRules);
                    }
                    if (!conditionSatisfied) {
                        allConditionsSatisfied = false;
                        break;
                    }
                }
                /*
                for (Condition condition : hypothesis.conditions.head) {
                    boolean conditionSatisfied = false;
                    for (Fact fact : facts) {
                        if (condition.attribute.equals(fact.attribute) && condition.value.equals(fact.value)) {
                            conditionSatisfied = true;
                            break;
                        }
                    }
                    if (!conditionSatisfied) {
                        conditionSatisfied = backwardChaining(condition, appliedRules);
                    }
                    if (!conditionSatisfied) {
                        allConditionsSatisfied = false;
                        break;
                    }
                }*/
                if (allConditionsSatisfied) {
                    printAppliedRules(appliedRules);
                    return true;
                } else {
                    appliedRules.remove(hypothesis);
                }
            }
        }
        return false;
    }

    private static void printAppliedRules(ArrayList<Hypothesis> appliedRules) {
    int ruleCount = 1;
    for (Hypothesis hypothesis : appliedRules) {
        if (hypothesis.conditions.head == null) {
            System.out.println("R" + ruleCount + ". " + hypothesis.fact);
        } else {
            System.out.print("R" + ruleCount + ". Si ");
            Iterator<Condition> conditionIterator = hypothesis.conditions.iterator();
            while (conditionIterator.hasNext()) {
                Condition condition = conditionIterator.next();
                System.out.print(condition);
                if (conditionIterator.hasNext()) {
                    System.out.print(" y ");
                }
            }
            System.out.println(", entonces " + hypothesis.fact + ".");
        }
        ruleCount++;
    }
}

    private static boolean isHypothesisValid(Hypothesis hypothesis, ArrayList<Hypothesis> appliedRules) {
        Iterator<Condition> conditionIterator = hypothesis.conditions.iterator();
        while (conditionIterator.hasNext()) {
            Condition currentCondition = conditionIterator.next();
            boolean conditionSatisfied = false;
            for (Fact fact : facts) {
                if (currentCondition.attribute.equals(fact.attribute) && currentCondition.value.equals(fact.value)) {
                    conditionSatisfied = true;
                    break;
                }
            }
            if (!conditionSatisfied) {
                for (Hypothesis otherHypothesis : hypotheses) {
                    if (otherHypothesis.fact.attribute.equals(currentCondition.attribute)
                            && otherHypothesis.fact.value.equals(currentCondition.value)
                            && !appliedRules.contains(otherHypothesis)) {
                        appliedRules.add(otherHypothesis);
                        if (isHypothesisValid(otherHypothesis, appliedRules)) {
                            conditionSatisfied = true;
                            break;
                        } else {
                            appliedRules.remove(otherHypothesis);
                        }
                    }
                }
            }
            if (!conditionSatisfied) {
                return false;
            }
        }
        return true;
    }

    private static void printAppliedRules(Hypothesis hypothesis, ArrayList<Hypothesis> appliedRules) {
        // conditionIterator = hypothesis.conditions.iterator();
        while (conditionIterator.hasNext()) {
            Condition currentCondition = conditionIterator.next();
            boolean conditionSatisfied = false;
            for (Fact fact : facts) {
                if (currentCondition.attribute.equals(fact.attribute) && currentCondition.value.equals(fact.value)) {
                    System.out.print(currentCondition + " ");
                    conditionSatisfied = true;
                    break;
                }
            }
            if (!conditionSatisfied) {
                for (Hypothesis otherHypothesis : hypotheses) {
                    if (otherHypothesis.fact.attribute.equals(currentCondition.attribute)
                            && otherHypothesis.fact.value.equals(currentCondition.value)
                            && appliedRules.contains(otherHypothesis)) {
                        System.out.print(otherHypothesis + " ");
                        conditionSatisfied = true;
                        break;
                    }
                }
            }
        }
        System.out.println("=> " + hypothesis);
    }
}
