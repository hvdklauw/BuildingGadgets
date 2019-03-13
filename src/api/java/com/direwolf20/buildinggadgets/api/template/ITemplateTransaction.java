package com.direwolf20.buildinggadgets.api.template;

import com.direwolf20.buildinggadgets.api.exceptions.TransactionExecutionException;
import net.minecraft.util.math.BlockPos;

/**
 * This class represents a transaction used for modifying {@link ITemplate} in more advanced ways than just translating the {@link ITemplate}
 * to some other location. It has therefore the following responsibilities:
 * <ul>
 *     <li>Provide a possibility for registering {@link ITransactionOperator} which specify the actual work to be performed by this Transaction.</li>
 *     <li>Provide a possibility to execute the {@link ITransactionOperator}'s as efficiently as possible for the backing {@link ITemplate}.</li>
 * </ul><br>
 * For more detailed Information about how the {@code ITemplateTransaction} will be executed see {@link #execute()}.
 */
public interface ITemplateTransaction {
    /**
     * Registers the given {@link ITransactionOperator} with this {@code ITemplateTransaction}.
     * The passed in {@link ITransactionOperator} may be a duplicate.<br>
     * <b>This Method does not execute the {@link ITransactionOperator}, see {@link #execute()} for more Information.</b>
     * @param operator The {@link ITransactionOperator} to register with this {@code ITemplateTransaction}
     * @throws NullPointerException if faced by an null value
     */
    void operate(ITransactionOperator operator);

    /**
     * This Method executes all via {@link #operate(ITransactionOperator)} specified {@link ITransactionOperator}'s in the order in which they were specified.
     * It is recommended that the implementation iterates over the Contents of the backing {@link ITemplate} only once and executes the
     * {@link ITransactionOperator} on each position as specified. Further optimization might be possible by taking {@link ITransactionOperator#characteristics()}
     * into account which describe, what kind of Operations a given {@link ITransactionOperator} performs.
     * <p>
     *     The required execution order for the specified Operations is as follows:
     *     <ol>
     *         <li>Call {@link ITransactionOperator#createPos()} until it returns null. The resulting positions must exist in the resulting {@link ITemplate}.</li>
     *         <li>Call {@link ITransactionOperator#createDataForPos(BlockPos)} for each Position previously returned by {@link ITransactionOperator#createPos()}</li>
     *         <li>Call {@link ITransactionOperator#transformData(BlockData)} <b>for each BlockData in the {@link ITemplate}.</b></li>
     *         <li>Call {@link ITransactionOperator#transformPos(BlockPos, BlockData)} <b>for each Position in the {@link ITemplate}.</b></li>
     *     </ol>
     * </p>
     * @implSpec Notice that an {@code ITemplateTransaction} is considered invalid as soon as {@code execute()} has been called.
     *           Any further calls should throw {@link UnsupportedOperationException}.
     * @implNote Note that {@link ITransactionOperator#createPos()} may produce Positions already contained in the backing {@link ITemplate}.
     *           This indicates that current Data should be replaced by whatever {@link ITransactionOperator#createDataForPos(BlockPos)} returns.
     * @implNote Note that {@link ITransactionOperator#createDataForPos(BlockPos)} may also return null in cases where data is already present in the backing
     *           {@link ITemplate}. Returning null from this method, when no data is present for the given Position is considered an error and
     *           should therefore throw an {@link com.direwolf20.buildinggadgets.api.exceptions.OperatorExecutionFailedException}.
     * @implNote Note that {@link ITransactionOperator#transformData(BlockData)} and {@link ITransactionOperator#transformPos(BlockPos, BlockData)} may
     *           return null to indicate that a certain position or <b>all positions referenced by a specific {@link BlockData}</b> should be removed from
     *           the backing {@link ITemplate}.
     * @return The transformed Template, as specified by the passed in {@link ITransactionOperator}'s. May be the original or a new instance.
     * @throws com.direwolf20.buildinggadgets.api.exceptions.ConcurrentTransactionExecutionException If this method is attempted to be used concurrently, but
     *         concurrent execution is not supported by this {@code ITemplateTransaction}
     * @throws com.direwolf20.buildinggadgets.api.exceptions.OperatorExecutionFailedException If an {@link ITransactionOperator} throws an {@link Exception}
     * @throws TransactionExecutionException If any other {@link Exception} is encountered or the implementation specifies additional subclasses
     * @throws UnsupportedOperationException If {@code execute} has already been called on this {@code ITemplateTransaction}
     */
    ITemplate execute() throws TransactionExecutionException;
}
