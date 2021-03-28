package edu.njit.cs630.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 3/27/2021
 */
public class Processor {
    private String name;
    private long contextSwitches;
    private long interrupts;
    private String vendorFequency;
    private String cpuLoad;
    private Map<String, String> cpuLoadPerProcessor = new LinkedHashMap<>();
    private Map<String, String> frequencyPerCore = new LinkedHashMap<>();

    public Processor() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getContextSwitches() {
        return contextSwitches;
    }

    public void setContextSwitches(long contextSwitches) {
        this.contextSwitches = contextSwitches;
    }

    public long getInterrupts() {
        return interrupts;
    }

    public void setInterrupts(long interrupts) {
        this.interrupts = interrupts;
    }

    public String getVendorFequency() {
        return vendorFequency;
    }

    public void setVendorFequency(String vendorFequency) {
        this.vendorFequency = vendorFequency;
    }

    public String getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(String cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public Map<String, String> getCpuLoadPerProcessor() {
        return cpuLoadPerProcessor;
    }

    public void setCpuLoadPerProcessor(LinkedHashMap<String, String> cpuLoadPerProcessor) {
        this.cpuLoadPerProcessor = cpuLoadPerProcessor;
    }

    public Map<String, String> getFrequencyPerCore() {
        return frequencyPerCore;
    }

    public void setFrequencyPerCore(LinkedHashMap<String, String> frequencyPerCore) {
        this.frequencyPerCore = frequencyPerCore;
    }
}
