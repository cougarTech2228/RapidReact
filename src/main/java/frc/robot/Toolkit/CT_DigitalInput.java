package frc.robot.Toolkit;

import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.BiConsumer;

import edu.wpi.first.wpilibj.AsynchronousInterrupt;
import edu.wpi.first.wpilibj.Timer;

public class CT_DigitalInput {
    private DigitalInput m_digitalInput;

    private Runnable m_methodToRun;
    private Runnable m_lastMethodToRun;
    private boolean m_isInterruptLatched;
    private boolean m_negateLogic;
    private double m_startTime;

    private double m_ignoringInterruptIteration;
    private boolean m_needToRelatchInterrupt;

    private boolean m_handleInterrupts;
    private String m_lastEdgeTriggered;
    
    /**
     * The sole reason for seperate interrupt "state" flags (m_handleInterrupts and m_isInterruptLatched)
     * is so that there are seperate flags for simply handing the interrupt on conditions and doing relatively
     * complex logic within this class. Having the same variable for EVERY method to alter saw inconsistent and 
     * incorrect results, leading to that single flag being altered at inconsistent times when it probably should/shouldn't
     * be. Additionally, the m_isInterruptLatched is more a replacement for calling enable/disableInterrupts, 
     * while m_handleInterrupts is, as the name suggests, handling them not and necessarily enabling/disabling them alltogether. 
     * There might be a way to simplify the flags down to one, but for now handleInterrupts will be with the
     * onlyHandleInterruptsWhen method and everything else will use m_isInterruptLatched. 
     */

    /**
     * Negate logic is useful if you want to reverse the output of getStatus(). A use for setting this variable true
     * would be if you were using a light beam sensor and wanted to differentiate between searching for something blocking
     * the sensors, or searching for the absence of something. 
     */ 

    /**
     * Creates a default DigitalIO instance with the ability to negate the logic.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     * @param negateLogic can negate the return value of getStatus() for this instance.
     */
    public CT_DigitalInput(int pin, boolean negateLogic) {
        this(pin, null, negateLogic);
    }

    /**
     * Creates a default DigitalIO instance.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     */
    public CT_DigitalInput(int pin) {
        this(pin, false);
    }

    /**
     * Creates a DigitalIO instance with a method.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     * @param methodToRun the method that will be ran in the runWhenTripped method.
     */
    public CT_DigitalInput(int pin, Runnable methodToRun) { 
        this(pin, methodToRun, false);
    }

    /**
     * Creates a DigitalIO instance with a method and the ability to negate logic.
     * Used for sensors connected through DIO ports on the RoboRio. Limit Switches, Light Beam sensors, etc.
     * 
     * @param pin the channel the sensor is on.
     * @param methodToRun the method that will be ran in the runWhenTripped method.
     * @param negateLogic can negate the return value of getStatus() for this instance
     */
    public CT_DigitalInput(int pin, Runnable methodToRun, boolean negateLogic) { 
        m_digitalInput = new DigitalInput(pin);
        m_negateLogic = negateLogic;
        m_methodToRun = methodToRun;
        m_lastMethodToRun = m_methodToRun;
        m_isInterruptLatched = false;
        m_startTime = 0;
        m_ignoringInterruptIteration = 0;
        m_needToRelatchInterrupt = false;
        m_handleInterrupts = false;
        m_lastEdgeTriggered = "None";
    }

    /**
     * POLLING/NON-INTERRUPT METHOD
     * 
     * Sets the method that will be ran in the runWhenTripped method.
     * 
     * @param methodToRun the method that will be ran in the runWhenTripped method.
     */
    public void setMethodToRun(Runnable methodToRun) {
        m_methodToRun = methodToRun;
        m_lastMethodToRun = m_methodToRun;
    }

    /**
     * POLLING/NON-INTERRUPT METHOD
     * 
     * Resets the method to run for the runWhenTripped method to use as it won't run 
     * again if a method is not inputted or isn't reseted.
     */
    public void resetMethodToRun() {
        m_methodToRun = m_lastMethodToRun;
    }

    /**
     * POLLING/NON-INTERRUPT METHOD
     * 
     * Checks to see if the digital IO is tripped depending on if it is acive high or low.
     * If the sensor was tripped, the method either passed in to a constructor or DigitalIO method setMethodToRun will run.
     * This method will not work if an interrupt has been enabled. 
     * The given method will only run ONCE and will have to be reset by the DigitalIO method setMethodToRun.
     * This method is best used when called in the periodic method of a subsystem.
     * 
     * @return If the digital IO was tripped.
     */
    public boolean runWhenTripped() {
        if(get() && !m_isInterruptLatched) {

            if(m_methodToRun != null) {
                Runnable method = m_methodToRun;
                m_methodToRun = null;
                method.run();
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Sets an interrupt for the digital input. Can be used in conjunction with the 
     * onlyHandleInterruptsWhen() method to only run the method when certain conditions are met.
     * To use a command, for example use: "() -> new PrintCommand("Interrupt Fired").schedule" for the runnable.
     * 
     * @param runnable the runnable that will run when the interrupt is fired.
     * @param interruptOnRisingEdge fire interrupt on the rising edge.
     * @param interruptOnFallingEdge fire interrupt on the falling edge.
     */
    public void setInterrupt(Runnable runnable, boolean interruptOnRisingEdge, boolean interruptOnFallingEdge) {

        BiConsumer<Boolean, Boolean> callback = (risingEdge, fallingEdge) -> runInterruptMethod(runnable, risingEdge, fallingEdge);
        AsynchronousInterrupt interrupt = new AsynchronousInterrupt(m_digitalInput, callback);
        interrupt.setInterruptEdges(interruptOnRisingEdge, interruptOnFallingEdge);

        interrupt.enable();
        setInterruptLatched(true);
        m_handleInterrupts = true;
    }

    /**
     * Runs the runnable given by the user when the interrupt is fired and
     * sets the m_lastEdgeTriggered variable to what edge just ran. 
     */
    private void runInterruptMethod(Runnable runnable, boolean risingEdge, boolean fallingEdge) {
        if(risingEdge) {
            m_lastEdgeTriggered = "Rising Edge";
        } else if (fallingEdge) {
            m_lastEdgeTriggered = "Falling Edge";
        } else {
            System.out.println("Unexpected output for edge callback");
        }
        runnable.run();
    }

    /**
     * Sets an interrupt for the digital input. Can be used in conjunction with the 
     * onlyHandleInterruptsWhen() method to only run the method when certain conditions are met.
     * To use a command, for example use: "() -> new PrintCommand("Interrupt Fired").schedule" for the runnable.
     * 
     * @param runnable the runnable that will run when the interrupt is fired.
     * @param interruptOnRisingEdge fire interrupt on the rising edge.
     * @param interruptOnFallingEdge fire interrupt on the falling edge.
     * @param time time in seconds for the interrupt to be ignored after the interrupt is activated.
     */
    public void setTimedInterrupt(Runnable runnable, boolean interruptOnRisingEdge, boolean interruptOnFallingEdge, double time) {
        BiConsumer<Boolean, Boolean> callback = (risingEdge, fallingEdge) -> runTimedInterruptMethod(runnable, risingEdge, fallingEdge, time);
        AsynchronousInterrupt interrupt = new AsynchronousInterrupt(m_digitalInput, callback);
        interrupt.setInterruptEdges(interruptOnRisingEdge, interruptOnFallingEdge);

        interrupt.enable();
        setInterruptLatched(true);
        m_handleInterrupts = true;
    }

    /**
     * Runs the runnable given by the user when the interrupt is fired and
     * sets the m_lastEdgeTriggered variable to what edge just ran. 
     */
    private void runTimedInterruptMethod(Runnable runnable, boolean risingEdge, boolean fallingEdge, double time) {
        if(risingEdge) {
            m_lastEdgeTriggered = "Rising Edge";
        } else if (fallingEdge) {
            m_lastEdgeTriggered = "Falling Edge";
        } else {
            System.out.println("Unexpected output for edge callback");
        }

        if((Timer.getFPGATimestamp() - m_startTime) >= time) {
            if(m_isInterruptLatched && m_handleInterrupts) {
                runnable.run();
                // System.out.println("FPGA Time: " + Timer.getFPGATimestamp());
                // System.out.println("Start Time: " + m_startTime);
                m_startTime = Timer.getFPGATimestamp();
            } else { /* Do Nothing */ }
        }
    }

    /**
     * Gets the last edge triggered by the setInterrupt methods.
     */
    public String getLastEdgeTriggered() {
        return m_lastEdgeTriggered;
    }

    /**
     * This method is for if you want to ignore interrupts after a certain action as taken place.
     * Use this method in a periodic to gain full effect as it relies on iteration to count time.
     * Use this method in conjunction with ignoreInterruptsNow().
     * 
     * @param seconds time in seconds interrupts will be ignored after ignoreInterruptsNow() is called.
     */
    public void ignoreInterruptsFor(double seconds) {
        if(m_needToRelatchInterrupt) {
            m_ignoringInterruptIteration++;

            if(m_ignoringInterruptIteration >= (seconds * 50)) {
                //System.out.println("Interrupts can now be seen");
                setInterruptLatched(true);
                m_needToRelatchInterrupt = false;
                m_ignoringInterruptIteration = 0;
            }
        }
    }

    /**
     * This method works with the ignoreInterruptsFor() method, which should be called in a periodic.
     * Call this method when you want interrupts to be ignored for the amount of time passed into ignoreInterruptsFor()
     */
    public void ignoreInterruptsNow() {
        //System.out.println("Ignoring Interrupts");
        setInterruptLatched(false);
        m_needToRelatchInterrupt = true;
    }

    /**
     * Enables the interrupts when all of the conditions are true.
     * Use this method in a periodic for full effect.
     * @param conditions the conditions to be met for the interrupt to be activated. 
     */
    public void onlyHandleInterruptsWhen(boolean... conditions) {
        m_handleInterrupts = isAllTrue(conditions);
    }

    /**
     * Setter method for the latch interrupt variable. Will decide if said interrupt will be run or not.
     * 
     * @param latchInterrupt boolean variable to be set to m_isInterruptLatched
     */
    private void setInterruptLatched(boolean latchInterrupt) {
        if(latchInterrupt != m_isInterruptLatched)
            System.out.println("Interrupt Latched was: " + m_isInterruptLatched);
        m_isInterruptLatched = latchInterrupt;
    }

    public boolean isInterruptLatched() {
        return m_isInterruptLatched;
    }

    /**
     * Gets the status of the digital input. 
     * Returned value is negated depending on the value of negateLogic passed into the constructor.
     * If no negateLogic boolean was passed into the constructor, then the logic will not be negated.
     * 
     * @return the status of the digital input.
     */
    public boolean get() {
        if(m_negateLogic)
            return !m_digitalInput.get();
        else
            return m_digitalInput.get();
    }

    /**
     * Determines if every value in a boolean array is true.
     * @return whether or not every value is true.
     */
    private boolean isAllTrue(boolean[] array) {
        for (boolean b: array) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

}