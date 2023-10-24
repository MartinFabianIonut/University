package Paths;

public enum CONVOLUTION_PATHS {
    A("src\\Inputs\\convolution3x3.txt"),
    B("src\\Inputs\\convolution5x5.txt");

    private final String path;

    CONVOLUTION_PATHS(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
