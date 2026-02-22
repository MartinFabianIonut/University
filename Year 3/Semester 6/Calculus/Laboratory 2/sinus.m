function seria = sinus(xx)
    % reducere la primul cadran
    [x, semn_sin, ~] = reducere_la_primul_cadran(xx);
    
    seria = 0;  % construim seria taylor pentru sin 
    taylor = x;
    n = 1;
    while seria + taylor ~= seria
        seria = seria + taylor;
        taylor = -taylor * x^2 / ((n+1)*(n+2));
        n = n + 2;
    end
    seria = semn_sin * abs(seria);
end