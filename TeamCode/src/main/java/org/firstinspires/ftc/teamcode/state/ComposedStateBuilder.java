package org.firstinspires.ftc.teamcode.state;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public final class ComposedStateBuilder {
    private final ArrayList<Supplier<ComposableStateSource>> sources = new ArrayList<>();

    @SafeVarargs
    public final ComposedStateBuilder compose(@NonNull Supplier<ComposableStateSource> source, Supplier<ComposableStateSource>... others) {
        sources.add(source);
        sources.addAll(Arrays.asList(others));

        return this;
    }

    public State build(@NonNull Supplier<State> finalState) {
        return new ComposedState(sources, finalState);
    }
}
