package edu.njit.cs630.domain;

import oshi.hardware.ComputerSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 3/27/2021
 */
public class OsInfo {
    private List<String> details;
    private ComputerSystem computerSystem;

    public OsInfo(List<String> details, ComputerSystem computerSystem) {
        this.details = details;
        this.computerSystem = computerSystem;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public ComputerSystem getComputerSystem() {
        return computerSystem;
    }

    public void setComputerSystem(ComputerSystem computerSystem) {
        this.computerSystem = computerSystem;
    }
}
