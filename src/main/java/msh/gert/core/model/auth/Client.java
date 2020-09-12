package msh.gert.core.model.auth;

public class Client {
    private String id;
    private String name;
    private String secret;
    private ClientTypes type;

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return this.secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public ClientTypes getType() {
        return this.type;
    }
    public void setType(ClientTypes type) {
        this.type = type;
    }
}
