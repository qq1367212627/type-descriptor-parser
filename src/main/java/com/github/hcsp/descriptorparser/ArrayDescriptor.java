package com.github.hcsp.descriptorparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数组类型的描述符，如输入[[Ljava/lang/Object;
 * 得到的name是java.lang.Object[][]
 * dimension为2（二维数组）
 * rawType代表无数组的原始类型
 */
public class ArrayDescriptor implements TypeDescriptor {
    // [ -> 1
    // [[ -> 2
    private String name;
    private String descriptor;
    private int dimension;
    private TypeDescriptor rawType;


    public static final String CHARACTER_REGEX = "\\[";

    public static final String REFERENCE_TYPE_PREFIX = "L";

    public static final String ARRAY_SUFFIX = "[]";

    private static final Pattern arrayPattern;

    static {
        arrayPattern = Pattern.compile(CHARACTER_REGEX);
    }


    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;

        String arrayDimension = buildArrayDimensionDesc(descriptor);

        String dataTypeDesc = dimension > 0 ? descriptor.substring(dimension) : descriptor;

        if (dataTypeDesc.startsWith(REFERENCE_TYPE_PREFIX)) {
            this.rawType =  new ReferenceDescriptor(dataTypeDesc);
        } else {
            this.rawType = PrimitiveTypeDescriptor.of(dataTypeDesc);
        }

        this.name = rawType.getName() + arrayDimension;
    }

    /**
     * 构造数组维度
     * @param descriptor
     * @return 返回构造好的数组维度描述
     */
    private String buildArrayDimensionDesc(String descriptor) {
        Matcher matcher = arrayPattern.matcher(descriptor);

        StringBuffer arrayDimensionDesc = new StringBuffer(1 << 5);
        while (matcher.find()) {
            dimension++;
            arrayDimensionDesc.append(ARRAY_SUFFIX);
        }
        return arrayDimensionDesc.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public TypeDescriptor getRawType() {
        return rawType;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}
