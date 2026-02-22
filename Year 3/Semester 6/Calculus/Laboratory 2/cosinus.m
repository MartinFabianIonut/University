function seria = cosinus(xx)
    % reducere la primul cadran
    [x, ~, semn_cos] = reducere_la_primul_cadran(xx);
    
    seria = 0; % construim seria taylor pentru cos
    taylor = 1;
    n = 0;
    while seria + taylor ~= seria
        n = n + 1;
        seria = seria + taylor;
        taylor = (-1)^n * ((x^(2*n)) / (factorial(2*n)));
    end
    seria = semn_cos * abs(seria);
end