package dev.com.kupreychik.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Device {
    private long id;
    private String name;
    private String model;
    private boolean isActive;

    @JsonIgnore
    private List<Port> ports = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public void addPort(Port port) {
        this.ports.add(port);
    }

    public List<Port> getPorts() {
        return List.copyOf(this.ports);
    }
}