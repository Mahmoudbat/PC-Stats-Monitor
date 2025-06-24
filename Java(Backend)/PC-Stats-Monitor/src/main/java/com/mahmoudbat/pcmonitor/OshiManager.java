package com.mahmoudbat.pcmonitor;

import oshi.SystemInfo;
import oshi.hardware.*;

/**
 * This class uses the OSHI library to gather system hardware information.
 * It retrieves RAM usage, CPU usage, and CPU temperature.
 * Can be extended to support GPU monitoring as well.
 */

public class OshiManager {

    // --- OSHI system interfaces ---
    private final SystemInfo si;
    private final GlobalMemory memory;
    private final CentralProcessor cpu;
    private final Sensors sensors;


    // Used to calculate CPU usage between two points in time
    private long[] prevTicks;

    /**
     * Constructor initializes OSHI components and captures initial CPU tick state.
     */
    public OshiManager(){
        this.si = new SystemInfo();
        this.memory = si.getHardware().getMemory();
        cpu = si.getHardware().getProcessor();
        this.sensors = si.getHardware().getSensors();
        this.prevTicks = cpu.getSystemCpuLoadTicks();
    }

    /**
     * Returns RAM usage percentage.
     * @return memory usage as a percentage of total RAM.
     */
    double getRamUsage(){
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();

        return (double) (totalMemory - availableMemory) / totalMemory * 100;
    }

    /**
     * Returns CPU usage since the last check.
     * This uses ticks recorded previously to calculate usage over time.
     * @return CPU usage percentage
     */
    double getCpuUsage(){
        double cpuload = cpu.getSystemCpuLoadBetweenTicks(prevTicks) * 100;

        this.prevTicks = cpu.getSystemCpuLoadTicks();

        return (double) cpuload;
    }

    /**
     * Returns current CPU temperature in Celsius.
     * @return CPU temperature
     * might not give accurate value on linux
     */
    double getCpuTemp() {
        double ctemp = sensors.getCpuTemperature();



        return ctemp;
    }

    //TODO
    /**
     * Placeholder for GPU usage.
     * To be implemented later using vendor-specific APIs (e.g., NVML for NVIDIA).
     * @return dummy value (0 for now)
     */
    double getGpuUsage(){

        return 0;
    }

    //TODO
    /**
     * Placeholder for GPU temperature.
     * To be implemented in the future.
     * @return dummy value (0 for now)
     */
    double getGpuTemp(){

        return 0;
    }


    }


