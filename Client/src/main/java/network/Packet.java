package network;

import java.io.Serializable;

public class Packet implements Serializable {
    private String index;

    private String username;

    private String name;

    private String role;
    public Packet(String index) {
        this.index = index;
    }

    public String getUsername() { return this.username; }

    public String getName() { return this.name; }

    public String getRole() { return this.role; }

    public String getIndex() { return this.index; }

    public void setUsername(String username) { this.username = username; }

    public void setIndex(String index) { this.index = index; }

    public void setName(String name) { this.name = name; }

    public void setRole(String role) { this.role = role; }
}