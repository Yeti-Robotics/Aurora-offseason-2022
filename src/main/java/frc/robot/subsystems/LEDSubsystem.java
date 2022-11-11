package frc.robot.subsystems;


import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDSubsystem extends SubsystemBase {
    public LEDSubsystem() {

        AddressableLED m_led = new AddressableLED(1);

        AddressableLEDBuffer m_ledBuffer = new AddressableLEDBuffer(60);
        m_led.setLength(288);

        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values for red
            m_ledBuffer.setRGB(i, 255, 0, 0);
        }

        m_led.setData(m_ledBuffer);
        m_led.start();

    }
}

