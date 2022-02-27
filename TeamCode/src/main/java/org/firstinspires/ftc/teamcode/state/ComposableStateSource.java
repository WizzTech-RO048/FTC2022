package org.firstinspires.ftc.teamcode.state;

public abstract class ComposableStateSource {
    protected double time = 0, startTime = 0, timePassed = 0;

    public abstract boolean update();

    public void stop() {}

    void setTime(double time, double startTime, double timePassed) {
        this.time = time;
        this.startTime = startTime;
        this.timePassed = timePassed;
    }
}
