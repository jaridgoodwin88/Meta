package com.github.t1.meta2;

import static com.github.t1.meta2.StructureKind.*;

import java.util.List;

public interface Mapping {
    public interface Property {
        default StructureKind getKind() {
            if (this instanceof Scalar)
                return scalar;
            if (this instanceof Sequence)
                return sequence;
            if (this instanceof Mapping)
                return mapping;
            throw new IllegalStateException("undefined kind");
        }

        String getName();

        Scalar getScalarValue();
    }

    public default Scalar getScalar(String name) {
        return getProperty(name).getScalarValue();
    }

    public Property getProperty(String name);

    public List<Property> getProperties();
}
