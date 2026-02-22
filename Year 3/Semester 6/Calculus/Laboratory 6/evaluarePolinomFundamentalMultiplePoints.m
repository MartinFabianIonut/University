function [val] = evaluarePolinomFundamentalMultiplePoints(nodes, k, x)
    val = zeros(size(x));
    [~, c] = size(x);
    for i = 1 : c 
       val(1, i) = evaluarePolinomFundamental(nodes, k, x(1, i)); 
    end
end