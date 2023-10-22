package Paths;

public enum CONVOLUTION_PATHS {
    A("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Inputs\\convolution3x3.txt"),
    B("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Inputs\\convolution5x5.txt");

    private final String path;

    CONVOLUTION_PATHS(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
