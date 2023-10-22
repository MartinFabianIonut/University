package Paths;

public enum INPUT_PATHS {
    A("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Inputs\\data10x10.txt"),
    B("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Inputs\\data1000x1000.txt"),
    C("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Inputs\\data10x10000.txt"),
    D("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix Java\\out\\production\\ConvolutionMatrix Java\\Inputs\\data10000x10.txt");

    private final String path;

    INPUT_PATHS(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
