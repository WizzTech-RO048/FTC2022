package org.firstinspires.ftc.teamcode.state;

import androidx.annotation.NonNull;

import java.util.function.Supplier;

final class ComposableState extends State {
    private final ComposableStateSource source;
    private final State parentState;

    ComposableState(@NonNull Supplier<ComposableStateSource> source, @NonNull State parentState) {
        this.source = source.get();
        this.parentState = parentState;
    }

    @Override
    public State update() {
        source.setTime(time, startTime, timePassed);

        if (source.update()) {
            return parentState;
        }

        return this;
    }

    @Override
    public void stop() {
        source.stop();
    }
}
