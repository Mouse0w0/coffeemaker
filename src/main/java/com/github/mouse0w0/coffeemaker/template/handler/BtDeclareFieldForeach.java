package com.github.mouse0w0.coffeemaker.template.handler;

import com.github.mouse0w0.coffeemaker.evaluator.Evaluator;
import com.github.mouse0w0.coffeemaker.evaluator.LocalVar;
import com.github.mouse0w0.coffeemaker.template.Field;
import com.github.mouse0w0.coffeemaker.template.TemplateProcessException;
import com.github.mouse0w0.coffeemaker.template.tree.BtField;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class BtDeclareFieldForeach extends BtField {
    private final String iterable;
    private final String variableName;
    private final String expression;

    public BtDeclareFieldForeach(String iterable, String variableName, String expression) {
        this.iterable = iterable;
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public void accept(ClassVisitor classVisitor, Evaluator evaluator) {
        for (Object element : evaluator.eval(iterable, Iterable.class)) {
            try (LocalVar localVar = evaluator.pushLocalVar()) {
                localVar.put(variableName, element);
                final Field field = evaluator.eval(expression, Field.class);
                if (field == null) {
                    throw new TemplateProcessException("The field of expression \"" + expression + "\" cannot be null");
                }
                final FieldVisitor fieldVisitor = classVisitor.visitField(
                        computeInt(ACCESS, evaluator),
                        field.getName(),
                        field.getDescriptor(),
                        computeString(SIGNATURE, evaluator),
                        compute(VALUE, evaluator));
                if (fieldVisitor != null) {
                    accept(fieldVisitor, evaluator);
                }
            }
        }
    }
}
