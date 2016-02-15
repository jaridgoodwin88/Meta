package com.github.t1.meta2;

/**
 * The structure of an object: <ul>
 *   <li>{@link Scalar} for objects without a sub-structure,
 *   <li>{@link Sequence} for repeated objects, or
 *   <li>{@link Mapping} for a projection of a name to a value.
 * </ul>
 */
public enum StructureKind {
    scalar,
    sequence,
    mapping
}
