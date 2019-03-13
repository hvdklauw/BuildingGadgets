package com.direwolf20.buildinggadgets.api.template;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Represents an operation which can be performed by an {@link ITemplateTransaction} in order to change a given {@link ITemplate}.
 * The following 3 types of operations are supported
 * <ul>
 *     <li>Transforming or removing Block-Positions</li>
 *     <li>Transforming or removing Block-Data</li>
 *     <li>Attaching new Blocks and the Data to this Template.</li>
 * </ul>
 * @implNote All implementations of this class are expected to throw {@link NullPointerException} when any argument passed to any method was null.
 */
public interface ITransactionOperator {
    /**
     * Represents characteristics (operations) an {@code ITransactionOperator} might have.
     * <p>
     * <b>It is not guaranteed that the order of this {@link Enum}'s values will remain consistend nor is
     * it guaranteed that no values will be added.</b> Users switch casing over this enum should therefore always provide
     * a default case. Users may furthermore not depend on this Enum's ordinal for serialisation.
     * </p>
     */
    enum Characteristic {
        /**
         * This {@code Characteristic} represents the ability of an {@code ITransactionOperator} to transform positions via
         * {@link #transformPos(BlockPos, BlockData)}.
         */
        TRANSFORM_POSITION,
        /**
         * This {@code Characteristic} represents the ability of an {@code ITransactionOperator} to transform data via
         * {@link #transformData(BlockData)}.
         */
        TRANSFORM_DATA,
        /**
         * This {@code Characteristic} represents the ability of an {@code ITransactionOperator} to create new data via
         * {@link #createPos()} and {@link #createDataForPos(BlockPos)}.
         */
        CREATE_DATA
    }

    /**
     * Allows this {@code ITransactionOperator} to add arbitrary {@link BlockPos} to a given {@link ITemplate}. Notice that returning a
     * {@link BlockPos} which is already contained in a given {@link ITemplate} will effectively replace the previous data with whatever
     * {@link BlockData} {@link #createDataForPos(BlockPos)} returns.
     * @return A new {@link BlockPos} to add to a given {@link ITemplate} or null if no more positions should be added
     */
    @Nullable
    default BlockPos createPos() {
        return null;
    }

    /**
     * This method creates arbitrary {@link BlockData} for a given {@link BlockPos} returned by {@link #createPos()},
     * overwriting previous data in the process.
     * <p>
     * Notice that even though you may return null from this Method, doing so for an {@link BlockPos} which is not already present in
     * the backing {@link ITemplate} is considered an Exceptional-Condition and will result in Runtime-Failure of the executing
     * {@link ITemplateTransaction}.
     * </p>
     * @param pos The pos for which to create {@link BlockData} for.
     * @return A new {@link BlockData} for a given {@link BlockPos} or null if none can or should be created.
     */
    @Nullable
    default BlockData createDataForPos(BlockPos pos) {
        throw new UnsupportedOperationException("Default implementation does not support creating BlockData!");
    }

    /**
     * Performs arbitrary operations on the given {@link BlockData}. <br>
     * Will be called after {@link #createPos()} and {@link #createDataForPos(BlockPos)} are finished executing.
     * @param data The {@link BlockData} to be transformed by this {@code ITransactionOperator}'s
     * @return The transformed {@link BlockData} or null to remove it <b>and all referencing positions</b> from the Template.
     * @implNote The default implementation is the identity function.
     */
    @Nullable
    default BlockData transformData(BlockData data) {
        return data;
    }

    /**
     * Performs arbitrary operations on the given {@link BlockPos}. The {@link BlockData} associated with this position is passed into this Method
     * in order to provide more context. <br>
     * Will be called after {@link #transformData(BlockData)} is finished executing for the {@link BlockData} associated with this {@link BlockPos}.
     * @param pos The position to transform
     * @param data The {@link BlockData} associated with this position
     * @return The new transformed {@link BlockPos} or null if it should be removed from the given {@link ITemplate}
     * @implNote The default implementation just returns the given position
     */
    @Nullable
    default BlockPos transformPos(BlockPos pos, BlockData data) {
        return pos;
    }

    /**
     * Returns an {@link Set} of {@link Characteristic} to indicate which operations are performed by this {@code ITransactionOperator}.
     * All {@link Characteristic} returned by this Method are guaranteed to be executed.
     * @return A {@link Set} representing the {@link Characteristic} of this {@code ITransactionOperator}
     */
    default Set<Characteristic> characteristics() {
        return ImmutableSet.of();
    }
}
