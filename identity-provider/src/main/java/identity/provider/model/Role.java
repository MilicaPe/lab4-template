package identity.provider.model;

public enum Role {
    USER (1),
    ADMIN (2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public static Role valueOf(int value) {
        switch (value) {
              case 2:
                return Role.ADMIN;
            default:
                return Role.USER;
        }
    }

    public int getValue() {
        return value;
    }




}
