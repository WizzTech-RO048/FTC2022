package org.firstinspires.ftc.teamcode.state;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Supplier;

final class ComposedState extends State {
    private final List<Supplier<ComposableStateSource>> sources;
    private final Supplier<State> finalState;
    private int currentSourceIndex = -1;

    ComposedState(@NonNull List<Supplier<ComposableStateSource>> sources, @NonNull Supplier<State> finalState) {
        this.sources = sources;
        this.finalState = finalState;
    }

    @Override
    public State update() {
        currentSourceIndex++;
        if (currentSourceIndex == sources.size()) {
            return finalState.get();
        }

        return new ComposableState(sources.get(currentSourceIndex), this);
    }
}
