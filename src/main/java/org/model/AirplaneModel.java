package org.model;

/**
 * MODEL: Dati di Airplane e simulazione della fisica
 */
public class AirplaneModel extends GameObject {

    // ── physics parameters ────────────────────────────────────────────────────
    private double maxEnginePower = 20000;
    private double pitchSpeed = 30;
    private double yawSpeed = 20;
    private double rollSpeed = 40;
    private double gravity = 65;
    private double mass = 1000;
    private double liftCoefficient = 1.5;
    private double dragCoefficient = 0.2;
    private double angularDragCoef = 1;
    private double aerodynamicEffect = 0.01;
    private double yawRollEffect = 30;
    private double groundLevel = 0;

    // ── runtime state ─────────────────────────────────────────────────────────
    private double throttle = 0;
    private double deltaTime = 0.01;
    private double lastFrame = 0;
    private boolean physicsEnabled = false;
    private boolean takenOff = false;

    // ── physics internals ─────────────────────────────────────────────────────
    private Vector3 physicsPosition = new Vector3();
    private EulerAngle physicsRotation = new EulerAngle();
    public Vector3 velocity = new Vector3();
    private double forwardSpeed = 0;
    private double velocityPitch = 0;
    private double velocityYaw = 0;
    private double velocityRoll = 0;

    // ── input flags (set by controller) ──────────────────────────────────────
    public boolean inputThrottleUp, inputThrottleDown;
    public boolean inputPitchUp, inputPitchDown;
    public boolean inputRollLeft, inputRollRight;
    public boolean inputYawLeft, inputYawRight;
    public boolean inputBrakes;

    public AirplaneModel() {
        super("Airplane", new Mesh("airplane.obj", "airplaneTexture.png", new Vector3(), new EulerAngle(), 2, true, true), new Transform(new Vector3()));
        lastFrame = System.currentTimeMillis();
    }

    // ── public API ────────────────────────────────────────────────────────────

    public void startPhysics() {
        physicsEnabled = true;
        lastFrame = System.currentTimeMillis();
    }
    public void stopPhysics() {
        physicsEnabled = false;
    }

    /**
     * Advances one physics tick. Should be called from a controller timer.
     * @return true if physics ran (model was not stopped)
     */
    public boolean tick() {
        if (!physicsEnabled) return false;

        // throttle
        if (inputThrottleUp && throttle < 1) throttle += deltaTime;
        if (inputThrottleDown && throttle > 0) throttle -= deltaTime;

        // angular torques from input
        if (inputPitchUp) addPitchTorque(-pitchSpeed);
        if (inputPitchDown) addPitchTorque(pitchSpeed);
        if (inputRollLeft) addRollTorque(rollSpeed);
        if (inputRollRight) addRollTorque(-rollSpeed);
        if (inputYawLeft) addYawTorque(-yawSpeed);
        if (inputYawRight) addYawTorque(yawSpeed);

        // physics steps (order matters)
        calculateForwardV();
        applyAerodynamicEffect();
        applyYawRollEffect();
        applyGravity();
        applyLift();
        applyDrag();
        applyAngularDrag();
        updateOrientation();
        updatePosition();
        applyThrust(throttle * maxEnginePower);
        applyBrakes();

        getMesh().refreshLighting();

        deltaTime = (System.currentTimeMillis() - lastFrame) / 1000.0;
        lastFrame = System.currentTimeMillis();
        return true;
    }

    /** Resets the airplane to the origin with zeroed physics. */
    public void resetPhysics() {
        physicsRotation = new EulerAngle();
        physicsPosition = new Vector3();
        velocity = new Vector3();
        velocityPitch = velocityYaw = velocityRoll = 0;
        throttle = 0;
        takenOff = false;
        getMesh().resetPosition();
        getMesh().rotate(getTransform().toLocalMatrix(), new Vector3());
        setTransform(new Transform(new Vector3()));
        updateOrientation();
        updatePosition();
    }

    // ── physics helpers ───────────────────────────────────────────────────────

    private void addForce(Vector3 force) {
        velocity.add(Vector3.multiply(force, 1.0 / mass));
    }

    private double controlScale() {
        return Math.min(1, forwardSpeed / 20);
    }

    private void addPitchTorque(double amt) {
        velocityPitch += amt * controlScale() / mass * deltaTime;
    }
    private void addYawTorque(double amt)   {
        velocityYaw   += amt * controlScale() / mass * deltaTime;
    }
    private void addRollTorque(double amt)  {
        velocityRoll  += amt * controlScale() / mass * deltaTime;
    }

    private void calculateForwardV() {
        forwardSpeed = Vector3.dotProduct(velocity, getTransform().getForward());
    }

    private void applyGravity() {
        velocity.add(new Vector3(0, -gravity * deltaTime, 0));
    }

    private double altitudeFactor(double extraBase) {
        double f = 2000 / (physicsPosition.y - groundLevel + 2000) + extraBase;
        return f * f * f;
    }

    private void applyLift() {
        addForce(Vector3.multiply(Vector3.crossProduct(velocity, getTransform().getRight()).getNormalized(), Math.min(2000, forwardSpeed * forwardSpeed * liftCoefficient * deltaTime * altitudeFactor(1))));
    }

    private void applyDrag() {
        if (velocity.getSqrMagnitude() > 0.1) {
            double af = altitudeFactor(0.6);
            velocity = Vector3.subtract(velocity, Vector3.multiply(velocity, Math.min(1, dragCoefficient * velocity.getMagnitude() * deltaTime * af / 700)));
            Vector3 vDrag = Vector3.projectToVector(velocity, getTransform().getUp());
            velocity = Vector3.subtract(velocity, Vector3.multiply(vDrag, Math.min(1, dragCoefficient * vDrag.getMagnitude() * deltaTime * af)));
        }
    }

    private void applyAngularDrag() {
        velocityPitch -= velocityPitch * angularDragCoef * deltaTime;
        velocityYaw -= velocityYaw * angularDragCoef * deltaTime;
        velocityRoll -= velocityRoll * angularDragCoef * deltaTime;
    }

    private void applyYawRollEffect() {
        if (velocity.x != 0 && velocity.z != 0 && (inputYawLeft || inputYawRight)) {
            double roll = Vector3.dotProduct(Vector3.projectToPlane(velocity, getTransform().getUp()).getNormalized(), getTransform().getRight());
            roll *= roll * roll;
            velocityRoll += deltaTime * roll * yawRollEffect;
        }
    }

    private void applyAerodynamicEffect() {
        if (velocity.getSqrMagnitude() > 0) {
            double cf = Vector3.dotProduct(getTransform().getForward(), velocity.getNormalized());
            cf *= cf;
            velocity = Vector3.lerp(velocity, Vector3.projectToVector(velocity, getTransform().getForward()), cf * forwardSpeed * aerodynamicEffect * deltaTime / 2);
            Vector3 dir = getTransform().transformToLocal(
                    Vector3.lerp(getTransform().getForward(), velocity.getNormalized(), cf * aerodynamicEffect * deltaTime * forwardSpeed));
            physicsRotation.y += (dir.x < 0) ? -Math.atan(dir.z / dir.x) - Math.PI / 2 :  Math.PI / 2 - Math.atan(dir.z / dir.x);
            physicsRotation.x += Math.atan(dir.y / Math.sqrt(dir.x * dir.x + dir.z * dir.z)) / 5;
        }
    }

    private void applyThrust(double amount) {
        addForce(Vector3.multiply(getTransform().getForward(), amount * deltaTime));
    }

    private void applyBrakes() {
        dragCoefficient = inputBrakes ? 1.0 : 0.2;
    }

    private void updateOrientation() {
        physicsRotation.x += velocityPitch;
        physicsRotation.y += velocityYaw;
        physicsRotation.z += velocityRoll;
        getTransform().setPitch(physicsRotation.x);
        getTransform().setYaw(physicsRotation.y);
        getTransform().setRoll(physicsRotation.z);
    }

    private void updatePosition() {
        if (Vector3.add(physicsPosition, velocity).y > groundLevel) {
            physicsPosition.add(velocity);
            if (physicsPosition.y > 50 && !takenOff) takenOff = true;
        } else {
            physicsPosition.y = groundLevel;
            velocity.y = 0;
            physicsPosition.add(velocity);
        }
        getTransform().setPosition(Vector3.add(getTransform().getPosition(), velocity));
    }

    // ── telemetry (read by view / controller) ─────────────────────────────────
    public double getAltitude() {
        return physicsPosition.y - groundLevel;
    }
    public double getSpeed() {
        return velocity.getMagnitude();
    }
    public double getThrottle() {
        return throttle;
    }
    public double getVerticalClimb() {
        return velocity.y;
    }
    public EulerAngle getOrientation() {
        return physicsRotation;
    }
    public Vector3 getPhysicsPosition() {
        return physicsPosition;
    }
    public boolean isPhysicsEnabled() {
        return physicsEnabled;
    }
}
