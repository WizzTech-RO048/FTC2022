package org.firstinspires.ftc.teamcode.ComputerVision;

public class StartPosition {
    public enum Position {
        RED1(),
        RED2(),
        BLUE1(),
        BLUE2();

        @Override
        public String toString() {
            switch (this) {
                case RED1:
                    return "Red 1";
                case RED2:
                    return "Red 2";
                case BLUE1:
                    return "Blue 1";
                case BLUE2:
                    return "Blue 2";
                default:
                    return "None";
            }
        }
    }
}
