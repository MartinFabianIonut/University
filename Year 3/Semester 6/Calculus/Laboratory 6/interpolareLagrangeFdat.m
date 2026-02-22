function [val] = interpolareLagrangeFdat(x, f, m, nodes)
    considered = nodes(1 : m);
    consideredVals = f(considered);
    val = interpolareLagrange(considered, consideredVals, x);
end