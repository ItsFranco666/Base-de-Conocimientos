package main;

/**
 *
 * @author Andres
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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

    /*static ArrayList<Fact> facts = new ArrayList<>(Arrays.asList(
            new Fact("branquias", "si"),
            new Fact("habitat", "acuatico")
    ));

    static Hypothesis[] hypotheses = {
        new Hypothesis(new Fact("animal", "pez"),
        new Condition[]{
            new Condition("branquias", "si"),
            new Condition("habitat", "acuatico")
        })
    };*/
    static ArrayList<Fact> facts = new ArrayList<>();
    static ArrayList<Hypothesis> hypotheses = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los hechos (atributo valor) o 'fin' para terminar:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("fin")) {
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length == 2) {
                facts.add(new Fact(parts[0], parts[1]));
            } else {
                System.out.println("Formato inválido. Intente nuevamente.");
            }
        }

        System.out.println("\nIngrese las hipótesis y sus condiciones (hipotesis valor condicion1 valor condicion2 valor ...) o 'fin' para terminar:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("fin")) {
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length >= 3 && (parts.length - 2) % 2 == 0) {
                String attribute = parts[0];
                String value = parts[1];
                Fact fact = new Fact(attribute, value);
                List<Condition> conditions = new ArrayList<>();
                for (int i = 2; i < parts.length; i += 2) {
                    conditions.add(new Condition(parts[i], parts[i + 1]));
                }
                hypotheses.add(new Hypothesis(fact, conditions.toArray(new Condition[0])));
            } else {
                System.out.println("Formato inválido. Intente nuevamente.");
            }
        }
        
        System.out.println("Lista de hipótesis y condiciones:");
        for (Hypothesis hypothesis : hypotheses) {
            System.out.print(hypothesis + " -> ");
            Iterator<Condition> conditionIterator = hypothesis.conditions.iterator();
            while (conditionIterator.hasNext()) {
                Condition condition = conditionIterator.next();
                System.out.print(condition);
                if (conditionIterator.hasNext()) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
        
        System.out.println("\nIngrese la hipótesis para el encadenamiento hacia atrás (hipotesis valor):");
        String input = scanner.nextLine();
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            Hypothesis targetHypothesis = new Hypothesis(new Fact(parts[0], parts[1]), new Condition[0]);
            boolean isValid = backwardChaining(targetHypothesis, new ArrayList<>(), new ArrayList<>());

            if (isValid) {
                System.out.println("La hipótesis " + targetHypothesis.fact + " es válida.");
            } else {
                System.out.println("La hipótesis " + targetHypothesis.fact + " no es válida.");
            }
        } else {
            System.out.println("Formato inválido para la hipótesis.");
        }

        scanner.close();
    }

    private static boolean backwardChaining(Hypothesis targetHypothesis, ArrayList<Hypothesis> appliedRules, ArrayList<Hypothesis> solutionPath) {
        for (Hypothesis hypothesis : hypotheses) {
            if (hypothesis.fact.attribute.equals(targetHypothesis.fact.attribute)
                    && hypothesis.fact.value.equals(targetHypothesis.fact.value)) {
                if (appliedRules.contains(hypothesis)) {
                    // Ciclo infinito
                    return false;
                }
                appliedRules.add(hypothesis);
                solutionPath.add(hypothesis);
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
                        conditionSatisfied = backwardChaining(new Hypothesis(condition, new Condition[0]), appliedRules, solutionPath);
                    }
                    if (!conditionSatisfied) {
                        allConditionsSatisfied = false;
                        break;
                    }
                }
                if (allConditionsSatisfied) {
                    printAppliedRules(solutionPath);
                    return true;
                } else {
                    appliedRules.remove(hypothesis);
                    solutionPath.remove(hypothesis);
                }
            }
        }
        return false;
    }

    /*private static boolean backwardChaining(Hypothesis targetHypothesis, ArrayList<Hypothesis> appliedRules) {
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
                }//
                if (allConditionsSatisfied) {
                    printAppliedRules(appliedRules);
                    return true;
                } else {
                    appliedRules.remove(hypothesis);
                }
            }
        }
        return false;
    }*/
    private static void printAppliedRules(ArrayList<Hypothesis> solutionPath) {
        int ruleCount = 1;
        for (int i = solutionPath.size() - 1; i >= 0; i--) {
            Hypothesis hypothesis = solutionPath.get(i);
            System.out.print("R" + ruleCount + ". ");
            if (hypothesis.conditions.head == null) {
                System.out.println(hypothesis.fact);
            } else {
                System.out.print("Si ");
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

}
