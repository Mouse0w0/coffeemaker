package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.LocalVar;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnList;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtInsnNode;
import com.github.mouse0w0.coffeemaker.template.tree.insn.BtLabel;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

public class BtForeach extends BtInsnNode {
    private final String iterable;
    private final String variableName;
    private final BtInsnList insnList;

    public BtForeach(String iterable, String variableName, BtInsnList insnList) {
        this.iterable = iterable;
        this.variableName = variableName;
        this.insnList = insnList;
    }

    @Override
    public void accept(MethodVisitor methodVisitor, Evaluator evaluator) {
        Iterable<?> iterable = evaluator.eval(this.iterable);
        for (Object value : iterable) {
            insnList.resetLabels();
            try (LocalVar localVar = evaluator.pushLocalVar()) {
                localVar.put(variableName, value);
                insnList.accept(methodVisitor, evaluator);
            }
        }
    }

    @Override
    public BtInsnNode clone(Map<BtLabel, BtLabel> clonedLabels) {
        return new BtForeach(iterable, variableName, insnList);
    }
}