package Paths;

public enum INPUT_PATHS {
    A("src\\Inputs\\data10x10.txt"),
    B("src\\Inputs\\data1000x1000.txt"),
    C("src\\Inputs\\data10000x10000.txt");

    private final String path;

    INPUT_PATHS(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
