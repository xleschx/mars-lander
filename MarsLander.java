import java.util.Scanner;

class MarsLander {
    private static final int SAFETY_MARGIN = 20;
    private static final int SPEED_MARGIN = 5;
    private static final int MAX_VERTICAL_SPEED = 40;
    private static final int MAX_HORIZONTAL_SPEED = 20;
    private static final double GRAVITY_ON_MARS = 3.711;
    
    private int xPos, yPos, horizontalSpeed, verticalSpeed, fuel, rotationAngle, thrustPower;
    private int landingZoneStart, landingZoneEnd, landingZoneAltitude;
    
    public MarsLander() {}

    public void updateState(int xPos, int yPos, int horizontalSpeed, int verticalSpeed, int fuel, int rotationAngle, int thrustPower) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.horizontalSpeed = horizontalSpeed;
        this.verticalSpeed = verticalSpeed;
        this.fuel = fuel;
        this.rotationAngle = rotationAngle;
        this.thrustPower = thrustPower;
    }

    public void setLandingZone(int start, int end, int altitude) {
        this.landingZoneStart = start;
        this.landingZoneEnd = end;
        this.landingZoneAltitude = altitude;
    }

    public boolean isAboveLandingZone() {
        return xPos >= landingZoneStart && xPos <= landingZoneEnd;
    }

    public boolean isApproachingLanding() {
        return yPos < landingZoneAltitude + SAFETY_MARGIN;
    }

    public boolean hasSafeSpeed() {
        return Math.abs(horizontalSpeed) <= MAX_HORIZONTAL_SPEED - SPEED_MARGIN &&
               Math.abs(verticalSpeed) <= MAX_VERTICAL_SPEED - SPEED_MARGIN;
    }

    public boolean isOffCourse() {
        return (xPos < landingZoneStart && horizontalSpeed < 0) || (xPos > landingZoneEnd && horizontalSpeed > 0);
    }

    public boolean isSpeedingHorizontally() {
        return Math.abs(horizontalSpeed) > 4 * MAX_HORIZONTAL_SPEED;
    }

    public boolean isTooSlowHorizontally() {
        return Math.abs(horizontalSpeed) < 2 * MAX_HORIZONTAL_SPEED;
    }

    public int calculateRotationToSlowDown() {
        double totalSpeed = Math.sqrt(horizontalSpeed * horizontalSpeed + verticalSpeed * verticalSpeed);
        return (int) Math.toDegrees(Math.asin((double) horizontalSpeed / totalSpeed));
    }

    public int calculateTargetRotation() {
        int targetRotation = (int) Math.toDegrees(Math.acos(GRAVITY_ON_MARS / 4.0));
        if (xPos < landingZoneStart)
            return -targetRotation;
        else if (xPos > landingZoneEnd)
            return targetRotation;
        else
            return 0;
    }

    public int calculateHoverThrust() {
        return (verticalSpeed >= 0) ? 3 : 4;
    }
}

class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        MarsLander lander = new MarsLander();
        int surfacePoints = in.nextInt();
        
        int prevX = -1;
        int prevY = -1;
        for (int i = 0; i < surfacePoints; i++) {
            int landX = in.nextInt();
            int landY = in.nextInt();
            if (landY == prevY) {
                lander.setLandingZone(prevX, landX, landY);
            }
            prevX = landX;
            prevY = landY;
        }

        while (true) {
            lander.updateState(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());

            if (!lander.isAboveLandingZone()) {
                if (lander.isOffCourse() || lander.isSpeedingHorizontally())
                    System.out.println(lander.calculateRotationToSlowDown() + " 4");
                else if (lander.isTooSlowHorizontally())
                    System.out.println(lander.calculateTargetRotation() + " 4");
                else
                    System.out.println("0 " + lander.calculateHoverThrust());
            }
            else {
                if (lander.isApproachingLanding())
                    System.out.println("0 3");
                else if (lander.hasSafeSpeed())
                    System.out.println("0 2");
                else
                    System.out.println(lander.calculateRotationToSlowDown() + " 4");
            }
        }
    }
}
