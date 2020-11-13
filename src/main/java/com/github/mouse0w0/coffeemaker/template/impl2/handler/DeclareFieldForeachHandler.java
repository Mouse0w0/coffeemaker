package com.github.mouse0w0.coffeemaker.template.impl2.handler;

import com.github.mouse0w0.coffeemaker.template.DeclareFieldForeach;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.AnnotationOwner;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtAnnotation;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtClass;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtField;
import org.objectweb.asm.Opcodes;

public class DeclareFieldForeachHandler extends AnnotationHandler {
    @Override
    protected Class<?>[] getAcceptableAnnotations() {
        return new Class[]{DeclareFieldForeach.class};
    }

    @Override
    protected void handle(AnnotationOwner owner, BtAnnotation annotation) {
        String iterable = annotation.getValue("iterable");
        String elementName = annotation.getValue("elementName");
        int modifierFinal = annotation.getValue("modifierFinal", true) ? Opcodes.ACC_FINAL : 0;

        BtField field = (BtField) owner;
        BtClass clazz = (BtClass) field.getParent().getParent();
        clazz.getFields().remove(field);

        BtDeclareFieldForeach fieldForeach = new BtDeclareFieldForeach(iterable, elementName);
        fieldForeach.putValue(BtField.ACCESS, field.get(BtField.ACCESS).getAsInt() | modifierFinal);
        fieldForeach.put(BtField.SIGNATURE, field.get(BtField.SIGNATURE));
        fieldForeach.put(BtField.VALUE, field.get(BtField.VALUE));
        fieldForeach.put(BtField.ATTRIBUTES, field.get(BtField.ATTRIBUTES));
        field.getAnnotations().forEach(fieldForeach.getAnnotations()::add);
        fieldForeach.getAnnotations().remove(annotation);
        clazz.getFields().add(fieldForeach);
    }
}
