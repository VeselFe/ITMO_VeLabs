package enums;

public enum Sizes {
    BIG("большой"),
    SMALL("маленький"),
    NOSIZE("неопределённый");
    
    private final String description;
    
    Sizes(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
