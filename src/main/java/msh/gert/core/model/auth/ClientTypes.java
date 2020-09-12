package msh.gert.core.model.auth;

public enum ClientTypes {
    PUBLIC("public"),
    CONFIDENTIAL("confidential");

    private String type;

    ClientTypes(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.type;
    }
}