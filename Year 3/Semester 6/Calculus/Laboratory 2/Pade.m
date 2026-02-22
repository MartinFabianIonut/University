function R = Pade(f, m, k, val)

    if k>m
        aux=m;
        m=k;
        k=aux;
    end

    syms x;

    c = zeros(1, m+k+1);
    if m > k
        b = zeros(m + 1, 1);
    else
        b = zeros(k + 1, 1);
    end
    a = zeros(1, m + 1);

    [val, ~, ~] = reducere_la_primul_cadran(val);
    fact = 1;
    c(1) = eval(subs(f,x,0));
    for i = 1:m+k
        fact = fact * i;
        c(i + 1) = eval(subs((diff(f, i) / fact), x, 0));
    end

    leftMatrix = toeplitz(c(m + 1 : m + k), c(m + 1:-1:m - k + 2));
    rightMatrix = -c(m + (1:k) + 1)';

    b(1) = 1;
    b(2:k + 1) = leftMatrix \ rightMatrix;

    for j = 1:m + 1
        for l = 1:j
            a(j) = a(j) + c(j - l + 1) * b(l); 
        end
    end

    sumUp = 0;
    for i = 0:m
        sumUp = sumUp + (a(i + 1) * (val ^ i));
    end

    sumDown = 0;
    for j = 0:k
        sumDown = sumDown + (b(j + 1) * (val ^ j));
    end

    R = sumUp / sumDown;
end
