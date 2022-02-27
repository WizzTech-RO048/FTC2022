package org.firstinspires.ftc.teamcode.state;

import androidx.annotation.NonNull;

import java.util.function.Supplier;

public abstract class State {
    /** The current time, in seconds. */
    protected double time = 0;
    /** The time this state was initialized, in seconds. */
    protected double startTime = 0;
    /** Seconds passed since this state was initialized. */
    protected double timePassed = 0;

    public final void setTime(double time) {
        this.time = time;
        if (startTime == 0) {
            startTime = time;
        }
        timePassed = time - startTime;
    }

    public abstract State update();

    public void stop() {}

    @SafeVarargs
    public static ComposedStateBuilder compose(@NonNull Supplier<ComposableStateSource> source, Supplier<ComposableStateSource>... others) {
        return new ComposedStateBuilder().compose(source, others);
    }
}
