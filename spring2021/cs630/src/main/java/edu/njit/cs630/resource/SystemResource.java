package edu.njit.cs630.resource;

import edu.njit.cs630.domain.OsInfo;
import edu.njit.cs630.domain.Processor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OSService;
import oshi.software.os.OSSession;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.time.Instant;
import java.util.*;

import static oshi.software.os.OperatingSystem.ProcessFiltering.ALL_PROCESSES;
import static oshi.software.os.OperatingSystem.ProcessSorting.CPU_DESC;

/**
 * @author Roland Junior Toussaint
 * @version 1.0
 * @since 3/27/2021
 */

@RestController
@RequestMapping(path = {"/system"})
public class SystemResource {
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hardware = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();

    @GetMapping("/osinfo")
    public ResponseEntity<OsInfo>getOSInfo() {
        List<String> details = new ArrayList<>();
        details.add(String.valueOf(os));
        details.add("Booted: " + Instant.ofEpochSecond(os.getSystemBootTime()));
        details.add("Uptime: " + FormatUtil.formatElapsedSecs(os.getSystemUptime()));
        details.add("Sessions:");
        for (OSSession s : os.getSessions()) {
            details.add(" " + s.toString());
        }
        OsInfo osInfo = new OsInfo(details, hardware.getComputerSystem());
        return ResponseEntity.ok(osInfo);
    }

    @GetMapping("/memory")
    public ResponseEntity<GlobalMemory>getGlobalMemory() {
        return ResponseEntity.ok(hardware.getMemory());
    }

    @GetMapping("/processor")
    public ResponseEntity<Processor>getProcessor() {
        Processor processor = new Processor();
        processor.setName(hardware.getProcessor().getProcessorIdentifier().getName());
        processor.setContextSwitches(hardware.getProcessor().getContextSwitches());
        processor.setInterrupts(hardware.getProcessor().getInterrupts());
        processor.setVendorFequency(FormatUtil.formatHertz(hardware.getProcessor().getProcessorIdentifier().getVendorFreq()));
        long[] prevTicks = hardware.getProcessor().getSystemCpuLoadTicks();
        long[][] prevProcTicks = hardware.getProcessor().getProcessorCpuLoadTicks();
        // Wait a second...
        Util.sleep(1000);
        processor.setCpuLoad(String.format("%.2f%%", hardware.getProcessor().getSystemCpuLoadBetweenTicks(prevTicks) * 100));
        double[] cores = hardware.getProcessor().getProcessorCpuLoadBetweenTicks(prevProcTicks);
        LinkedHashMap<String, String> cpuLoadPerCore = new LinkedHashMap<>();
        int coresCounter = 0;
        for (double core : cores) {
            cpuLoadPerCore.put("CPU Core " + String.valueOf(++coresCounter), String.format("%.1f%%", core * 100));
        }
        processor.setCpuLoadPerCore(cpuLoadPerCore);
        LinkedHashMap<String, String> frequencyPerCore = new LinkedHashMap<>();
        long[] freqencies = hardware.getProcessor().getCurrentFreq();
        coresCounter = 0;
        if (freqencies[0] > 0) {
            StringBuilder sb = new StringBuilder("Current Frequencies: ");
            for (int i = 0; i < freqencies.length; i++) {
                frequencyPerCore.put("CPU Core " + String.valueOf(++coresCounter), FormatUtil.formatHertz(freqencies[i]));
            }
            processor.setFrequencyPerCore(frequencyPerCore);
        }
        return ResponseEntity.ok(processor);
    }

    @GetMapping("/processes")
    public ResponseEntity<List<OSProcess>>getProcesses() {
        return ResponseEntity.ok(os.getProcesses(ALL_PROCESSES, CPU_DESC, 10));
        //return ResponseEntity.ok(os.getProcesses());
    }

    @GetMapping("/services")
    public ResponseEntity<OSService[]>getServices() {
        return ResponseEntity.ok(os.getServices());
    }
}
