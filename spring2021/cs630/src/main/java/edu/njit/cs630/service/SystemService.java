package edu.njit.cs630.service;

import org.jutils.jhardware.HardwareInfo;
import org.jutils.jhardware.model.*;
import org.springframework.stereotype.Service;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 3/26/2021
 */

@Service
public class SystemService {

    public ProcessorInfo getProcessorInfo() {
        return HardwareInfo.getProcessorInfo();
    }

    public BiosInfo getBiosInfo() {
        return HardwareInfo.getBiosInfo();
    }

    public OSInfo getOSInfo() {
        return HardwareInfo.getOSInfo();
    }

    public MemoryInfo getMemoryInfo() {
        return HardwareInfo.getMemoryInfo();
    }

    public MotherboardInfo getMotherboardInfo() {
        return HardwareInfo.getMotherboardInfo();
    }

    public DisplayInfo getDisplayInfo() {
        return HardwareInfo.getDisplayInfo();
    }

    public NetworkInfo getNetworkInfo() {
        return HardwareInfo.getNetworkInfo();
    }

    public GraphicsCardInfo getGraphicsCardInfo() {
        return HardwareInfo.getGraphicsCardInfo();
    }
}
